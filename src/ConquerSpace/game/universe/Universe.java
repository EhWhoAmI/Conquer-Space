package ConquerSpace.game.universe;

import java.util.ArrayList;

/**
 *
 * @author Zyun
 */
public class Universe {

    private ArrayList<Sector> sectors;

    /**
     *
     */
    public Universe() {
        sectors = new ArrayList<>();
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
}
