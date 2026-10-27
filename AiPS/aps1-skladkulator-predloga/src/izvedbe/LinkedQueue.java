package izvedbe;

import zbirke.CollectionException;
import zbirke.Queue;

/**
 * Izvedba vmesnika {@link zbirke.Queue} z enojno povezanim seznamom in z dvema kazalcema, ki kažeta na začetek
 * in konec vrste. Kapaciteta ni omejena (isFull() vedno vrne false). get(i)/set(i, x) se od začetka sprehodita
 * i korakov (indeks 0 = začetek).
 * <p>
 * Zahtevnost: enqueue, dequeue, front, clear O(1); get, set O(i); contains, toString O(n).
 */
public class LinkedQueue<T> implements Queue<T> {
    // TODO: atributi

    public LinkedQueue() {
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
}
