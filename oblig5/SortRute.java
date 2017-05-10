public class SortRute extends Rute {
    SortRute(Labyrint labyrintReferanse, int kol, int rad){
        super(labyrintReferanse, kol, rad);
    }

    @Override
    public char tilTegn(){
        return '#';
    }
}
