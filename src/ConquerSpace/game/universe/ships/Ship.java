package ConquerSpace.game.universe.ships;

import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.Vector;
import ConquerSpace.game.universe.ships.components.ShipComponent;
import ConquerSpace.game.universe.ships.hull.Hull;
import java.util.ArrayList;

/**
 *
 * @author Zyun
 */
public class Ship implements SpaceShip, Orbitable {
    private static int ticker = 0;
    String sclass;
    private String name = "";
    private long X;
    private long Y;
    private Vector v;
    private UniversePath location;
    private boolean isOrbiting = false;
    private int mass;
    private long goingToX;
    private long goingToY;

    private int id;
    private Hull hull;
    private ArrayList<ShipComponent> components;

    public Ship(ShipClass sclass, long X, long Y, Vector v, UniversePath location) {
        this.X = X;
        this.Y = Y;
        goingToX = X;
        goingToY = Y;
        this.v = v;
        this.location = location;
        //Set ship's id
        id = ticker++;

        components = new ArrayList<>();
        //Get components
        if (!sclass.components.isEmpty()) {
            for (ShipComponent s : sclass.components) {
                components.add((ShipComponent) s.clone());
            }
        }
        this.hull = (Hull) sclass.getHull().clone();
        this.mass = sclass.getMass();
        this.sclass = sclass.getName();
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

    @Override
    public String toString() {
        if(getName().isEmpty()) {
            return "ship";
        }
        return (getName());
    }

    @Override
    public UniversePath getOrbiting() {
        return location;
    }

    public void setIsOrbiting(boolean isOrbiting) {
        this.isOrbiting = isOrbiting;
    }

    public boolean isOrbiting() {
        return isOrbiting;
    }

    public long getGoingToX() {
        return goingToX;
    }

    public long getGoingToY() {
        return goingToY;
    }

    public void setGoingToX(long goingToX) {
        this.goingToX = goingToX;
    }

    public void setGoingToY(long goingToY) {
        this.goingToY = goingToY;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Ship && ((Ship) obj).id == this.id);
    }
}
