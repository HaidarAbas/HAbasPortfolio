package localhost.haidarabas.portfolio.asteroids;

import java.util.Random;
import javafx.scene.shape.Polygon;


public class Asteroid extends Character {
    private double rotationalMovement;
    
    public Asteroid(double x, double y) {
        super(new PolygonGenerator().generatePentagon(), x, y);
        
        Random rnd = new Random();
        
        super.getCharacter().setRotate(rnd.nextInt(360));
        
        int accelerationAmmount = 1 + rnd.nextInt(5);
        for (int i = 0; i < accelerationAmmount; i++) {
            this.accelerate(rnd.nextDouble());
        }
        
        this.rotationalMovement = 0.5 - rnd.nextDouble();
    }
    
    @Override
    public void move() {
        super.move();
        super.getCharacter().setRotate(super.getCharacter().getRotate() + this.rotationalMovement); //override the move method to change the asteroid rotaion everytime its called
    }
}
