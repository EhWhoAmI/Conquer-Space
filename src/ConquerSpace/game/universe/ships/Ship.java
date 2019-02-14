package ConquerSpace.game.universe.ships;

import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.Vector;

/**
 *
 * @author Zyun
 */
public class Ship implements SpaceShip{
    ShipClass sclass;
    private String name = "";
    private long X;
    private long Y;
    private Vector v;
    private UniversePath location;

    public Ship(ShipClass sclass, long X, long Y, Vector v, UniversePath location) {
        this.sclass = sclass;
        this.X = X;
        this.Y = Y;
        this.v = v;
        this.location = location;
    }

    
    @Override
    public UniversePath getLocation() {
        return location;
    }

    @Override
    public long getX() {
        return X;
    }

    @Override
    public long getY() {
        return Y;
    }

    @Override
    public Vector getVector() {
        return v;
    }

    public void setVector(Vector v) {
        this.v = v;
    }

    public void setX(long X) {
        this.X = X;
    }

    public void setY(long Y) {
        this.Y = Y;
    }

    public void setLocation(UniversePath location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    
}
