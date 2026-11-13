package stroj;
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
            // (preostali ukazi)
			case "pop": {
				stack.pop();
				return true;
			}
			case "over": {
				stack.pop();
				String y = stack.pop();
				stack.push(y);
				stack.push(y);
				return true;
			}
			case "rot":{
				String x = stack.pop();
				String y = stack.pop();
				String z = stack.pop();
				stack.push(x);
				stack.push(z);
				stack.push(y);
				return true;
			}
			case "char":{
				int x = popInt();
				char y = (char) x;
				stack.push(String.valueOf(y));
				return true;
			}
			case "strlen":{
				String x = stack.pop();
				int y = x.length();
				stack.push(String.valueOf(y));
				return true;
			}
			
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
