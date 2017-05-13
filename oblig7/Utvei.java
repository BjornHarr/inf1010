public class Utvei implements Comparable<Utvei>{
    private Rute rute;
    private int antFlytt;
    private String vei;
    private String labyrint;
    private String koordinat;

    private Labyrint labRef;
    private boolean[][] losningTabell;

    private String skriv;

    Utvei(Rute rute, int antFlytt, String vei, String labyrint, boolean minimalUtskrift, Labyrint labRef){
        this.vei = vei;
        this.rute = rute;
        this.antFlytt = antFlytt;
        this.labyrint = labyrint;
        this.koordinat = rute.hentKoordinat();

        this.labRef = labRef;
        losningTabell = losningStringTilTabell(vei, labRef.hentKolonner(), labRef.hentRader());

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

    public boolean[][] hentBoolLosning(){
        return losningTabell;
    }

    /**
     * Konverterer losning-String fra oblig 5 til en boolean[][]-representasjon
     * av losningstien.
     * @param losningString String-representasjon av utveien
     * @param bredde        bredde til labyrinten
     * @param hoyde         hoyde til labyrinten
     * @return              2D-representasjon av rutene der true indikerer at
     *                      ruten er en del av utveien.
     */
    private boolean[][] losningStringTilTabell(String losningString, int bredde, int hoyde) {
        boolean[][] losning = new boolean[hoyde][bredde];
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\(([0-9]+),([0-9]+)\\)");
        java.util.regex.Matcher m = p.matcher(losningString.replaceAll("\\s",""));
        while(m.find()) {
            int x = Integer.parseInt(m.group(1))-1;
            int y = Integer.parseInt(m.group(2))-1;
            losning[y][x] = true;
        }
        return losning;
    }
}
