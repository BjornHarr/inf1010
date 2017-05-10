public class HvitRute extends Rute{
    HvitRute(Labyrint labyrintReferanse, int kol, int rad){
        super(labyrintReferanse, kol, rad);
    }

    @Override
    public char tilTegn(){
        return (paaVeien ? '-' : ' ');
    }
}
