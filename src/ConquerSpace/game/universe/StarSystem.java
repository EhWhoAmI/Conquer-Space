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
    
    public StarSystem(int id) {
        this.id = id;
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
}
