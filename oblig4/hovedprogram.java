import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Scanner;

class Oblig4 {
    private static Tabell<Pasient> pasienter = new StatiskTabell<Pasient>(25);
    private static Tabell<Legemiddel> legemidler = new StatiskTabell<Legemiddel>(30);
    private static Tabell<Resept> resepter = new StatiskTabell<Resept>(100);
    private static Legeliste leger = new Legeliste();
    private static String filnavn;

    public static void main(String[] args) {
        // tvinger engelsk for aa sikre at desimalskilletegnet er '.'
        Locale.setDefault(new Locale("en", "GB"));

        if (args.length > 0) {
            filnavn = args[0];
        } else {
            filnavn = IO.ikketomStringFraBruker("Filnavn: ");
        }

        if (Datafil.lesFraFil(filnavn)) { // vellykket ved true
            Meny.hovedmeny();
            Datafil.skrivTilFil(filnavn);
        } else {
            System.out.println("Innlesning mislyktes. Avslutter.");
        }
    }


    private static class Datafil {
        private enum Spesiallinje {
            PASIENTER("# Pasienter (id, navn, fodselsnummer, adresse, postnr)"),
            LEGEMIDLER("# Legemidler (ID, navn, type, pris, virkestoff [, styrke])"),
            LEGER("# Leger (navn, avtalenr / 0 hvis ingen avtale)"),
            RESEPTER("# Resepter (id, hvit/blaa, pasientID, legenavn, legemiddelID, reit)"),
            SLUTT("# Slutt");

            public final String tekst;

            Spesiallinje(String tekst) {
                this.tekst = tekst;
            }

            public String toString() {
                return tekst;
            }
        }

        private static boolean lesFraFil(String filnavn) {
            String linje = "Ingen linje";
            Scanner innfil = null;
            try {
                innfil = new Scanner(new File(filnavn), "utf-8");
            } catch (FileNotFoundException e) {
                System.out.format("Fant ikke filen '%s'.\n", filnavn);
                return false;
            }
            try {
                while (innfil.hasNextLine()) {
                    linje = innfil.nextLine();
                    if (linje.equals("# Slutt")) {
                        return true;
                    } else if (linje.startsWith("# Pasienter")) {
                        while (innfil.hasNextLine() && linje.length() > 0) {
                            linje = innfil.nextLine();
                            if (linje.length() > 0) {
                                String[] pasientData = linje.split(", ");
                                lagNyPasientFraFil(pasientData);
                            }
                        }
                    } else if (linje.startsWith("# Legemidler")) {
                        while (innfil.hasNextLine() && linje.length() > 0) {
                            linje = innfil.nextLine();
                            if (linje.length() > 0) {
                                String[] legemiddeldata = linje.split(", ");
                                lagNyttLegemiddelFraFil(legemiddeldata);
                            }
                        }
                    } else if (linje.startsWith("# Leger")) {
                        while (innfil.hasNextLine() && linje.length() > 0) {
                            linje = innfil.nextLine();
                            if (linje.length() > 0) {
                                String[] legedata = linje.split(", ");
                                lagNyLegeFraFil(legedata);
                            }
                        }
                    } else if (linje.startsWith("# Resepter")) {
                        while (innfil.hasNextLine() && linje.length() > 0) {
                            linje = innfil.nextLine();
                            if (linje.length() > 0) {
                                String[] reseptdata = linje.split(", ");
                                lagNyReseptFraFil(reseptdata);
                            }
                        }
                    } else {
                        System.out.println("Feil i fil? Fant ingen "
                                           + "behandling for linjen: "
                                           + linje);
                    }
                }
                System.out.println("Advarsel: Filen sluttet uventet.");
                return true;
            } catch (NullPointerException e) {
                e.printStackTrace();
                System.out.format("Krasjet paa linje: %s\n", linje);
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
                System.out.format("Krasjet paa linje: %s\n", linje);
            } finally {
                innfil.close();
            }
            return false;
        }

        private static String[] trimForste(String[] inn) {
            String[] ut = new String[inn.length-1];
            for (int i = 0; i < ut.length; i++) {
                ut[i] = inn[i+1];
            }
            return ut;
        }

        private static void lagNyPasientFraFil(String[] data) {
            // dropp id
            data = trimForste(data);

            lagNyPasient(data);
        }

