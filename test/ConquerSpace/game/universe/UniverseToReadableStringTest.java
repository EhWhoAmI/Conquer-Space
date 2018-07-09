package ConquerSpace.game.universe;

import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.Sector;
import ConquerSpace.game.universe.spaceObjects.Star;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.Universe;

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
        Sector s = new Sector(new GalaticLocation(0, 0), 0);
        StarSystem system = new StarSystem(0, new GalaticLocation(0, 0));
        system.addStar(new Star(0, 0, 0));
        system.addPlanet(new Planet(0, 0, 0, 0, 0, 0));
        s.addStarSystem(system);
        universe.addSector(s);
        System.out.println(universe.toReadableString());
    }
}
