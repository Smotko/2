package izvedbe;

import zbirke.CollectionException;
import zbirke.Set;

/**
 * Izvedba vmesnika {@link zbirke.Set} s poljem in linearnim iskanjem. Kapaciteta je fiksna, določena ob
 * ustvarjanju objekta. Vrstni red elementov pri toString ni določen.
 * <p>
 * Zahtevnost: add, remove, contains, toString O(n); count, clear O(1).
 */
public class ArraySet<T> implements Set<T> {
    // TODO: atributi

    public ArraySet(int capacity) {
        // TODO
    }

    // ---- Collection ----

    /** Ali je zbirka prazna? */
    public boolean isEmpty() {
        throw new UnsupportedOperationException("TODO");
    }

    /** Ali je zbirka polna (število elementov je doseglo kapaciteto)? */
    public boolean isFull() {
        throw new UnsupportedOperationException("TODO");
    }

    /** Vrne število elementov v zbirki. */
    public int count() {
        throw new UnsupportedOperationException("TODO");
    }

    /** Izprazni zbirko. */
    public void clear() {
        throw new UnsupportedOperationException("TODO");
    }

    /** Ali zbirka vsebuje element x (pri slovarju: ključ x)? */
    public boolean contains(T x) {
        throw new UnsupportedOperationException("TODO");
    }

    /** Vrne vsebino zbirke kot niz, elementi so ločeni s presledkom (za izpis in razhroščevanje). */
    public String toString() {
        throw new UnsupportedOperationException("TODO");
    }

    // ---- Set ----

    /** Doda x v množico. Če x že obstaja, se stanje ne spremeni (tudi če je množica polna). Če je množica polna in x ne obstaja, sproži CollectionException. */
    public void add(T x) throws CollectionException {
        throw new UnsupportedOperationException("TODO");
    }

    /** Odstrani x iz množice. Če ga ni, sproži CollectionException. */
    public void remove(T x) throws CollectionException {
        throw new UnsupportedOperationException("TODO");
    }
}
