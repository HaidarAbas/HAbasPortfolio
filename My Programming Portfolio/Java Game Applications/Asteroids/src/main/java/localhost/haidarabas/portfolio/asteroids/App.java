package localhost.haidarabas.portfolio.asteroids;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {
    protected static final int WIDTH = 1100;
    protected static final int HEIGHT = 800;
    
    
    @Override
    public void start(Stage stage) {
        Pane pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);
        
        Character ship = new Ship(100, 100);
        
        pane.getChildren().add(ship.getCharacter());
        
        Scene scene = new Scene(pane);
        stage.setTitle("Asteroids");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}