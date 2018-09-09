package ConquerSpace.game.universe.ships.satellites;

import ConquerSpace.game.universe.GalaticLocation;
import ConquerSpace.game.universe.ships.Launchable;

/**
 *
 * @author Zyun
 */
public class Satellite implements Launchable{
    
    protected GalaticLocation location;
    protected int mass;
    protected String name = "";
    protected int id;
        
    public Satellite(int distance, int mass) {
        this.mass = mass;
        location = new GalaticLocation(0, distance);
    }

    public int getMass() {
        return mass;
    }
    
    /**
     * Get orbit distance, in km. A distance of 0 means that the satellite can orbit in any orbit.
     * @return 
     */
    public int getDistance() {
        return location.getDistance();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return getName();
    }
    
    
}