package ConquerSpace.game.universe.spaceObjects;

import ConquerSpace.game.universe.GalaticLocation;
import ConquerSpace.game.GameObject;
import java.util.ArrayList;

/**
 * A collection of 20 - 30 star systems.
 * @author Zyun
 */
public class Sector extends GameObject{

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
        e.setParent(id);
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
    
    public void modifyDegrees(float degs) {
        getGalaticLocation().setDegrees(getGalaticLocation().getDegrees() + degs);
    }
    
    public void modifyDistance(int dist) {
        getGalaticLocation().setDistance((int) (getGalaticLocation().getDistance() + dist));
    }
    public int getSize() {
            
            //Use the same process for the star systems.
            int largeStarSystem = 0;
            for (int i = 0; i < this.getStarSystemCount(); i ++) {
                if (this.getStarSystem(i).getGalaticLocation().getDistance() > largeStarSystem)
                    largeStarSystem = (int) this.getStarSystem(i).getGalaticLocation().getDistance();
            }
            
            // Then add the two distances together.
            return (largeStarSystem * 2);
    }
}
