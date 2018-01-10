package ConquerSpace.game.universe;

/**
 *
 * @author Zyun
 */
public class UniverseToReadableStringTest {
    public static void main(String[] args) {
        //Create universe
        Universe universe = new Universe();
        //Create sector
        Sector s = new Sector(new GalaticLocation(0, 0), 0);
        StarSystem system = new StarSystem(0, new GalaticLocation(0, 0));
        system.addStar(new Star(0, 0, 0));
        system.addPlanet(new Planet(0, 0, 0, 0));
        s.addStarSystem(system);
        universe.addSector(s);
        System.out.println(universe.toReadableString());
    }
}
