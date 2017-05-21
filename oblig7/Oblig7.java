import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.geometry.Pos;

import java.io.File;
import java.io.FileNotFoundException;

public class Oblig7 extends Application{

    private Stage hovedVindu;
    private Scene velgFilnavn = null;

    private int startRad;
    private int startKol;

    private String filnavn;

    @Override
    public void start(Stage vindu) throws Exception {
        this.hovedVindu = vindu;
        hovedVindu.setTitle("bjorhhar - oblig7");

        velgFilnavn();

        settNyScene(velgFilnavn);

        velgStartpunkt("labyrint4.in");
    }

    private void velgFilnavn(){
        VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);

        velgFilnavn = new Scene(vbox, 300, 200);

        //kobler scenen til CSS-stylesheet, for aa kunne endre paa scenen.
        velgFilnavn.getStylesheets().add("CSS/stylesheet.css");
        vbox.setId("velgFilnavn-vbox");

        Text informasjon = new Text();
            informasjon.setText("Skriv inn filnavnet til\nlabyrinten du vil lose");
        vbox.getChildren().add(informasjon);

        TextField tekstFelt = new TextField();
            tekstFelt.setOnAction(action -> {  velgStartpunkt(tekstFelt.getText());  });
        vbox.getChildren().add(tekstFelt);

        Button velgStartKnapp = new Button("Velg Startpunkt");
            velgStartKnapp.setOnAction(action -> {  velgStartpunkt(tekstFelt.getText());  });
        vbox.getChildren().add(velgStartKnapp);
    }

    private void velgStartpunkt(String filnavn){
        try{
            this.filnavn = filnavn;
            File fil = new File(filnavn);
            Labyrint l = Labyrint.lesFraFil(fil);

            VBox vBox = new VBox();
            Scene velgStartpunkt = new Scene(vBox);

                //kobler scenen til CSS-stylesheet, for aa kunne endre paa scenen.
                velgStartpunkt.getStylesheets().add("CSS/stylesheet.css");
                vBox.setId("velgStartPunkt-hovedBox");

                    Text overskrift = new Text("Velg rute");
                    overskrift.setId("velgStartPunkt-overskrift");

                    GridPane labyrinten = skrivUlostLabyrint(l);

                vBox.getChildren().addAll(overskrift, labyrinten);

            hovedVindu.sizeToScene();
            settNyScene(velgStartpunkt);

        }catch(FileNotFoundException fnfe){
            velgFilnavn();
        }
    }

    private void losLabyrint(Labyrint l, int inKol, int inRad){
        VBox vBox = new VBox();
        Scene lostLabyrint = new Scene(vBox);

        //kobler scenen til CSS-stylesheet, for aa kunne endre paa scenen.
        lostLabyrint.getStylesheets().add("CSS/stylesheet.css");
        vBox.setId("losLabyrint-hovedBox");

            Text overskrift = new Text();
                String overskriftTekst = String.format("Raskeste losning fra (%2d, %2d)", inKol, inRad);
                overskrift.setText(overskriftTekst);
            overskrift.setId("losLabyrint-overskrift");

            //Loser labyrinten og henter utveiene
            OrdnetLenkeliste<Utvei> utveier = (OrdnetLenkeliste<Utvei>) l.finnUtveiFra(inKol, inRad);

            if (utveier.storrelse() > 0){
                GridPane[] losninger = new GridPane[utveier.storrelse()];

                int run = 0;
                for (Utvei u : utveier){
                    GridPane labyrint = new GridPane();
                    labyrint.setId("losLabyrint-labyrinten");

                    //Labyrinten i boolsk-format
                    boolean[][] boolLabyrint = l.hentBoolLabyrint();
                    boolean[][] lostBoolLabyrint = losningStringTilTabell(u.hentVei(), l.hentKolonner() + 1, l.hentRader() + 1);

                    //Setter opp GridPane med ruter der sort rute er sort, hvit rute er hvit, og paaVeien er gul
                    for (int i = 0; i < boolLabyrint.length; i++){
                        for (int j = 0; j < boolLabyrint[i].length; j++){
                            if (boolLabyrint[i][j]) {
                                int rad = i + 1;
                                int kol = j + 1;

                                Button rute = new Button(" ");
                                    rute.setMaxWidth(Double.MAX_VALUE);
                                    rute.setOnAction(action -> {  losLabyrint(l, kol, rad);  });
                                    labyrint.add(rute, j, i);

                                if (lostBoolLabyrint[i][j]){
                                    rute.setId("losLabyrint-hvitRute-paaveien");
                                }

                            }else{
                                Rectangle rute = new Rectangle(0, 0, 28, 28);
                                    labyrint.add(rute, j, i);
                            }
                        }
                    }
                    losninger[run] = labyrint;
                    run++;
                }
                vBox.getChildren().addAll(overskrift, losninger[0]);
            }else{
                overskriftTekst = String.format("Ingen utveier fra (%2d, %2d)", inKol, inRad);
                overskrift.setText(overskriftTekst);

                GridPane tomLabyrint = skrivUlostLabyrint(l);

                vBox.getChildren().addAll(overskrift, tomLabyrint);
            }

        hovedVindu.sizeToScene();
        settNyScene(lostLabyrint);
    }

    private GridPane skrivUlostLabyrint(Labyrint l){
        GridPane labyrinten = new GridPane();
        labyrinten.setId("velgStartPunkt-labyrinten");
        //Labyrinten i boolsk-format
        boolean[][] boolLabyrint = l.hentBoolLabyrint();
        //Setter opp GridPane med knapper der det er hvit rute
        for (int i = 0; i < boolLabyrint.length; i++){
            for (int j = 0; j < boolLabyrint[i].length; j++){
                if (boolLabyrint[i][j]) {
                    int rad = i + 1;
                    int kol = j + 1;
                    Button rute = new Button(" ");
                        rute.setMaxWidth(Double.MAX_VALUE);
                        rute.setOnAction(action -> {  losLabyrint(l, kol, rad);  });
                        labyrinten.add(rute, j, i);
                }else{
                    Rectangle rute = new Rectangle(0, 0, 28, 28);
                        labyrinten.add(rute, j, i);
                }
            }
        }
        return labyrinten;
    }

    /**
     * Konverterer losning-String fra oblig 5 til en boolean[][]-representasjon
     * av losningstien.
     * @param losningString String-representasjon av utveien
     * @param bredde        bredde til labyrinten
     * @param hoyde         hoyde til labyrinten
     * @return              2D-representasjon av rutene der true indikerer at
     *                      ruten er en del av utveien.
     */
    private boolean[][] losningStringTilTabell(String losningString, int bredde, int hoyde) {
        boolean[][] losning = new boolean[hoyde][bredde];
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\(([0-9]+),([0-9]+)\\)");
        java.util.regex.Matcher m = p.matcher(losningString.replaceAll("\\s",""));
        while(m.find()) {
            int x = Integer.parseInt(m.group(1))-1;
            int y = Integer.parseInt(m.group(2))-1;
            losning[y][x] = true;
        }
        return losning;
    }

    private void settNyScene(Scene nyScene){
        hovedVindu.setScene(nyScene);
        hovedVindu.centerOnScreen();
        hovedVindu.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
