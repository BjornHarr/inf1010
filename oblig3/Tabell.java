public interface Tabell<T> extends Iterable<T> {
    /**
     * Beregner antall elementer i tabellen
     * @return      antall elementer i tabellen
     */
    public int storrelse();

    /**
     * Sjekker om tabellen er tom
     * @return      om tabellen er tom
     */
    public boolean erTom();

    /**
     * Setter inn et element i tabellen
     * @param   element             elementet som settes inn
     * @throws  FullTabellUnntak    hvis tabellen allerede er full
     */
    public void settInn(T element);

    /**
     * Henter (uten aa fjerne) et element fra tabellen
     * @param  plass    plassen i tabellen som det hentes fra
     * @return          elementet paa plassen
     * @throws  UgyldigPlassUnntak  hvis plassen ikke er en gyldig
                                    indeks i arrayet eller plassen
                                    ikke inneholder noe element
     */
    public T hentFraPlass(int plass);
}
