package stroj;
import zbirke.Collection;
import zbirke.CollectionException;
import zbirke.Deque;
import zbirke.Indexed;
import zbirke.Map;
import zbirke.Queue;
import zbirke.Stack;
import izvedbe.ArrayDeque;
import izvedbe.ArrayMap;
import izvedbe.ArrayQueue;
import izvedbe.ArrayStack;
import izvedbe.LinkedDeque;
import izvedbe.LinkedQueue;
import izvedbe.LinkedStack;

/**
 * Directory je nabor poimenovanih zbirk: preslikava imena v zbirko (slovar {@code Map<Collection<String>>}). To so pomožne zbirke,
 * ločene od glavnega sklada. Zbirko ustvari create, vrsto zbirke pa določa ukaz, ki jo ustvari:
 *
 *   as, ls (sklad s poljem, s kazalci), aq, lq (vrsta), ad, ld (dvostrana vrsta) in fun (funkcija).
 *
 * Funkcija je zbirka razreda Directory.Function (podrazred {@code ArrayQueue<String>}), ki je že podan. Zbirke, ki jih ni v
 * slovarju, ni: get zanjo sproži CollectionException.
 */
public class Directory {
    /** Funkcija: vrsta nizov (simbolov telesa), ki se izvede, kadar se v programu pojavi njeno ime. Od navadne vrste jo loči samo tip. */
    public static class Function extends ArrayQueue<String> {
        public Function(int capacity) {
            super(capacity);
        }
    }

    private Map<Collection<String>> zbirke;
    private int capacity;

    /** Directory za največ count zbirk; zbirke, izvedene s poljem, imajo kapaciteto capacity. */
    public Directory(int count, int capacity) {
        // TODO: ustvarite slovar zbirke (ArrayMap) in si zapomnite kapaciteto.
        throw new UnsupportedOperationException("TODO");
    }

    /** Ali je ime obstoječa funkcija, torej zbirka razreda Function (Directory.Function). */
    public boolean isFunction(String name) throws CollectionException {
        // TODO: zbirka s tem imenom obstaja in je razreda Function (instanceof).
        throw new UnsupportedOperationException("TODO");
    }

    /** Ali zbirka s tem imenom obstaja. */
    public boolean exists(String name) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Zbirka z danim imenom; če ne obstaja, sproži CollectionException. */
    public Collection<String> get(String name) throws CollectionException {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Zbirka z danim imenom kot {@code Indexed<String>}. To je skupni vmesnik vseh vrst zbirk (Stack, Queue in Deque
     * vsi razširjajo Indexed), zato ga lahko uporabimo povsod, kjer vrsta zbirke ni pomembna.
     */
    @SuppressWarnings("unchecked")
    public Indexed<String> indexed(String name) throws CollectionException {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** V zbirki zamenja elementa na indeksih i in j - glej Indexed.get() in Indexed.set(). */
    public void swap(String name, int i, int j) throws CollectionException {
        // TODO: uporabite indexed(name).
        throw new UnsupportedOperationException("TODO");
    }

    /** Doda x v zbirko: push (sklad) ali enqueue (vrsta, tudi dvostrana, na zadnji konec). */
    @SuppressWarnings("unchecked")
    public void store(String name, String x) throws CollectionException {
        // TODO: glede na vrsto zbirke (instanceof Stack) kličite push ali enqueue.
        throw new UnsupportedOperationException("TODO");
    }

    /** Odstrani in vrne element iz zbirke: pop (sklad) ali dequeue (vrsta, tudi dvostrana, s sprednjega konca). */
    @SuppressWarnings("unchecked")
    public String load(String name) throws CollectionException {
        // TODO: glede na vrsto zbirke kličite pop ali dequeue.
        throw new UnsupportedOperationException("TODO");
    }

    /** Odstrani in vrne zadnji element zbirke: pop (sklad) ali dequeueBack (dvostrana vrsta). Navadna vrsta tega ne zmore. */
    @SuppressWarnings("unchecked")
    public String loadLast(String name) throws CollectionException {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Doda x na sprednji konec dvostrane vrste. */
    @SuppressWarnings("unchecked")
    public void storeFront(String name, String x) throws CollectionException {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Odstrani in vrne element z zadnjega konca dvostrane vrste. */
    @SuppressWarnings("unchecked")
    public String loadBack(String name) throws CollectionException {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Ustvari prazno zbirko vrste type (as, ls, aq, lq, ad, ld ali fun) in jo vstavi v slovar pod ime name;
     * obstoječo zbirko s tem imenom nadomesti. Za neznano vrsto sproži CollectionException.
     */
    public void create(String name, String type) throws CollectionException {
        // TODO: glede na type ustvarite ustrezno zbirko (zbirke s poljem naj imajo kapaciteto capacity).
        // Pri Naloga1 podprite as, aq, ad in fun; pri Naloga2 dodajte še ls, lq in ld.
        throw new UnsupportedOperationException("TODO");
    }
}
