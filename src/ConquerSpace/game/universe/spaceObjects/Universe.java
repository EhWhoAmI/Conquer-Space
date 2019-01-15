package ConquerSpace.game.universe.spaceObjects;

import ConquerSpace.game.StarDate;
import ConquerSpace.game.UniversePath;
import ConquerSpace.game.universe.civilization.Civilization;
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

    private ArrayList<Sector> sectors;
    private ArrayList<Civilization> civs;

    private ArrayList<StarSystem> starSystems;

    public HashMap<UniversePath, Integer> control;

    public Universe(long seed) {
        this.seed = seed;
        sectors = new ArrayList<>();
        civs = new ArrayList<>();
        control = new HashMap<>();
        starSystems = new ArrayList<>();
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

    /**
     * Add a sector to the universe
     *
     * @param s Sector to add
     */
    public void addSector(Sector s) {
        s.id = sectors.size();

        //Set the id of the parent sector.
        for (StarSystem e : s.starSystems) {
            for (int i = 0; i < e.getPlanetCount(); i++) {
                e.getPlanet(i).setParentSector(s.id);
            }
        }

        //Add sector and contents to control.
        for (int i = 0; i < s.getStarSystemCount(); i++) {
            StarSystem system = s.getStarSystem(i);

            //Add planets
            for (int n = 0; n < system.getPlanetCount(); n++) {
                Planet p = system.getPlanet(n);
                control.put(p.getUniversePath(), ControlTypes.NONE_CONTROLLED);
            }

            //Add stars
            for (int n = 0; n < system.getStarCount(); n++) {
                Star star = system.getStar(n);
                control.put(star.getUniversePath(), ControlTypes.NONE_CONTROLLED);
            }

            //Add star system
            control.put(system.getUniversePath(), ControlTypes.NONE_CONTROLLED);
        }
        sectors.add(s);
    }

    /**
     *
     * @param i ID of sector
     * @return The sector,
     */
    public Sector getSector(int i) {
        return (sectors.get(i));
    }

    /**
     * Get number of sectors in planet.
     *
     * @return Number of sectors
     */
    public int getSectorCount() {
        return (sectors.size());
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

    @Override
    public void processTurn(int GameRefreshRate, StarDate stardate) {
        //Process turns of all the internals
        for (StarSystem sector : starSystems) {
            sector.processTurn(GameRefreshRate, stardate);
        }
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
