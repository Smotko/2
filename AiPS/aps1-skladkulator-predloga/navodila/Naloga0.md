# Naloga0 – Temelj Skladkulatorja

V programskem jeziku Java napišite programabilni skladovni kalkulator, ki zna s pomočjo sklada ovrednotiti programe v obratnem poljskem zapisu. Kasneje pa mu bomo dodali še nekatere druge funkcionalnosti.

V tej nalogi postavite temeljno ogrodje: berite in obdelujte programe s standardnega vhoda, uporabite en sam glavni sklad in peščico osnovnih ukazov. Poznejše naloge bodo temelj razširjale z več skladi in še drugimi zbirkami.

> Uporaba javanskih zbirk (Java Collections Framework, npr. `ArrayList`, `LinkedList`, `Stack`, `HashMap`) ni dovoljena – vse zbirke implementirate sami. Za branje vhoda je dovoljen le razred `java.util.Scanner` ter osnovni razredi iz `java.lang` (npr. `String`, `Integer`, `Math`). Druge knjižnice niso potrebne (niti dovoljene). To seveda velja za vse naloge.

V pomoč smo vam pripravili predlogo projekta, ki jo lahko uporabite (po želji). Vsebuje strukturo projekta, potrebne vmesnike in teste za zbirke. S pregledom predloge začnite pri datoteki README.md.

## Glavni sklad

Za začetek boste potrebovali le en **sklad nizov** (`Stack<String>`), ki mu bomo kasneje rekli tudi glavni sklad. Vsi elementi na skladu so nizi – tudi števila se hranijo kot nizi in se pretvorijo v `int` vsakič, ko nek ukaz potrebuje število. Sklad implementirajte s poljem:
- `izvedbe.ArrayStack<T>`, ki uresničuje vmesnik `zbirke.Stack<T>`.

> Vmesnik `Stack<T>` razširja `Indexed<T>` in ta razširja `Collection<T>`. Dobro si oglejte vse tri vmesnike in metode, ki jih je treba implementirati.

## Programi

Program za Skladkulator je sestavljen iz zaporedja simbolov (token), ločenih z belimi znaki (presledek, tabulator, konec vrstice). Branje naslednjega simbola (s standardnega vhoda) enostavno naredimo z `Scanner.next()`. Primer programa:
- `3 2 1 + * echo nl`

Predpostavite lahko, da so vsi vhodni oz. testni programi sintaktično in semantično pravilni. Simboli se obdelujejo po naslednjih pravilih:

- Če se simbol ujema z imenom enega od vgrajenih ukazov, se ustrezni ukaz izvede.

- Če se simbol začne z znakom `'`, gre za *citiranje* (quotation), kar pomeni, da se preostanek niza (simbol brez `'`) potisne na sklad dobesedno (tudi če bi se ta sicer ujemal z imenom ukaza ali pa gre za število).

- Sicer pa se simbol (lahko gre za število ali za nek niz, ki ni ne ukaz ne citiranje) potisne na sklad.

Vhod lahko vsebuje tudi komentarje oblike `/* ... */`. To pomeni, da se vsi simboli med (in vključno z) `/*` in `*/` izločijo, še preden se karkoli izvede. Gnezdenje komentarjev ni podprto.

> Branje in obdelava simbolov (`run`, `process`, `intercept` za komentarje in `fallback` za citiranje oz. potiskanje na sklad) so že implementirani v predlogi :) Vi dopolnite samo ukaze, ki jih opisuje nadaljevanje (`eval`).

## Izvajanje ukazov

Ukaz iz glavnega sklada prebere in odstrani ustrezno število elementov (argumenti ukaza), nato se izvede in na sklad zapiše svoje rezultate. Oglejmo si obdelavo programa `3 2 1 + * echo nl`:

| simbol | dejanje                                    | sklad po izvedbi (dno → vrh) | izpis |
|--------|--------------------------------------------|------------------------------|-------|
| `3`    | število se potisne na sklad                | 3                            |       |
| `2`    | število se potisne na sklad                | 3 2                          |       |
| `1`    | število se potisne na sklad                | 3 2 1                        |       |
| `+`    | vzame 2 in 1, potisne njuno vsoto 3        | 3 3                          |       |
| `*`    | vzame 3 in 3, potisne njun zmnožek 9       | 9                            |       |
| `echo` | vzame 9 in jo izpiše                       | (prazen sklad)               | 9     |
| `nl`   | izpiše novo vrstico                        | (prazen sklad)               | ⏎     |

Nekateri ukazi imajo namesto (ali poleg) zapisa rezultata na sklad tudi **stranski učinek** – naredijo nekaj navzven vidnega, kar ni razvidno le iz stanja sklada, npr. izpis na standardni izhod.

