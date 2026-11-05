//ne package
//prevodi brez opozoril
import java.util.Scanner;
import zbirke.CollectionException;
import zbirke.Stack;
import izvedbe.ArrayStack;

/** Naloga0 - ogrodje: en glavni sklad, osnovni ukazi. */
public class Engine0 {
    public static final int CAPACITY = 256;

    protected Stack<String> stack;

    public Engine0() {
        stack = new ArrayStack<String>(CAPACITY);
    }

    /** Odstrani vrh sklada in ga pretvori v int. */
    protected int popInt() throws CollectionException {
        return Integer.parseInt(stack.pop());
    }

    /** Prebere in obdela vse simbole iz podanega vhoda. */
    public void run(Scanner sc) throws CollectionException {
        while (sc.hasNext()) process(sc.next());
    }

    /**
     * Obdela en simbol do konca v treh zaporednih korakih: najprej ga morda prestreže način ({@link #intercept}),
     * nato vgrajeni ukaz ({@link #eval}), sicer rezervni korak ({@link #fallback}). Prvi korak, ki simbol uspešno
     * obdela (vrne true), konča metodo; fallback se izvede le, če noben od prejšnjih korakov ni uspel.
     */
    protected void process(String token) throws CollectionException {
        if (intercept(token)) return;
        if (eval(token)) return;
        fallback(token);
    }

    protected boolean inComment = false;

    /**
     * Prestreže simbol, ki ga je treba obdelati drugače kot navaden ukaz (npr. komentar ali blok). Vrne true, če je
     * simbol obdelan.
     * <p>
     * Komentarje obdelamo tako, da si zapomnimo, da smo znotraj komentarja, in izpustimo vse simbole do vključno
     * <code>*&#47;</code>. Kasneje boste sem dodali še obdelavo blokov.
     */
    protected boolean intercept(String token) throws CollectionException {
        if (inComment) {
            if ("*/".equals(token)) inComment = false;
            return true;
        } else if ("/*".equals(token)) {
            inComment = true;
            return true;
        }
        return false;
    }

    /**
     * Izvede vgrajeni ukaz z danim imenom cmd, če obstaja. Vrne true, če je bil ukaz prepoznan.
     * <p>
     * Simbol primerjajte z imeni ukazov (npr. s switch). Če se ujema, ukaz izvedite in korak je uspel, sicer ne.
     */
    protected boolean eval(String cmd) throws CollectionException {
        switch (cmd) {
            case "dup":
                stack.push(stack.top());
                return true;
            case "swap": {
                String y = stack.pop(), x = stack.pop();
                stack.push(y);
                stack.push(x);
                return true;
            }
            case "+": {
                int y = popInt(), x = popInt();
                stack.push(String.valueOf(x + y));
                return true;
            }
            // TODO: dodajte preostale ukaze iz Naloga0
			case "pop": {
				
			}
			case "over": {
				
			}
			case "rot":{
				
			}
			case "'":
				stack
            // (preostali ukazi)
            default:
                return false;
        }
    }

    /**
     * Pokliče se, kadar simbol ni bil ne prestrežen ne prepoznan kot vgrajeni ukaz. Če se simbol začne z
     * <code>'</code>, na sklad potisne preostanek niza, sicer simbol samo potisne na sklad. Kasneje boste sem
     * dodali še obdelavo funkcij.
     */
    protected void fallback(String token) throws CollectionException {
        stack.push(token.startsWith("'") ? token.substring(1) : token);
    }
}
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

    public ArrayStack(int capacity) {
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