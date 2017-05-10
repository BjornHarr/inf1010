public class Kryptograf implements Runnable{

    private Monitor monitorKrypterte;
    private Monitor monitorDekrypterte;

    Kryptograf(Monitor monitorKrypterte, Monitor monitorDekrypterte){
        this.monitorKrypterte = monitorKrypterte;
        this.monitorDekrypterte = monitorDekrypterte;
    }

    public void run(){
        try{
            Melding melding = monitorKrypterte.hentMelding();
            while (melding != null){
                melding.dekrypter();
                monitorDekrypterte.settInnMelding(melding);
                melding = monitorKrypterte.hentMelding();
            }
        }catch(InterruptedException ie){
            System.out.println("<---------------Kryptograf  INTERRUPTED---------->");
        }finally{
            Oblig6.kryptografTraadSluttet();            
        }
    }
}
