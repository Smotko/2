#!/usr/bin/env python3
"""Združi vse datoteke, ki jih potrebuje Skladkulator, v eno datoteko za oddajo.

Skripta začne pri src/Skladkulator.java in sledi odvisnostim (importi in
razredi iz istega paketa). Pri vsaki datoteki odstrani deklaracijo paketa in
importe razredov iz projekta ter besedo public pri razredih in vmesnikih
najvišjega nivoja. Importi razredov iz javanske knjižnice (npr. java.util.Scanner)
se zberejo na vrhu. Glavni razred je v izhodni datoteki prvi, zato jo je mogoče
zagnati tudi neposredno z "java Skladkulator.java".

Uporaba (iz mape Skladkulator):
    python3 skripte/flatten.py                        # src -> out/Skladkulator.java; rezultat se vedno prevede (javac)
    python3 skripte/flatten.py -s src -m Skladkulator -o out/Skladkulator.java
"""
import argparse
import re
import subprocess
import sys
import tempfile
from collections import deque
from pathlib import Path

TYPE_RE = re.compile(r"\b(class|interface|enum|record)\s+([A-Za-z_]\w*)")
PACKAGE_RE = re.compile(r"^[ \t]*package\s+([\w.]+)\s*;[ \t]*\n?", re.M)
IMPORT_RE = re.compile(r"^[ \t]*import\s+(static\s+)?([\w.]+?)(\.\*)?\s*;[ \t]*\n?", re.M)
MODIFIERS = r"(?:(?:abstract|final|sealed|non-sealed|strictfp)\s+)*"
PUBLIC_RE = re.compile(r"\bpublic\s+(?=" + MODIFIERS + r"(?:@interface|class|interface|enum|record)\b)")


def mask(src: str) -> str:
    """Komentarje ter vsebino nizov in znakov nadomesti s presledki (dolžina in vrstice ostanejo enake)."""
    out = []
    i, n = 0, len(src)
    while i < n:
        c = src[i]
        nxt = src[i + 1] if i + 1 < n else ""
        if c == "/" and nxt == "/":
            j = src.find("\n", i)
            j = n if j < 0 else j
            out.append(" " * (j - i))
            i = j
        elif c == "/" and nxt == "*":
            j = src.find("*/", i + 2)
            j = n if j < 0 else j + 2
            out.append("".join(ch if ch == "\n" else " " for ch in src[i:j]))
            i = j
        elif src.startswith('"""', i):
            j = src.find('"""', i + 3)
            j = n if j < 0 else j + 3
            out.append('"""' + "".join(ch if ch == "\n" else " " for ch in src[i + 3:j - 3]) + '"""')
            i = j
        elif c in "\"'":
            j = i + 1
            while j < n and src[j] != c:
                j += 2 if src[j] == "\\" else 1
            j = min(j + 1, n)
            out.append(c + " " * (j - i - 2) + c if j - i >= 2 else c)
            i = j
        else:
            out.append(c)
            i += 1
    return "".join(out)


def top_level_types(masked: str):
    """Imena razredov, vmesnikov, ... najvišjega nivoja (globina zavitih oklepajev 0)."""
    depth, names = 0, []
    pos = 0
    for m in re.finditer(r"[{}]|\b(?:class|interface|enum|record)\s+[A-Za-z_]\w*", masked):
        t = m.group(0)
        if t == "{":
            depth += 1
        elif t == "}":
            depth -= 1
        elif depth == 0:
            names.append(TYPE_RE.match(t).group(2))
    return names


class Unit:
    def __init__(self, path: Path):
        self.path = path
        self.text = path.read_text(encoding="utf-8")
        self.masked = mask(self.text)
        m = PACKAGE_RE.search(self.masked)
        self.package = m.group(1) if m else ""
        self.types = top_level_types(self.masked)


def load_units(src: Path):
    units = [Unit(p) for p in sorted(src.rglob("*.java"))]
    index = {}  # (paket, ime) -> Unit
    for u in units:
        for name in u.types:
            key = (u.package, name)
            if key in index:
                sys.exit(f"podvojen razred {u.package}.{name}: {index[key].path} in {u.path}")
            index[key] = u
    simple = {}
    for (pkg, name), u in index.items():
        simple.setdefault(name, []).append(pkg)
    dup = {n: p for n, p in simple.items() if len(p) > 1}
    if dup:
        sys.exit("isto ime razreda v več paketih (po združitvi bi se podvojilo): "
                 + ", ".join(f"{n} ({', '.join(p or ['<privzeti>'])})" for n, p in sorted(dup.items())))
    return units, index


def identifiers(masked: str):
    return set(re.findall(r"\b[A-Za-z_]\w*\b", masked))


