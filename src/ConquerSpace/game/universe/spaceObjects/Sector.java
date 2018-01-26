package ConquerSpace.game.universe.spaceObjects;

import ConquerSpace.game.universe.GalaticLocation;
import java.util.ArrayList;

/**
 * A collection of 20 - 30 star systems.
 * @author Zyun
 */
public class Sector {

    private ArrayList<StarSystem> starSystems;
    private GalaticLocation loc;
    int id;
    
    /**
     *
     * @param location
     * @param id
     */
    public Sector(GalaticLocation location, int id) {
        loc = location;
        this.id = id;
        starSystems = new ArrayList<>();
    }

    /**
     *
     * @param e
     */
    public void addStarSystem(StarSystem e) {
        starSystems.add(e);
    }

    /**
     *
     * @param i
     * @return
     */
    public StarSystem getStarSystem(int i) {
        return (starSystems.get(i));
    }

    /**
     *
     * @return
     */
    public int getStarSystemCount() {
        return (starSystems.size());
    }
    
    /**
     *
     * @return
     */
    public GalaticLocation getGalaticLocation(){
        return loc;
    }
    
    public int getID() {
        return id;
    }
    /**
     *
     * @return
     */
    public String toReadableString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Sector " + this.id + " Position-" + loc.toString() + ": {");
        for (StarSystem s : starSystems){
            builder.append(s.toReadableString());
        }
        
        builder.append("}\n");
        return(builder.toString());
    }
}