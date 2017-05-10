import java.util.Iterator;

public class StatiskTabell<T> implements Tabell<T> {

	protected T[] tabell;
    protected int antallElementer;

	StatiskTabell(int kapasitet){
		tabell = (T[]) new Object[kapasitet];
        antallElementer = 0;
	}

	/**
     * Beregner antall elementer i tabellen
     * @return  antallElementer     antall elementer i tabellen
     */
    public int storrelse(){
        return antallElementer;
    }

    /**
     * Sjekker om tabellen er tom
     * @return      om tabellen er tom
     */
    public boolean erTom(){
		return storrelse() == 0;
    }

    /**
     * Setter inn et element i tabellen
     * @param   element             elementet som settes inn
     * @throws  FullTabellUnntak    hvis tabellen allerede er full
     */
    public void settInn(T element){
        if (tabell.length == storrelse()){
            throw new FullTabellUnntak(storrelse());
        }

        tabell[storrelse()] = element;

        antallElementer++;
    }

    /**
     * Henter (uten aa fjerne) et element fra tabellen
     * @param  plass    plassen i tabellen som det hentes fra
     * @return          elementet paa plassen
     * @throws  UgyldigPlassUnntak  hvis plassen ikke er en gyldig indeks i arrayet eller plassen ikke inneholder noe element
     */
    public T hentFraPlass(int plass){
        if (storrelse() <= plass || plass < 0){
            throw new UgyldigPlassUnntak(plass, storrelse());
        }

        return tabell[plass];
    }

    public Iterator<T> iterator(){
            return new Listeiterator<T>();
    }

    public class Listeiterator<T> implements Iterator<T>{
        int itElement; // Iterator element

        Listeiterator(){
            itElement = 0;
        }

        public boolean hasNext(){
            return itElement < storrelse();
        }

        public T next(){
            if (!this.hasNext()){
                return null;
            }

            return (T) tabell[itElement++];
        }

        public void remove(){
            throw new UnsupportedOperationException();
        }
    }
}

/*


*/
