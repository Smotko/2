# Naloga1 – Poimenovane zbirke in bloki

## Vrsta in dvostrana vrsta

Poleg sklada bomo potrebovali še vrsto in dvostrano vrsto. Dodajte še dve izvedbi s poljem:
- vrsta (FIFO): razred `izvedbe.ArrayQueue<T>`, ki uresničuje vmesnik `Queue<T>`,
- dvostrana vrsta: razred `izvedbe.ArrayDeque<T>`, ki uresničuje vmesnik `Deque<T>`.

Skladkulator bo tako zaenkrat uporabljal tri izvedbe zbirk: `ArrayStack` (iz Naloga0), `ArrayQueue` in `ArrayDeque`.

## Slovar zbirk

Vse tipe zbirk bomo hranili v slovarju, ki ime zbirke preslika v zbirko samo. Slovar implementirajte glede na vmesnik `Map<T>` iz predloge. Ključi slovarja so nizi `String`, vrednosti pa so tipa `Collection<String>`. Celoten tip slovarja je torej `Map<Collection<String>>`.

Izvedbo `ArrayMap<T>` slovarja lahko preprosto implementirate z dvema poljema (kar je sicer neučinkovito, a boste boljše izvedbe spoznali pri APS2). Pri tem eno polje nizov uporabite za ključe, drugo polje pa za vrednosti. V prvem polju se torej na indeksu *i* hrani ključ, v drugem polju pa temu ključu pripadajoča vrednost. O podrobnostih implementacije pa razmislite sami.

## Ustvarjanje zbirk

Najprej dodajte ukaze za **ustvarjanje zbirk**. Vsak vzame ime z glavnega sklada, ustvari prazno zbirko ustreznega tipa, jo vstavi v slovar (ključ je ime, vrednost pa sama zbirka), in ime porine nazaj na sklad:
- *p* `as` → *p* : ustvari sklad s poljem (`ArrayStack`) z imenom *p*
- *p* `aq` → *p* : ustvari vrsto s poljem (`ArrayQueue`) z imenom *p*
- *p* `ad` → *p* : ustvari dvostrano vrsto s poljem (`ArrayDeque`) z imenom *p*

> Če zbirka z imenom *p* v slovarju že obstaja, jo nadomesti nova prazna zbirka (morda drugega tipa). Kapaciteta zbirk naj bo enaka kot pri glavnem skladu.

## Osnovni ukazi nad zbirkami

Dodajte še ukaze, ki delujejo nad poljubno zbirko (ime je podano na glavnem skladu):
- *p* `clear` : izprazni zbirko *p*
- *p* `count` → *n* : *n* je število elementov v zbirki *p*
- *x* *p* `contains` → *r* : *r* = 1, če zbirka *p* vsebuje element *x*, sicer 0
- *p* `dumpc` : v vrstici izpiše vsebino zbirke *p* (podobno kot `dump` za glavni sklad)

> Pri `dumpc` se zbirka izpiše od začetka proti koncu: pri skladu od dna proti vrhu, pri vrsti od začetka vrste.

Ker ukaz ime pusti na skladu, ga lahko takoj uporabimo naprej:
```
seznam ad count  /* -> 0, nova zbirka je prazna */
```

## Prenos elementov

Elemente je mogoče prenašati iz glavnega sklada v zbirke in nazaj:

- *x* *p* `store` : premakne element *x* z glavnega sklada v zbirko *p*
- *p* `load` → *x* : premakne element iz zbirke *p* na glavni sklad
- *p* *n* `storen` : enako kot `store`, le da premik ponovi *n*-krat (*n* na vrhu)
- *p* *n* `loadn` → ... : enako kot `load`, le da premik ponovi *n*-krat – na sklad pristane *n* elementov

Prenos je prilagojen tipu zbirke *p*: `pop`/`push` za sklad, `dequeue`/`enqueue` za vrsto. Ker `Deque<T>` razširja `Queue<T>`, se dvostrana vrsta obravnava enako kot navadna vrsta. Predpostavite lahko, da bo ime zbirke vedno pravilno.

Primera (sklad in vrsta vračata elemente v različnem vrstnem redu):
```
1 2 3 sklad as 3 storen
sklad dumpc                    /* »3 2 1« */
sklad load pop                 /* odstrani 1, nazadnje vstavljeni element */
0 sklad store
sklad dumpc                    /* »3 2 0« */

4 5 6 vrsta aq 3 storen
vrsta dumpc                    /* »6 5 4« */
vrsta load pop                 /* odstrani 6, najprej vstavljeni element */
7 vrsta store
vrsta dumpc                    /* »5 4 7« */
```

### Dvostrana vrsta

Sklad in vrsta dovoljujeta dodajanje in odvzemanje elementov le na določenih koncih zbirke. Za celovito uporabo dvostrane vrste pa sta potrebna še dva ukaza:

- *x* *p* `storefront` : element *x* doda na sprednji konec dvostrane vrste *p*
- *p* `loadback` → *x* : odstrani in na glavni sklad porine element z zadnjega konca dvostrane vrste *p*

