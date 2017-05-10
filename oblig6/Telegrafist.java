import krypto.*;

public class Telegrafist implements Runnable{
    private Kanal kanal;
    private int kanalId;
    private int sekvensNummer;
    private Monitor monitorKrypterte;

    Telegrafist(Kanal kanal, int kanalId, Monitor monitorKrypterte){
        this.kanal = kanal;
        this.kanalId = kanalId;
        this.monitorKrypterte = monitorKrypterte;
        sekvensNummer = 0;
    }

    //Finner nye meldinger fra kanalen, og skriver de til monitoren for krypterte meldinger
    public void run(){
        String meldingen = kanal.lytt();
        while (meldingen != null){
            Melding melding = new Melding(meldingen, kanalId, sekvensNummer++);
            monitorKrypterte.settInnMelding(melding); //setter inn ny melding
            meldingen = kanal.lytt(); //Henter neste melding
        }
        Oblig6.telegrafistThreadEnded();
    }
}
