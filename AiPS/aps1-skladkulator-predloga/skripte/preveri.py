#!/usr/bin/env python3
"""Preveri primere iz navodil in programe iz mape programi/.

Uporaba (iz mape Skladkulator, po prevajanju; ukaza ./run.sh test-primeri in ./run.sh test-programi prevedeta sama):
    python3 skripte/preveri.py examples [N ...]    # bloki kode iz navodila/NalogaN.md (privzeto vse naloge in docs/Bonus.md)
    python3 skripte/preveri.py programs             # programi/*/*.in proti pripadajočim .out
    dodatno: -v izpiše tudi uspešne bloke

Primeri v navodilih: vsak blok kode (med ```) se požene z Engine stopnje N (iz imena datoteke NalogaN.md).
Pričakovani izpis berejo iz komentarjev:
  - »...« v komentarjih: izpis programa (po simbolih, po vrstnem redu pojavitve; prazne vrstice se ne štejejo),
  - /* -> ... */: vsebina glavnega sklada (od dna proti vrhu) po izvedeni vrstici; besedilo za vejico
    se ne upošteva, besedilo v oklepajih prav tako, (prazen sklad) pomeni prazen sklad.
  - večvrstični komentar, ki se začne v novi vrstici in nima » « ali ->: izpis programa (sledi urejanja).
Če blok samostojno ne uspe (npr. potrebuje definicije iz prejšnjega bloka), skripta pred njega po vrsti
doda prejšnje bloke iste datoteke (nazadnje še naslednja dva: rešitev je lahko za primeri). Bloki brez komentarjev /* */ (npr. risbe) in bloki, označeni s ```text, se preskočijo.
"""
import re
import subprocess
import sys
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
OUT = ROOT / "out"

COMMENT_RE = re.compile(r"/\*(.*?)\*/", re.S)
OUTPUT_RE = re.compile(r"»([^«]*)«")


def run_program(level, src):
    r = subprocess.run(["java", "-cp", str(OUT), "Skladkulator", str(level)],
                       input=src, capture_output=True, text=True, timeout=60)
    return r.stdout, r.stderr


def code_blocks(md):
    """Seznam (številka vrstice začetka bloka, koda)."""
    res = []
    for m in re.finditer(r"^```([^\n]*)\n(.*?)^```", md, re.S | re.M):
        if m.group(1).strip() == "text":     # blok, označen s ```text, se ne preverja
            continue
        res.append((md.count("\n", 0, m.start(2)) + 1, m.group(2)))
    return res


def expected_output(code):
    toks = []
    for m in COMMENT_RE.finditer(code):
        text = m.group(1)
        if text.startswith("\n") and "»" not in text and not text.lstrip().startswith("->"):
            toks += text.split()        # večvrstični komentar brez » «: sled izpisa (urejanje)
            continue
        for g in OUTPUT_RE.finditer(text):
            toks += g.group(1).split()
    return toks


def stack_checks(code):
    """Seznam (položaj konca komentarja, pričakovani sklad)."""
    res = []
    for m in COMMENT_RE.finditer(code):
        text = m.group(1)
        if not text.lstrip().startswith("->"):
            continue
        k = text.find("->")
        s = OUTPUT_RE.sub("", text[k + 2:]).split(",")[0].strip()
        if s.startswith("(prazen sklad)"):
            res.append((m.end(), []))
        else:
            res.append((m.end(), re.sub(r"\([^)]*\)", "", s).split()))
    return res


