package ConquerSpace.game.universe.ships;

import ConquerSpace.game.universe.ships.components.ShipComponent;
import ConquerSpace.game.universe.ships.hull.Hull;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Zyun
 */
public class ShipClass implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private Hull hull;
    public ArrayList<ShipComponent> components;
    private int mass = 1;
    private long estimatedThrust = 0;

    public ShipClass(String name, Hull h) {
        this.name = name;
        this.hull = h;
        components = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public Hull getHull() {
        return hull;
    }

    public void setHull(Hull hull) {
        this.hull = hull;
    }

    public int getMass() {
        return mass;
    }

    public long getEstimatedThrust() {
        return estimatedThrust;
    }

    public void setEstimatedThrust(long estimatedThrust) {
        this.estimatedThrust = estimatedThrust;
    }
}
