public class Node{
	private int minne;
	private Prosessor[] prosessorer;

	/**
	 *
	 * @param minne 		Nodens minne i GB
	 * @param p1_Kjerner 	Antall kjerner til prosessor_1
	 * @param p1_Klok 		Klokkehastigheten til prosessor_1
	 */
	public Node(int minne, int p1_Kjerner, double p1_Klok){
		this.minne = minne;
		prosessorer = new Prosessor[1];
		prosessorer[0] = new Prosessor(p1_Kjerner, p1_Klok);
	}

	/**
	 *
	 * @param minne 		Nodens minne i GB
	 * @param p1_Kjerner 	Antall kjerner til prosessor_1
	 * @param p1_Klok 		Klokkehastigheten til prosessor_1
	 * @param p2_Kjerner 	Antall kjerner til prosessor_2
	 * @param p2_Klok 		Klokkehastigheten til prosessor_2
	 */
	public Node(int minne, int p1_Kjerner, double p1_Klok, int p2_Kjerner, double p2_Klok){
		this.minne = minne;
		prosessorer = new Prosessor[2];
		prosessorer[0] = new Prosessor(p1_Kjerner, p1_Klok);
		prosessorer[1] = new Prosessor(p2_Kjerner, p2_Klok);
	}

	/**
	 *
	 * @return prosessorer Array som inneholder alle prosessorene i noden
	 */
	public Prosessor[] hentProsessorer(){
		return prosessorer;
	}

	/**
	 *
	 * @return minne Returnerer minnet (/RAM'en) til noden
	 */
	public int hentMinne(){
		return minne;
	}
}