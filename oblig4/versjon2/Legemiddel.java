public abstract class Legemiddel {
    protected String navn;
    protected double pris;
    protected double virkestoff;

    protected int id;
    private static int nesteId = 0;

    Legemiddel(String navn, double pris, double virkestoff){
        this.navn = navn;
        this.pris = pris;
        this.virkestoff = virkestoff;
        this.id = nesteId++;
    }

    public int hentId() {
        return id;
    }

    public String hentNavn() {
        return navn;
    }

    public double hentPris() {
        return pris;
    }

    public double hentVirkestoff() {
        return virkestoff;
    }

    @Override
    public String toString(){
        return String.format("Navn: %s  Pris: %f  Virkestoff: %s", navn, pris, virkestoff);
    }
}