        private static void lagNyLegeFraFil(String[] data) {
            lagNyLege(data);
        }

        private static void lagNyttLegemiddelFraFil(String[] data) {
            // dropp id
            data = trimForste(data);

            lagNyttLegemiddel(data);
        }

        private static void lagNyReseptFraFil(String[] data) {
            // dropp id
            data = trimForste(data);

            lagNyResept(data);
        }


        /*** UT ***/

        public static String linje(Pasient pasient) {
            return String.format("%d, %s, %011d, %s, %04d",
                                 pasient.hentId(),
                                 pasient.hentNavn(),
                                 pasient.hentFodselsnummer(),
                                 pasient.hentGateadresse(),
                                 pasient.hentPostnummer());
        }

        public static String linje(Lege lege) {
            int avtalenummer = 0;
            if (lege instanceof Fastlege) {
                avtalenummer = ((Fastlege) lege).hentAvtalenummer();
            }
            return String.format("%s, %d", lege.hentNavn(), avtalenummer);
        }

        public static String linje(Legemiddel legemiddel) {
            String type = null;
            int styrke = -1;
            if (legemiddel instanceof LegemiddelA) {
                type = "a";
                styrke = ((LegemiddelA) legemiddel).hentNarkotiskStyrke();
            } else if (legemiddel instanceof LegemiddelB) {
                type = "b";
                styrke = ((LegemiddelB) legemiddel).hentVanedannendeStyrke();
            } else if (legemiddel instanceof LegemiddelC) {
                type = "c";
            }

            String linje = String.format("%d, %s, %s, %.2f, %s",
                                         legemiddel.hentId(),
                                         legemiddel.hentNavn(),
                                         type,
                                         legemiddel.hentPris(),
                                         String.valueOf(
                                            legemiddel.hentVirkestoff()));
            if (styrke != -1) {
                linje += String.format(", %d", styrke);
            }

            return linje;
        }

        public static String linje(Resept resept) {
            return String.format("%d, %s, %d, %s, %d, %d",
                                 resept.hentId(),
                                 resept.farge().toLowerCase(),
                                 resept.hentPasientId(),
                                 resept.hentLege().hentNavn(),
                                 resept.hentLegemiddel().hentId(),
                                 resept.hentReit());
        }

        public static String pasientLinjer() {
            String s = Spesiallinje.PASIENTER.toString() + "\n";
            for (Pasient pasient : pasienter) {
                s += String.format("%s\n", linje(pasient));
            }
            return s;
        }

        public static String legeLinjer() {
            String s = Spesiallinje.LEGER.toString() + "\n";
            for (Lege lege : leger) {
                s += String.format("%s\n", linje(lege));
            }
            return s;
        }

        public static String legemiddelLinjer() {
            String s = Spesiallinje.LEGEMIDLER.toString() + "\n";
            for (Legemiddel legemiddel : legemidler) {
                s += String.format("%s\n", linje(legemiddel));
            }
            return s;
        }

        public static String reseptLinjer() {
            String s = Spesiallinje.RESEPTER.toString() + "\n";
            for (Resept resept : resepter) {
                s += String.format("%s\n", linje(resept));
            }
            return s;
        }

        public static void skrivTilFil(String filnavn) {
            PrintWriter ut;
            try {
                ut = new PrintWriter(new File(filnavn), "utf-8");
            } catch (FileNotFoundException e) {
                System.out.printf("Feil! Kunne ikke åpne '%s' for skriving.\n",
                                  filnavn);
                return;
            } catch (UnsupportedEncodingException e) {
                System.out.println("Feil! Kan ikke skrive til fil fordi UTF-8 "
                                  + "ikke er støttet på denne plattformen.");
                return;
            }

            /* vi bruker printf med '\n' for å sørge for at linjeskift-tegnet
            blir riktig på Windows */
            ut.printf("%s\n", pasientLinjer());
            ut.printf("%s\n", legemiddelLinjer());
            ut.printf("%s\n", legeLinjer());
            ut.printf("%s\n", reseptLinjer());
            ut.printf("%s\n", Spesiallinje.SLUTT);

            ut.close();
        }
    }

    private static class FraBruker {
        private static String[] reseptfarger = {"blaa", "hvit"};
        private static String[] legemiddeltyper = {"a", "b", "c"};

