public class DynamiskTabell<T> extends StatiskTabell<T>{

	DynamiskTabell(int kapasitet){
		super(kapasitet);
	}

	DynamiskTabell(){
		super(100);
	}

	/**
     * Setter inn et element i tabellen
     * @param   element             elementet som settes inn
     * @throws  FullTabellUnntak    hvis tabellen allerede er full
     */
	@Override
    public void settInn(T element){
        if (tabell.length == storrelse()){
            T[] nyTabell = (T[]) new Object[tabell.length * 2];

            for (int i = 0; i < tabell.length; i++){
            	nyTabell[i] = tabell[i];
            }
            tabell = nyTabell;
        }

        tabell[storrelse()] = element;

        antallElementer++;
    }
}
