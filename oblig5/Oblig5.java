import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Oblig5 {
    public static void main(String[] args) {
        String filnavn = null;

        if (args.length > 0) {
            filnavn = args[0];
        } else {
            System.out.println("FEIL! Riktig bruk: "
                               +"java Oblig5 <labyrintfil>");
            return;
        }
        File fil = new File(filnavn);
        Labyrint l = null;
        try {
            l = Labyrint.lesFraFil(fil);
        } catch (FileNotFoundException e) {
            System.out.printf("FEIL: Kunne ikke lese fra '%s'\n", filnavn);
            System.exit(1);
        }
        l.settMinimalUtskrift();


        // les start-koordinater fra standard input
        Scanner inn = new Scanner(System.in);
        boolean kunForste = false;
        boolean minUt = true;


        System.out.println(String.format("minimalUtskrift('u') = %b | SkrivKunForste('f') = %b | 'p' for aa printe labyrint", minUt, kunForste));
        while (inn.hasNextLine()) {
            String[] ord = inn.nextLine().split(" ");

            switch (ord[0]){
                case "u":
                    minUt = l.settMinimalUtskrift();
                    break;

                case "p":
                    System.out.println(l);
                    break;

                case "f":
                    kunForste = !kunForste;
                    break;

                default:
                    int startKol = Integer.parseInt(ord[0]);
                    int startRad = Integer.parseInt(ord[1]);
                    Liste<Utvei> utveier = l.finnUtveiFra(startKol, startRad);
                    if (!utveier.erTom()) {
                        if (kunForste){
                            System.out.println(utveier.fjern());
                        } else {
                            int str = 0;
                            for (Utvei u : utveier) {
                                System.out.println(u);
                                str++;
                            }
                            System.out.println("\nAntall utveier: " + str);
                        }
                    } else {
                        System.out.println("Ingen utveier.");
                    }
                    System.out.println();
                    break;
            }
            System.out.println(String.format("minimalUtskrift('u') = %b | SkrivKunForste('f') = %b", minUt, kunForste));
        }
    }
}
