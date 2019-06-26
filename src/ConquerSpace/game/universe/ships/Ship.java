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
public class Ship extends SpaceShip {
    private static int ticker = 0;
    String sclass;

    private Hull hull;
    public ArrayList<ShipComponent> components;
    
    private ShipClass shipClass;

    public Ship(ShipClass sclass, long X, long Y, Vector v, UniversePath location) {
        this.X = X;
        this.Y = Y;
        goingToX = X;
        goingToY = Y;
        this.v = v;
        this.location = location;
        shipClass = sclass;
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
    public Vector getVector() {
        return v;
    }

    public void setVector(Vector v) {
        this.v = v;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        if (getName().isEmpty()) {
            return "ship";
        }
        return (getName());
    }

    @Override
    public UniversePath getOrbiting() {
        return location;
    }

    public Hull getHull() {
        return hull;
    }

    public String getShipClassName() {
        return sclass;
    }

    public int getId() {
        return id;
    }

    public ShipClass getShipClass() {
        return shipClass;
    }

    @Override
    public long getSpeed() {
        return estimatedThrust / mass;
    }
}
