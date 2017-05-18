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
    private Scene losLabyrint = null;

    @Override
    public void start(Stage vindu) throws Exception {
        this.hovedVindu = vindu;
        hovedVindu.setTitle("bjorhhar - oblig7");

        byggSceneVelgFilnavn();

        settNyScene(velgFilnavn);
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
            tekstFelt.setOnAction(action -> {  byggSceneVelgStartpunkt(tekstFelt.getText());  });
        vbox.getChildren().add(tekstFelt);

        Button bekreftelsesKnapp = new Button("Los Labyrint");
            bekreftelsesKnapp.setAlignment(Pos.BOTTOM_RIGHT);
            bekreftelsesKnapp.setOnAction(action -> {  byggSceneVelgStartpunkt(tekstFelt.getText());  });
        vbox.getChildren().add(bekreftelsesKnapp);
    }

    private void byggSceneVelgStartpunkt(String filnavn){
        try{
            File fil = new File(filnavn);
            Labyrint l = Labyrint.lesFraFil(fil);

            ToggleGroup tg = new ToggleGroup();
            Scene velgStartpunkt = new Scene(tg, 600, 400);

            /*
            TODO:
                - Opprett en RadioButton for hver plass i labyrinten jeg kan gaa
                - Den RadioButton som er valgt, er der bruker vil begynne
            */
            for (int i = 1; i <= l.hentRader; i++){
                for (int j = 1; j <= l.hentKolonner; j++){
                    RadioButton rb = new RadioButton();
                    rb..setToggleGroup(tg);
                }
            }

            //Lag en knapp for aa byggeLosningen

            settNyScene(velgStartpunkt);
        }catch(FileNotFoundException fnfe){

        }
    }

/*    private void byggSceneLosLabyrint(String filnavn, int kol, int rad){
        BorderPane bp = new BorderPane();
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
    }
*/

    private void settNyScene(Scene nyScene){
        hovedVindu.setScene(nyScene);
        hovedVindu.centerOnScreen();
        hovedVindu.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
