package localhost.haidarabas.portfolio.asteroids;

import javafx.scene.shape.Polygon;

public class Projectile extends Character {
    
    public Projectile(double x, double y) {
        super(new Polygon(2, -2, 2, 2, -2, 2, -2, -2), x, y);
    }
}
