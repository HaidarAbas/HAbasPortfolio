package localhost.haidarabas.portfolio.asteroids;

import javafx.scene.shape.*;

public class Ship extends Character {
    
    public Ship(double x, double y) {
        super(new Polygon(-5, -5, 0, 10, 5, -5), x, y);
    }
}
