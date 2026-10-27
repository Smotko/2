package zbirke;

/**
 * Abstraktni podatkovni tip vrsta (FIFO - First In, First Out).
 */
public interface Queue<T> extends Indexed<T> {
    /** Doda x na konec vrste. Če je vrsta polna, sproži CollectionException. */
    void enqueue(T x) throws CollectionException;

    /** Odstrani in vrne element z začetka vrste. Če je vrsta prazna, sproži CollectionException. */
    T dequeue() throws CollectionException;

    /** Vrne element z začetka vrste brez odstranitve. Če je vrsta prazna, sproži CollectionException. */
    T front() throws CollectionException;
}
