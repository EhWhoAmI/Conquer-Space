package ConquerSpace.game.universe.ships;

/**
 *
 * @author Zyun
 */
public class ShipClass {
    private String name;

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
