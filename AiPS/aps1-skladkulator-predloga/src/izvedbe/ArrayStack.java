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
	private T[] tabela;
	
	@SuppressWarnings("unchecked")
    public ArrayStack(int capacity) {
        // TODO
		tabela = (T[]) new Object[capacity];
    }

    // ---- Collection ----

    /** Ali je zbirka prazna? */
    public boolean isEmpty() {
		if (count() == 0){
			return true;
		}
		return false;
    }

    /** Ali je zbirka polna (število elementov je doseglo kapaciteto)? */
    public boolean isFull() {
		if (count() == tabela.length){
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
    }

    /** Ali zbirka vsebuje element x (pri slovarju: ključ x)? */
    public boolean contains(T x) {
        for(int i = 0; i < tabela.length; i++){
			if(tabela[i] == null){
				break;
			}
			if (tabela[i].equals(x)){
				return true;
			}
		}
		return false;
    }

    /** Vrne vsebino zbirke kot niz, elementi so ločeni s presledkom (za izpis in razhroščevanje). */
    public String toString() {
		StringBuilder niz = new StringBuilder();
        for(int i = count()-1; i >= 0; i--){
				if(i == 0){
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
        if(count() <= i || i<0){
			throw new CollectionException("neveljaven indeks");
		}
		return(tabela[count()-i-1]);
    }

    /** Element na indeksu i zamenja z x. Za neveljaven indeks (negativen ali vsaj count()) sproži CollectionException. */
    public void set(int i, T x) throws CollectionException {
        if(count() <= i || i<0){
			throw new CollectionException("neveljaven indeks");
		}
		tabela[i] = x;
    }

    // ---- Stack ----

    /** Doda x na vrh sklada. Če je sklad poln, sproži CollectionException. */
    public void push(T x) throws CollectionException {
        if(count() == tabela.length){
			throw new CollectionException("sklad je poln");
		}
		for (int i = count(); i > 0;i--){
			tabela[i] = tabela[i-1];
		} 
		tabela[0] = x;
    }

    /** Odstrani in vrne vrh sklada. Če je sklad prazen, sproži CollectionException. */
    public T pop() throws CollectionException {
        if (count() == 0){
			throw new CollectionException("sklad je prazen");
		}
		T ret = tabela[0];
		for (int i = 1; i < count(); i ++){
			tabela[i-1] = tabela[i];
		}
		tabela[count()-1] = null;
		return ret;
		
    }

    /** Vrne vrh sklada brez odstranitve. Če je sklad prazen, sproži CollectionException. */
    public T top() throws CollectionException {
        if (count() == 0){
			throw new CollectionException("sklad je prazen");
		}
		return tabela[0];
    }
}
