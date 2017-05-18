import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.geometry.Pos;

public class Oblig7 extends Application{

    private Stage hovedVindu;
    private Scene velgFilnavn = null;
    private String filNavn = null;

    @Override
    public void start(Stage vindu) throws Exception {
        this.hovedVindu = vindu;

        vindu.setTitle("bjorhhar - oblig7");

        byggSceneVelgFilnavn();
            vindu.setScene(velgFilnavn);
            vindu.show();

        byggSceneLosLabyrint();

    }

    private void byggSceneVelgFilnavn(){
        VBox vbox = new VBox();
            vbox.setSpacing(20);
            vbox.setAlignment(Pos.CENTER);

        velgFilnavn = new Scene(vbox, 300, 200);

        //kobler scenen til CSS-stylesheet, for aa kunne endre paa scenen.
        velgFilnavn.getStylesheets().add("CSS/hentFilnavn.css");
        vbox.getStyleClass().add("vbox"); //Lager en

        Text informasjon = new Text();
            informasjon.setTextAlignment(TextAlignment.LEFT);
            informasjon.setText("Skriv inn filnavnet til\nlabyrinten du vil lose");
        vbox.getChildren().add(informasjon);

        TextField tekstFelt = new TextField();
            tekstFelt.setAlignment(Pos.BOTTOM_LEFT);
            tekstFelt.setPrefColumnCount(14);
        vbox.getChildren().add(tekstFelt);

        Button bekreftelsesKnapp = new Button("Los Labyrint");
            bekreftelsesKnapp.setAlignment(Pos.BOTTOM_RIGHT);
            bekreftelsesKnapp.setOnAction(action -> {
                System.out.println("Knapp trykket -> bekreftelsesKnapp");
                filNavn = tekstFelt.getText();
            });
        vbox.getChildren().add(bekreftelsesKnapp);
    }

    private void byggSceneLosLabyrint(){

    }

    public static void main(String[] args){
        launch(args);
    }
}
