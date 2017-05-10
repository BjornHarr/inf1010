public class Utvei implements Comparable<Utvei>{
    private Rute rute;
    private int antFlytt;
    private String vei;
    private String labyrint;
    private String koordinat;

    private String skriv;

    Utvei(Rute rute, int antFlytt, String vei, String labyrint, boolean minimalUtskrift){
        this.vei = vei;
        this.rute = rute;
        this.antFlytt = antFlytt;
        this.labyrint = labyrint;
        this.koordinat = rute.hentKoordinat();

        this.skriv = String.format("Utvei funnet i %s, antFlytt: %d", koordinat, antFlytt);
        if (!minimalUtskrift) skriv += String.format("\n%s\n%s\n", labyrint, vei);
    }

    @Override
    public int compareTo(Utvei annenUtvei){
        return  this.antFlytt - annenUtvei.hentAntFlytt();
    }

    @Override
    public String toString(){
        return skriv;
    }

    public int hentAntFlytt(){
        return antFlytt;
    }

    public String hentKoordinat(){
        return koordinat;
    }

    public String hentVei(){
        return vei;
    }
}
