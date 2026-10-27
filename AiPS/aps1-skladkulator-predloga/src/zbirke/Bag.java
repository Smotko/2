package zbirke;

/**
 * Abstraktni podatkovni tip mnogoterost/vreča (bag) - podvojeni elementi
 * so dovoljeni (za razliko od Set).
 */
public interface Bag<T> extends Collection<T> {
    /** Doda x v vrečo. Če je vreča polna, sproži CollectionException. */
    void add(T x) throws CollectionException;

    /** Odstrani eno pojavitev elementa x iz vreče. Če je ni, sproži CollectionException. */
    void remove(T x) throws CollectionException;
}
