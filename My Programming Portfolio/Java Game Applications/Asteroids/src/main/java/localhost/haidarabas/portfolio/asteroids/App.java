package localhost.haidarabas.portfolio.asteroids;

import java.util.Map;
import java.util.HashMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
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
    private HashMap<String, String> keyValue;
    private boolean turnLeft = false;
    private boolean turnRight = false;
    private boolean accelerate = false;
    
    
    @Override
    public void start(Stage stage) {
        Pane pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);
        Scene scene = new Scene(pane);
        
        Character ship = new Ship(WIDTH / 2, HEIGHT / 2);
        detectKeyPress(scene);
        new AnimationTimer() {

            @Override
            public void handle(long l) {
                if (turnLeft == true) {
                    ship.turnLeft();
                }
                if (turnRight == true) {
                    ship.turnRight();
                }
                if (accelerate == true) {
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
    
    public void detectKeyPress(Scene scene) {
        File keyMapFile = new File("KeyMapConfig.json");
        
        if (keyMapFile.exists()) {
            try {
                FileReader reader = new FileReader(keyMapFile);
                Type type = new TypeToken<HashMap<String, String>>(){}.getType();
                
                Gson gson = new Gson();
                keyValue = gson.fromJson(reader, type);
                
                scene.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.valueOf(keyValue.get("turn left"))) {
                        turnLeft = true;
                    }
                    if (event.getCode() == KeyCode.valueOf(keyValue.get("turn right"))) {
                        turnRight = true;
                    }
                    if (event.getCode() == KeyCode.valueOf(keyValue.get("accelerate"))) {
                        accelerate = true;
                    }
                });
                
                scene.setOnKeyReleased(event -> {
                    if (event.getCode() == KeyCode.valueOf(keyValue.get("turn left"))) {
                        turnLeft = false;
                    }
                    if (event.getCode() == KeyCode.valueOf(keyValue.get("turn right"))) {
                        turnRight = false;
                    }
                    if (event.getCode() == KeyCode.valueOf(keyValue.get("accelerate"))) {
                        accelerate = false;
                    }
                });
                
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
    }

}