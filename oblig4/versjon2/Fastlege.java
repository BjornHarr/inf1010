public class Fastlege extends Lege implements Kommuneavtale{
    private int avtalenummer;

    Fastlege(String navn, int avtalenummer){
        super(navn);
        this.avtalenummer = avtalenummer;
    }

    public int hentAvtalenummer(){
        return avtalenummer;
    }
}
