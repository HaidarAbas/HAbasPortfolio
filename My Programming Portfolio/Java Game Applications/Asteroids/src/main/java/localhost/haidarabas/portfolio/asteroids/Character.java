package localhost.haidarabas.portfolio.asteroids;

import java.lang.Math;
import javafx.geometry.Point2D;
import javafx.scene.shape.*;

public abstract class Character {
    private Polygon character;
    private Point2D movement;
    
    public Character(Polygon polygon, double x, double y) {
        this.character = polygon;
        
        this.character.setTranslateX(x);
        this.character.setTranslateY(y);
        
        this.movement = new Point2D(0,0);
    }
    
    public Polygon getCharacter() {
        return this.character;
    }
    
    public Point2D getMovement() {
        return this.movement;
    }
    
    /**
     * Set a fixed movement value. Default character movement set at (0, 0).
     * @param point Value for new fixed movement
     */
    public void setMovement(Point2D point) {
        this.movement = point;
    }
    
    public void turnLeft() {
        this.character.setRotate(this.character.getRotate() - 5);
    }
    
    public void turnRight() {
        this.character.setRotate(this.character.getRotate() + 5);
    }
    
    /**
     * Characters move by translating their coordinates on a 2D plane. Magnitude is based on user given parameters.
     * When this method is called the character will always move in the direction it is facing.
     * @param xAccel Multiplier for translation along the x-axis
     * @param yAccel Multiplier for translation along the y-axis
     */
    public void accelerate(double xAccel, double yAccel) {
        double newMovementX = Math.cos(Math.toRadians(this.character.getRotate()));
        double newMovementY = Math.sin(Math.toRadians(this.character.getRotate()));
        
        newMovementX *= xAccel;
        newMovementY *= yAccel;
        
        this.movement = this.movement.add(newMovementX, newMovementY);
    }
    
    /**
     * When invoked this method will set the character movement to half its current value (for X and Y).
     * This method is intended to be called multiple times until the character comes to a gradual stop.
     */
    public void decelerate() {
        double newMovementX = this.movement.midpoint(movement).getX();
        double newMovementY = this.movement.midpoint(movement).getY();
        
        this.movement = new Point2D(newMovementX, newMovementY);
    }
    
    /**
     * When invoked this method will set the character movement to a portion of its current value (for X and Y).
     * User given parameter will determine rate of deceleration.
     * This method is intended to be called multiple times until the character comes to a gradual stop.
     * <p>
     * Method throws an IllegalArgumentException if the parameter provided is less then 1.
     * 
     * @param decelRate Rate of deceleration
     */
    public void decelerate(double decelRate) {
        if (decelRate < 1.0) {
            throw new IllegalArgumentException("Value for decelRate can not be less then 1");
        }
        
        double newMovementX = this.movement.midpoint(movement.getX() / decelRate, movement.getY() / decelRate).getX();
        double newMovementY = this.movement.midpoint(movement.getX() / decelRate, movement.getY() / decelRate).getY();
        
        this.movement = new Point2D(newMovementX, newMovementY);
    }
    
    /**
     * Move the character by translating the character along the x and y axis based on movement value.
     */
    public void move() {
        this.character.setTranslateX(this.character.getTranslateX() + this.movement.getX());
        this.character.setTranslateY(this.character.getTranslateY() + this.movement.getY());
    }
    
    /**
     * Returns true if the character intersects with another character (or shape), otherwise returns false.
     * @param other Character to be collided with
     * @return 
     */
    public boolean collide(Character other) {
        Shape collisionShape = Shape.intersect(this.character, other.character);
        
        return collisionShape.getBoundsInLocal().getWidth() != -1;
    }
}
