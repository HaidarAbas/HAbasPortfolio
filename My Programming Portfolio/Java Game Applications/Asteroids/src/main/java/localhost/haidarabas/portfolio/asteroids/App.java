package localhost.haidarabas.portfolio.asteroids;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class App extends Application {
    protected static final int WIDTH = 1100;
    protected static final int HEIGHT = 800;
    private boolean keyPressLeft = false;
    private boolean keyPressRight = false;
    private boolean keyPressUp = false;
    
    
    @Override
    public void start(Stage stage) {
        Pane pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);
        Scene scene = new Scene(pane);
        
        Character ship = new Ship(100, 100);
        
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                keyPressLeft = true;
            }
            if (event.getCode() == KeyCode.RIGHT) {
                keyPressRight = true;
            }
            if (event.getCode() == KeyCode.UP) {
                keyPressUp = true;
            }
        });
        
        scene.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                keyPressLeft = false;
            }
            if (event.getCode() == KeyCode.RIGHT) {
                keyPressRight = false;
            }
            if (event.getCode() == KeyCode.UP) {
                keyPressUp = false;
            }
        });
        
        new AnimationTimer() {

            @Override
            public void handle(long l) {
                if (keyPressLeft == true) {
                    ship.turnLeft();
                }
                if (keyPressRight == true) {
                    ship.turnRight();
                }
                if (keyPressUp == true) {
                    ship.accelerate(0.3, 0.3);
                }
                
                ship.move();
                ship.decelerate(1.3);
            }
        }.start();
        
        pane.getChildren().add(ship.getCharacter());
        
        stage.setTitle("Asteroids");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}