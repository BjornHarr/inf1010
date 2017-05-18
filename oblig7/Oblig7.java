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
    }

    private void velgFilnavn(){
        VBox vbox = new VBox();
            vbox.setSpacing(20);
            vbox.setAlignment(Pos.CENTER);

        velgFilnavn = new Scene(vbox, 300, 200);

        //kobler scenen til CSS-stylesheet, for aa kunne endre paa scenen.
        velgFilnavn.getStylesheets().add("CSS/stylesheet.css");
        vbox.setId("velgFilnavn-vbox");

        Text informasjon = new Text();
            informasjon.setText("Skriv inn filnavnet til\nlabyrinten du vil lose");
        vbox.getChildren().add(informasjon);

        TextField tekstFelt = new TextField();
//            tekstFelt.setOnAction(action -> {  velgStartpunkt(tekstFelt.getText());  });
            tekstFelt.setOnAction(action -> {  velgStartpunkt("labyrint1.in");  });
        vbox.getChildren().add(tekstFelt);

        Button velgStartKnapp = new Button("Velg Startpunkt");
            velgStartKnapp.setOnAction(action -> {  velgStartpunkt(tekstFelt.getText());  });
        vbox.getChildren().add(velgStartKnapp);

/*TODO        Button finnAlleKnapp = new Button("Finn alle losninger");
            finnAlleKnapp.setOnAction(action -> {  finnAlleLosninger(tekstFelt.getText());  });
        vbox.getChildren().add(finnAlleKnapp);
*/
    }

    /**
    *
    *
    *
     @param filnavn Filnavnet som skrives inn dersom
    */
    private void velgStartpunkt(String filnavn){
        try{
            File fil = new File(filnavn);
            Labyrint l = Labyrint.lesFraFil(fil);

            BorderPane layout = new BorderPane();
            Scene velgStartpunkt = new Scene(layout, 1000, 800);

            //kobler scenen til CSS-stylesheet, for aa kunne endre paa scenen.
            velgFilnavn.getStylesheets().add("CSS/stylesheet.css");
            layout.setId("velgStartPunkt-layout");

            Text labyrinten = new Text(l.toString());

            HBox koordinatVelger = new HBox();
                koordinatVelger.setAlignment(Pos.CENTER);
                koordinatVelger.setId("velgStartPunkt-koordinatVelger");

                TextField radKoordinat = new TextField();
                    radKoordinat.setId("velgStartPunkt-textField");
                    radKoordinat.setText("Rad...");
                koordinatVelger.getChildren().add(radKoordinat);

                TextField kolKoordinat = new TextField();
                    kolKoordinat.setId("velgStartPunkt-textField");
                    kolKoordinat.setText("Kol...");
                koordinatVelger.getChildren().add(kolKoordinat);

                Button bekreftelsesKnapp = new Button("Los Labyrint");
                    bekreftelsesKnapp.setAlignment(Pos.CENTER);
                    bekreftelsesKnapp.setOnAction(action -> {

                        losLabyrint(fil, Integer.parseInt(kolKoordinat.getText()), Integer.parseInt(radKoordinat.getText()));
                    });
                koordinatVelger.getChildren().add(bekreftelsesKnapp);

            layout.setCenter(labyrinten);
            layout.setBottom(koordinatVelger);

            settNyScene(velgStartpunkt);
        }catch(FileNotFoundException fnfe){
            velgFilnavn();
        }
    }

    private void losLabyrint(File fil, int kol, int rad){
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
