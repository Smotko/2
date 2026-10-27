package testi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Consumer;

import zbirke.CollectionException;
import zbirke.Deque;
import zbirke.Map;
import zbirke.Queue;
import zbirke.Set;
import zbirke.Stack;

/**
 * Preprost program za samostojno preverjanje vaših izvedb zbirk (paket izvedbe).
 * <p>
 * Zagon (iz mape projekta, v kateri je src; ukaz kodo tudi prevede):
 * <ul>
 *   <li>./run.sh test-zbirke - vse obstoječe izvedbe</li>
 *   <li>./run.sh test-zbirke ArrayQueue LinkedStack - samo navedene izvedbe</li>
 * </ul>
 * Podprta imena: ArrayStack, LinkedStack, ArrayQueue, LinkedQueue, ArrayDeque, LinkedDeque, ArraySet in ArrayMap.
 * Ročno: javac -d out src/zbirke/*.java src/izvedbe/*.java src/testi/TestZbirke.java in
 * java -cp out testi.TestZbirke [Izvedba ...].
 * <p>
 * Kaj program naredi:
 * <ul>
 *   <li>Za vsako izvedbo poišče razred izvedbe.&lt;Ime&gt;. Če razreda še ni, izvedbo preskoči, zato lahko program
 *       uporabljate sproti, ko pišete posamezne izvedbe.</li>
 *   <li>Primerek zbirke ustvari z refleksijo, zato mora imeti razred javni konstruktor: (int capacity) pri izvedbah
 *       s poljem (omejena kapaciteta, testira se tudi isFull() in sprožanje izjem pri polni zbirki) oziroma brez
 *       parametrov pri izvedbah s kazalci (kapaciteta ni omejena, isFull() je vedno false).</li>
 *   <li>Za vsako zbirko požene nabor testov: osnovne operacije, toString, isEmpty, count, clear, contains, get/set,
 *       izjeme pri prazni ali polni zbirki in pri indeksih izven meja. Pri vrsti in dvostrani vrsti so dodani še
 *       testi krožnega pomnilnika (enqueue in dequeue čez konec polja), ki so smiselni predvsem za izvedbe s poljem.
 *       Dvostrana vrsta se poleg lastnih testov preveri še z vsemi testi za vrsto.</li>
 *   <li>Izjeme preverja v skladu s pravili iz javadoca razreda CollectionException: pričakovana je
 *       CollectionException, druga izjema šteje za napako.</li>
 *   <li>Pri ArraySet in ArrayMap test toString ne preverja vrstnega reda, saj ta ni določen (glej javadoc metode
 *       Collection.toString). Pri slovarju preveri le izpis ključev.</li>
 * </ul>
 * Izpis: vsak test izpiše OK ali NAPAKA, na koncu pa program izpiše skupno število uspešnih in neuspešnih testov.
 * Pri napaki je izpisano, kaj je bilo preverjano, kaj je bilo pričakovano in kaj dobljeno:
 * <pre>
 * ArrayStack
 *   OK      push, pop, top
 *   NAPAKA  toString
 *           toString() po push(1), push(2), push(3): pričakovano "1 2 3", dobljeno "3 2 1"
 *   OK      isEmpty, count, clear
 *   ...
 *
 * Skupaj: 7 OK, 1 NAPAKA
 * </pre>
 * Če izvedba sproži nepričakovano izjemo, je v oklepaju izpisana še datoteka in vrstica v paketu izvedbe, kjer je
 * izjema nastala (npr. (ArrayStack.java:23)), kar običajno pokaže mesto napake.
 * <p>
 * Opozorila: program ne nadomešča lastnega razmisleka o robnih primerih. Ko vsi testi uspejo, to še ne pomeni,
 * da je izvedba pravilna - dopolnite ga s svojimi testi. Program je bil v celoti generiran z UI agentom (sicer na
 * podlagi ročno preverjene kode); uporabljajte ga na lastno odgovornost in pamet.
 */
