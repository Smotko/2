package izvedbe;

import zbirke.CollectionException;
import zbirke.Stack;

/**
 * Izvedba vmesnika {@link zbirke.Stack} z enojno povezanim seznamom in s kazalcem, ki kaže na vrh sklada oz.
 * začetek seznama. Kapaciteta ni omejena (isFull() vedno vrne false).
 * <p>
 * Vrstni red pri toString() je od dna do vrha sklada, torej nasproten smeri sprehoda po seznamu (od vrha proti
 * dnu); za gradnjo niza priporočamo rekurzijo. Enako velja za get(i)/set(i, x): indeks 0 (dno) je najbolj oddaljen
 * od vrha, zato je sprehod dolg count - 1 - i korakov.
 * <p>
 * Zahtevnost: push, pop, top, clear O(1); get, set O(n - i); contains, toString O(n).
 */
public class LinkedStack<T> implements Stack<T> {
    // TODO: atributi

    public LinkedStack() {
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

    // ---- Stack ----

    /** Doda x na vrh sklada. Če je sklad poln, sproži CollectionException. */
    public void push(T x) throws CollectionException {
        throw new UnsupportedOperationException("TODO");
    }

    /** Odstrani in vrne vrh sklada. Če je sklad prazen, sproži CollectionException. */
    public T pop() throws CollectionException {
        throw new UnsupportedOperationException("TODO");
    }

    /** Vrne vrh sklada brez odstranitve. Če je sklad prazen, sproži CollectionException. */
    public T top() throws CollectionException {
        throw new UnsupportedOperationException("TODO");
    }
}
