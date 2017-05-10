public class OrdnetLenkeliste<T extends Comparable<T>> extends Lenkeliste<T>{
	/**
     * Setter inn et element i listen
     * @param   element     elementet som settes inn
     */
	@Override
    public void settInn(T element){
		Node nyNode = new Node(element);
		Node itNode = forste;
		Node forrigeNode = null;

		if(forste == null){
			forste = nyNode;
			siste = nyNode;
			return;
		}

		for (T itElement : this){ //-------------ENDRING-----------------
			if (element.compareTo(itElement) >= 0){
				forrigeNode = itNode;
				if (itNode.neste != null){
					itNode = itNode.neste;
				}
			}else{
				if (forrigeNode != null){
					forrigeNode.neste = nyNode;
					nyNode.forrige = forrigeNode;
				}
				if (itNode != null){
					itNode.forrige = nyNode;
					nyNode.neste = itNode;
				}
				break;
			}
		}
		System.out.println("Focking worked ;)");	
    }
}
