/**
 * Vmesniki zbirk. Pri APS1 so potrebni predvsem sklad, vrsta, dvostrana vrsta, slovar in množica.
 * <p>
 * Hierarhija vmesnikov:
 * <pre>
 * Collection
 * ├── Indexed
 * │   ├── Stack
 * │   └── Queue
 * │       └── Deque
 * ├── Map
 * ├── Set
 * └── Bag
 * </pre>
 * <ul>
 *   <li>{@link zbirke.Collection} - skupne operacije vseh zbirk</li>
 *   <li>{@link zbirke.Indexed} - indeksiran dostop do elementov ({@link zbirke.Stack}, {@link zbirke.Queue} in {@link zbirke.Deque})</li>
 *   <li>{@link zbirke.Stack} - sklad</li>
 *   <li>{@link zbirke.Queue} - vrsta</li>
 *   <li>{@link zbirke.Deque} - dvostrana vrsta</li>
 *   <li>{@link zbirke.Map} - slovar</li>
 *   <li>{@link zbirke.Set} - množica</li>
 *   <li>{@link zbirke.Bag} - vreča</li>
 * </ul>
 * Izjema {@link zbirke.CollectionException} je opisana v istoimenskem razredu.
 */
package zbirke;
