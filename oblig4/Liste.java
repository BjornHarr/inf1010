public interface Liste<T> extends Iterable<T> {
    /**
     * Beregner antall elementer i listen
     * @return      antall elementer i listen
     */
    public int storrelse();

    /**
     * Sjekker om listen er tom
     * @return      om listen er tom
     */
    public boolean erTom();

    /**
     * Setter inn et element i listen
     * @param   element     elementet som settes inn
     */
    public void settInn(T element);

    /**
     * Fjerner et element fra listen. Hvis listen er tom,
     * returneres null.
     * @return      elementet
     */
    public T fjern();
}
