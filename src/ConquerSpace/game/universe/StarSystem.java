package ConquerSpace.game.universe;

import java.util.ArrayList;

/**
 *
 * @author Zyun
 */
public class StarSystem {
    private ArrayList<Planet> planets;
    private ArrayList<Star> stars;
    private int id;
    private GalaticLocation location;
    
    public StarSystem(int id, GalaticLocation location) {
        this.id = id;
        this.location = location;
        planets = new ArrayList<>();
        stars = new ArrayList<>();
    }
    
    public void addStar(Star star) {
        stars.add(star);
    }
    
    public void addPlanet(Planet planet) {
        planets.add(planet);
    }
    
    public Planet getPlanet(int i) {
        return (planets.get(i));
    }
    
    public int getPlanetCount() {
        return (planets.size());
    }
    
    public Star getStar(int i) {
        return (stars.get(i));
    }
    
    public int getStarCount() {
        return (stars.size());
    }

    public GalaticLocation getGalaticLocation(){
        return location;
    }
    
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
}