        @SuppressWarnings("unchecked")
        private static <T> T[] tilArray(Tabell<T> tabellen) {
            T[] ut = (T[]) new Object[tabellen.storrelse()];
            int pos = 0;
            for (T element : tabellen) {
                ut[pos++] = element;
            }
            return ut;
        }

        @SuppressWarnings("unchecked")
        private static <T> T[] tilArray(Liste<T> listen) {
            T[] ut = (T[]) new Object[listen.storrelse()];
            int pos = 0;
            for (T element : listen) {
                ut[pos++] = element;
            }
            return ut;
        }

        /* VALG FRA BRUKER */

        public static Pasient velgPasient() {
            return IO.valgFraBruker("Pasient: ", tilArray(pasienter), true);
        }

        public static Lege velgLege() {
            String[] legenavn = leger.stringArrayMedNavn();
            String navn = IO.valgFraBruker("Lege: ", legenavn, true);
            return leger.finnLege(navn);
        }

        public static Legemiddel velgLegemiddel() {
            return IO.valgFraBruker("Legemiddel: ", tilArray(legemidler), true);
        }

        public static Legemiddel velgLegemiddel(Liste<Legemiddel>
                                                legemiddelliste) {
            return IO.valgFraBruker("Legemiddel: ", tilArray(legemiddelliste),
                                    true);
        }

        public static Resept velgResept(Liste<Resept> reseptliste) {
            return IO.valgFraBruker("Resept: ", tilArray(reseptliste), true);
        }


        /* OPPRETTELSE AV NYE OBJEKTER */

        public static void opprettPasient() {
            String[] data = new String[4];
            int dataPos = 0;
            data[dataPos++] = IO.ikketomStringFraBruker("Navn: ");
            long fodselsnummer = IO.longFraBruker("Fødselsnummer: ");
            data[dataPos++] = String.valueOf(fodselsnummer);
            data[dataPos++] = IO.ikketomStringFraBruker("Gatenavn og nummer: ");
            int postnummer = IO.intFraBruker("Postnummer: ");
            data[dataPos++] = String.valueOf(postnummer);
            lagNyPasient(data);
        }

        public static void opprettLege() {
            String[] data = new String[2];
            int dataPos = 0;
            data[dataPos++] = IO.ikketomStringFraBruker("Navn: ");
            int avtalenummer = IO.intFraBruker("Avtalenummer: ");
            data[dataPos++] = String.valueOf(avtalenummer);
            lagNyLege(data);
        }

        public static void opprettLegemiddel() {
            String[] data = new String[5];
            int dataPos = 0;
            data[dataPos++] = IO.ikketomStringFraBruker("Navn: ");
            String type = IO.valgFraBruker("Type: ", legemiddeltyper, true);
            data[dataPos++] = type;
            double pris = IO.doubleFraBruker("Pris: ");
            data[dataPos++] = String.valueOf(pris);
            double virkestoff = IO.doubleFraBruker("Virkestoff: ");
            data[dataPos++] = String.valueOf(virkestoff);

            if (type.equals("a") || type.equals("b")) {
                int styrke = IO.intFraBruker("Styrke: ");
                data[dataPos++] = String.valueOf(styrke);
            } else {
                data[dataPos++] = null;
            }
            lagNyttLegemiddel(data);
        }

        public static void opprettResept() {
            if (pasienter.erTom()) {
                System.out.println("Det finnes ingen pasienter. Kan ikke "
                                   + "opprette en resept uten en pasient.");
                return;
            } else if (leger.erTom()) {
                System.out.println("Det finnes ingen leger. Kan ikke "
                                   + "opprette en resept uten en lege.");
                return;
            } else if (legemidler.erTom()) {
                System.out.println("Det finnes ingen legemidler. Kan ikke "
                                   + "opprette en resept uten et legemiddel.");
                return;
            }

            String[] data = new String[5];
            int dataPos = 0;
            data[dataPos++] = IO.valgFraBruker("Farge: ", reseptfarger, true);

            Pasient pasient = velgPasient();
            int pasientId = pasient.hentId();
            data[dataPos++] = String.valueOf(pasientId);

            data[dataPos++] = velgLege().hentNavn();

            Legemiddel legemiddel = velgLegemiddel();
            int legemiddelId = legemiddel.hentId();
            data[dataPos++] = String.valueOf(legemiddelId);

            int reit = IO.intFraBrukerFomTom("Reit: ", 0, Integer.MAX_VALUE);
            data[dataPos++] = String.valueOf(reit);

            lagNyResept(data);
        }
    }

