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

    @Override
    public void start(Stage vindu) throws Exception {
        this.hovedVindu = vindu;
        hovedVindu.setTitle("bjorhhar - oblig7");

        velgFilnavn();

        settNyScene(velgFilnavn);


        velgStartpunkt("labyrint1.in"); //TODO fjern denne!!
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

    /**
    *
    *   @param filnavn Filnavnet som skrives inn dersom
    */
    private void velgStartpunkt(String filnavn){
        try{
            File fil = new File(filnavn);
            Labyrint l = Labyrint.lesFraFil(fil);

                //Labyrinten i boolsk-streng-format
                GridPane labyrinten = new GridPane();
                Scene velgStartpunkt = new Scene(labyrinten, 380, 340);

                //kobler scenen til CSS-stylesheet, for aa kunne endre paa scenen.
                velgStartpunkt.getStylesheets().add("CSS/stylesheet.css");
                labyrinten.setId("velgStartPunkt-labyrinten");

                boolean[][] boolLabyrint = l.hentBoolLabyrint();

                for (int i = 0; i < boolLabyrint.length; i++){
                    for (int j = 0; j < boolLabyrint[i].length; j++){
                        if (boolLabyrint[i][j]) {
                            Button rute = new Button("  ");
                                rute.setId("velgStartPunkt-labyrinten-hvitRute");
                                int rad = i + 1;
                                int kol = j + 1;
                                rute.setOnAction(action -> {  losLabyrint(fil, kol, rad);  });
                            labyrinten.add(rute, j, i);
                        }
                    }
                }

            settNyScene(velgStartpunkt);

        }catch(FileNotFoundException fnfe){
            velgFilnavn();
        }
    }

    private void losLabyrint(File fil, int kol, int rad){
        Button test = new Button(kol + ", " + rad);
            test.setOnAction(action -> {  velgStartpunkt("labyrint1.in");  } ); //TODO fjern denne knappen
        Scene testScene = new Scene(test, 300, 200);
        settNyScene(testScene);


/*        BorderPane bp = new BorderPane();
        losLabyrint = new Scene(bp, 1000, 600);

        HBox meny = new HBox();
        meny.setAlignment(Pos.CENTER);

        Text koordinatInfo = new Text();
            koordinatInfo.setTextAlignment(TextAlignment.LEFT);
            koordinatInfo.setText("Skriv koordinatet du vil begynne i");
        meny.getChildren().add(koordinatInfo);

        TextField tekstFelt = new TextField();
        meny.getChildren().add(tekstFelt);

        TextField tekstFelt = new TextField();
        meny.getChildren().add(tekstFelt);

        Button bekreftelsesKnapp = new Button("Los Labyrint");
            bekreftelsesKnapp.setOnAction(action -> {  byggSceneLosLabyrint(tekstFelt.getText());  });
        meny.getChildren().add(bekreftelsesKnapp);

        ToggleButton skrivUtForste = new ToggleButton("Skriv kun forste");



        bp.setLeft(meny);
        settNyScene(losLabyrint);
*/
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
