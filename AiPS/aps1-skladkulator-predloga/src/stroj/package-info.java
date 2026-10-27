/**
 * Skladovni izvajalni stroj Skladkulatorja (izrazi v obratnem poljskem zapisu).
 * <ul>
 *   <li>{@link stroj.Engine0} ... {@link stroj.Engine5} - razredi za izvajanje ukazov, vsak ustreza eni nalogi
 *       in razširja prejšnjega (EngineN razširja Engine(N-1))</li>
 *   <li>{@link stroj.Directory} - nabor poimenovanih zbirk: preslikava imena v zbirko (pomembno od Naloge1 dalje);
 *       vgnezdeni razred {@link stroj.Directory.Function} (funkcija: vrsta nizov, ki jo od navadne vrste loči tip)
 *       je dan in ga ni treba spreminjati</li>
 * </ul>
 */
package stroj;
