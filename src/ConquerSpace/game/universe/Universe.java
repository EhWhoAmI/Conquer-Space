package ConquerSpace.game.universe;

import java.util.ArrayList;

/**
 *
 * @author Zyun
 */
public class Universe {

    private ArrayList<Sector> sectors;

    public Universe() {
        sectors = new ArrayList<>();
    }
    
    public String toReadableString() {
        //To a string that is human readable
        StringBuilder builder = new StringBuilder();
        //Just call the same method in the various containing objects
        for (Sector s : sectors) {
            builder.append(s.toReadableString());
        }
        return (builder.toString());
    }
    
    public void addSector(Sector s) {
        sectors.add(s);
    }
    
    public Sector getSector(int i) {
        return (sectors.get(i));
    }
    
    public int getSectorCount() {
        return(sectors.size());
    }
}
