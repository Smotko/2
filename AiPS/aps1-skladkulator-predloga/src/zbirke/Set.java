package zbirke;

/**
 * Abstraktni podatkovni tip množica (brez podvojenih elementov).
 */
public interface Set<T> extends Collection<T> {
    /**
     * Doda x v množico. Če x že obstaja, se stanje ne spremeni (tudi če je množica polna).
     * Če je množica polna in x ne obstaja, sproži CollectionException.
     */
    void add(T x) throws CollectionException;

    /** Odstrani x iz množice. Če ga ni, sproži CollectionException. */
    void remove(T x) throws CollectionException;
}
