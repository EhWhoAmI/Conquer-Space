package ConquerSpace.game.universe.spaceObjects;

import ConquerSpace.game.universe.GalaticLocation;
import java.util.ArrayList;

/**
 * A collection of 20 - 30 star systems.
 *
 * @author Zyun
 */
public class Sector extends SpaceObject {

    private ArrayList<StarSystem> starSystems;
    private GalaticLocation loc;
    int id;

    /**
     * Creates a new sector
     * @param location Galatic location of the sector
     * @param id id of sector
     */
    public Sector(GalaticLocation location, int id) {
        loc = location;
        this.id = id;
        starSystems = new ArrayList<>();
    }

    /**
     * Add a star system to this.
     * @param e Star system
     */
    public void addStarSystem(StarSystem e) {
        e.setParent(id);
        e.id = starSystems.size();
        
        starSystems.add(e);
        starSystems.trimToSize();
    }

    /**
     * Get star system
     * @param i id of system
     * @return get the star system i
     */
    public StarSystem getStarSystem(int i) {
        return (starSystems.get(i));
    }

    /**
     * Get number of star systems.
     * @return number of star systems
     */
    public int getStarSystemCount() {
        return (starSystems.size());
    }

    /**
     * Get the galatic location of this sector
     * @return Galatic location of this sector
     */
    public GalaticLocation getGalaticLocation() {
        return loc;
    }

    public int getID() {
        return id;
    }

    /**
     * Readable string
     * @return This sector in a readable string
     */
    public String toReadableString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Sector " + this.id + " Position-" + loc.toString() + ": {");
        for (StarSystem s : starSystems) {
            builder.append(s.toReadableString());
        }

        builder.append("}\n");
        return (builder.toString());
    }

    public void modifyDegrees(float degs) {
        getGalaticLocation().setDegrees(getGalaticLocation().getDegrees() + degs);
    }

    public void modifyDistance(int dist) {
        getGalaticLocation().setDistance((int) (getGalaticLocation().getDistance() + dist));
    }

    public int getSize() {
        //Use the same process for the star systems.
        int largeStarSystem = 0;
        for (int i = 0; i < this.getStarSystemCount(); i++) {
            if (this.getStarSystem(i).getGalaticLocation().getDistance() > largeStarSystem) {
                largeStarSystem = (int) this.getStarSystem(i).getGalaticLocation().getDistance();
            }
        }
        // Then add the two distances together.
        return (largeStarSystem * 2);
    }

    @Override
    public void processTurn(int turn) {
        for (StarSystem starSystem : starSystems) {
            starSystem.processTurn(turn);
        }
    }    
}
