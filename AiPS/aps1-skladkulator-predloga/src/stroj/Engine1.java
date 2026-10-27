package stroj;
import zbirke.CollectionException;

/** Naloga1 - poimenovane zbirke (Directory), bloki in funkcije. */
public class Engine1 extends Engine0 {
    public static final int COUNT = 1024;

    protected Directory directory;

    public Engine1() {
        directory = new Directory(COUNT, CAPACITY);
    }

    /**
     * Poleg komentarjev prestreže tudi bloke: `{` s sklada vzame ime zbirke in odpre blok, simbole bloka
     * shrani v zbirko, `}` pa blok zaključi. Na skladu ne ostane nič.
     */
    @Override
    protected boolean intercept(String token) throws CollectionException {
        // TODO: preusmeritev dela vhodnega toka { ... }; za vse ostalo pokličite super.intercept(token)
        return super.intercept(token);
    }

    /** Simbol, ki ni vgrajen ukaz: če je ime obstoječe funkcije, zaporedoma obdela simbole njenega telesa, sicer ga potisne na sklad. */
    @Override
    protected void fallback(String token) throws CollectionException {
        // TODO: klic funkcije po imenu; za vse ostalo pokličite super.fallback(token)
        super.fallback(token);
    }

    /** Pri vsakem ukazu najprej s sklada preberemo argumente (imena kot v navodilih, od zadnjega proti prvemu), nato ukaz izvedemo. */
    @Override
    protected boolean eval(String cmd) throws CollectionException {
        // TODO: ukazi iz Naloga1; za vse ostalo pokličite super.eval(cmd)
        return super.eval(cmd);
    }
}
