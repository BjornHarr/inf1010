public class BlaaResept extends Resept{

    BlaaResept(Legemiddel legemiddel, Lege utskrivendeLege, int pasientId, int reit){
        super(legemiddel, utskrivendeLege, pasientId, reit);
    }

    /**
     * Returnerer reseptens farge. Enten "blaa" eller "hvit".
     * @return      reseptens farge
     */
    public String farge(){
        return "blaa";
    }

    /**
     * Returnerer prisen pasienten maa betale.
     * @return      prisen pasienten maa betale
     */
    @Override
    public double prisAaBetale(){
        return legemiddel.hentPris() * 0.25;
    }
}
