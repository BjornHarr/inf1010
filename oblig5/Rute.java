public abstract class Rute {

    private Labyrint labyrintReferanse;
    private static OrdnetLenkeliste<Utvei> utveier;
    private static boolean minimalUtskrift;

    protected boolean paaVeien;

    // Rutens posisjon i labyrinten
    private int kol;
    private int rad;

    // Rutens naboer
    private Rute nord = null;
    private Rute syd  = null;
    private Rute oest = null;
    private Rute vest = null;

    private int skrivKol;
    private int skrivRad;

    Rute(Labyrint labyrintReferanse, int kol, int rad){
        this.labyrintReferanse = labyrintReferanse;
        this.kol = kol;
        this.rad = rad;
        skrivKol = kol + 1;
        skrivRad = rad + 1;
    }

    public abstract char tilTegn();

    //Setter naboer for denne ruten
    public void settNaboer(int kolonner, int rader){
        if (rad > 0)        nord = labyrintReferanse.hentRute(kol    , rad - 1); //Setter nabo Nord
        if (rad < rader)    syd  = labyrintReferanse.hentRute(kol    , rad + 1); //Setter nabo Syd

        if (kol < kolonner) oest = labyrintReferanse.hentRute(kol + 1, rad    ); //Setter nabo Oest
        if (kol > 0)        vest = labyrintReferanse.hentRute(kol - 1, rad    ); //Setter nabo Vest
    }

    public OrdnetLenkeliste<Utvei> finnUtvei(boolean minimalUtskrift){
        utveier = new OrdnetLenkeliste<Utvei>();
        this.minimalUtskrift = minimalUtskrift;

        if (this instanceof SortRute){
            System.out.println("Du kan ikke starte paa en sort rute...");
            return utveier;
        }

        if (this instanceof Aapning){
            paaVeien = true;
            this.lagreUtvei("", 0);
            paaVeien = false;
            return utveier;
        }

        gaa(this, "", -1);
        return utveier;
    }

    public void gaa(Rute fra, String vei, int lengde){
        lengde++;

        if (!paaVeien){

            paaVeien = !paaVeien;

            if (!minimalUtskrift){
                vei += String.format("%s --> ", this.hentKoordinat());
            }

            if (this instanceof Aapning){
                this.lagreUtvei(vei, lengde);
            }

            // Gaar videre
            if (nord != fra && nord instanceof HvitRute) nord.gaa(this, vei, lengde);
            if (oest != fra && oest instanceof HvitRute) oest.gaa(this, vei, lengde);
            if (syd  != fra && syd  instanceof HvitRute)  syd.gaa(this, vei, lengde);
            if (vest != fra && vest instanceof HvitRute) vest.gaa(this, vei, lengde);

            paaVeien = !paaVeien;
        }
    }

    private void lagreUtvei(String vei, int lengde){
        utveier.settInn(new Utvei(this, lengde, vei + "Aapning funnet", labyrintReferanse.toString(), minimalUtskrift));
    }

    public String hentKoordinat(){
        return String.format("(%d,%d)", skrivKol, skrivRad);
    }

    public int hentSkrivRad(){
        return skrivRad;
    }

    public int hentSkrivKol(){
        return skrivKol;
    }

    public int hentRad(){
        return rad;
    }

    public int hentKol(){
        return kol;
    }

    public int hentRader(){
        return labyrintReferanse.hentRader();
    }

    public int hentKolonner(){
        return labyrintReferanse.hentKolonner();
    }
}
