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
}
