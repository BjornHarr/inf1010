import java.util.Iterator;

public abstract class Lenkeliste<T> implements Liste<T>{

	protected class Node{
		public Node neste;
		public Node forrige;
		public T element;

		Node(T element){
			this.element = element;
			this.forrige = null;
			this.neste = null;
		}

		Node (){
			this(null);
		}
	}

	protected Node forste;
	protected Node siste;

	Lenkeliste(){
		this.forste = null;
		this.siste = null;
	}

	/**
     * Beregner antall elementer i listen
     * @return      antall elementer i listen
     */
    public int storrelse(){
// bruk teller

    	Node node = forste;
    	int antallElementer = 0;

    	while (node != null){
    		antallElementer++;
    		node = node.neste;
    	}

    	return antallElementer;
    }

    /**
     * Sjekker om listen er tom
     * @return      om listen er tom
     */
    public boolean erTom(){
    	if (forste == null){
    		return true;
    	}
    	return false;
    }

    /**
     * Setter inn et element i listen
     * @param   element     elementet som settes inn
     */
    public void settInn(T element){

    }

    /**
     * Fjerner et element fra listen. Hvis listen er tom,
     * returneres null.
     * @return      elementet
     */
    public T fjern(){
		if (erTom()){
    		return null;
    	}

    	Node hentetNode = forste;

    	forste = forste.neste;

    	if (forste != null){
    		forste.forrige = null;

    	}

    	return hentetNode.element;
    }

    public Iterator<T> iterator(){
    		return new Listeiterator();
    }

    private class Listeiterator implements Iterator<T>{
    	Node itNode; // Iterator Node

    	Listeiterator(){
    		itNode = forste;
    	}

    	@Override
    	public boolean hasNext(){
    		return itNode != null;
    	}

    	@Override
    	public T next(){
			T hentUt = itNode.element;
    		itNode = itNode.neste;
    		return hentUt;
    	}

    	@Override
    	public void remove(){}
    }
}
