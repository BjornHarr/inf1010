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

    /**
    *
    *   @param filnavn Filnavnet som skrives inn dersom
    */
    private void velgStartpunkt(String filnavn){
        try{
            File fil = new File(filnavn);
            Labyrint l = Labyrint.lesFraFil(fil);

                VBox vBox = new VBox();
                Scene velgStartpunkt = new Scene(vBox);

                //kobler scenen til CSS-stylesheet, for aa kunne endre paa scenen.
                velgStartpunkt.getStylesheets().add("CSS/stylesheet.css");
                vBox.setId("velgStartPunkt-vBox");

                    Text overskrift = new Text("Velg rute");
                    overskrift.setId("velgStartPunkt-overskrift");

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
                                    rute.setId("velgStartPunkt-labyrinten-hvitRute");
                                    rute.setOnAction(action -> {  losLabyrint(fil, kol, rad);  });
                                labyrinten.add(rute, j, i);
                            }
                        }
                    }
                vBox.getChildren().addAll(overskrift, labyrinten);

            hovedVindu.sizeToScene();
            settNyScene(velgStartpunkt);

        }catch(FileNotFoundException fnfe){
            velgFilnavn();
        }
    }

    private void losLabyrint(File fil, int kol, int rad){
        Button test = new Button(kol + ", " + rad);
            test.setOnAction(action -> {  velgStartpunkt("labyrint4.in");  } ); //TODO fjern denne knappen
        Scene testScene = new Scene(test, 300, 200);
        settNyScene(testScene);
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