    private static class Meny {
        private enum Hovedmeny {
            SKRIV_UT_MENY("Skriver ut denne menyen"),
            LEGG_TIL("Legg til nytt objekt"),
            SKRIV_UT_ALLE("Skriv ut alle pasienter/leger/legemidler/resepter"),
            HENT_UT_PAA_RESEPT("Hent ut et legemiddel på en resept"),
            STATISTIKK("Skriv ut statistikk"),
            SKRIV_TIL_FIL("Skriv alle data til en fil"),
            AVSLUTT("Avslutt programmet og skriv tilbake til fil");

            private String beskrivelse;

            Hovedmeny(String beskrivelse) {
                this.beskrivelse = beskrivelse;
            }

            public String toString() {
                return String.format("[%d] %s", ordinal(), beskrivelse);
            }
        }

        private static void hovedmeny() {
            Hovedmeny menyvalg = Hovedmeny.SKRIV_UT_MENY;
            Hovedmeny[] alternativer = Hovedmeny.values();
            int menyMin = 0;
            int menyMax = alternativer.length - 1;
            while (true) {
                switch(menyvalg) {
                    case SKRIV_UT_MENY:
                        for (Hovedmeny alternativ : alternativer) {
                            System.out.println(alternativ);
                        }
                        break;

                    case LEGG_TIL:
                        menyLeggTil();
                        break;

                    case SKRIV_UT_ALLE:
                        menySkrivUtAlle();
                        break;

                    case STATISTIKK:
                        menyStatistikk();
                        break;

                    case HENT_UT_PAA_RESEPT:
                        menyHentUtPaaResept();
                        break;

                    case SKRIV_TIL_FIL:
                        String filnavn = IO.ikketomStringFraBruker("Filnavn: ");
                        Datafil.skrivTilFil(filnavn);
                        break;

                    case AVSLUTT:
                        return;

                }
                int nyttValgTall = IO.intFraBrukerFomTom("~|Hovedmeny: ",
                                                         menyMin, menyMax);
                menyvalg = alternativer[nyttValgTall];
            }

        }

        private enum MenyLeggTil {
            SKRIV_UT_MENY("Skriv ut denne menyen"),
            PASIENT("Legg til en ny pasient"),
            LEGE("Legg til en ny lege"),
            LEGEMIDDEL("Legg til et nytt legemiddel"),
            RESEPT("Legg til en ny resept"),
            TILBAKE("Gå tilbake til hovedmenyen");

            private String beskrivelse;

            MenyLeggTil(String beskrivelse) {
                this.beskrivelse = beskrivelse;
            }

            public String toString() {
                return String.format("[%d] %s", ordinal(), beskrivelse);
            }
        }

        private static void menyLeggTil() {
            MenyLeggTil menyvalg = MenyLeggTil.SKRIV_UT_MENY;
            MenyLeggTil[] alternativer = MenyLeggTil.values();
            int menyMin = 0;
            int menyMax = alternativer.length - 1;
            while (true) {
                switch(menyvalg) {
                    case SKRIV_UT_MENY:
                        for (MenyLeggTil alternativ : alternativer) {
                            System.out.println(alternativ);
                        }
                        break;

                    case PASIENT:
                        FraBruker.opprettPasient();
                        return;

                    case LEGE:
                        FraBruker.opprettLege();
                        return;

                    case LEGEMIDDEL:
                        FraBruker.opprettLegemiddel();
                        return;

                    case RESEPT:
                        FraBruker.opprettResept();
                        return;

                    case TILBAKE:
                        return;

                }
                int nyttValgTall = IO.intFraBrukerFomTom("~|Legg til: ",
                                                         menyMin, menyMax);
                menyvalg = alternativer[nyttValgTall];
            }
        }

        private enum MenySkrivUtAlle {
            SKRIV_UT_MENY("Skriv ut denne menyen"),
            PASIENTER("Skriv ut alle pasienter"),
            LEGER("Skriv ut alle leger"),
            LEGEMIDLER("Skriv ut alle legemidler"),
            RESEPTER("Skriv ut alle resepter"),
            TILBAKE("Gå tilbake til hovedmenyen");

            private String beskrivelse;

            MenySkrivUtAlle(String beskrivelse) {
                this.beskrivelse = beskrivelse;
            }

