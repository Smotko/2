package zbirke;

/**
 * Abstraktni podatkovni tip slovar (ključ je niz, vrednost poljubnega tipa T).
 * Elementi zbirke so ključi: contains(key), count() in toString() se nanašajo
 * na ključe. Za iskanje ključa je namenjen contains(key).
 */
public interface Map<T> extends Collection<String> {
    /**
     * Shrani vrednost pod ključem. Če ključ že obstaja, se vrednost prepiše (tudi če je slovar poln).
     * Če je slovar poln in ključa ni, sproži CollectionException.
     */
    void put(String key, T value) throws CollectionException;

    /** Vrne vrednost, shranjeno pod ključem. Če ključa ni, sproži CollectionException. */
    T get(String key) throws CollectionException;

    /** Odstrani ključ in njegovo vrednost. Če ključa ni, sproži CollectionException. */
    void remove(String key) throws CollectionException;

    /** Ali slovar vsebuje vrednost value? */
    boolean containsValue(T value);
}
