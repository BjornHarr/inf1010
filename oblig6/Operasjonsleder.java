import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.io.PrintWriter;
import java.io.File;

public class Operasjonsleder{
    private static Object[] kanalMeldinger;
    private static Monitor  monitorDekrypterte;

    public static void skrivUtNaarKlar(int antallKanaler, Monitor inMonitorDekrypterte)throws InterruptedException{
        kanalMeldinger = new Object[antallKanaler];

        for (int i = 0; i < antallKanaler; i++){
            OrdnetLenkeliste<Melding> nyListe = new OrdnetLenkeliste<Melding>();
            kanalMeldinger[i] = (Object) nyListe;
        }
        monitorDekrypterte = inMonitorDekrypterte;

        while(Oblig6.aktiveKryptografTraader()){
            settInnMeldinger();
        }

        skrivTilFil();
    }

    private static void settInnMeldinger()throws InterruptedException{
        Melding melding = monitorDekrypterte.hentMelding();
        while (melding != null){
            OrdnetLenkeliste<Melding> liste = (OrdnetLenkeliste<Melding>) kanalMeldinger[melding.hentKanalId()];
            liste.settInn(melding);
            melding = monitorDekrypterte.hentMelding();
        }
    }

    private static void skrivTilFil(){
    System.out.println("skrivTilFil");
        try{
            for (int i = 0; i < kanalMeldinger.length; i++){
                PrintWriter pw = new PrintWriter("kanal_" + i + ".txt", "UTF-8");
                OrdnetLenkeliste<Melding> liste = (OrdnetLenkeliste<Melding>) kanalMeldinger[i];
                for (Melding melding : liste){
                    pw.println(melding.hentMelding());
                }
                pw.close();
            }
        }catch(UnsupportedEncodingException|FileNotFoundException err){
            System.out.println(err);
        }
        System.out.println("<----------PROCESS FERDIG---------->");
    }
}
