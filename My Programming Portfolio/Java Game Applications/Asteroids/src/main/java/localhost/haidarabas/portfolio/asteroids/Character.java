package localhost.haidarabas.portfolio.asteroids;

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
    
    public void setMovement(Point2D point) {
        this.movement = point;
    }
    
    public void turnLeft() {
        this.character.setRotate(this.character.getRotate() - 5);
    }
    
    public void turnRight() {
        this.character.setRotate(this.character.getRotate() + 5);
    }
}
