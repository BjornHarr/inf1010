import java.util.Iterator;
import java.lang.reflect.InvocationTargetException;

/**
 * Testene forutsetter at klassenavn er som beskrevet i obligteksten,
 * at grensesnittene Tabell<T> og Liste<T> finnes, og at unntakene
 * UgyldigPlassUnntak og FullTabellUnntak finnes. Disse grensesnittene og
 * klassene er gitt i oppgaveteksten.
 *
 * Du trenger ikke sette deg inn i koden i denne filen - det holder at du
 * kompilerer filen og kjorer TestOblig3. Om det er kompileringsfeil er det
 * sannsynligvis fordi du ikke har lagt inn de 4 klassene/grensesnittene listet
 * ovenfor.
 *
 * MERK: Om filen ikke kompilerer, trenger du Java 1.8.
 *
 * @version 1.1 2017-02-24
 */
@SuppressWarnings("unchecked")
public class TestOblig3 {
    private static boolean verbose = false;
    private static int antallTester = 0;
    private static int antallPasserte = 0;
    private static String ansiRed = System.getProperty("os.name").startsWith("Windows") ? "" : "\u001B[31m";
    private static String ansiGrn = System.getProperty("os.name").startsWith("Windows") ? "" : "\u001B[32m";
    private static String ansiClr = System.getProperty("os.name").startsWith("Windows") ? "" : "\u001B[0m";

    public static void main(String[] args) {
        if (args.length > 0 && (args[0].equals("-v") || args[0].equals("--verbose"))) {
            verbose = true;
        } else {
            System.out.println("Tips: Vis alle tester som passerer med -v eller --verbose: 'java TestOblig3 -v'");
        }

        testStatiskTabell();
        testStatiskTabellIterator();
        testDynamiskTabell();
        testDynamiskTabellIterator();
        testStabel();
        testStabelIterator();
        testKoe();
        testKoeIterator();
        testOrdnetLenkeliste();
        testOrdnetLenkelisteIterator();

        System.out.printf("\n====== Oppsummering =======\n%d av %d tester OK\n", antallPasserte, antallTester);

        if (antallTester != antallPasserte) {
            System.err.printf(ansiRed + ">>>> FEIL i %d tester! <<<<\n" + ansiClr, antallTester-antallPasserte);
        }
        else {
            System.out.println(ansiGrn + ">>>> Alle tester OK! <<<<" + ansiClr);
            System.out.println("Merk: Dette er ingen garanti for at ALT er implementert riktig.");
        }
    }

