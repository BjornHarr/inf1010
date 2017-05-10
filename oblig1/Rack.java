public class Rack{
	private Node[] nodene;
	private int teller;

	/**
	 *
	 * @param noderIRack Antall noder rack'et skal inneholde
	 */
	public Rack(int noderIRack){
		nodene = new Node[noderIRack];
		teller = 0;
	}

	/**
	 *
	 * @param node Noden som skal legges til i nodene
	 */
	public void leggTilNode(Node node){
			nodene[teller++] = node;
	}

	/**
	 *
	 * @return nodene Returnerer nodene som er i Racket
	 */
	public Node[] hentNoder(){
		return nodene;
	}

	/**
	 *
	 * @return fullt Boolean-verdi som "sier" om racket er fullt eller ikke
	 */
	public boolean erFullt(){
		boolean fullt = true;
		if (nodene[nodene.length-1] == null){
			fullt = false;
		}
		return fullt;
	}
}