public class Stabel<T> extends Lenkeliste<T>{
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

		forste.forrige = nyNode;
		nyNode.neste = forste;
		forste = nyNode;
    }
}