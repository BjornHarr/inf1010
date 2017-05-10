public class Pasient{

    private Stabel<Resept> reseptliste;
    private int id;
    private static int nesteId = 0;

    private String navn;
    private long fodselsnummer;
    private String gateadresse;
    private int postnummer;

    Pasient(String navn, long fodselsnummer, String gateadresse, int postnummer){
        this.reseptliste = new Stabel<Resept>();
        this.navn = navn;
        this.fodselsnummer = fodselsnummer;
        this.gateadresse = gateadresse;
        this.postnummer = postnummer;
        this.id = nesteId++;
    }

    public int hentId(){
        return id;
    }

    public String hentNavn(){
        return navn;
    }

    public long hentFodselsnummer(){
        return fodselsnummer;
    }

    public String hentGateadresse(){
        return gateadresse;
    }

    public int hentPostnummer(){
        return postnummer;
    }

    public void leggTilResept(Resept resept){
        reseptliste.settInn(resept);
    }

    public Stabel<Resept> hentReseptliste(){
        return reseptliste;
    }

    @Override
    public String toString(){
        return String.format("%s (%o)", navn, fodselsnummer);
    }
}
