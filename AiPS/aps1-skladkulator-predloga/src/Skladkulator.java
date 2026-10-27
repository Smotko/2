import java.util.Scanner;
import zbirke.CollectionException;
import stroj.Engine0;
import stroj.Engine1;
import stroj.Engine2;
import stroj.Engine3;
import stroj.Engine4;
import stroj.Engine5;

/** Požene ustrezen Engine glede na podano stopnjo (argument ukazne vrstice). */
public class Skladkulator {
    public static void main(String[] args) {
        String level = args.length > 0 ? args[0] : "5";
        Engine0 engine;
        switch (level) {
            case "0":
                engine = new Engine0();
                break;
            case "1":
                engine = new Engine1();
                break;
            case "2":
                engine = new Engine2();
                break;
            case "3":
                engine = new Engine3();
                break;
            case "4":
                engine = new Engine4();
                break;
            case "5":
                engine = new Engine5();
                break;
            default:
                System.err.println("Neznana stopnja: " + level);
                return;
        }

        try {
            engine.run(new Scanner(System.in));
        } catch (CollectionException e) {
            System.err.println(e.getMessage());
        }
    }
}