    private static void testStatiskTabell() {
        System.out.println("\n===== testStatiskTabell =====");
        int startTester = antallTester;
        int startPasserte = antallPasserte;

        try {
            Tabell<String> statiskTabell = (Tabell<String>) Class.forName("StatiskTabell").getDeclaredConstructor(int.class).newInstance(4);
            testErTom(statiskTabell, "erTom() i tom StatiskTabell med 4 plasser", true);
            testStorrelse(statiskTabell, "storrelse() i tom StatiskTabell med 4 plasser", 0);

            try {
                statiskTabell.hentFraPlass(5); // her er det forventet at det kastes unntak
                failTest("UgyldigPlassUnntak ved hentFraPlass(5) der det bare er 4 plasser", "ingen unntak", "UgyldigPlassUnntak");
            } catch(UgyldigPlassUnntak e) { //klassen er gitt i oppgaveteksten
                passTest("UgyldigPlassUnntak ved hentFraPlass(5) der det bare er 4 plasser");
            } catch(Exception e) {
                failTest("UgyldigPlassUnntak ved hentFraPlass(5) der det bare er 4 plasser",
                        "ingen/feil unntakshandtering ("+e.getClass().getCanonicalName()+")", "UgyldigPlassUnntak");
            }

            testSettInn(statiskTabell, "element 1", "innsetting av element i tom StatiskTabell");

            testErTom(statiskTabell, "erTom() i StatiskTabell med ett element", false);
            testStorrelse(statiskTabell, "storrelse() i StatiskTabell med ett element", 1);

            try {
                sjekkLikhet("hentFraPlass(0) i StatiskTabell med ett element", statiskTabell.hentFraPlass(0), "element 1");
            } catch(Exception e) { uventetUnntak(e, "hentFraPlass(0) i StatiskTabell med ett element"); }

            testSettInn(statiskTabell, "element 2", "innsetting av element 2 i beholder med kapasitet 4");
            testSettInn(statiskTabell, "element 3", "innsetting av element 3 i beholder med kapasitet 4");
            testSettInn(statiskTabell, "element 4", "innsetting av element 4 i beholder med kapasitet 4");

            testErTom(statiskTabell, "erTom() i StatiskTabell med fire elementer", false);
            testStorrelse(statiskTabell, "storrelse() i StatiskTabell med fire elementer", 4);

            try {
                sjekkLikhet("hentFraPlass(3) i StatiskTabell med fire elementer", statiskTabell.hentFraPlass(3), "element 4");
            } catch (Exception e) { uventetUnntak(e, "hentFraPlass(3) i StatiskTabell med fire elementer"); }

            try {
                statiskTabell.settInn("element 5"); // her er det forventet at det kastes unntak
                failTest("FullTabellUnntak", "ingen unntak", "FullTabellUnntak");
            } catch(FullTabellUnntak e) { //klassen er gitt i oppgaveteksten
                passTest("FullTabellUnntak");
            } catch(Exception e) {
                failTest("FullTabellUnntak", "ingen/feil unntakshandtering ("+e.getClass().getCanonicalName()+")", "FullTabellUnntak");
            }

            try {
                statiskTabell.hentFraPlass(-1);
                failTest("UgyldigPlassUnntak ved hentFraPlass(-1)", "ingen unntak", "UgyldigPlassUnntak");
            } catch(UgyldigPlassUnntak e) {
                passTest("UgyldigPlassUnntak ved hentFraPlass(-1)");
            } catch(Exception e) {
                failTest("UgyldigPlassUnntak ved hentFraPlass(-1)", "ingen/feil unntakshandtering ("+e.getClass().getCanonicalName()+")", "UgyldigPlassUnntak");
            }
        }
        catch(ClassNotFoundException e) {
            System.out.println("Kan ikke teste StatiskTabell-iterator siden klassen ikke finnes");
            antallTester++;
        }
        catch(NoSuchMethodException e) {
            System.out.println("Kan ikke teste StatiskTabell-iterator siden konstruktoren StatiskTabell(int) ikke finnes");
            antallTester++;
        }
        // Om neste kodelinje gir kompileringsfeil, har du en gammel versjon av Java.
        // Da skal du installere Java 1.8, som du uansett vil trenge senere i INF1010.
        catch(InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.printf(">> %d av %d tester OK\n", antallPasserte-startPasserte, antallTester-startTester);
    }

    private static void testStatiskTabellIterator() {
        System.out.println("\n===== testStatiskTabellIterator =====");
        int startTester = antallTester;
        int startPasserte = antallPasserte;

        try {
            Tabell<String> statiskTabell = (Tabell<String>) Class.forName("StatiskTabell").getDeclaredConstructor(int.class).newInstance(4);
            Iterator<String> it = testOpprettIteratorObjekt(statiskTabell);

            testIteratorHasNext(it, "hasNext() i tom statiskTabell", false);

            testSettInn(statiskTabell, "element1");

            it = testOpprettIteratorObjekt(statiskTabell);
            testIteratorHasNext(it, "hasNext() i tabell med ett element", true);
            testIteratorNext(it, "next() i tabell med ett element", "element1");
            testIteratorHasNext(it, "hasNext() etter next() i tabell med ett element", false);

            testSettInn(statiskTabell, "element2");
            testSettInn(statiskTabell, "element3");
            testSettInn(statiskTabell, "element4");

            it = testOpprettIteratorObjekt(statiskTabell);
            testIteratorNext(it, "at next() i en ny iterator begynner fra starten igjen", "element1");
            testIteratorNext(it, "at next() fortsetter fra forste element", "element2");
            testIteratorNext(it, null, null);
            testIteratorNext(it, "at next() kommer til siste element", "element4");
            testIteratorHasNext(it, "hasNext() ved slutten av tabellen", false);
        }
        catch(ClassNotFoundException e) {
            System.out.println("Kan ikke teste StatiskTabell-iterator siden klassen ikke finnes");
            antallTester++;
        }
        catch(NoSuchMethodException e) {
            System.out.println("Kan ikke teste StatiskTabell-iterator siden konstruktoren StatiskTabell(int) ikke finnes");
            antallTester++;
        }
        catch(InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        catch(OpprettIteratorUnntak e) {
            System.out.println(e.getMessage());
        }
        System.out.printf(">> %d av %d tester OK\n", antallPasserte-startPasserte, antallTester-startTester);
    }

    private static void testDynamiskTabell() {
        System.out.println("\n===== testDynamiskTabell =====");
        int startTester = antallTester;
        int startPasserte = antallPasserte;

        try {
            Tabell<String> dynamiskTabell = (Tabell<String>) Class.forName("DynamiskTabell").getDeclaredConstructor(int.class).newInstance(4);
            testErTom(dynamiskTabell, "erTom() i tom DynamiskTabell opprettet med 'new DynamiskTabell(4)'", true);
            testStorrelse(dynamiskTabell, "storrelse() i tom DynamiskTabell opprettet med 'new DynamiskTabell(4)'", 0);
            try {
                dynamiskTabell.hentFraPlass(0);
                failTest("UgyldigPlassUnntak ved hentFraPlass(0) i tom DynamiskTabell", "ingen unntak", "UgyldigPlassUnntak");
            } catch(UgyldigPlassUnntak e) {
                passTest("UgyldigPlassUnntak ved hentFraPlass(0) i tom DynamiskTabell");
            } catch(Exception e) {
                failTest("UgyldigPlassUnntak ved hentFraPlass(0) i tom DynamiskTabell",
                        "ingen/feil unntakshandtering ("+e.getClass().getCanonicalName()+")", "UgyldigPlassUnntak");
            }

            testSettInn(dynamiskTabell, "element 1", "innsetting av element i tom dynamiskTabell");
            testErTom(dynamiskTabell, "erTom() i DynamiskTabell med ett element", false);
            testStorrelse(dynamiskTabell, "storrelse() i DynamiskTabell med ett element", 1);
            try {
                sjekkLikhet("hentFraPlass(0) i DynamiskTabell med ett element", dynamiskTabell.hentFraPlass(0), "element 1");
            }  catch (Exception e) { uventetUnntak(e, "hentFraPlass(0) i DynamiskTabell med ett element"); }

            testSettInn(dynamiskTabell, "element 2");
            testSettInn(dynamiskTabell, "element 3");
            testSettInn(dynamiskTabell, "element 4");
            testErTom(dynamiskTabell, "erTom() i DynamiskTabell med 4 elementer", false);
            testStorrelse(dynamiskTabell, "storrelse() i DynamiskTabell med 4 elementer", 4);
            try {
                sjekkLikhet("hentFraPlass(3) i DynamiskTabell med 4 elementer", dynamiskTabell.hentFraPlass(3), "element 4");
            }  catch (Exception e) { uventetUnntak(e, "hentFraPlass(3) i DynamiskTabell med 4 elementer"); }

            try {
                dynamiskTabell.hentFraPlass(4);
                failTest("UgyldigPlassUnntak ved hentFraPlass(4) i DynamiskTabell med 4 initielle plasser & 4 elementer (maksindeks 3 forventet)", "ingen unntak", "UgyldigPlassUnntak");
            } catch(UgyldigPlassUnntak e) {
                passTest("UgyldigPlassUnntak ved hentFraPlass(4) i tom DynamiskTabell");
            } catch(Exception e) {
                failTest("UgyldigPlassUnntak ved hentFraPlass(4) i tom DynamiskTabell med 4 initielle plasser & 4 elementer (maksindeks 3 forventet)",
                    "ingen/feil unntakshandtering ("+e.getClass().getCanonicalName()+")", "UgyldigPlassUnntak");
            }

            try {
                for(int i = 5; i < 11000; i++) {
                    dynamiskTabell.settInn("element "+i);
                }
                passTest("settInn() i full DynamiskTabell");
            }
            catch(Exception e) {
                failTest("settInn() i full DynamiskTabell", "uforutsett unntak", "ingen unntak");
                e.printStackTrace();
            }
            try {
                sjekkLikhet("hentFraPlass(10123) i DynamiskTabell med 11000 elementer", dynamiskTabell.hentFraPlass(10123), "element 10124");
            } catch (Exception e) { uventetUnntak(e, "hentFraPlass(10123) i DynamiskTabell med 11000 elementer"); }

            try {
                dynamiskTabell.hentFraPlass(-1);
                failTest("UgyldigPlassUnntak ved hentFraPlass(-1)", "ingen unntak", "UgyldigPlassUnntak");
            } catch(UgyldigPlassUnntak e) {
                passTest("UgyldigPlassUnntak ved hentFraPlass(-1)");
            } catch(Exception e) {
                failTest("UgyldigPlassUnntak ved hentFraPlass(-1)", "ingen/feil unntakshandtering ("+e.getClass().getCanonicalName()+")", "UgyldigPlassUnntak");
            }

            dynamiskTabell = (Tabell<String>) Class.forName("DynamiskTabell").newInstance();
            testErTom(dynamiskTabell, "erTom() i tom DynamiskTabell opprettet med konstruktoren uten parametre", true);
            testStorrelse(dynamiskTabell, "storrelse() i tom DynamiskTabell opprettet med konstruktoren uten parametre", 0);
            try {
                dynamiskTabell.hentFraPlass(0);
                failTest("UgyldigPlassUnntak ved hentFraPlass(0) i tom DynamiskTabell", "ingen unntak", "UgyldigPlassUnntak");
            } catch(UgyldigPlassUnntak e) {
                passTest("UgyldigPlassUnntak ved hentFraPlass(0) i tom DynamiskTabell");
            } catch(Exception e) {
                failTest("UgyldigPlassUnntak ved hentFraPlass(0) i tom DynamiskTabell", "ingen/feil unntakshandtering ("+e.getClass().getCanonicalName()+")", "UgyldigPlassUnntak");
            }

            try {
                for(int i = 0; i < 11000; i++) {
                    dynamiskTabell.settInn("element "+i);
                }
                passTest("settInn() i DynamiskTabell opprettet med konstruktor uten parametre");
            }
            catch(Exception e) {
                failTest("settInn() i DynamiskTabell opprettet med konstruktor uten parametre", "uforutsett unntak", "ingen unntak");
                e.printStackTrace();
            }

            testErTom(dynamiskTabell, "erTom() i fylt DynamiskTabell opprettet med konstruktor uten parametre", false);
            testStorrelse(dynamiskTabell, "storrelse() i fylt DynamiskTabell opprettet med konstruktor uten parametre", 11000);
        }
        catch(ClassNotFoundException e) {
            System.out.println(">> Hopper over testing av DynamiskTabell (klassen finnes ikke)");
        }
        catch(NoSuchMethodException e) {
            System.out.println("FEIL: Kan ikke teste DynamiskTabell siden konstruktoren DynamiskTabell(int) ikke finnes");
            antallTester++;
        }
        catch(InstantiationException e) {
            System.out.println("FEIL: Kan ikke teste alt i DynamiskTabell siden konstruktoren uten parametre ikke finnes");
            antallTester++;
        }
        catch(IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        if (antallTester != startTester) {
            System.out.printf(">> %d av %d tester OK\n", antallPasserte-startPasserte, antallTester-startTester);
        }
    }

    private static void testDynamiskTabellIterator() {
        int startTester = antallTester;
        int startPasserte = antallPasserte;

        try {
            Tabell<String> dynamiskTabell = (Tabell<String>) Class.forName("DynamiskTabell").getDeclaredConstructor(int.class).newInstance(3);
            System.out.println("\n===== testDynamiskTabellIterator ====="); // bare print om vi faktisk kan lage iteratoren
            Iterator<String> it = testOpprettIteratorObjekt(dynamiskTabell);
            testIteratorHasNext(it, "hasNext() i tom DynamiskTabell", false);

            testSettInn(dynamiskTabell, "element1");

            it = testOpprettIteratorObjekt(dynamiskTabell);
            testIteratorHasNext(it, "hasNext() i tabell med ett element", true);
            testIteratorNext(it, "next() i tabell med ett element", "element1");
            testIteratorHasNext(it, "hasNext() etter next() i tabell med ett element", false);

            try {
                testSettInn(dynamiskTabell, "element2");
                testSettInn(dynamiskTabell, "element3");
                testSettInn(dynamiskTabell, "element4");

                it = testOpprettIteratorObjekt(dynamiskTabell);
                testIteratorNext(it, "at next() i en ny iterator begynner fra starten igjen", "element1");
                testIteratorNext(it, "at next() fortsetter fra forste element", "element2");
                testIteratorNext(it, null, null);
                testIteratorNext(it, "at next() kommer til siste element", "element4");
                testIteratorHasNext(it, "hasNext() ved slutten av tabellen", false);

                try {
                    testSettInn(dynamiskTabell, null, "innsetting av null-element i dynamiskTabell");
                    testSettInn(dynamiskTabell, "element6", "innsetting av element etter at null-element ble satt inn");
                    it = testOpprettIteratorObjekt(dynamiskTabell);
                    testIteratorNext(it,null,null);
                    testIteratorNext(it,null,null);
                    testIteratorNext(it,null,null);
                    testIteratorNext(it,null,null);
                    testIteratorHasNext(it, "hasNext() der neste element er null", true);
                    testIteratorNext(it, "next() der neste element er null", null);
                    testIteratorHasNext(it, "hasNext() etter et element som er null", true);
                    testIteratorNext(it, "next() etter et element som er null", "element6");
                    testIteratorHasNext(it, "hasNext() ved slutten av en tabell som inneholder null-elementer", false);
                } catch(Exception e) {
                    failTest("Innsetting og testing av null-elementer", "unntak", "ingen unntak");
                    e.printStackTrace();
                }
            }
            catch(Exception e) {
                // Feiler typisk om dynamiskTabell ikke utvides automatisk
                failTest("Innsetting og testing av elementer utover DynamiskTabell sin initielle storrelse", "unntak", "ingen unntak");
                e.printStackTrace();
            }
        }
        catch(ClassNotFoundException e) {
            if (verbose) System.out.println(">> Hopper over testing av DynamiskTabell-iterator (klassen DynamiskTabell finnes ikke)");
        }
        catch(NoSuchMethodException e) {
            System.out.println("FEIL: Kan ikke teste DynamiskTabell-iterator siden konstruktoren DynamiskTabell(int) ikke finnes");
            antallTester++;
        }
        catch(InstantiationException e) {
            System.out.println("FEIL: Kan ikke teste alt i DynamiskTabell-iterator siden konstruktoren uten parametre ikke finnes");
            antallTester++;
        }
        catch(IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        catch(OpprettIteratorUnntak e) {
            System.out.println(e.getMessage());
        }
        if (antallTester != startTester) {
            System.out.printf(">> %d av %d tester OK\n", antallPasserte-startPasserte, antallTester-startTester);
        }
    }

    private static void testStabel() {
        System.out.println("\n===== testStabel =====");
        int startTester = antallTester;
        int startPasserte = antallPasserte;

        try {
            Liste<String> stabel = (Liste<String>) Class.forName("Stabel").newInstance();
            testStorrelse(stabel, "storrelse() i tom Stabel", 0);
            testErTom(stabel, "erTom() i tom Stabel",true);
            testSettInn(stabel, "element1", "settInn() i tom stabel");
            testStorrelse(stabel, "storrelse() i Stabel med ett element", 1);
            testErTom(stabel, "erTom() i Stabel med ett element", false);
            testFjern(stabel, "fjern() i Stabel med ett element", "element1");
            testStorrelse(stabel, "storrelse() etter innsetting+fjerning av ett element", 0);
            testErTom(stabel, "erTom() etter innsetting+fjerning av ett element", true);

            try {
                for (int i = 0; i < 198; i++) {
                    stabel.settInn("elm"+i);
                }
            } catch(Exception e) {
                failTest("Innsetting av 200 elementer i stabel", "uventet unntak", "ingen unntak");
            }
            testSettInn(stabel, "nestsiste");
            testSettInn(stabel, "elm33"); // duplikat element, forventes at det settes inn som et nytt element
            testStorrelse(stabel, "storrelse() etter innsetting av 200 elementer", 200);
            testFjern(stabel, "fjern() en gang etter innsetting av 200 elementer", "elm33");
            testErTom(stabel, "erTom() etter fjerning av ett av 200 elementer", false);
            testStorrelse(stabel, "storrelse() etter fjerning av ett av 200 elementer", 199);
            testFjern(stabel, "fjern() etter fjerning av to av 200 elementer", "nestsiste");

            try {
                testSettInn(stabel, null, "innsetting av null-pekere i stabel");
                testSettInn(stabel, null, "innsetting av null-pekere i stabel");
                testFjern(stabel, "fjern() null-element", null);
                testErTom(stabel, "erTom() etter fjerning av null-element", false);
                testFjern(stabel, "fjern() null-element for andre gang (det var to null-elementer i stabelen)", null);
                testStorrelse(stabel, "storrelse() etter innsetting+fjerning av to null-elementer", 198);
            }
            catch(NullPointerException e) {
                failTest("NullPointerException ved innsetting av null-elementer", "Fikk NullPointerException", "ingen NullPointerException");
                e.printStackTrace();
            }
        }
        catch(ClassNotFoundException e) {
            System.out.println("Kan ikke teste Stabel siden klassen ikke finnes");
            antallTester++;
        }
        catch(InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.printf(">> %d av %d tester OK\n", antallPasserte-startPasserte, antallTester-startTester);
    }

    private static void testStabelIterator() {
        System.out.println("\n===== testStabelIterator =====");
        int startTester = antallTester;
        int startPasserte = antallPasserte;

        try {
            Liste<String> stabel = (Liste<String>) Class.forName("Stabel").newInstance();
            Iterator<String> stabelIterator = testOpprettIteratorObjekt(stabel);
            testIteratorHasNext(stabelIterator, "hasNext() i tom stabel", false);

            // iterator-rekkefolgen er udefinert i oppaveteksten,
            // setter inn elementer slik at iterasjon begge veier funker
            testSettInn(stabel, "el1");
            testSettInn(stabel, "el2");
            testSettInn(stabel, "el3");
            testSettInn(stabel, "el2");
            testSettInn(stabel, "el1");

            stabelIterator = testOpprettIteratorObjekt(stabel);
            testIteratorHasNext(stabelIterator, "hasNext() i stabel med elementer", true);
            testIteratorNext(stabelIterator, "next() i stabel med elementer", "el1");
            testIteratorHasNext(stabelIterator, "hasNext() etter next()", true);

            stabelIterator = testOpprettIteratorObjekt(stabel);
            testIteratorNext(stabelIterator, "at next() i en ny iterator begynner fra starten igjen", "el1");
            testIteratorNext(stabelIterator, null, null);
            testIteratorNext(stabelIterator, "at next() fortsetter fra forste element", "el3");
            testIteratorNext(stabelIterator, null, null);
            testIteratorNext(stabelIterator, "at next() kommer til siste element", "el1");
            testIteratorHasNext(stabelIterator, "hasNext() ved siste element", false);
        }
        catch(ClassNotFoundException e) {
            System.out.println("Kan ikke teste Stabel-iterator siden Stabel-klassen ikke finnes");
            antallTester++;
        }
        catch(InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        catch(OpprettIteratorUnntak e) {
            System.out.println(e.getMessage());
        }
        System.out.printf(">> %d av %d tester OK\n", antallPasserte-startPasserte, antallTester-startTester);
    }

    private static void testKoe() {
        System.out.println("\n===== testKoe =====");
        int startTester = antallTester;
        int startPasserte = antallPasserte;

        try {
            Liste<String> koe = (Liste<String>) Class.forName("Koe").newInstance();
            testStorrelse(koe, "storrelse() i tom Koe", 0);
            testErTom(koe, "erTom() i tom Koe", true);

            testSettInn(koe, "element1");
            testStorrelse(koe, "storrelse() i Koe med ett element", 1);
            testErTom(koe, "erTom() i Koe med ett element", false);
            testFjern(koe, "fjern() i Koe med ett element", "element1");
            testStorrelse(koe, "storrelse() etter innsetting+fjerning av ett element", 0);
            testErTom(koe, "erTom() etter innsetting+fjerning av ett element", true);

            testSettInn(koe, "elm0"); // duplikat element, forventes at det settes inn som et nytt element
            testSettInn(koe, "andreElement");
            try {
                for (int i = 0; i < 198; i++) {
                    koe.settInn("elm"+i);
                }
            } catch(Exception e) {
                failTest("Innsetting av 200 elementer i Koe", "uventet unntak", "ingen unntak");
            }
            testStorrelse(koe, "storrelse() etter innsetting av 200 elementer", 200);
            testFjern(koe, "fjern() etter innsetting av 200 elementer", "elm0");
            testErTom(koe, "erTom() etter fjerning av ett av 200 elementer", false);
            testStorrelse(koe, "storrelse() etter fjerning av ett av 200 elementer", 199);
            testFjern(koe, "fjern() etter fjerning av to av 200 elementer", "andreElement");

            try {
                Liste<String> koe2 = (Liste<String>) Class.forName("Koe").newInstance();
                testSettInn(koe2, null, "innsetting av null-element forst i Koe");
                testSettInn(koe2, "ikkeNull", "innsetting av element etter null-element i Koe");
                testSettInn(koe2, null);
                testFjern(koe2, "fjern() av et null-element", null);
                testErTom(koe, "erTom() etter fjerning av et null-element", false);
                testFjern(koe2, "fjern() etter fjerning av et null-element", "ikkeNull");
                testStorrelse(koe2, "storrelse() etter fjerning av et null-element og et vanlig element", 1);
            }
            catch(NullPointerException e) {
                sjekkLikhet("NullPointerException", "Fikk NullPointerException", "ingen NullPointerException");
                e.printStackTrace();
            }
        }
        catch(ClassNotFoundException e) {
            System.out.println("Kan ikke teste Koe siden klassen ikke finnes");
            antallTester++;
        }
        catch(InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        System.out.printf(">> %d av %d tester OK\n", antallPasserte-startPasserte, antallTester-startTester);
    }

    private static void testKoeIterator() {
        System.out.println("\n===== testKoeIterator =====");
        int startTester = antallTester;
        int startPasserte = antallPasserte;

        try {
            Liste<String> koe = (Liste<String>) Class.forName("Koe").newInstance();
            Iterator<String> koeIterator = testOpprettIteratorObjekt(koe);
            testIteratorHasNext(koeIterator, "hasNext() i tom Koe", false);

            testSettInn(koe, "el1");
            testSettInn(koe, "el2");
            testSettInn(koe, "el3");

            koeIterator = testOpprettIteratorObjekt(koe);
            testIteratorHasNext(koeIterator, "hasNext() i Koe med elementer", true);
            testIteratorNext(koeIterator, "next() i Koe med elementer", "el1");
            testIteratorHasNext(koeIterator, "hasNext() etter next()", true);

            koeIterator = testOpprettIteratorObjekt(koe);
            testIteratorNext(koeIterator, "at next() i en ny iterator begynner fra starten igjen", "el1");
            testIteratorNext(koeIterator, "at next() fortsetter fra forste element", "el2");
            testIteratorNext(koeIterator, "at next() kommer til siste element", "el3");
            testIteratorHasNext(koeIterator, "hasNext() ved siste element", false);
        }
        catch(ClassNotFoundException e) {
            System.out.println("Kan ikke teste Koe-iterator siden Koe-klassen ikke finnes");
            antallTester++;
        }
        catch(InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        catch(OpprettIteratorUnntak e) {
            System.out.println(e.getMessage());
        }

        System.out.printf(">> %d av %d tester OK\n", antallPasserte-startPasserte, antallTester-startTester);
    }

    private static void testOrdnetLenkeliste() {
        System.out.println("\n===== testOrdnetLenkeliste =====");
        int startTester = antallTester;
        int startPasserte = antallPasserte;

        try {
            Liste<String> ordnetliste = (Liste<String>) Class.forName("OrdnetLenkeliste").newInstance();
            testStorrelse(ordnetliste, "storrelse() i tom OrdnetLenkeliste", 0);
            testErTom(ordnetliste, "erTom() i tom OrdnetLenkeliste", true);

            testSettInn(ordnetliste, "element1");
            testStorrelse(ordnetliste, "storrelse() i OrdnetLenkeliste med ett element", 1);
            testErTom(ordnetliste, "erTom() i OrdnetLenkeliste med ett element", false);
            testFjern(ordnetliste, "fjern() i OrdnetLenkeliste med ett element", "element1");
            testStorrelse(ordnetliste, "storrelse() etter innsetting+fjerning av ett element", 0);
            testErTom(ordnetliste, "erTom() etter innsetting+fjerning av ett element", true);

            testSettInn(ordnetliste, "elementC");
            testSettInn(ordnetliste, "elementAA");
            testSettInn(ordnetliste, "elementAA");
            testSettInn(ordnetliste, "elementBBB");
            testSettInn(ordnetliste, "elementD");

            testStorrelse(ordnetliste, "storrelse() etter innsetting av 5 elementer der to er like", 5);
            testFjern(ordnetliste, "fjern() etter innsetting av 5 elementer", "elementAA");
            testStorrelse(ordnetliste, "storrelse() etter fjerning av ett av 5 elementer", 4);
            testFjern(ordnetliste, "fjern() for andre gang der et duplikat skal hentes", "elementAA");
            testFjern(ordnetliste, "fjern() for tredje gang etter to duplikater ble fjernet", "elementBBB");
            testStorrelse(ordnetliste, "storrelse() etter fjerning av tre elementer", 2);
        }
        catch(ClassNotFoundException e) {
            System.out.println("Kan ikke teste OrdnetLenkeliste siden klassen ikke finnes");
            antallTester++;
        }
        catch(InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        System.out.printf(">> %d av %d tester OK\n", antallPasserte-startPasserte, antallTester-startTester);
    }

    private static void testOrdnetLenkelisteIterator() {
        System.out.println("\n===== testOrdnetLenkelisteIterator =====");
        int startTester = antallTester;
        int startPasserte = antallPasserte;

        try {
            Liste<String> ordnetliste = (Liste<String>) Class.forName("OrdnetLenkeliste").newInstance();
            Iterator<String> ordnetlisteIterator = testOpprettIteratorObjekt(ordnetliste);
            testIteratorHasNext(ordnetlisteIterator, "hasNext() i tom OrdnetLenkeliste", false);

            testSettInn(ordnetliste, "elementC");
            testSettInn(ordnetliste, "elementAA");
            testSettInn(ordnetliste, "elementB");
            testSettInn(ordnetliste, "elementB");

            ordnetlisteIterator = testOpprettIteratorObjekt(ordnetliste);
            testIteratorHasNext(ordnetlisteIterator, "hasNext() i OrdnetLenkeliste med elementer", true);
            testIteratorNext(ordnetlisteIterator, "next() i OrdnetLenkeliste med elementer", "elementAA");
            testIteratorHasNext(ordnetlisteIterator, "hasNext() etter next()", true);

            ordnetlisteIterator = testOpprettIteratorObjekt(ordnetliste);
            testIteratorNext(ordnetlisteIterator, "at next() i en ny iterator begynner fra starten igjen", "elementAA");
            testIteratorNext(ordnetlisteIterator, "at next() fortsetter fra forste element", "elementB");
            testIteratorNext(ordnetlisteIterator, null, null);
            testIteratorNext(ordnetlisteIterator, "at next() kommer til siste element", "elementC");
            testIteratorHasNext(ordnetlisteIterator, "hasNext() ved siste element", false);
        }
        catch(ClassNotFoundException e) {
            System.out.println("Kan ikke teste OrdnetLenkeliste siden klassen ikke finnes");
            antallTester++;
        }
        catch(InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        catch(OpprettIteratorUnntak e) {
            System.out.println(e.getMessage());
        }
        System.out.printf(">> %d av %d tester OK\n", antallPasserte-startPasserte, antallTester-startTester);
    }

    public static void sjekkLikhet(String beskrivelse, String aktuellVerdi, String forventetVerdi) {
        if (forventetVerdi == null) {
            if (aktuellVerdi == null) passTest(beskrivelse);
            else failTest(beskrivelse, aktuellVerdi, forventetVerdi);
        }
        else if (forventetVerdi.compareTo(aktuellVerdi) == 0) {
            passTest(beskrivelse);
        }
        else {
            failTest(beskrivelse, aktuellVerdi, forventetVerdi);
        }
    }

    public static void sjekkLikhet(String beskrivelse, int aktuellVerdi, int forventetVerdi) {
        sjekkLikhet(beskrivelse, ""+aktuellVerdi, ""+forventetVerdi);
    }

    public static void sjekkLikhet(String beskrivelse, boolean aktuellVerdi, boolean forventetVerdi) {
        sjekkLikhet(beskrivelse, ""+aktuellVerdi, ""+forventetVerdi);
    }

    private static void passTest(String beskrivelse) {
        antallPasserte++;
        antallTester++;
        if (verbose) System.out.printf("== OK: Tester %s\n", beskrivelse);
    }

    private static void failTest(String beskrivelse, String aktuellVerdi, String forventetVerdi) {
        antallTester++;
        System.err.printf(ansiRed + "== FEIL: Tester %s\n", beskrivelse);
        System.err.printf("Aktuell verdi: %s -- Forventet verdi: %s\n" + ansiClr + "\n", aktuellVerdi, forventetVerdi);
    }

    private static void uventetUnntak(Exception e, String beskrivelse) {
        antallTester++;
        System.err.printf(ansiRed + "== FEIL:%s uventet unntak (ingen unntak forventet)\n", beskrivelse == null ? "" : " " + beskrivelse + ";");
        e.printStackTrace();
        System.err.print("\n"+ansiClr);
    }

    private static void testErTom(Object beholder, String beskrivelse, boolean forventetVerdi) {
        try {
            if (beholder instanceof Tabell) {
                sjekkLikhet(beskrivelse, ((Tabell<String>)beholder).erTom(), forventetVerdi);
            }
            else if (beholder instanceof Liste) {
                sjekkLikhet(beskrivelse, ((Liste<String>)beholder).erTom(), forventetVerdi);
            }
        } catch(Exception e) { uventetUnntak(e, beskrivelse); }
    }

    private static void testStorrelse(Object beholder, String beskrivelse, int forventetVerdi) {
        try {
            if (beholder instanceof Tabell) {
                sjekkLikhet(beskrivelse, ((Tabell<String>)beholder).storrelse(), forventetVerdi);
            }
            else if (beholder instanceof Liste) {
                sjekkLikhet(beskrivelse, ((Liste<String>)beholder).storrelse(), forventetVerdi);
            }
        } catch(Exception e) { uventetUnntak(e, beskrivelse); }
    }

    private static void testSettInn(Object beholder, String verdi, String beskrivelse) {
        try {
            if (beholder instanceof Tabell) {
                ((Tabell<String>)beholder).settInn(verdi);
            }
            else if (beholder instanceof Liste) {
                ((Liste<String>)beholder).settInn(verdi);
            }
        } catch(Exception e) {
            antallTester++;
            if (beskrivelse == null) { uventetUnntak(e, "settInn(\""+verdi+"\")"); }
            else { uventetUnntak(e, beskrivelse); }
        }
    }

    private static void testSettInn(Object beholder, String verdi) {
        testSettInn(beholder, verdi, null);
    }

    private static void testFjern(Liste<String> beholder, String beskrivelse, String forventetVerdi) {
        try {
            sjekkLikhet(beskrivelse, beholder.fjern(), forventetVerdi);
        } catch(Exception e) { uventetUnntak(e, beskrivelse); }
    }

    private static void testIteratorHasNext(Iterator iteratorObjekt, String beskrivelse, boolean forventetVerdi) {
        try {
            sjekkLikhet(beskrivelse, iteratorObjekt.hasNext(), forventetVerdi);
        } catch(Exception e) { uventetUnntak(e, beskrivelse); }
    }

    private static void testIteratorNext(Iterator<String> iteratorObjekt, String beskrivelse, String forventetVerdi) {
        try {
            if (beskrivelse == null) iteratorObjekt.next();
            else sjekkLikhet(beskrivelse, iteratorObjekt.next(), forventetVerdi);
        } catch(Exception e) { uventetUnntak(e, beskrivelse); }
    }

    private static Iterator<String> testOpprettIteratorObjekt(Iterable<String> beholder) {
        try {
            Iterator<String> it = beholder.iterator();
            if (it == null) throw new OpprettIteratorUnntak("Kunne ikke fullfore Iterator-tester siden iterator-objekt er null");

            return it;
        }
        catch(Exception e) {
            uventetUnntak(e, "opprettelse av iterator-objekt");
            throw new OpprettIteratorUnntak("Kunne ikke fullfore Iterator-tester siden iterator-objekt ikke kunne opprettes");
        }
    }

    private static class OpprettIteratorUnntak extends RuntimeException {
        public OpprettIteratorUnntak(String message) {
            super(message);
        }
    }
}
