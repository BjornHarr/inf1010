import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;

public class Oblig7 extends Application{

    @Override
    public void start(Stage window) throws Exception {
        window.setTitle("bjorhhar - oblig7");

        GridPane tabell = new GridPane();

        for (int i = 1; i < 4; i++){
            for (int j = 1; j < 4; j++){
                tabell.add(new Button("Button" + i*j), j, i, 1, 1);
            }
        }

        tabell.setHgap(10);
        tabell.setVgap(10);

        Scene textScene = new Scene(tabell, 240, 120);
        window.setScene(textScene);
        window.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
