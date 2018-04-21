package ConquerSpace.game.universe.spaceObjects;

import ConquerSpace.game.universe.GalaticLocation;
import java.util.ArrayList;

/**
 * A star system.
 * @author Zyun
 */
public class StarSystem extends SpaceObject{
    private ArrayList<Planet> planets;
    private ArrayList<Star> stars;
    private int id;
    private GalaticLocation location;
    private int parent;
    
    /**
     *
     * @param id
     * @param location
     */
    public StarSystem(int id, GalaticLocation location) {
        this.id = id;
        this.location = location;
        planets = new ArrayList<>();
        stars = new ArrayList<>();
    }
    
    /**
     *
     * @param star
     */
    public void addStar(Star star) {
        stars.add(star);
    }
    
    /**
     *
     * @param planet
     */
    public void addPlanet(Planet planet) {
        planets.add(planet);
    }
    
    /**
     *
     * @param i
     * @return
     */
    public Planet getPlanet(int i) {
        return (planets.get(i));
    }
    
    /**
     *
     * @return
     */
    public int getPlanetCount() {
        return (planets.size());
    }
    
    /**
     *
     * @param i
     * @return
     */
    public Star getStar(int i) {
        return (stars.get(i));
    }
    
    /**
     *
     * @return
     */
    public int getStarCount() {
        return (stars.size());
    }

    public int getId() {
        return id;
    }

    public GalaticLocation getLocation() {
        return location;
    }
    
    /**
     *
     * @return
     */
    public GalaticLocation getGalaticLocation(){
        return location;
    }
    
    /**
     *
     * @return
     */
    public String toReadableString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Star system " + this.id + " Location-" + location.toString() + ": [\n");
        //Display stars
        builder.append("Stars: <");
        for (Star s: stars) {
            builder.append(s.toReadableString());
        }
        builder.append(">\n");
        
        //Display planets
        builder.append("Planets: <");
        for (Planet p : planets) {
            builder.append(p.toReadableString());
        }
        builder.append(">\n");
        return (builder.toString());
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    @Override
    public void processTurn(int turn) {
        //Process turn of the planets then the stars.
        //Maybe later the objects in space.
        for (Planet planet : planets) {
            planet.processTurn(turn);
        }
        
        for (Star star : stars) {
            star.processTurn(turn);
        }
    }    
}
