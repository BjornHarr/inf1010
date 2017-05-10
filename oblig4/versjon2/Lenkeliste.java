import java.util.Iterator;

public abstract class Lenkeliste<T> implements Liste<T>{

	protected class Node<T>{
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
    public abstract void settInn(T element);

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

    	return (T) hentetNode.element;
    }

    public Iterator<T> iterator(){
    		return new Listeiterator<T>();
    }

    private class Listeiterator<T> implements Iterator<T>{
    	Node itNode = new Node(null); // Iterator Node

    	Listeiterator(){
    		itNode.neste = forste;
    	}

    	@Override
    	public boolean hasNext(){
    		if (itNode.neste != null){
    			return true;
    		}
    		return false;
    	}

    	@Override
    	public T next(){
    		if (hasNext()){
    			itNode = itNode.neste;
    			return (T) itNode.element;
    		}
    		return null;
    	}

    	@Override
    	public void remove(){}
    }
}
