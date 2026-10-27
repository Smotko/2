package zbirke;

/**
 * Izjema, ki jo sprožijo operacije nad zbirko. Rokovanje z izjemami je poenostavljeno: na voljo je le ta razred.
 * <p>
 * Operacija sproži CollectionException, kadar je ni mogoče izvesti:
 * <ul>
 *   <li>branje ali odstranjevanje iz <b>prazne</b> zbirke (pop, top, dequeue, front, dequeueBack, back),</li>
 *   <li>dodajanje v <b>polno</b> zbirko (push, enqueue, enqueueFront, add, put),</li>
 *   <li><b>indeks izven meja</b> (get(i), set(i, x), če je i negativen ali vsaj count()),</li>
 *   <li>odstranjevanje ali branje <b>neobstoječega</b> elementa oz. ključa (remove pri Set, Bag in Map, Map.get).</li>
 * </ul>
 * Od tega pravila odstopata Set.add (če element že obstaja, se stanje ne spremeni, tudi če je množica polna) in
 * Map.put (če ključ že obstaja, se vrednost prepiše, tudi če je slovar poln): v teh dveh primerih izjema ni sprožena.
 */
public class CollectionException extends Exception {
    public CollectionException(String msg) {
        super(msg);
    }
}