            public String toString() {
                return String.format("[%d] %s", ordinal(), beskrivelse);
            }
        }

        private static void menySkrivUtAlle() {
            MenySkrivUtAlle menyvalg = MenySkrivUtAlle.SKRIV_UT_MENY;
            MenySkrivUtAlle[] alternativer = MenySkrivUtAlle.values();
            int menyMin = 0;
            int menyMax = alternativer.length - 1;
            while (true) {
                switch(menyvalg) {
                    case SKRIV_UT_MENY:
                        for (MenySkrivUtAlle alternativ : alternativer) {
                            System.out.println(alternativ);
                        }
                        break;

                    case PASIENTER:
                        System.out.println(Datafil.pasientLinjer());
                        return;

                    case LEGER:
                        System.out.println(Datafil.legeLinjer());
                        return;

                    case LEGEMIDLER:
                        System.out.println(Datafil.legemiddelLinjer());
                        return;

                    case RESEPTER:
                        System.out.println(Datafil.reseptLinjer());
                        return;

                    case TILBAKE:
                        return;

                }
                int nyttValgTall = IO.intFraBrukerFomTom("~|Skriv ut: ",
                                                         menyMin, menyMax);
                menyvalg = alternativer[nyttValgTall];
            }
        }

        private static void menyHentUtPaaResept() {
            if (pasienter.erTom()) {
                System.out.println("Det finnes ingen pasienter.");
                return;
            }
            Pasient pasienten = FraBruker.velgPasient();

            Liste<Resept> pasientensResepter = pasienten.hentReseptliste();
            if (pasientensResepter.erTom()) {
                System.out.printf("%s har ingen resepter\n", pasienten);
                return;
            }

            System.out.println("Velg hvilken resept som skal benyttes");
            Resept resepten = FraBruker.velgResept(pasientensResepter);

            boolean gyldig = resepten.bruk();
            if (gyldig) {
                Legemiddel legemidlet = resepten.hentLegemiddel();
                double pris = resepten.prisAaBetale();
                System.out.printf("%s betaler %.2f kr for %s\n",
                                  pasienten, pris, legemidlet);
                int reit = resepten.hentReit();
                if (reit > 0) {
                    System.out.printf("Resepten kan benyttes %d ganger til.\n",
                                      reit);
                } else {
                    System.out.println("Resepten kan ikke brukes flere"
                                       +" ganger.");
                }
            } else {
                System.out.println("Resepten er ikke lenger gyldig.");
            }
        }

        private enum MenyStatistikk {
            SKRIV_UT_MENY("Skriv ut denne menyen"),
            PASIENT("Skriv ut alle blå resepter tilhørende en bestemt pasient"),
            LEGE("Skriv ut alle blå resepter tilhørende en bestemt lege"),
            /*
            RESEPT_NARKOTISK("Skriv ut alle resepter på narkotiske legemidler"),
            RESEPT_VANEDANNENDE("Skriv ut alle resepter på vanedannende "
                                + "legemidler"),
            */
            TILBAKE("Gå tilbake til hovedmenyen");

            private String beskrivelse;

            MenyStatistikk(String beskrivelse) {
                this.beskrivelse = beskrivelse;
            }

            public String toString() {
                return String.format("[%d] %s", ordinal(), beskrivelse);
            }
        }

        private static void menyStatistikk() {
            MenyStatistikk menyvalg = MenyStatistikk.SKRIV_UT_MENY;
            MenyStatistikk[] alternativer = MenyStatistikk.values();
            int menyMin = 0;
            int menyMax = alternativer.length - 1;
            while (true) {
                switch(menyvalg) {
                    case SKRIV_UT_MENY:
                        for (MenyStatistikk alternativ : alternativer) {
                            System.out.println(alternativ);
                        }
                        break;

                    case PASIENT:
                        statistikkPasient();
                        return;

                    case LEGE:
                        statistikkLege();
                        return;

                    /*
                    case RESEPT_NARKOTISK:
                        //
                        return;

                    case RESEPT_VANEDANNENDE:
                        //
                        return;
                    */

                    case TILBAKE:
                        return;

                }
                int nyttValgTall = IO.intFraBrukerFomTom("~|Skriv ut: ",
                                                         menyMin, menyMax);
                menyvalg = alternativer[nyttValgTall];
            }
        }

