package ConquerSpace.game.universe.ships.satellites;

import ConquerSpace.game.universe.GalacticLocation;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.ships.Launchable;
import ConquerSpace.game.universe.ships.Orbitable;

/**
 *
 * @author Zyun
 */
public class Satellite implements Launchable, Orbitable{
    
    protected GalacticLocation location;
    protected int mass;
    protected String name = "";
    protected int id;
    protected int owner = -1;
    protected UniversePath orbiting = null;
        
    public Satellite(int distance, int mass) {
        this.mass = mass;
        location = new GalacticLocation(0, distance);
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

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    @Override
    public UniversePath getOrbiting() {
        return orbiting;
    }

    public void setOrbiting(UniversePath orbiting) {
        this.orbiting = orbiting;
    }
    
    
}