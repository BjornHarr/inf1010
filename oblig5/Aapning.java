public class Aapning extends HvitRute {
    Aapning(Labyrint labyrintReferanse, int kol, int rad){
        super(labyrintReferanse, kol, rad);
    }

    @Override
    public char tilTegn(){
        return (paaVeien ? 'X' : ' ');
    }
}
