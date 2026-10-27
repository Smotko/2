#!/usr/bin/env bash
# Pomožna skripta za delo s projektom Skladkulator.
#
#   ./run.sh build                      prevede vso kodo v mapo out
#   ./run.sh run [stopnja]              prevede in požene Skladkulator (stopnja 0-5, privzeto 5); vhod z stdin
#   ./run.sh doc                        ustvari javadoc (out/doc/index.html)
#   ./run.sh test-zbirke [Izvedba ...]  prevede in požene TestZbirke (vse izvedbe zbirk ali samo navedene)
#   ./run.sh test-primeri [N ...] [-v]  preveri primere iz navodil/NalogaN.md (privzeto vse naloge)
#   ./run.sh pripravi-oddajo            združi kodo v eno datoteko za oddajo (out/Skladkulator.java) in preveri, da se prevede
#   ./run.sh clean                      izbriše mapo out (vključno z združeno datoteko)
#   ./run.sh help                       ta pomoč
#
# Primeri:
#   ./run.sh run 0 <<< "1 2 swap dump"
#   ./run.sh run 1 <<< "s as { a b c } s dumpc"
#   ./run.sh run 2 < primer.txt
#   ./run.sh test-zbirke ArrayStack LinkedQueue
#   ./run.sh pripravi-oddajo

set -euo pipefail
cd "$(dirname "$0")"

OUT=out

usage() {
    sed -n '2,/^$/p' "$0" | sed 's/^# \{0,1\}//'
}

build() {
    mkdir -p "$OUT"
    local sources
    sources=$(mktemp)
    find src -name '*.java' > "$sources"
    if ! javac -d "$OUT" "@$sources"; then
        rm -f "$sources"
        echo "prevajanje ni uspelo" >&2
        exit 1
    fi
    rm -f "$sources"
}

cmd=${1:-help}
[ $# -gt 0 ] && shift

case "$cmd" in
    build)
        build
        echo "prevajanje: OK ($OUT)"
        ;;
    run)
        build
        java -cp "$OUT" Skladkulator "$@"
        ;;
    doc)
        javadoc -quiet -Xdoclint:none -d "$OUT/doc" -sourcepath src zbirke izvedbe stroj
        echo "javadoc: $OUT/doc/index.html"
        ;;
    test-zbirke)
        build
        java -cp "$OUT" testi.TestZbirke "$@"
        ;;
    test-primeri)
        build
        python3 skripte/preveri.py examples "$@"
        ;;
    pripravi-oddajo)
        python3 skripte/flatten.py "$@"
        ;;
    clean)
        rm -rf "$OUT" skripte/__pycache__
        echo "pobrisano: $OUT"
        ;;
    help|-h|--help)
        usage
        ;;
    *)
        echo "neznan ukaz: $cmd" >&2
        usage >&2
        exit 2
        ;;
esac
