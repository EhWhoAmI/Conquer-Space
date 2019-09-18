package ConquerSpace.game.universe.spaceObjects;

import ConquerSpace.game.population.Species;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.ships.SpaceShip;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Universe Object.
 *
 * @author Zyun
 */
public class Universe extends SpaceObject {

    private final long seed;

    private ArrayList<Civilization> civs;

    private ArrayList<StarSystem> starSystems;

    public HashMap<UniversePath, Integer> control;

    private ArrayList<Resource> resources;

    public ArrayList<SpaceShip> spaceShips;
    
    public ArrayList<Species> species;
    
    public Universe(long seed) {
        this.seed = seed;
        civs = new ArrayList<>();
        control = new HashMap<>();
        starSystems = new ArrayList<>();
        spaceShips = new ArrayList<>();
        species = new ArrayList<>();
    }

    /**
     * Returns a human-readable string
     *
     * @return a human-readable string.
     */
    public String toReadableString() {
        //To a string that is human readable
        StringBuilder builder = new StringBuilder();
        //Just call the same method in the various containing objects
        for (StarSystem s : starSystems) {
            builder.append(s.toReadableString());
        }
        for (Civilization c : civs) {
            builder.append(c.toReadableString());
        }
        return (builder.toString());
    }

    public void addStarSystem(StarSystem s) {
        s.id = starSystems.size();
        starSystems.add(s);
        //Add system to control
        control.put(s.getUniversePath(), -1);
    }

    public int getStarSystemCount() {
        return starSystems.size();
    }

    public StarSystem getStarSystem(int i) {
        return starSystems.get(i);
    }

    public Iterator<StarSystem> getStarSystemIterator() {
        return starSystems.iterator();
    }

    public void addCivilization(Civilization c) {
        civs.add(c);
    }

    public int getCivilizationCount() {
        return (civs.size());
    }

    public Civilization getCivilization(int i) {
        return (civs.get(i));
    }

    /**
     * Get the space object (Planet, sector, etc,) as referenced by UniversePath
     * <code>p</code>.
     *
     * @param p Path
     * @return Space object
     */
    public SpaceObject getSpaceObject(UniversePath p) {

        StarSystem system = getStarSystem(p.getSystemID());
        if (p.getPlanetID() != -1) {
            return system.getPlanet(p.getPlanetID());
        } else if (p.getStarID() != -1) {
            return system.getStar(p.getStarID());
        } else {
            return system;
        }

    }

    public long getSeed() {
        return seed;
    }
}
