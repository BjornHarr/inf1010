public class Lege implements Comparable<Lege>{

    protected String navn;
    protected Koe<Resept> reseptliste;

    Lege (String navn){
        this.navn = navn;
        reseptliste = new Koe<Resept>();
    }

    public String hentNavn(){
        return this.navn;
    }

    public int compareTo(Lege annenLege){
        return navn.compareTo(annenLege.hentNavn());
    }

    public void leggTilResept(Resept resept){
        reseptliste.settInn(resept);
    }

    public Koe<Resept> hentReseptliste(){
        return this.reseptliste;
    }
}
