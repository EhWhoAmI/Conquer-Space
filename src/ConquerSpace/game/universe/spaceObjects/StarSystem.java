package ConquerSpace.game.universe.spaceObjects;

import ConquerSpace.game.UniversePath;
import ConquerSpace.game.universe.GalaticLocation;
import java.util.ArrayList;

/**
 * A star system.
 *
 * @author Zyun
 */
public class StarSystem extends SpaceObject {
    /**
     * All planets in this star system.
     */
    private ArrayList<Planet> planets;
    
    /**
     * All stars in this star system.
     */
    private ArrayList<Star> stars;
    
    /**
     * ID of this star system.
     */
    int id;
    
    /**
     * Galatic location.
     */
    private GalaticLocation location;
    
    /**
     * Parent sector.
     */
    private int parent;

    /**
     * Creates a new star system.
     * @param id ID of this star system
     * @param location Galatic location.
     */
    public StarSystem(int id, GalaticLocation location) {
        this.id = id;
        this.location = location;
        planets = new ArrayList<>();
        stars = new ArrayList<>();
    }

    /**
     * Add star.
     * @param star Star
     */
    public void addStar(Star star) {
        star.setParentSector(parent);
        star.setParentStarSystem(id);
        star.id = stars.size();
        
        stars.add(star);
        stars.trimToSize();
    }

    /**
     * Add planet to this star system
     * @param planet Planet
     */
    public void addPlanet(Planet planet) {
        planet.setParentSector(parent);
        planet.setParentStarSystem(id);
        planet.id = planets.size();
        
        planets.add(planet);
        planets.trimToSize();
    }

    /**
     * Get planet
     * @param i Get the planet <code>i</code>
     * @return The planet on i
     */
    public Planet getPlanet(int i) {
        return (planets.get(i));
    }

    /**
     * Get number of planets.
     * @return number of planets
     */
    public int getPlanetCount() {
        return (planets.size());
    }

    /**
     * Get the star.
     * @param i star id
     * @return star id as <code>i</code> puts it.
     */
    public Star getStar(int i) {
        return (stars.get(i));
    }

    /**
     * Get number of stars
     * @return Number of stars
     */
    public int getStarCount() {
        return (stars.size());
    }

    /**
     * Get this star system's id
     * @return ID
     */
    public int getId() {
        return id;
    }


    /**
     * Get galatic location of this star system
     * @return Galatic location of this star system
     */
    public GalaticLocation getGalaticLocation() {
        return location;
    }

    /**
     * Get readable string of the star system, stars and planets.
     * @return a readable string
     */
    public String toReadableString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Star system " + this.id + " Location-" + location.toString() + ": [\n");
        //Display stars
        builder.append("Stars: <");
        for (Star s : stars) {
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

    /**
     * Get parent sector id
     * @return parent sector id.
     */
    public int getParent() {
        return parent;
    }

    /**
     * Set the parent sector
     * @param parent id of parent sector
     */
    public void setParent(int parent) {
        this.parent = parent;
    }
    
    /**
     * Processes turns of the planets
     */
    @Override
    public void processTurn() {
        //Process turn of the planets then the stars.
        //Maybe later the objects in space.
        for (Planet planet : planets) {
            planet.processTurn();
        }

        for (Star star : stars) {
            star.processTurn();
        }
    }
    
    /**
     * Get the path of this star system
     * @return The path of this star system
     */
    public UniversePath getUniversePath() {
        return (new UniversePath(parent, id));
    }
}
