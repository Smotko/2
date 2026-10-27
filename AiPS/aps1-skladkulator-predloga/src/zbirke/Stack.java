package zbirke;

/**
 * Abstraktni podatkovni tip sklad (LIFO - Last In, First Out).
 */
public interface Stack<T> extends Indexed<T> {
    /** Doda x na vrh sklada. Če je sklad poln, sproži CollectionException. */
    void push(T x) throws CollectionException;

    /** Odstrani in vrne vrh sklada. Če je sklad prazen, sproži CollectionException. */
    T pop() throws CollectionException;

    /** Vrne vrh sklada brez odstranitve. Če je sklad prazen, sproži CollectionException. */
    T top() throws CollectionException;
}