        private static void statistikkPasient() {
            if (pasienter.erTom()) {
                System.out.println("Det finnes ingen pasienter å skrive ut "
                                   + "statistikk for");
                return;
            }
            Pasient pasienten = FraBruker.velgPasient();
            Liste<Resept> pasientensResepter = pasienten.hentReseptliste();
            int blaaResepter = 0;
            for (Resept resept : pasientensResepter) {
                if (resept instanceof BlaaResept) {
                    blaaResepter++;
                }
            }
            if (blaaResepter == 0) {
                System.out.printf("%s har ingen blå resept(er).\n", pasienten);
            } else {
                System.out.printf("%s har %d blå resept(er):\n", pasienten,
                                                                 blaaResepter);

                int teller = 1;
                for (Resept resept : pasientensResepter) {
                    if (resept instanceof BlaaResept) {

                        System.out.printf("#%d: %s (reit=%d, "
                                          + "skrevet ut av %s)\n",
                                          teller++,
                                          resept.hentLegemiddel().hentNavn(),
                                          resept.hentReit(),
                                          resept.hentLege().hentNavn());
                    }
                }
            }
        }

        private static void statistikkLege() {
            if (leger.erTom()) {
                System.out.println("Det finnes ingen leger å skrive ut "
                                   + "statistikk for");
                return;
            }
            Lege legen = FraBruker.velgLege();
            Liste<Resept> legensResepter = legen.hentReseptliste();
            int blaaResepter = 0;
            for (Resept resept : legensResepter) {
                if (resept instanceof BlaaResept) {
                    blaaResepter++;
                }
            }
            if (blaaResepter == 0) {
                System.out.printf("%s har ikke skrevet ut noen blå "
                                  +"resept(er).\n", legen.hentNavn());
            } else {
                System.out.printf("%s har skrevet ut %d blå resept(er):\n",
                                  legen.hentNavn(), blaaResepter);

                int teller = 1;
                for (Resept resept : legensResepter) {
                    if (resept instanceof BlaaResept) {
                        int pasientId = resept.hentPasientId();
                        Pasient pasient = pasienter.hentFraPlass(pasientId);
                        System.out.printf("#%d: %s (reit=%d, "
                                          + "skrevet ut til %s)\n",
                                          teller++,
                                          resept.hentLegemiddel().hentNavn(),
                                          resept.hentReit(),
                                          pasient);
                    }
                }
            }
        }
    }


    private static class Objektfabrikk {
        public static Pasient lagNyPasient(String[] data) {
            int dataPos = 0;
            String navn = data[dataPos++];
            long fodselsnummer = Long.parseLong(data[dataPos++]);
            String adresse = data[dataPos++];
            int postnummer = Integer.parseInt(data[dataPos++]);

            return new Pasient(navn, fodselsnummer, adresse, postnummer);
        }

        public static Lege lagNyLege(String[] data) {
            int dataPos = 0;
            String navn = data[dataPos++];
            int avtalenummer = Integer.parseInt(data[dataPos++]);

            if (avtalenummer == 0) {
                return new Lege(navn);
            } else {
                return new Fastlege(navn, avtalenummer);
            }
        }

        public static Legemiddel lagNyttLegemiddel(String[] data) {
            int dataPos = 0;
            String navn = data[dataPos++];
            String legemiddeltype = data[dataPos++].toLowerCase();
            double pris = Double.parseDouble(data[dataPos++]);
            double virkestoff = Double.parseDouble(data[dataPos++]);

            if (legemiddeltype.equals("a") || legemiddeltype.equals("b")) {
                int styrke = Integer.parseInt(data[dataPos++]);
                if (legemiddeltype.equals("a")) {
                    return new LegemiddelA(navn, pris, virkestoff, styrke);
                } else {
                    return new LegemiddelB(navn, pris, virkestoff, styrke);
                }
            } else if (legemiddeltype.equals("c")) {
                return new LegemiddelC(navn, pris, virkestoff);
            } else {
                throw new RuntimeException("Ugyldig legemiddeltype: "
                                           + legemiddeltype);
            }
        }

