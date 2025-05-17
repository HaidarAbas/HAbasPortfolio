package localhost.haidarabas.portfolio.asteroids;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class Ship extends Character {
    
    public Ship(double x, double y) {
        super(new Polygon(-5, -5, 10, 0, -5, 5), x, y);
        this.getCharacter().setFill(Color.BLUE);
    }
}
