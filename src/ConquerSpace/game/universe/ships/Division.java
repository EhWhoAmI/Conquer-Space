package ConquerSpace.game.universe.ships;

import ConquerSpace.game.universe.GalacticLocation;
import java.util.ArrayList;

/**
 *
 * @author Zyun
 */
public class Division {
    private int id;
    private ArrayList<Ship> ships;

    private int parentfleetID;
    private GalacticLocation location;

    public Division(int id,int parentfleetID, GalacticLocation location) {
        this.id = id;
        ships = new ArrayList<>();
        this.parentfleetID = parentfleetID;
        this.location = location;
    }

    public void setLocation(GalacticLocation location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public GalacticLocation getLocation() {
        return location;
    }

    public int getParentfleetID() {
        return parentfleetID;
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }
    
    public void addShip(Ship s) {
        ships.add(s);
    }
}
