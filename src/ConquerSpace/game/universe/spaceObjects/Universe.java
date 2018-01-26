package ConquerSpace.game.universe.spaceObjects;

import ConquerSpace.game.universe.civilizations.Civilization;
import ConquerSpace.game.GameObject;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Zyun
 */
public class Universe extends GameObject{

    private ArrayList<Sector> sectors;
    private ArrayList<Civilization> civs;
    /**
     *
     */
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
     *
     * @param s
     */
    public void addSector(Sector s) {
        sectors.add(s);
    }
    
    /**
     *
     * @param i
     * @return
     */
    public Sector getSector(int i) {
        return (sectors.get(i));
    }
    
    /**
     *
     * @return
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
}
