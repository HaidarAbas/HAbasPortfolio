package localhost.haidarabas.portfolio.asteroids;

import java.util.HashMap;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


public class App extends Application {
    protected static final int WIDTH = 1100;
    protected static final int HEIGHT = 800;
    private HashMap<String, String> keyValue;
    private boolean turnLeft = false;
    private boolean turnRight = false;
    private boolean accelerate = false;
    private List<Asteroid> asteroids;
    
    
    @Override
    public void start(Stage stage) {
        Pane pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);
        Scene scene = new Scene(pane);
        
        Character ship = new Ship(WIDTH / 2, HEIGHT / 2);
        
        generateAsteroids();
        
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
                    ship.accelerate(0.3);
                }
                
                ship.move();
                ship.decelerate(1.3);
                asteroids.forEach(ast -> ast.move());
                
                for (Asteroid ast: asteroids) {
                    if (ship.collide(ast)) {
                        stop();
                    }
                }
            }
        }.start();
        
        pane.getChildren().add(ship.getCharacter());
        asteroids.forEach(ast -> pane.getChildren().add(ast.getCharacter()));
        stage.setTitle("Asteroids");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private void detectKeyPress(Scene scene) {
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
    
    private void generateAsteroids() {
        this.asteroids = new ArrayList<>();
        
        Random rnd = new Random();
        
        //Ensure asteroids spawn in random spot in a couple of portions of pane. So as to not spawn on ship from inception
        for (int c = 0; c < 5; c++) {
            Asteroid asteroid1 = new Asteroid(rnd.nextInt(WIDTH / 4), rnd.nextInt(HEIGHT));
            Asteroid asteroid2 = new Asteroid(rnd.nextInt(WIDTH / 4) * 4, rnd.nextInt(HEIGHT));
            asteroids.add(asteroid1);
            asteroids.add(asteroid2);
        }
    }

}