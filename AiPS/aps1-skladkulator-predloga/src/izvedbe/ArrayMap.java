package izvedbe;

import zbirke.CollectionException;
import zbirke.Map;

/**
 * Izvedba vmesnika {@link zbirke.Map} z dvema vzporednima poljema (ključi in vrednosti) in linearnim iskanjem
 * po ključih. Kapaciteta je fiksna, določena ob ustvarjanju objekta. Vrstni red ključev pri toString ni določen.
 * <p>
 * Zahtevnost: put, get, remove, contains, containsValue, toString O(n); count, clear O(1).
 */
public class ArrayMap<T> implements Map<T> {
    // TODO: atributi

    public ArrayMap(int capacity) {
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
    public boolean contains(String x) {
        throw new UnsupportedOperationException("TODO");
    }

    /** Vrne vsebino zbirke kot niz, elementi so ločeni s presledkom (za izpis in razhroščevanje). */
    public String toString() {
        throw new UnsupportedOperationException("TODO");
    }

    // ---- Map ----

    /** Shrani vrednost pod ključem. Če ključ že obstaja, se vrednost prepiše (tudi če je slovar poln). Če je slovar poln in ključa ni, sproži CollectionException. */
    public void put(String key, T value) throws CollectionException {
        throw new UnsupportedOperationException("TODO");
    }

    /** Vrne vrednost, shranjeno pod ključem. Če ključa ni, sproži CollectionException. */
    public T get(String key) throws CollectionException {
        throw new UnsupportedOperationException("TODO");
    }

    /** Odstrani ključ in njegovo vrednost. Če ključa ni, sproži CollectionException. */
    public void remove(String key) throws CollectionException {
        throw new UnsupportedOperationException("TODO");
    }

    /** Ali slovar vsebuje vrednost value? */
    public boolean containsValue(T value) {
        throw new UnsupportedOperationException("TODO");
    }
}
