# Skladkulator – predloga

Programabilni skladovni kalkulator v Javi (programi v obratnem poljskem zapisu). Ta paket je ogrodje projekta: vmesniki, osnutki razredov in orodja za delo. Mesta, ki jih je treba dopolniti, so označena s `TODO`.

## Zgradba

Izvorna koda je v mapi `src/` in njenih podmapah:

- `zbirke/` – vmesniki zbirk (ni jih treba spreminjati)
  - vmesniki `Collection`, `Indexed`, `Stack`, `Queue`, `Deque`, `Map`, `Set`, `Bag`
  - izjema `CollectionException` pri delu z zbirkami
- `izvedbe/` – izvedbe zbirk (te boste implementirali)
  - izvedbi sklada `ArrayStack`, `LinkedStack`
  - izvedbi vrste `ArrayQueue`, `LinkedQueue`
  - izvedbi dvostrane vrste `ArrayDeque`, `LinkedDeque`
  - izvedbi slovarja `ArrayMap` in množice `ArraySet`
- `stroj/` – skladovni izvajalni stroj
  - razredi `Engine0` … `Engine5` (vsak ustreza eni nalogi)
  - razred `Directory` (nabor poimenovanih zbirk)
- `Skladkulator.java` – glavni program
  - številko naloge (0–5) podate kot argument in sam izbere ustrezen `EngineN`
- `testi/TestZbirke.java` – samopreverjanje izvedb zbirk

Dokumentacija paketov, vmesnikov zbirk in njihovih izvedb je v javadocu (`package-info.java` in komentarji pri razredih in metodah v mapi `src/`), navodila za testiranje izvedb pa v javadocu razreda `testi.TestZbirke`.

Javadoc si lahko ogledate v ustreznem urejevalniku (ob premiku miške nad razred ali metodo) ali pa ga ustvarite kot spletne strani z ukazom `./run.sh doc` in odprete `out/doc/index.html`.

## Datoteke za dopolnitev

Izvorne javanske datoteke, ki jih je treba dopolniti pri posamezni nalogi, so podane v naslednji tabeli:

| naloga | datoteke |
|---|---|
| Naloga0 | `ArrayStack`, `Engine0` |
| Naloga1 | `ArrayQueue`, `ArrayDeque`, `ArrayMap`, `Directory`, `Engine1` |
| Naloga2 | `LinkedStack`, `LinkedQueue`, `LinkedDeque`, `Directory`, `Engine2` |
| Naloga3 | `Engine3` |
| Naloga4 | `Engine4` |
| Naloga5 | `ArraySet`, `Engine5` |

## Delo (`./run.sh`)

    ./run.sh build                      prevede kodo v mapo out
    ./run.sh run [stopnja]              požene Skladkulator (vhod s standardnega vhoda)
    ./run.sh doc                        ustvari javadoc (out/doc/index.html)
    ./run.sh test-zbirke [Izvedba ...]  požene TestZbirke (vse izvedbe zbirk ali samo navedene)
    ./run.sh test-primeri [N ...]       preveri primere iz navodil (komentarji »…« in /* -> … */ v blokih kode)
    ./run.sh pripravi-oddajo            združi kodo v eno datoteko (out/Skladkulator.java) in preveri, da se prevede
    ./run.sh clean                      izbriše mapo out

`test-zbirke` preveri izvedbe zbirk (paket `izvedbe`, Naloga0 in Naloga1), `test-primeri` pa stroj (razredi `EngineN`) s primeri iz navodil.

Primer: `./run.sh run 0 <<< "1 2 swap dump"`

## Namigi

### Razredi `Engine`*X*

Razredi `Engine`*X*, kjer je *X=0,...,5*, so namenjeni implementaciji skladovnega izvajalnega stroja. Vsak `Engine` razširja predhodnika. Pri tem povozi vsaj `eval` (po potrebi pa še `intercept` ali `fallback`) in doda nove zmožnosti. Tako se zmožnosti kopičijo in višja stopnja ohrani vse ukaze nižjih.

### Obdelava programov

Branje in obdelava simbolov sta v razredu `Engine0` (metode `run()`, `process()`, `intercept()`, `eval()` in `fallback()`); njihovo delovanje je opisano v javadocu metod.

### Izvajanje ukazov

- Argumenti ukaza so na glavnem skladu v istem vrstnem redu, kot so zapisani pri specifikaciji ukaza, zato jih s sklada vzemite v obratnem vrstnem redu: pri *x* *y* `+` najprej odstranite *y*, nato *x*.
- Števila so na skladu nizi. Pomožna metoda `popInt()` iz ogrodja odstrani vrh in ga pretvori v `int`, kar vam bo prihranilo veliko ponavljanja.
- `String.compareTo` vrne poljubno število (ne nujno -1, 0 ali 1) – za `strcmp` ga ustrezno preslikajte (poiščite metodo razreda `Integer`, ki naredi natanko to).
- `dump` izpiše sklad od dna proti vrhu in elemente sklada pusti nedotaknjene.

### Nedefinirana obnašanja

Včasih bomo pri nekaterih ukazih zapisali, da je določen del obnašanja nedefiniran. Ali še pogosteje, določenega dela obnašanja niti ne bomo opisali. Torej v navodilih ni podano, kako naj se ukaz v nekem (običajno robnem) primeru obnaša. Posledično se sami odločite, kako boste tak primer implementirali. Seveda primerov nedefiniranega obnašanja ne bomo avtomatsko testirali.
