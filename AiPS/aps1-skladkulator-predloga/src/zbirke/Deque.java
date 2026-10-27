package zbirke;

/**
 * Abstraktni podatkovni tip dvostrana vrsta (deque) - razširja {@link Queue}
 * z operacijami na obeh koncih.
 */
public interface Deque<T> extends Queue<T> {
    /** Doda x na začetek vrste. Če je vrsta polna, sproži CollectionException. */
    void enqueueFront(T x) throws CollectionException;

    /** Odstrani in vrne element s konca vrste. Če je vrsta prazna, sproži CollectionException. */
    T dequeueBack() throws CollectionException;

    /** Vrne element s konca vrste brez odstranitve. Če je vrsta prazna, sproži CollectionException. */
    T back() throws CollectionException;
}