public class TestZbirke {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        implementation("ArrayStack", args, TestZbirke::stackTests);
        implementation("LinkedStack", args, TestZbirke::stackTests);
        implementation("ArrayQueue", args, TestZbirke::queueTests);
        implementation("LinkedQueue", args, TestZbirke::queueTests);
        implementation("ArrayDeque", args, TestZbirke::dequeTests);
        implementation("LinkedDeque", args, TestZbirke::dequeTests);
        implementation("ArraySet", args, TestZbirke::setTests);
        implementation("ArrayMap", args, TestZbirke::mapTests);
        System.out.printf("%nSkupaj: %d OK, %d NAPAKA%n", passed, failed);
    }

    // ------------------------------------------------------------------
    // Sklad
    // ------------------------------------------------------------------

    private static void stackTests(Impl impl) {
        check("push, pop, top", () -> {
            Stack<Integer> s = impl.create(5);
            s.push(1);
            s.push(2);
            s.push(3);
            expectEquals("top()", 3, s.top());
            expectEquals("count() po treh push", 3, s.count());
            expectEquals("pop()", 3, s.pop());
            expectEquals("pop()", 2, s.pop());
            expectEquals("top() po dveh pop", 1, s.top());
            expectEquals("count() po dveh pop", 1, s.count());
        });
        check("toString", () -> {
            Stack<Integer> s = impl.create(5);
            expectEquals("toString() prazne zbirke", "", s.toString());
            s.push(1);
            s.push(2);
            s.push(3);
            expectEquals("toString() po push(1), push(2), push(3)", "1 2 3", s.toString());
        });
        check("isEmpty, count, clear", () -> {
            Stack<Integer> s = impl.create(5);
            expectEquals("isEmpty() nove zbirke", true, s.isEmpty());
            expectEquals("count() nove zbirke", 0, s.count());
            s.push(1);
            expectEquals("isEmpty() po push", false, s.isEmpty());
            s.clear();
            expectEquals("isEmpty() po clear", true, s.isEmpty());
            expectEquals("count() po clear", 0, s.count());
            expectEquals("toString() po clear", "", s.toString());
            s.push(7);
            expectEquals("top() po clear in push(7)", 7, s.top());
        });
        check("izjeme pri prazni zbirki", () -> {
            Stack<Integer> s = impl.create(5);
            expectException("pop() na praznem skladu", () -> s.pop());
            expectException("top() na praznem skladu", () -> s.top());
            s.push(1);
            s.pop();
            expectException("pop() po izpraznitvi", () -> s.pop());
        });
        check("kapaciteta (isFull, polna zbirka)", () -> {
            Stack<Integer> s = impl.create(3);
            expectEquals("isFull() prazne zbirke", false, s.isFull());
            s.push(1);
            s.push(2);
            if (impl.bounded) {
                expectEquals("isFull() pred zadnjim push", false, s.isFull());
                s.push(3);
                expectEquals("isFull() pri kapaciteti 3 s tremi elementi", true, s.isFull());
                expectException("push() na polnem skladu", () -> s.push(4));
                expectEquals("count() po neuspešnem push", 3, s.count());
                s.pop();
                expectEquals("isFull() po pop", false, s.isFull());
                s.push(5);
                expectEquals("top() po pop in push(5)", 5, s.top());
            } else {
                for (int i = 0; i < 1000; i++) s.push(i);
                expectEquals("isFull() pri izvedbi s kazalci", false, s.isFull());
                expectEquals("count() po 1002 push", 1002, s.count());
            }
        });
        check("get, set", () -> {
            Stack<Integer> s = impl.create(5);
            s.push(10);
            s.push(20);
            s.push(30);
            expectEquals("get(0) - dno sklada", 10, s.get(0));
            expectEquals("get(2) - vrh sklada", 30, s.get(2));
            s.set(1, 99);
            expectEquals("get(1) po set(1, 99)", 99, s.get(1));
            expectEquals("toString() po set(1, 99)", "10 99 30", s.toString());
            expectEquals("top() po set", 30, s.top());
        });
        check("izjeme pri get in set izven meja", () -> {
            Stack<Integer> s = impl.create(5);
            s.push(1);
            s.push(2);
            expectException("get(-1)", () -> s.get(-1));
            expectException("get(count())", () -> s.get(s.count()));
            expectException("set(-1, 0)", () -> s.set(-1, 0));
            expectException("set(count(), 0)", () -> s.set(s.count(), 0));
        });
        check("contains", () -> {
            Stack<Integer> s = impl.create(5);
            s.push(1);
            s.push(2);
            expectEquals("contains(2)", true, s.contains(2));
            expectEquals("contains(3)", false, s.contains(3));
            s.pop();
            expectEquals("contains(2) po pop", false, s.contains(2));
        });
    }

    // ------------------------------------------------------------------
    // Vrsta
    // ------------------------------------------------------------------

    private static void queueTests(Impl impl) {
        check("enqueue, dequeue, front", () -> {
            Queue<Integer> q = impl.create(5);
            q.enqueue(1);
            q.enqueue(2);
            q.enqueue(3);
            expectEquals("front()", 1, q.front());
            expectEquals("count() po treh enqueue", 3, q.count());
            expectEquals("dequeue()", 1, q.dequeue());
            expectEquals("dequeue()", 2, q.dequeue());
            expectEquals("front() po dveh dequeue", 3, q.front());
            expectEquals("count() po dveh dequeue", 1, q.count());
        });
        check("toString", () -> {
            Queue<Integer> q = impl.create(5);
            expectEquals("toString() prazne zbirke", "", q.toString());
            q.enqueue(1);
            q.enqueue(2);
            q.enqueue(3);
            expectEquals("toString() po enqueue(1), enqueue(2), enqueue(3)", "1 2 3", q.toString());
        });
        check("isEmpty, count, clear", () -> {
            Queue<Integer> q = impl.create(5);
            expectEquals("isEmpty() nove zbirke", true, q.isEmpty());
            q.enqueue(1);
            expectEquals("isEmpty() po enqueue", false, q.isEmpty());
            q.clear();
            expectEquals("isEmpty() po clear", true, q.isEmpty());
            expectEquals("count() po clear", 0, q.count());
            expectEquals("toString() po clear", "", q.toString());
            q.enqueue(7);
            expectEquals("front() po clear in enqueue(7)", 7, q.front());
        });
        check("izjeme pri prazni zbirki", () -> {
            Queue<Integer> q = impl.create(5);
            expectException("dequeue() na prazni vrsti", () -> q.dequeue());
            expectException("front() na prazni vrsti", () -> q.front());
            q.enqueue(1);
            q.dequeue();
            expectException("dequeue() po izpraznitvi", () -> q.dequeue());
        });
        check("kapaciteta (isFull, polna zbirka)", () -> {
            Queue<Integer> q = impl.create(3);
            expectEquals("isFull() prazne zbirke", false, q.isFull());
            q.enqueue(1);
            q.enqueue(2);
            if (impl.bounded) {
                q.enqueue(3);
                expectEquals("isFull() pri kapaciteti 3 s tremi elementi", true, q.isFull());
                expectException("enqueue() na polni vrsti", () -> q.enqueue(4));
                expectEquals("count() po neuspešnem enqueue", 3, q.count());
                q.dequeue();
                expectEquals("isFull() po dequeue", false, q.isFull());
                q.enqueue(5);
                expectEquals("toString() po dequeue in enqueue(5)", "2 3 5", q.toString());
            } else {
                for (int i = 0; i < 1000; i++) q.enqueue(i);
                expectEquals("isFull() pri izvedbi s kazalci", false, q.isFull());
                expectEquals("count() po 1002 enqueue", 1002, q.count());
            }
        });
        check("get, set", () -> {
            Queue<Integer> q = impl.create(5);
            q.enqueue(10);
            q.enqueue(20);
            q.enqueue(30);
            q.dequeue();
            q.enqueue(40);
            expectEquals("get(0) - začetek vrste", 20, q.get(0));
            expectEquals("get(2) - konec vrste", 40, q.get(2));
            q.set(1, 99);
            expectEquals("toString() po set(1, 99)", "20 99 40", q.toString());
            expectEquals("front() po set", 20, q.front());
        });
        check("izjeme pri get in set izven meja", () -> {
            Queue<Integer> q = impl.create(5);
            q.enqueue(1);
            q.enqueue(2);
            expectException("get(-1)", () -> q.get(-1));
            expectException("get(count())", () -> q.get(q.count()));
            expectException("set(-1, 0)", () -> q.set(-1, 0));
            expectException("set(count(), 0)", () -> q.set(q.count(), 0));
        });
        check("contains", () -> {
            Queue<Integer> q = impl.create(5);
            q.enqueue(1);
            q.enqueue(2);
            expectEquals("contains(1)", true, q.contains(1));
            expectEquals("contains(3)", false, q.contains(3));
            q.dequeue();
            expectEquals("contains(1) po dequeue", false, q.contains(1));
        });
        check("krožni pomnilnik: enqueue in dequeue čez konec polja", () -> {
            Queue<Integer> q = impl.create(3);
            q.enqueue(1);
            q.enqueue(2);
            q.enqueue(3);
            expectEquals("dequeue()", 1, q.dequeue());
            q.enqueue(4);
            expectEquals("toString() po enqueue(1,2,3), dequeue(), enqueue(4)", "2 3 4", q.toString());
            expectEquals("get(0)", 2, q.get(0));
            expectEquals("get(2)", 4, q.get(2));
            expectEquals("contains(4)", true, q.contains(4));
            for (int k = 0; k < 20; k++) {
                expectEquals("dequeue() v " + k + ". krogu", 2 + k, q.dequeue());
                q.enqueue(5 + k);
            }
            expectEquals("toString() po 20 krogih dequeue in enqueue", "22 23 24", q.toString());
        });
        check("izpraznitev in ponovna uporaba", () -> {
            Queue<Integer> q = impl.create(3);
            q.enqueue(1);
            q.enqueue(2);
            q.dequeue();
            q.dequeue();
            expectEquals("isEmpty() po izpraznitvi", true, q.isEmpty());
            q.enqueue(3);
            expectEquals("front() po ponovnem enqueue(3)", 3, q.front());
            expectEquals("toString() po ponovnem enqueue(3)", "3", q.toString());
            expectEquals("count() po ponovnem enqueue(3)", 1, q.count());
        });
        check("clear po krožnem premiku in ponovna uporaba", () -> {
            Queue<Integer> q = impl.create(3);
            q.enqueue(1);
            q.enqueue(2);
            q.enqueue(3);
            q.dequeue();
            q.enqueue(4);
            q.clear();
            expectEquals("isEmpty() po clear", true, q.isEmpty());
            q.enqueue(7);
            q.enqueue(8);
            expectEquals("toString() po clear in enqueue(7), enqueue(8)", "7 8", q.toString());
            expectEquals("dequeue()", 7, q.dequeue());
        });
    }

    // ------------------------------------------------------------------
    // Dvostrana vrsta (poleg testov za vrsto)
    // ------------------------------------------------------------------

    private static void dequeTests(Impl impl) {
        queueTests(impl);
        check("enqueueFront, dequeueBack, back", () -> {
            Deque<Integer> d = impl.create(5);
            d.enqueue(1);
            d.enqueueFront(0);
            d.enqueueFront(-1);
            expectEquals("toString() po enqueue(1), enqueueFront(0), enqueueFront(-1)", "-1 0 1", d.toString());
            expectEquals("front()", -1, d.front());
            expectEquals("back()", 1, d.back());
            expectEquals("get(0)", -1, d.get(0));
            expectEquals("dequeueBack()", 1, d.dequeueBack());
            expectEquals("dequeueBack()", 0, d.dequeueBack());
            expectEquals("toString() po dveh dequeueBack", "-1", d.toString());
            expectEquals("dequeue()", -1, d.dequeue());
            expectEquals("isEmpty()", true, d.isEmpty());
        });
        check("izjeme pri prazni zbirki (dequeueBack, back)", () -> {
            Deque<Integer> d = impl.create(5);
            expectException("dequeueBack() na prazni zbirki", () -> d.dequeueBack());
            expectException("back() na prazni zbirki", () -> d.back());
            d.enqueueFront(1);
            d.dequeueBack();
            expectException("dequeueBack() po izpraznitvi", () -> d.dequeueBack());
        });
        check("kapaciteta pri enqueueFront", () -> {
            Deque<Integer> d = impl.create(3);
            d.enqueueFront(1);
            d.enqueueFront(2);
            if (impl.bounded) {
                d.enqueueFront(3);
                expectEquals("isFull()", true, d.isFull());
                expectException("enqueueFront() na polni zbirki", () -> d.enqueueFront(4));
                expectException("enqueue() na polni zbirki", () -> d.enqueue(4));
                expectEquals("toString() po neuspešnih vstavljanjih", "3 2 1", d.toString());
            } else {
                for (int i = 0; i < 1000; i++) d.enqueueFront(i);
                expectEquals("isFull() pri izvedbi s kazalci", false, d.isFull());
            }
        });
        check("krožni pomnilnik: enqueueFront in dequeueBack čez začetek polja", () -> {
            Deque<Integer> d = impl.create(3);
            d.enqueueFront(1);
            d.enqueueFront(2);
            d.enqueueFront(3);
            expectEquals("toString() po enqueueFront(1,2,3)", "3 2 1", d.toString());
            expectEquals("dequeueBack()", 1, d.dequeueBack());
            d.enqueueFront(4);
            expectEquals("toString() po dequeueBack() in enqueueFront(4)", "4 3 2", d.toString());
            expectEquals("get(0)", 4, d.get(0));
            expectEquals("back()", 2, d.back());
            for (int k = 0; k < 20; k++) {
                int expected = k == 0 ? 2 : k == 1 ? 3 : k == 2 ? 4 : 100 + (k - 3);
                expectEquals("dequeueBack() v " + k + ". krogu", expected, d.dequeueBack());
                d.enqueueFront(100 + k);
            }
            expectEquals("toString() po 20 krogih", "119 118 117", d.toString());
        });
        check("praznjenje z obeh koncev in ponovna uporaba", () -> {
            Deque<Integer> d = impl.create(3);
            d.enqueue(1);
            d.enqueue(2);
            d.enqueue(3);
            expectEquals("dequeue()", 1, d.dequeue());
            expectEquals("dequeueBack()", 3, d.dequeueBack());
            expectEquals("dequeue()", 2, d.dequeue());
            expectEquals("isEmpty()", true, d.isEmpty());
            d.enqueueFront(5);
            d.enqueue(6);
            expectEquals("toString() po enqueueFront(5), enqueue(6)", "5 6", d.toString());
            expectEquals("back()", 6, d.back());
            expectEquals("front()", 5, d.front());
        });
        check("ponovna uporaba po izpraznitvi z dequeueBack", () -> {
            Deque<Integer> d = impl.create(3);
            d.enqueue(1);
            d.dequeueBack();
            expectEquals("isEmpty() po izpraznitvi z dequeueBack", true, d.isEmpty());
            d.enqueueFront(9);
            expectEquals("toString() po enqueueFront(9) v prazno zbirko", "9", d.toString());
            expectEquals("front()", 9, d.front());
            expectEquals("back()", 9, d.back());
            d.dequeueBack();
            d.enqueue(8);
            expectEquals("toString() po enqueue(8) v prazno zbirko", "8", d.toString());
            expectEquals("count()", 1, d.count());
        });
        check("mešano dodajanje in odstranjevanje na obeh koncih", () -> {
            Deque<Integer> d = impl.create(4);
            d.enqueue(2);
            d.enqueueFront(1);
            d.enqueue(3);
            expectEquals("dequeueBack()", 3, d.dequeueBack());
            d.enqueueFront(0);
            expectEquals("toString()", "0 1 2", d.toString());
            expectEquals("dequeue()", 0, d.dequeue());
            expectEquals("dequeueBack()", 2, d.dequeueBack());
            expectEquals("toString() na koncu", "1", d.toString());
            expectEquals("count()", 1, d.count());
        });
    }

    // ------------------------------------------------------------------
    // Množica
    // ------------------------------------------------------------------

    private static void setTests(Impl impl) {
        check("add, contains, count", () -> {
            Set<Integer> s = impl.create(5);
            expectEquals("isEmpty() nove zbirke", true, s.isEmpty());
            s.add(1);
            s.add(2);
            expectEquals("count() po add(1), add(2)", 2, s.count());
            expectEquals("contains(1)", true, s.contains(1));
            expectEquals("contains(3)", false, s.contains(3));
        });
        check("add obstoječega elementa ne spremeni množice", () -> {
            Set<Integer> s = impl.create(5);
            s.add(1);
            s.add(2);
            s.add(1);
            expectEquals("count() po add(1), add(2), add(1)", 2, s.count());
        });
        check("remove", () -> {
            Set<Integer> s = impl.create(5);
            s.add(1);
            s.add(2);
            s.remove(1);
            expectEquals("contains(1) po remove(1)", false, s.contains(1));
            expectEquals("contains(2) po remove(1)", true, s.contains(2));
            expectEquals("count() po remove(1)", 1, s.count());
            expectEquals("toString() po remove(1)", "2", s.toString());
        });
        check("remove neobstoječega elementa sproži izjemo", () -> {
            Set<Integer> s = impl.create(5);
            s.add(1);
            expectException("remove(7), ki ga ni v množici", () -> s.remove(7));
            expectEquals("count() po neuspešnem remove", 1, s.count());
        });
        check("toString vsebuje vse elemente, ločene s presledkom", () -> {
            Set<Integer> s = impl.create(5);
            expectEquals("toString() prazne množice", "", s.toString());
            s.add(1);
            expectEquals("toString() z enim elementom", "1", s.toString());
            s.add(2);
            s.add(3);
            String[] parts = s.toString().split(" ");
            expectEquals("število elementov v toString() po add(1), add(2), add(3)", 3, parts.length);
            expectEquals("toString() vsebuje 1", true, s.toString().matches(".*\\b1\\b.*"));
            expectEquals("toString() vsebuje 2", true, s.toString().matches(".*\\b2\\b.*"));
            expectEquals("toString() vsebuje 3", true, s.toString().matches(".*\\b3\\b.*"));
        });
        check("clear", () -> {
            Set<Integer> s = impl.create(5);
            s.add(1);
            s.add(2);
            s.clear();
            expectEquals("isEmpty() po clear", true, s.isEmpty());
            expectEquals("contains(1) po clear", false, s.contains(1));
            s.add(1);
            expectEquals("count() po clear in add(1)", 1, s.count());
        });
        check("kapaciteta (isFull, polna množica)", () -> {
            Set<Integer> s = impl.create(3);
            s.add(1);
            s.add(2);
            expectEquals("isFull() pred zadnjim add", false, s.isFull());
            s.add(3);
            expectEquals("isFull() pri kapaciteti 3 s tremi elementi", true, s.isFull());
            s.add(2);
            expectEquals("count() po add obstoječega elementa na polni množici", 3, s.count());
            expectException("add(4) na polni množici", () -> s.add(4));
            s.remove(1);
            expectEquals("isFull() po remove", false, s.isFull());
            s.add(4);
            expectEquals("contains(4) po remove in add(4)", true, s.contains(4));
        });
    }

    // ------------------------------------------------------------------
    // Slovar (ključi so nizi, vrednosti nizi)
    // ------------------------------------------------------------------

    private static void mapTests(Impl impl) {
        check("put, get, count", () -> {
            Map<String> m = impl.create(5);
            expectEquals("isEmpty() nove zbirke", true, m.isEmpty());
            m.put("a", "x");
            m.put("b", "y");
            expectEquals("get(\"a\")", "x", m.get("a"));
            expectEquals("get(\"b\")", "y", m.get("b"));
            expectEquals("count() po dveh put", 2, m.count());
        });
        check("put z obstoječim ključem prepiše vrednost", () -> {
            Map<String> m = impl.create(5);
            m.put("a", "x");
            m.put("a", "z");
            expectEquals("get(\"a\") po drugem put", "z", m.get("a"));
            expectEquals("count() po drugem put z istim ključem", 1, m.count());
        });
        check("contains išče po ključih, containsValue po vrednostih", () -> {
            Map<String> m = impl.create(5);
            m.put("a", "x");
            m.put("b", "y");
            expectEquals("contains(\"a\") - ključ obstaja", true, m.contains("a"));
            expectEquals("contains(\"x\") - \"x\" je vrednost, ne ključ", false, m.contains("x"));
            expectEquals("containsValue(\"x\")", true, m.containsValue("x"));
            expectEquals("containsValue(\"a\") - \"a\" je ključ, ne vrednost", false, m.containsValue("a"));
        });
        check("remove", () -> {
            Map<String> m = impl.create(5);
            m.put("a", "x");
            m.put("b", "y");
            m.remove("a");
            expectEquals("contains(\"a\") po remove", false, m.contains("a"));
            expectEquals("get(\"b\") po remove(\"a\")", "y", m.get("b"));
            expectEquals("count() po remove", 1, m.count());
            expectEquals("containsValue(\"x\") po remove(\"a\")", false, m.containsValue("x"));
        });
        check("izjeme pri neobstoječem ključu", () -> {
            Map<String> m = impl.create(5);
            m.put("a", "x");
            expectException("get(\"q\") - ključa ni", () -> m.get("q"));
            expectException("remove(\"q\") - ključa ni", () -> m.remove("q"));
            expectEquals("count() po neuspešnih klicih", 1, m.count());
        });
        check("toString izpiše ključe", () -> {
            Map<String> m = impl.create(5);
            expectEquals("toString() prazne zbirke", "", m.toString());
            m.put("a", "x");
            expectEquals("toString() z enim parom", "a", m.toString());
        });
        check("clear", () -> {
            Map<String> m = impl.create(5);
            m.put("a", "x");
            m.clear();
            expectEquals("isEmpty() po clear", true, m.isEmpty());
            expectEquals("contains(\"a\") po clear", false, m.contains("a"));
            m.put("a", "y");
            expectEquals("get(\"a\") po clear in put", "y", m.get("a"));
        });
        check("kapaciteta (isFull, poln slovar)", () -> {
            Map<String> m = impl.create(3);
            m.put("a", "1");
            m.put("b", "2");
            expectEquals("isFull() pred zadnjim put", false, m.isFull());
            m.put("c", "3");
            expectEquals("isFull() pri kapaciteti 3 s tremi pari", true, m.isFull());
            m.put("a", "9");
            expectEquals("get(\"a\") po put obstoječega ključa na polnem slovarju", "9", m.get("a"));
            expectException("put(\"d\", ...) na polnem slovarju", () -> m.put("d", "4"));
            m.remove("b");
            expectEquals("isFull() po remove", false, m.isFull());
            m.put("d", "4");
            expectEquals("get(\"d\") po remove in put", "4", m.get("d"));
        });
    }

    // ------------------------------------------------------------------
    // Pomožno: izvedbe, preverjanje, izpis
    // ------------------------------------------------------------------

    /** Izvedba zbirke, najdena z refleksijo: z (int) kapaciteto ali brez parametrov. */
    private static class Impl {
        final Constructor<?> constructor;
        final boolean bounded;

        Impl(Constructor<?> constructor, boolean bounded) {
            this.constructor = constructor;
            this.bounded = bounded;
        }

        @SuppressWarnings("unchecked")
        <C> C create(int capacity) {
            try {
                return (C) (bounded ? constructor.newInstance(capacity) : constructor.newInstance());
            } catch (InvocationTargetException e) {
                throw new RuntimeException("konstruktor je sprožil " + e.getCause(), e.getCause());
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private interface Body {
        void run() throws Exception;
    }

    private static class Failure extends RuntimeException {
        Failure(String message) {
            super(message);
        }
    }

    private static void implementation(String name, String[] args, Consumer<Impl> suite) {
        if (args.length > 0 && !java.util.Arrays.asList(args).contains(name)) return;
        System.out.println(name);
        Class<?> cls;
        try {
            cls = Class.forName("izvedbe." + name);
        } catch (ClassNotFoundException e) {
            System.out.println("  preskočeno (razred izvedbe." + name + " ne obstaja)");
            return;
        }
        Impl impl;
        try {
            impl = new Impl(cls.getConstructor(int.class), true);
        } catch (NoSuchMethodException e1) {
            try {
                impl = new Impl(cls.getConstructor(), false);
            } catch (NoSuchMethodException e2) {
                System.out.println("  preskočeno (ni javnega konstruktorja z (int capacity) ali brez parametrov)");
                return;
            }
        }
        suite.accept(impl);
    }

    private static void check(String name, Body body) {
        try {
            body.run();
            passed++;
            System.out.println("  OK      " + name);
        } catch (Failure f) {
            fail(name, f.getMessage());
        } catch (CollectionException e) {
            fail(name, "nepričakovana CollectionException: " + e.getMessage() + where(e));
        } catch (StackOverflowError e) {
            fail(name, "StackOverflowError (preglobok klic rekurzije?)" + where(e));
        } catch (Throwable t) {
            fail(name, "nepričakovana izjema " + t + where(t));
        }
    }

    private static void fail(String name, String message) {
        failed++;
        System.out.println("  NAPAKA  " + name);
        System.out.println("          " + message);
    }

    /** Prva vrstica sledi klicev, ki je v paketu izvedbe (kjer je verjetno napaka). */
    private static String where(Throwable t) {
        for (StackTraceElement el : t.getStackTrace()) {
            if (el.getClassName().startsWith("izvedbe.")) {
                return " (" + el.getFileName() + ":" + el.getLineNumber() + ")";
            }
        }
        return "";
    }

    private static void expectEquals(String what, Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new Failure(what + ": pričakovano " + show(expected) + ", dobljeno " + show(actual));
        }
    }

    private static void expectException(String what, Body body) {
        try {
            body.run();
        } catch (CollectionException e) {
            return;
        } catch (Exception e) {
            throw new Failure(what + ": pričakovana CollectionException, sprožena " + e);
        }
        throw new Failure(what + ": pričakovana CollectionException, izjema ni bila sprožena");
    }

    private static String show(Object o) {
        return o instanceof String ? "\"" + o + "\"" : String.valueOf(o);
    }
}
