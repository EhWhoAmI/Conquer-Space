package ConquerSpace.game.universe;

/**
 *
 * @author Zyun
 */
public class Vector {
    public int degrees;
    /**
     * meters per second
     */
    public int speed;

    public Vector(int degrees, int speed) {
        this.degrees = degrees;
        this.speed = speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setDegrees(int degrees) {
        this.degrees = degrees;
    }

    public int getDegrees() {
        return degrees;
    }

    public int getSpeed() {
        return speed;
    }
    
    
}