def dependencies(unit: Unit, index):
    """Enote (iz projekta), ki jih unit potrebuje: importi in razredi iz istega paketa, ki se pojavijo v kodi."""
    ids = identifiers(unit.masked)
    candidates = {k for k in index if k[0] == unit.package}
    for m in IMPORT_RE.finditer(unit.masked):
        name, wildcard = m.group(2), bool(m.group(3))
        if m.group(1):  # import static
            name = name.rsplit(".", 1)[0]
            wildcard = False
        if wildcard:
            candidates |= {k for k in index if k[0] == name}
        else:
            pkg, _, cls = name.rpartition(".")
            if (pkg, cls) in index:
                candidates.add((pkg, cls))
    deps = []
    for pkg, name in sorted(candidates):
        target = index[(pkg, name)]
        if target is not unit and name in ids:
            deps.append(target)
    return deps


def collect(entry: Unit, index):
    """Enote od vstopne naprej (iskanje v širino), vstopna je prva."""
    order, seen, queue = [], {entry.path}, deque([entry])
    while queue:
        u = queue.popleft()
        order.append(u)
        for d in dependencies(u, index):
            if d.path not in seen:
                seen.add(d.path)
                queue.append(d)
    return order


def transform(unit: Unit, project_packages, project_names):
    """Vrne (telo brez paketa/importov projekta in brez public, importi iz knjižnic)."""
    edits = []  # (začetek, konec, nadomestek)
    lib_imports = []
    for m in PACKAGE_RE.finditer(unit.masked):
        edits.append((m.start(), m.end(), ""))
    for m in IMPORT_RE.finditer(unit.masked):
        static, name, wildcard = m.group(1), m.group(2), m.group(3)
        base = name.rsplit(".", 1)[0] if static else name
        pkg = base if wildcard else base.rpartition(".")[0]
        if pkg in project_packages and not name.startswith("java."):
            edits.append((m.start(), m.end(), ""))
        else:
            lib_imports.append(re.sub(r"\s+", " ", unit.text[m.start():m.end()].strip()))
            edits.append((m.start(), m.end(), ""))
    # public pri razredih najvišjega nivoja
    depth = 0
    for m in re.finditer(r"[{}]|\bpublic\s+(?=" + MODIFIERS + r"(?:@interface|class|interface|enum|record)\b)", unit.masked):
        t = m.group(0)
        if t == "{":
            depth += 1
        elif t == "}":
            depth -= 1
        elif depth == 0:
            edits.append((m.start(), m.end(), ""))
    # polno kvalificirana imena razredov iz projekta (npr. zbirke.Stack) -> Stack
    for pkg in sorted(project_packages, key=len, reverse=True):
        if not pkg:
            continue
        pattern = re.compile(r"(?<![\w.])" + re.escape(pkg) + r"\.(?=(?:" + "|".join(map(re.escape, project_names)) + r")\b)")
        for m in pattern.finditer(unit.masked):
            if any(start <= m.start() < end for start, end, _ in edits):
                continue  # že znotraj odstranjenega importa/paketa
            edits.append((m.start(), m.end(), ""))
    text = unit.text
    for start, end, repl in sorted(edits, reverse=True):
        text = text[:start] + repl + text[end:]
    return text.strip("\n"), lib_imports


def flatten(src: Path, main: str, out: Path):
    units, index = load_units(src)
    entries = [u for (pkg, name), u in index.items() if name == main and pkg == ""]
    if not entries:
        entries = [u for (pkg, name), u in index.items() if name == main]
    if not entries:
        sys.exit(f"razreda {main} ni v mapi {src}")
    order = collect(entries[0], index)
    project_packages = {u.package for u in units}
    project_names = sorted({n for (_, n) in index})
    bodies, imports = [], set()
    for u in order:
        body, libs = transform(u, project_packages, project_names)
        bodies.append(f"// ---- {u.path.relative_to(src)} ----\n{body}")
        imports.update(libs)
    parts = ["// Samodejno ustvarjeno s skripto flatten.py - ne urejajte ročno."]
    if imports:
        parts.append("\n".join(sorted(imports)))
    parts.extend(bodies)
    out.parent.mkdir(parents=True, exist_ok=True)
    out.write_text("\n\n".join(parts) + "\n", encoding="utf-8")
    return order


def check(out: Path):
    with tempfile.TemporaryDirectory() as tmp:
        r = subprocess.run(["javac", "-d", tmp, str(out)], capture_output=True, text=True)
    if r.returncode != 0:
        sys.exit("prevajanje združene datoteke ni uspelo:\n" + r.stderr)
    print("prevajanje: OK")


def main():
    ap = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    ap.add_argument("-s", "--src", default="src", help="mapa z izvorno kodo (privzeto: src)")
    ap.add_argument("-m", "--main", default="Skladkulator", help="glavni razred (privzeto: Skladkulator)")
    ap.add_argument("-o", "--out", default="out/Skladkulator.java", help="izhodna datoteka")
    args = ap.parse_args()
    src, out = Path(args.src), Path(args.out)
    order = flatten(src, args.main, out)
    print(f"združenih datotek: {len(order)} -> {out}")
    for u in order:
        print("  ", u.path.relative_to(src))
    check(out)


if __name__ == "__main__":
    main()
