package ConquerSpace.game.universe;

import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.Star;
import ConquerSpace.game.universe.bodies.StarSystem;
import ConquerSpace.game.universe.bodies.Universe;

/**
 *
 * @author Zyun
 */
public class UniverseToReadableStringTest {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        //Create universe
        Universe universe = new Universe(0l);
        //Create sector
        Sector s = new Sector(new GalacticLocation(0, 0), 0);
        StarSystem system = new StarSystem(0, new GalacticLocation(0, 0));
        system.addStar(new Star(0, 0, 0));
        system.addPlanet(new Planet(0, 0, 0, 0, 0, 0));
        s.addStarSystem(system);
        universe.addSector(s);
        System.out.println(universe.toReadableString());
    }
}
