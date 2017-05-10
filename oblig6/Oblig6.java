import krypto.*;
import java.util.concurrent.CountDownLatch;

public class Oblig6{
    private static final int antallTelegrafister = 3;
    private static final int antallKryptografer = 30;

    private static Monitor monitorKrypterte = new Monitor();
    private static Monitor monitorDekrypterte = new Monitor();

    private static int aktiveTelegrafistTraader;
    private static int aktiveKryptografTraader;

    private static CountDownLatch teleCount;
    private static CountDownLatch krypCount;


    public static void main (String[] args)throws InterruptedException{
        teleCount = new CountDownLatch(antallTelegrafister);
        krypCount = new CountDownLatch(antallKryptografer);

        Operasjonssentral ops = new Operasjonssentral(antallTelegrafister);
        Kanal[] kanal = ops.hentKanalArray();

        //Oppretter nye telegrafist-traader og starter de
        for (int i = 0; i < antallTelegrafister; i++){
            new Thread(new Telegrafist(kanal[i], i, monitorKrypterte)).start();
        }

        //Oppretter nye kryptograf-traader og starter de
        for (int i = 0; i < antallKryptografer; i++){
            new Thread(new Kryptograf(monitorKrypterte, monitorDekrypterte)).start();
        }

        Operasjonsleder.skrivUtNaarKlar(kanal.length, monitorDekrypterte);
    }

    public static boolean aktiveKryptografTraader(){
        return (krypCount.getCount() > 0 ? true : false);
    }

    public static void kryptografTraadSluttet(){
        krypCount.countDown();
        System.out.println("aktive kryptografer : " + krypCount.getCount());
    }

    public static boolean ingenAktiveTelegrafister(){
        return (teleCount.getCount() == 0 ? true : false);
    }

    public static void telegrafistThreadEnded(){
        teleCount.countDown();
        System.out.println("aktive telegrafister : " + teleCount.getCount());
    }
}
