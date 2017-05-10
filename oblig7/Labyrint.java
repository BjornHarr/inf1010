import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Labyrint{

    private Rute[][] labyrint;

    private int rader;
    private int kolonner;

    private boolean minimalUtskrift = false;

    private Labyrint(int kolonner, int rader){
        this.rader = rader;
        this.kolonner = kolonner;
        this.labyrint = new Rute[rader][kolonner];
    }

    public boolean settMinimalUtskrift(){
        this.minimalUtskrift = !this.minimalUtskrift;
        return minimalUtskrift;
    }

    public boolean hentMinimalUtskrift(){
        return minimalUtskrift;
    }

    @Override
    public String toString(){
        String retString = "";
        for (int i = 0; i <= rader; i++){
            for (int j = 0; j <= kolonner; j++){
                retString += Character.toString(labyrint[i][j].tilTegn()) + " ";
            }
            retString += "\n";
        }
        return retString;
    }

    //Denne er lagt til slik at labyrintreferansene lettere kan peke paa labyrinten.
    private void settInnLabyrint(Rute[][] labyrint){
        this.labyrint = labyrint;
    }

    //Finner utvei fra et bestemt sted
    public Liste<Utvei> finnUtveiFra(int kol, int rad){
        return (Liste) labyrint[rad-1][kol-1].finnUtvei(minimalUtskrift);
    }

    //Leser fra filen
    public static Labyrint lesFraFil(File fil)throws FileNotFoundException{
        Scanner sc = new Scanner(fil);

        //konverterer fra String til int og setter sotrrelse paa labyrinten
        String[] konverterer = sc.nextLine().split(" ");
        int antRader = Integer.parseInt(konverterer[0]);
        int antKolonner = Integer.parseInt(konverterer[1]);

        Labyrint nyLabyrint = new Labyrint(antKolonner, antRader);
            nyLabyrint.settRuter(antKolonner, antRader, sc);
            nyLabyrint.settNaboer();

        return nyLabyrint;
    }

    //Lager rutene og kaller oppdaterings-metoden
    private void settRuter(int antKolonner, int antRader, Scanner sc){
        for (int rad = 0; sc.hasNextLine(); rad++){
            String[] konverterer = sc.nextLine().split("");

            for (int kol = 0; kol < antKolonner; kol++){
                switch (konverterer[kol]){
                    case ".":
                        if (this.erAapning(konverterer[kol], kol, rad)){
                            this.oppdaterLabyrint(new Aapning (this, kol, rad), kol, rad);
                        } else {
                            this.oppdaterLabyrint(new HvitRute(this, kol, rad), kol, rad);
                        }
                        break;

                    case "#":
                        this.oppdaterLabyrint(new SortRute(this, kol, rad), kol, rad);
                        break;
                }
            }
        }
    }

    //Sjekker om ruten er en aapning
    private boolean erAapning(String c, int kol, int rad){
        return c.equals(".") && (rad == 0 || rad == rader || kol == 0 || kol == kolonner);
    }

    // Oppdaterer labyrinten med én ny rute
    private void oppdaterLabyrint(Rute rute, int kol, int rad){
        labyrint[rad][kol] = rute;
    }

    //Setter naboer for alle ruter
    private void settNaboer(){
        for (int rad = 0; rad <= rader; rad++){
            for (int kol = 0; kol <= kolonner; kol++){
                labyrint[rad][kol].settNaboer(kolonner, rader);
            }
        }
    }

    // Brukes for aa lettere kunne sette naboene
    public Rute hentRute(int kol, int rad){
        return labyrint[rad][kol];
    }

    public int hentRader(){
        return rader;
    }

    public int hentKolonner(){
        return kolonner;
    }
}
