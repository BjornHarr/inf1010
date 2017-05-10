public class Koe<T> extends Lenkeliste<T>{
	/**
     * Setter inn et element i listen
     * @param   element     elementet som settes inn
     */
	@Override
    public void settInn(T element){
    	Node nyNode = new Node(element);

    	// Hvis listen er tom
    	if (forste == null){
    		forste = nyNode;
    		siste = nyNode;
    		return;
    	}

	    siste.neste = nyNode;
	    nyNode.forrige = siste;
	    siste = nyNode;	
    }
}