Primeri:
```
'a d0 ad store 'b d0 store 'z d0 storefront d0 dumpc  /* »z a b« */
d0 loadback echo nl                                   /* »b« */
d0 load echo nl                                       /* »z« */
d0 dumpc                                              /* »a« */
```

### Dostop po indeksu

Zbirke (sklad, vrsta in dvostrana vrsta) so indeksirane od začetka (indeks 0): pri skladu je to dno, pri vrsti začetek vrste. Naslednja dva ukaza sta namenjena indeksiranemu dostopu do zbirke:
- *p* *i* `get` → *x* : *x* je element na indeksu *i* v zbirki *p* (zbirka ostane nespremenjena)
- *x* *p* *i* `set` : element na indeksu *i* v zbirki *p* zamenja z *x*

> Indeks *i* mora biti veljaven (med 0 in `count`-1). Za neveljavne indekse sta zgornja ukaza nedefinirana.

Primeri (uporabljajo bloke, ki so opisani v nadaljevanju):
```
s3 as { 10 20 30 }
s3 count echo nl  /* »3« */
s3 0 get echo nl  /* »10«, indeks 0 je dno sklada */

q3 aq { a b c }
q3 2 get echo nl  /* »c«, indeks 2 je zadnji element vrste */

q4 aq { a b c }
'x q4 1 set
q4 dumpc          /* »a x c« */
```

## Bloki

Kadar želimo v neko zbirko shraniti več elementov, bi to lahko poskusili narediti z zaporedjem elementov in `storen`, na primer `'a 'b 'c dv ad 3 storen` v dvostrano vrsto `dv` shrani nize `c`, `b` in `a`. Težava pa je, ker je vrstni red obrnjen, saj `storen` z glavnega sklada zaporedoma jemlje s `pop`. To seveda lahko popravimo z obračanjem zaporedja, kar pa lahko postane precej duhamorno.

Da bo nadaljnje programiranje v Skladkulatorju slajše, raje uvedemo dodaten konstrukt, ki ga bomo imenovali **blok**. Gre za **preusmeritev dela vhodnega toka** v poljubno zbirko:
- *p* `{ ... }` : surov tok simbolov, ujet med `{` in `}`, se zaporedoma (od leve proti desni, kot je zapisano) vstavlja v obstoječo zbirko z imenom *p*

Simboli v bloku se **ne izvajajo**, temveč se le shranjujejo (kot nizi, ki so enaki zapisu na vhodu). Če zbirka že vsebuje elemente, se novi dodajo za njimi.

> Gnezdenje `{` znotraj `{ ... }` ni definirano. Komentarji se, kot povsod, izločijo, še preden dosežejo blok.

Ukazi za ustvarjanje zbirk ime namenoma pustijo na skladu, zato lahko blok neposredno sledi ustvarjanju zbirke. Primeri:
```
s7 as { a b c }
s7 dumpc         /* »a b c« */

q1 aq { 1 dup + }
q1 dumpc         /* »1 dup +«, simboli se ne izvedejo */

d0 ad { x y z }
d0 dumpc         /* »x y z« */

q2 aq { a b c }
q2 { d e f g }   /* drugi blok doda elemente za prvimi */
q2 dumpc         /* »a b c d e f g« */
```

## Funkcije

Funkcije so s strani programerja definirani novi ukazi, ki izvedejo neko zaporedje ukazov. Ustvarimo jih podobno kot zbirke:
- *p* `fun` → *p* : ustvari funkcijo z imenom *p*

Interno so funkcije čisto navadne vrste `Queue<String>`: ukaz `fun` je pravzaprav skoraj enak ukazu `aq`. Tako kot vrste se tudi funkcije ustvarijo **prazne** (brez ukazov v telesu funkcije), zato jih je treba še napolniti. Ker gre za običajno vrsto, lahko za to uporabimo kar blok:
```
podvoji fun { dup + }      /* podvoji število */
kvadrat fun { dup * }      /* kvadrat števila */
kub fun { dup kvadrat * }  /* uporaba obstoječih funkcij */
```

Funkcije nimajo eksplicitnih argumentov, ampak so ti implicitno podani prek glavnega sklada. Pokličemo jih preprosto z navedbo njihovega imena:
```
7 podvoji echo nl              /* »14« */
6 kvadrat echo nl              /* »36« */
3 kub echo nl                  /* »27« */
2 podvoji kvadrat kub echo nl  /* »4096« */
```

V tem je tudi glavna razlika med funkcijami in vrstami: funkcijo lahko pokličemo, vrste pa ne moremo. Navedba imena funkcije torej pokliče funkcijo (zaporedoma obdela vse simbole njenega telesa, ki je shranjeno v vrsti). Navedba imena zbirke pa le potisne dano ime na sklad.

> Za potiskanje imena funkcije na sklad je torej treba uporabiti citiranje, npr. `'kub count` na sklad potisne 3, ker ima zgoraj definirana funkcija `kub` tri simbole v svojem telesu.