        public static Resept lagNyResept(String[] data) {
            int dataPos = 0;
            String farge = data[dataPos++].toLowerCase();
            int pasientId = Integer.parseInt(data[dataPos++]);
            String legenavn = data[dataPos++];
            Lege utskrivendeLege = leger.finnLege(legenavn);
            int legemiddelId = Integer.parseInt(data[dataPos++]);
            Legemiddel legemiddel = legemidler.hentFraPlass(legemiddelId);
            int reit = Integer.parseInt(data[dataPos++]);

            if (farge.equals("hvit")) {
                return new HvitResept(legemiddel, utskrivendeLege, pasientId,
                                      reit);
            } else if (farge.equals("blå") || farge.equals("blaa")) {
                return new BlaaResept(legemiddel, utskrivendeLege, pasientId,
                                      reit);
            } else {
                throw new RuntimeException("Ugyldig reseptfarge: "
                                           + farge);
            }
        }
    }


    private static void lagNyPasient(String[] data) {
        Pasient pasienten = Objektfabrikk.lagNyPasient(data);
        pasienter.settInn(pasienten);
    }

    private static void lagNyLege(String[] data) {
        Lege lege = Objektfabrikk.lagNyLege(data);
        leger.settInn(lege);
    }

    private static void lagNyttLegemiddel(String[] data) {
        Legemiddel legemiddel = Objektfabrikk.lagNyttLegemiddel(data);
        legemidler.settInn(legemiddel);
    }

    private static void lagNyResept(String[] data) {
        Resept resept = Objektfabrikk.lagNyResept(data);

        // legg til i felles resepttabell
        resepter.settInn(resept);

        // legg til i legens liste
        Lege legen = resept.hentLege();
        legen.leggTilResept(resept);

        // legg til i pasientens liste
        int pasientId = resept.hentPasientId();
        Pasient pasient = pasienter.hentFraPlass(pasientId);
        pasient.leggTilResept(resept);
    }
}



class IO {
    private static Scanner inn = new Scanner(System.in);
    public static int intFraBruker(String melding) {
        boolean suksess = false;
        int tallet = 0;
        while (!suksess) {
            System.out.print(melding);
            String linje = inn.nextLine();
            try {
                tallet = Integer.parseInt(linje);
                return tallet;
            } catch (NumberFormatException e) {
                System.out.printf("'%s' er ikke et heltall, din løk!\n",
                                  linje);
            }
        }
        return tallet;
    }

    public static int intFraBrukerFomTom(String melding, int fom, int tom) {
        boolean suksess = false;
        int tallet = 0;
        while (!suksess) {
            tallet = intFraBruker(melding);
            if (tallet < fom || tom < tallet) {
                System.out.printf("%d ligger ikke i intervallet [%d, %d]!\n",
                                  tallet, fom, tom);
            } else {
                suksess = true;
            }
        }
        return tallet;
    }

    public static long longFraBruker(String melding) {
        boolean suksess = false;
        long tallet = 0;
        while (!suksess) {
            System.out.print(melding);
            String linje = inn.nextLine();
            try {
                tallet = Long.parseLong(linje);
                return tallet;
            } catch (NumberFormatException e) {
                System.out.printf("'%s' er ikke et heltall, din løk!\n",
                                  linje);
            }
        }
        return tallet;
    }

    public static double doubleFraBruker(String melding) {
        boolean suksess = false;
        double tallet = 0;
        while (!suksess) {
            System.out.print(melding);
            String linje = inn.nextLine();
            try {
                tallet = Double.parseDouble(linje);
                return tallet;
            } catch (NumberFormatException e) {
                System.out.printf("'%s' er ikke et tall, din løk!\n",
                                  linje);
            }
        }
        return tallet;
    }


    public static String ikketomStringFraBruker(String melding) {
        boolean suksess = false;
        String linjen = null;
        while (!suksess) {
            System.out.print(melding);
            linjen = inn.nextLine();
            if (linjen.equals("")) {
                System.out.println("Oppgi minst ett tegn!");
            } else {
                suksess = true;
            }
        }
        return linjen;
    }

    public static <T> T valgFraBruker(String melding, T[] alternativer,
                                      boolean skrivUtAlternativer) {
        boolean suksess = false;
        T valgtElement = null;
        int minIndeks = 0;
        int maksIndeks = alternativer.length - 1;
        if (skrivUtAlternativer) {
            for (int i = minIndeks; i <= maksIndeks; i++) {
                System.out.printf("[%d] %s\n", i, alternativer[i]);
            }
        }

        int indeks = intFraBrukerFomTom(melding, minIndeks, maksIndeks);
        return alternativer[indeks];

    }
}
