import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor{
    private Koe<Melding> meldinger  = new Koe<Melding>();

    private Lock lock = new ReentrantLock();
    private Condition erTomCondition = lock.newCondition();

    public void settInnMelding(Melding melding){
        lock.lock();

        try{
            meldinger.settInn(melding);
            erTomCondition.signal();
        }finally{
            lock.unlock();
        }
    }

    public Melding hentMelding()throws InterruptedException{
        lock.lock();

        try{
            if (meldinger.erTom() && Oblig6.ingenAktiveTelegrafister() && Oblig6.aktiveKryptografTraader()){
                return null;
            }

            if (meldinger.erTom() && Oblig6.ingenAktiveTelegrafister() && !Oblig6.aktiveKryptografTraader()){
                return null;
            }

            if (meldinger.erTom()){
                erTomCondition.await();
            }

            return meldinger.fjern();
        }catch(InterruptedException ie){
            throw ie;
        }finally{
            lock.unlock();
        }
    }

    public boolean meldingerErTom(){
        return meldinger.erTom();
    }
}