Pri opisu ukaza najprej zapišemo njegovo sintaktično pravilo. Zapis *x* `dup` → *x* *x* pomeni, da ukaz `dup` vzame en element (poimenovan *x*) s sklada in ga nato dvakrat potisne nazaj na sklad. V nadaljevanju ne bomo več posebej poudarjali, da ukazi svoje argumente tudi odstranijo s sklada.

## Ukazi za delo z vrhom sklada

Naslednji ukazi spreminjajo enega ali več elementov na vrhu sklada:
- *x* `pop` : odstrani *x*
- *x* `dup` → *x* *x* : podvoji vrh sklada
- *x* *y* `over` → *x* *y* *x* : kopira element pod vrhom sklada na vrh
- *x* *y* `swap` → *y* *x* : zamenja vrhnja dva elementa sklada
- *x* *y* *z* `rot` → *y* *z* *x* : rotira vrhnje tri elemente sklada v levo

## Ukazi za delo z nizi

Še nekaj preprostih ukazov za delo z nizi:
- *x* `char` → *y* : *y* je znak, ki ima ASCII/Unicode kodo *x*
- *x* `strlen` → *y* : *y* je dolžina niza *x*
- *x* *y* `strcat` → *z* : *z* je zlepek nizov *x* in *y*
- *x* *y* `strcmp` → *r* : *r* je -1, 0 ali 1 – leksikografska primerjava *x* in *y*

## Aritmetični ukazi

Ukazi za osnovne aritmetične operacije s celimi števili:
- *x* *y* `+` → *z* : *z* je vsota *x* in *y*
- *x* *y* `-` → *z* : *z* je razlika *x* in *y*
- *x* *y* `*` → *z* : *z* je zmnožek *x* in *y*
- *x* *y* `/` → *z* : *z* je celoštevilski kvocient *x* in *y*
- *x* *y* `%` → *z* : *z* je ostanek pri deljenju *x* z *y*

## Ukazi za primerjavo števil

Naslednji ukazi vrhnja dva elementa primerjajo kot celi števili in na sklad potisnejo 1 (če je primerjava resnična) ali 0 (če ni):
- *x* *y* `<` → *r* : *r* = 1, če *x* < *y*
- *x* *y* `<=` → *r* : *r* = 1, če *x* <= *y*
- *x* *y* `==` → *r* : *r* = 1, če *x* == *y*
- *x* *y* `>=` → *r* : *r* = 1, če *x* >= *y*
- *x* *y* `>` → *r* : *r* = 1, če *x* > *y*

Pogoj je mogoče tudi negirati:
- *x* `not` → *r* : *r* = 1, če je *x* enak 0, sicer *r* = 0

## Ukazi za izpis

Posebni ukazi za izpis:
- `spc` : izpiše presledek
- `nl` : izpiše prelom vrstice
- *x* `echo` : izpiše in odstrani *x*
- `dump` : v vrstici izpiše vsebino celotnega sklada, od dna proti vrhu (sklad ostane nespremenjen)

## Primeri

Vsak spodnji primer je samostojen program: vrstice se izvajajo po vrsti in si delijo glavni sklad, različni primeri pa so med seboj neodvisni (vsak se začne s praznim skladom). V komentarju na koncu vrstice ločimo dvoje:
- `-> ...` : celotna vsebina sklada po izvedbi vrstice (elementi od dna proti vrhu, kot vrne `toString()`),
- `»...«` : kaj se izpiše na standardni izhod.

Delo s skladom in nizi:
```
1 2 swap                                /* -> 2 1 */
3 rot                                   /* -> 1 3 2 */
over                                    /* -> 1 3 2 3 */
+ + +                                   /* -> 9 */
65 char 66 char strcat                  /* -> 9 AB */
dup strlen                              /* -> 9 AB 2 */
swap 'AC strcmp                         /* -> 9 2 -1 */
```

Aritmetika in primerjave:
```
20 12 10 + +                            /* -> 42 */
2 * 4 /                                 /* -> 21 */
5 %                                     /* -> 1 */
20 15 - 20 15 +                         /* -> 1 5 35 */
<                                       /* -> 1 1 */
==                                      /* -> 1 */
not                                     /* -> 0 */
```

Izpis:
```
'Hi echo nl                                    /* »Hi« */
72 char 105 char strcat echo nl                /* »Hi« */
'Hello 32 char strcat 'world! strcat echo nl   /* »Hello world!« */
```

Izpis in sklad hkrati:
```
5 6 dup echo nl                         /* -> 5 6       »6« */
1 2 dump dump                           /* -> 5 6 1 2   »5 6 1 2« »5 6 1 2« (vsak dump izpiše svojo vrstico) */
```
