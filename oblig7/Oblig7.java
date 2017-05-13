import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class Oblig7 extends Application{

    @Override
    public void start(Stage window) throws Exception {
        Scene scene = new Scene(new Pane(), 1080, 560);
        window.setScene(scene);
        window.setTitle("bjorhhar - oblig7");
        window.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
