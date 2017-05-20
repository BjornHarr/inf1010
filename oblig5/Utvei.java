public class Utvei implements Comparable<Utvei>{
    private int antFlytt;
    private String vei;
    private String lostLabyrint;
    private int rad;
    private int kol;
    private String koordinat;
    private boolean minimalUtskrift;

    Utvei(int kol, int rad, int antFlytt, String vei, boolean minimalUtskrift, String lostLabyrint){
        this.kol = kol;
        this.rad = rad;
        this.vei = vei;
        this.antFlytt = antFlytt;
        this.lostLabyrint = lostLabyrint;
        this.koordinat = String.format("(%d,%d)", kol, rad);
        this.minimalUtskrift = minimalUtskrift;
    }

    @Override
    public int compareTo(Utvei annenUtvei){
        return  this.antFlytt - annenUtvei.hentAntFlytt();
    }

    @Override
    public String toString(){
        if (!minimalUtskrift){
            return String.format("%s \n Utvei funnet i %s, antFlytt: %d\n%s\n", lostLabyrint, koordinat, antFlytt, vei);
        }
        return String.format("Utvei funnet i %s", koordinat);
    }

    public String hentLostLabyrint(){
        return lostLabyrint;
    }

    public int hentAntFlytt(){
        return antFlytt;
    }

    public int hentRad(){
        return rad;
    }

    public int hentKol(){
        return kol;
    }

    public String hentKoordinat(){
        return koordinat;
    }

    public String hentVei(){
        return vei;
    }
}
