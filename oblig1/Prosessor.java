public class Prosessor{
    private int kjerner; //Antall kjerner
    private double klok; //Klokkehastighet

    /**
     *
     * @param kjerner 	Antall kjerner til prosessoren
     * @param klok 		Klokkehastigheten til prosessoren
     */
    public Prosessor(int kjerner, double klok){
        this.klok = klok;
        this.kjerner = kjerner;
    }

    /**
     *
     * @return klok Returnerer klokkehastigheten
     */
    public double hentKlok(){
        return klok;
    }

    /**
     *
     * @return kjerner Returnerer kjerner til prosessoren
     */
    public int hentKjerner(){
        return kjerner;
    }
}