def try_block(level, deps, code):
    """Poskusi z danimi odvisnostmi; vrne seznam napak (prazen seznam: uspeh)."""
    prefix = "".join(deps)
    src = prefix + code
    exp = []
    for d in deps:
        exp += expected_output(d)
    exp += expected_output(code)
    out, err = run_program(level, src)
    errors = []
    if err.strip():
        errors.append("stderr: " + err.strip().splitlines()[0])
    if out.split() != exp:
        errors.append("izpis: " + " ".join(out.split()) + "  | pričakovano: " + " ".join(exp))
    if errors:
        return errors
    for end, expected in stack_checks(code):
        out2, err2 = run_program(level, src[:len(prefix) + end] + "\ndump\n")
        lines = out2.split("\n")
        actual = lines[-2].split() if len(lines) >= 2 else []
        ok = actual == expected if not deps else actual[len(actual) - len(expected):] == expected
        if err2.strip() or not ok:
            errors.append(f"sklad: {' '.join(actual)}  | pričakovano: {' '.join(expected)}")
            break
    return errors


def check_block(level, previous, code, following=()):
    """Vrne (opombe, napake): število dodanih blokov in seznam napak ob neuspehu.
    Najprej samostojno, nato s prejšnjimi bloki, nazadnje z naslednjima (rešitev je lahko za primeri)."""
    first = None
    candidates = [previous[len(previous) - a:] for a in range(len(previous) + 1)]
    candidates += [list(following[:k]) for k in (1, 2) if len(following) >= k]
    for deps in candidates:
        errors = try_block(level, deps, code)
        if not errors:
            return len(deps), []
        if first is None:
            first = errors
    return 0, first


def examples(levels, verbose):
    total = bad = 0
    files = []
    for md_path in sorted((ROOT / "navodila").glob("Naloga*.md")):
        m = re.fullmatch(r"Naloga(\d+)\.md", md_path.name)
        if not levels or int(m.group(1)) in levels:
            files.append((md_path, int(m.group(1))))
    bonus = ROOT / "docs" / "Bonus.md"
    if bonus.exists() and not levels:       # dodatne naloge (samo pri avtorjih): vse zmožnosti, stopnja 5
        files.append((bonus, 5))
    for md_path, level in files:
        every = code_blocks(md_path.read_text(encoding="utf-8"))
        codes = [c for _, c in every]
        todo = [i for i, (_, c) in enumerate(every) if "/*" in c]     # bloki s komentarji; ostali so lahko odvisnosti
        blocks = [every[i] for i in todo]
        with ThreadPoolExecutor(max_workers=6) as pool:
            results = list(pool.map(lambda i: check_block(level, codes[:i], codes[i], codes[i + 1:]), todo))
        for (ln, _), (added, errors) in zip(blocks, results):
            total += 1
            if errors:
                bad += 1
                print(f"NAPAKA  {md_path.name}:{ln}")
                for e in errors:
                    print("          " + e)
            elif verbose:
                note = f" (+{added} prejšnjih blokov)" if added else ""
                print(f"OK      {md_path.name}:{ln}{note}")
    print(f"\nPrimeri v navodilih: {total - bad} OK, {bad} NAPAKA")
    return bad == 0


def programs(verbose):
    files = sorted((ROOT / "programi").glob("*/*.in"))
    bad = 0

    def one(f):
        out, err = run_program(5, f.read_text(encoding="utf-8"))
        return out == f.with_suffix(".out").read_text(encoding="utf-8") and not err.strip()

    with ThreadPoolExecutor(max_workers=6) as pool:
        results = list(pool.map(one, files))
    for f, ok in zip(files, results):
        name = f"{f.parent.name}/{f.stem}"
        if not ok:
            bad += 1
            print(f"NAPAKA  {name}")
        elif verbose:
            print(f"OK      {name}")
    print(f"\nProgrami: {len(files) - bad} OK, {bad} NAPAKA")
    return bad == 0


def main():
    args = [a for a in sys.argv[1:] if a != "-v"]
    verbose = "-v" in sys.argv[1:]
    if not args or args[0] not in ("examples", "programs"):
        print(__doc__)
        return 2
    if args[0] == "examples":
        ok = examples({int(a) for a in args[1:]}, verbose)
    else:
        ok = programs(verbose)
    return 0 if ok else 1


if __name__ == "__main__":
    sys.exit(main())
