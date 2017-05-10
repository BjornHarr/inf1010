public class OrdnetLenkeliste<T extends Comparable<T>> extends Lenkeliste<T>{
	/**
     * Setter inn et element i listen
     * @param   element     elementet som settes inn
     */
	@Override
    public void settInn(T element){
    	Node nyNode = new Node(element);

    	Node node = forste;

    	// Hvis listen er tom
    	if (forste == null){
    		forste = nyNode;
    		siste = nyNode;
    		return;
    	}

		// Lager et element
    	T nesteElement = (T) node.element;

		// Itererer gjennom nodene helt til elementet vi skal sette inn er mindre eller likt elementet
		// vi itererer til. Naar det skjer har vi funnet plassen den nye noden skal settes inn.
    	while (element.compareTo(nesteElement) >= 0 && nesteElement != null && node.neste != null){
	    	node = node.neste;
	    	nesteElement = (T) node.element;
    	}

		if (element.compareTo(nesteElement) >= 0){
			nyNode.forrige = node;
			node.neste = nyNode;
		}else{
			// Setter pekerne til nyNode til aa peke paa objektene den skal mellom.
		    nyNode.neste = node;

			// Setter peker forfra og til den nye noden.
	    	if (node.forrige != null){
				nyNode.forrige = node.forrige;
		    	node.forrige.neste = nyNode;
	    	}else{
	    		forste = nyNode;
	    	}

			//Setter itereringsnodens forrige-peker til aa peke paa nyNode.
	    	node.forrige = nyNode;
		}
    }
}
