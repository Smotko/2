package zbirke;

/**
 * Skupne operacije za vse zbirke (sklad, vrsta, zaporedje, slovar ...).
 */
public interface Collection<T> {
    /** Ali je zbirka prazna? */
    boolean isEmpty();

    /** Ali je zbirka polna (število elementov je doseglo kapaciteto)? */
    boolean isFull();

    /** Vrne število elementov v zbirki. */
    int count();

    /** Izprazni zbirko. */
    void clear();

    /** Ali zbirka vsebuje element x (pri slovarju: ključ x)? */
    boolean contains(T x);

    /**
     * Vrne vsebino zbirke kot niz, elementi so ločeni s presledkom (za izpis in razhroščevanje).
     * <p>
     * Pri zbirkah z indeksi ({@link Stack}, {@link Queue}, {@link Deque}) je vrstni red elementov enak ne glede na
     * notranjo izvedbo (polje ali kazalci): od začetka do konca, torej pri skladu od dna do vrha, pri vrsti in
     * dvostrani vrsti od začetka do konca vrste.
     * <p>
     * Pri {@link Set}, {@link Bag} in {@link Map} vrstni red elementov ni določen, odvisen je od izvedbe.
     */
    String toString();
}
