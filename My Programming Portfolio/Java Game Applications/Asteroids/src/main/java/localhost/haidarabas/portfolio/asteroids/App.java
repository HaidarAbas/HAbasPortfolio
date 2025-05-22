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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class App extends Application {
    private static volatile boolean shutdown = false;
    protected static final int WIDTH = 1100;
    protected static final int HEIGHT = 800;
    private boolean turnLeft = false;
    private boolean turnRight = false;
    private boolean accelerate = false;
    private boolean shoot = false;
    private HashMap<String, String> keyValue;
    private List<Asteroid> asteroids;
    private List<Projectile> projectiles;
    private List<Projectile> projectileQueue = new ArrayList<>();
    
    @Override
    public void start(Stage stage) {
        Pane pane = new Pane();
        pane.setPrefSize(WIDTH, HEIGHT);
        Scene scene = new Scene(pane);
        Text scoreBoard = new Text(10, 20, "Points: 0");
        readKeyMap();
        
        Character ship = new Ship(WIDTH / 2, HEIGHT / 2);
        this.projectiles = new ArrayList<>();
        generateAsteroids(10);
        clearQueue();
        detectKeyPress(scene);
        
        AtomicInteger points = new AtomicInteger();
        
        new AnimationTimer() {

            @Override
            public void handle(long now) {
                
                if (turnLeft == true) {
                    ship.turnLeft();
                }
                if (turnRight == true) {
                    ship.turnRight();
                }
                if (accelerate == true) {
                    ship.accelerate(0.3);
                }
                if (shoot == true && projectiles.size() < 10 && projectileQueue.isEmpty()) {
                    generateProjectiles(ship.getCharacter(), pane);
                }
                
                ship.move();
                ship.decelerate(1.3);
                asteroids.forEach(ast -> ast.move());
                projectiles.forEach(pro -> pro.move());
                removeProjectiles(pane, scoreBoard, points);
                
                asteroids.stream().forEach(ast -> {
                    if (ship.collide(ast)) {
                        stop();
                    }
                });
                
                if (Math.random() < 0.005) {
                    Asteroid asteroid = new Asteroid(WIDTH, HEIGHT);
                    if (!asteroid.collide(ship)) {
                        asteroids.add(asteroid);
                        pane.getChildren().add(asteroid.getCharacter());
                    }
                }
            }
        }.start();
        
        pane.getChildren().addAll(scoreBoard, ship.getCharacter());
        asteroids.forEach(ast -> pane.getChildren().add(ast.getCharacter()));
        stage.setTitle("Asteroids!");
        stage.setScene(scene);
        stage.show();
        
    }

    public static void main(String[] args) {
        launch(args);
        shutdown();
    }
    
    private void readKeyMap() throws Error {
        File keyMapFile = new File("KeyMapConfig.json");
        
        if (keyMapFile.exists()) {
            try {
                FileReader reader = new FileReader(keyMapFile);
                Type type = new TypeToken<HashMap<String, String>>(){}.getType();
                
                Gson gson = new Gson();
                keyValue = gson.fromJson(reader, type); //populate the hashmap with string:sting pair from the json file
                
            } catch (Exception e) {
                System.err.println("Error: " + e);
            }
        } else {
            throw new Error("Missing file KeyMapConfig.json");
        }
    }
    
    private void detectKeyPress(Scene scene) {
        
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
            if (event.getCode() == KeyCode.valueOf(keyValue.get("shoot"))) {
                shoot = true;
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
            if (event.getCode() == KeyCode.valueOf(keyValue.get("shoot"))) {
                shoot = false;
            }
        });
                
    }
    
    private void generateAsteroids(int maximum) {
        this.asteroids = new ArrayList<>();
        
        Random rnd = new Random();
        
        //Ensure asteroids spawn in random spot in a couple of portions of pane. So as to not spawn on ship from inception
        for (int c = 0; c < maximum/2; c++) {
            Asteroid asteroid1 = new Asteroid(rnd.nextInt(WIDTH / 4), rnd.nextInt(HEIGHT));
            Asteroid asteroid2 = new Asteroid(rnd.nextInt(WIDTH / 4) * 4, rnd.nextInt(HEIGHT));
            asteroids.add(asteroid1);
            asteroids.add(asteroid2);
        }
    }
    
    private void generateProjectiles(Node node, Pane pane) {
        Projectile pewpew = new Projectile(node.getTranslateX(), node.getTranslateY());
        pewpew.getCharacter().setRotate(node.getRotate());
        
        pewpew.accelerate(1);
        pewpew.setMovement(pewpew.getMovement().normalize().multiply(3));
        
        this.projectiles.add(pewpew);
        this.projectileQueue.add(pewpew);
        
        pane.getChildren().add(pewpew.getCharacter());
    }
    
    private void removeProjectiles(Pane pane, Text text, AtomicInteger aInt) {
        List<Projectile> projectileToRemove = projectiles.stream().filter(projectile -> {
            List<Asteroid> collisions = asteroids.stream().filter(asteroid -> asteroid.collide(projectile)).collect(Collectors.toList());
            
            if (collisions.isEmpty()) {
                return false;
            }
            
            collisions.stream().forEach(collided -> {
                asteroids.remove(collided);
                pane.getChildren().remove(collided.getCharacter());
            });
            
            text.setText("Points: " + aInt.addAndGet(100));
            
            return true;
        }).collect(Collectors.toList());
        
        projectileToRemove.forEach(projectile -> {
            projectiles.remove(projectile);
            pane.getChildren().remove(projectile.getCharacter());
        });
    }
    
    private void clearQueue() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    
                    @Override
                    public void run() {
                        projectileQueue.clear();
                    }
                    
                });
                
                if (shutdown == true) {
                    Platform.exit();
                    System.exit(0);
                }
                
            }
        }, 0, 500);
    }
    
    public static void shutdown() {
        shutdown = true;
    }

}