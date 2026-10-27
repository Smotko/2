package izvedbe;

import zbirke.CollectionException;
import zbirke.Deque;

/**
 * Izvedba vmesnika {@link zbirke.Deque} s poljem (krožni pomnilnik, kot pri ArrayQueue), le da vstavljanje
 * in odstranjevanje potekata na obeh koncih. Kapaciteta je fiksna, določena ob ustvarjanju objekta.
 * <p>
 * Ker ArrayQueue že implementira polovico funkcionalnosti vmesnika Deque, naj ArrayDeque z dedovanjem razširja
 * ArrayQueue namesto podvajanja kode.
 * <p>
 * Zahtevnost: enqueue, dequeue, front, enqueueFront, dequeueBack, back, get, set, clear O(1);
 * contains, toString O(n).
 */
public class ArrayDeque<T> implements Deque<T> {
    // TODO: atributi

    public ArrayDeque(int capacity) {
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

    // ---- Indexed ----

    /** Vrne element na indeksu i. Za neveljaven indeks (negativen ali vsaj count()) sproži CollectionException. */
    public T get(int i) throws CollectionException {
        throw new UnsupportedOperationException("TODO");
    }

    /** Element na indeksu i zamenja z x. Za neveljaven indeks (negativen ali vsaj count()) sproži CollectionException. */
    public void set(int i, T x) throws CollectionException {
        throw new UnsupportedOperationException("TODO");
    }

    // ---- Queue ----

    /** Doda x na konec vrste. Če je vrsta polna, sproži CollectionException. */
    public void enqueue(T x) throws CollectionException {
        throw new UnsupportedOperationException("TODO");
    }

    /** Odstrani in vrne element z začetka vrste. Če je vrsta prazna, sproži CollectionException. */
    public T dequeue() throws CollectionException {
        throw new UnsupportedOperationException("TODO");
    }

    /** Vrne element z začetka vrste brez odstranitve. Če je vrsta prazna, sproži CollectionException. */
    public T front() throws CollectionException {
        throw new UnsupportedOperationException("TODO");
    }

    // ---- Deque ----

    /** Doda x na začetek vrste. Če je vrsta polna, sproži CollectionException. */
    public void enqueueFront(T x) throws CollectionException {
        throw new UnsupportedOperationException("TODO");
    }

    /** Odstrani in vrne element s konca vrste. Če je vrsta prazna, sproži CollectionException. */
    public T dequeueBack() throws CollectionException {
        throw new UnsupportedOperationException("TODO");
    }

    /** Vrne element s konca vrste brez odstranitve. Če je vrsta prazna, sproži CollectionException. */
    public T back() throws CollectionException {
        throw new UnsupportedOperationException("TODO");
    }
}
