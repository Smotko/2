package izvedbe;

import zbirke.CollectionException;
import zbirke.Stack;

/**
 * Izvedba vmesnika {@link zbirke.Stack} s poljem. Kapaciteta je fiksna, določena ob ustvarjanju objekta.
 * Indeks 0 pri get(i)/set(i, x) je dno sklada.
 * <p>
 * Zahtevnost: push, pop, top, get, set, clear O(1); contains, toString O(n), kjer je n število elementov.
 */
public class ArrayStack<T> implements Stack<T> {
    // TODO: atributi
	private int števec;
	private T[] tabela;
	
	@SuppressWarnings("unchecked")
    public ArrayStack(int capacity) {
        // TODO
		T[] tabela = (T[]) new Object[capacity];
		števec = 0;
    }

    // ---- Collection ----

    /** Ali je zbirka prazna? */
    public boolean isEmpty() {
		if (števec == 0){
			return true;
		}
		return false;
    }

    /** Ali je zbirka polna (število elementov je doseglo kapaciteto)? */
    public boolean isFull() {
        if(števec == tabela.length){
			return true;
		}
		return false;
    }

    /** Vrne število elementov v zbirki. */
    public int count() {
		int številoElementov = 0;
        for (int i = 0; i < tabela.length;i++){
			if(tabela[i] != null){
				številoElementov++;
			}
		}
		return številoElementov;
    }

    /** Izprazni zbirko. */
    public void clear() {
        for (int i = 0; i < tabela.length; i++){
			tabela[i] = null;
		}
		števec = 0;
    }

    /** Ali zbirka vsebuje element x (pri slovarju: ključ x)? */
    public boolean contains(T x) {
        for(int i = 0; i < tabela.length; i++){
			if ( tabela[i].equals(x)){
				return true;
			}
		}
		return false;
    }

    /** Vrne vsebino zbirke kot niz, elementi so ločeni s presledkom (za izpis in razhroščevanje). */
    public String toString() {
		StringBuilder niz = new StringBuilder();
        for(int i = 0; i < tabela.length; i++){
			if(i == tabela.length-1){
				niz.append(tabela[i]);
				break;
			}
			niz.append(tabela[i]+ " ");
		}
		return niz.toString();
    }

    // ---- Indexed ----

    /** Vrne element na indeksu i. Za neveljaven indeks (negativen ali vsaj count()) sproži CollectionException. */
    public T get(int i) throws CollectionException {
        if(tabela.length < i || i<0 || i<števec){
			throw new CollectionException("neveljaven indeks");
		}
		return(tabela[i-1]);
    }

    /** Element na indeksu i zamenja z x. Za neveljaven indeks (negativen ali vsaj count()) sproži CollectionException. */
    public void set(int i, T x) throws CollectionException {
        if(tabela.length < i || i<0 || i<števec){
			throw new CollectionException("neveljaven indeks");
		}
		tabela[i] = x;
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
