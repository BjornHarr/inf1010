public class Legeliste extends OrdnetLenkeliste<Lege>{
    /**
    *   Soeker gjennom listen etter en lege med samme navn som ´navn´
    *   og returnerer legen (uten aa gjerne den fra listen)
    *   @param  navn    navnet til legen
    *   @return legen
    */
    public Lege finnLege(String navn){
        for (Lege itLege : this){
            if (itLege.hentNavn().equals(navn)){
                return itLege;
            }
        }
        return null;
    }

    /**
    *   Returnerer et String[] med navnene til alle legene i listen
    * i samme rekkefoelge som de staar i listen.
    *   @return array med alle legene
    */
    public String[] stringArrayMedNavn(){
        String[] legeNavn = new String[storrelse()];

        int i = 0;
        for (Lege itLege : this){
            legeNavn[i++] = itLege.hentNavn();
        }

        return legeNavn;
    }
}
