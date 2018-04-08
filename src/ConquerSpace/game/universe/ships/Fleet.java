package ConquerSpace.game.universe.ships;

import java.util.ArrayList;

/**
 *
 * @author Zyun
 */
public class Fleet {
    private ArrayList<Division> divisions;
    private String name;
    private int id;
    
    public Fleet(String name, int id) {
        this.id = id;
        this.name = name;
        divisions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Division> getDivisions() {
        return divisions;
    }
    
    public void addDivision(Division d) {
        divisions.add(d);
    }
}
