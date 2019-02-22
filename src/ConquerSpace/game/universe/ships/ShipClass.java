package ConquerSpace.game.universe.ships;

import ConquerSpace.game.universe.ships.hull.Hull;

/**
 *
 * @author Zyun
 */
public class ShipClass {
    private String name;
    private Hull hull;

    public ShipClass(String name) {
        this.name = name;
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
    
    
}
