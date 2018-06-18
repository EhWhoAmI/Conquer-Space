package ConquerSpace.game.universe.spaceObjects;

import ConquerSpace.game.universe.civilizations.Civilization;
import java.util.ArrayList;

/**
 * Universe Object.
 * @author Zyun
 */
public class Universe extends SpaceObject{

    private ArrayList<Sector> sectors;
    private ArrayList<Civilization> civs;

    public Universe() {
        sectors = new ArrayList<>();
        civs = new ArrayList<>();
    }
    
    /**
     * Returns a human-readable string
     * @return a human-readable string.
     */
    public String toReadableString() {
        //To a string that is human readable
        StringBuilder builder = new StringBuilder();
        //Just call the same method in the various containing objects
        for (Sector s : sectors) {
            builder.append(s.toReadableString());
        }
        for (Civilization c : civs) {
            builder.append(c.toReadableString());
        }
        return (builder.toString());
    }
    
    /**
     * Add a sector to the universe
     * @param s Sector to add
     */
    public void addSector(Sector s) {
        s.id = sectors.size();
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
     * @return Number of sectors
     */
    public int getSectorCount() {
        return(sectors.size());
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
    public void processTurn(int turn) {
        //Process turns of all the internals
        for (Sector sector : sectors) {
            sector.processTurn(turn);
        }
    }
}
