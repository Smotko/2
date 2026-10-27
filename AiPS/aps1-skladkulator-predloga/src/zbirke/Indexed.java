package zbirke;

/**
 * Indeksiran dostop do elementov zbirke - indeks 0 je element na začetku
 * (pri skladu na dnu, pri vrsti na začetku vrste); isti vrstni red, kot ga
 * uporablja toString(). Ta vmesnik podedujeta {@link Stack} in {@link Queue}
 * (torej tudi {@link Deque}).
 */
public interface Indexed<T> extends Collection<T> {
    /** Vrne element na indeksu i. Za neveljaven indeks (negativen ali vsaj count()) sproži CollectionException. */
    T get(int i) throws CollectionException;

    /** Element na indeksu i zamenja z x. Za neveljaven indeks (negativen ali vsaj count()) sproži CollectionException. */
    void set(int i, T x) throws CollectionException;
}
