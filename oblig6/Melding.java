import krypto.*;

public class Melding implements Comparable<Melding>{
    private String meldingen;
    private int kanalId;
    private int sekvensNummer;

    Melding(String meldingen, int kanalId, int sekvensNummer){
        this.meldingen = meldingen;
        this.kanalId = kanalId;
        this.sekvensNummer = sekvensNummer;
    }

    @Override
    public int compareTo(Melding annenMelding){
        return sekvensNummer - annenMelding.hentSekvensNummer();
    }

    public void dekrypter(){
        meldingen = Kryptografi.dekrypter(meldingen);
    }

    public int hentKanalId(){
        return kanalId;
    }

    public int hentSekvensNummer(){
        return sekvensNummer;
    }

    public String hentMelding(){
        return meldingen;
    }
}
