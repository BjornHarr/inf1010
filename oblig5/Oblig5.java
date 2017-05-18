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
        Boolean kunForste = true;
        Scanner inn = new Scanner(System.in);
        System.out.println("\n u : minimalUtskrift | p : skriv ut labyrint | f : skriv ut kun forste");
        while (inn.hasNextLine()) {
            String[] ord = inn.nextLine().split(" ");


            if (ord[0].equals("u")){
                l.settMinimalUtskrift();
                System.out.println("Endret minimal utskrift");

            }else if (ord[0].equals("p")){
                System.out.println(l);

            }else if (ord[0].equals("f")){
                kunForste = !kunForste;
                System.out.printf("kunForste er %b\n", kunForste);

            }else{
                int startKol = Integer.parseInt(ord[0]);
                int startRad = Integer.parseInt(ord[1]);
                Liste<Utvei> utveier = l.finnUtveiFra(startKol, startRad);
                if (!utveier.erTom()) {
                    if (kunForste){
                        System.out.println(utveier.fjern());
                    }else{
                        for (Utvei u : utveier) {
                            System.out.println(u);

                        }
                    }
                } else {
                    System.out.println("Ingen utveier.");
                }
                System.out.println("\n u : minimalUtskrift | p : skriv ut labyrint | f : skriv ut kun forste");

            }
        }
    }
}
