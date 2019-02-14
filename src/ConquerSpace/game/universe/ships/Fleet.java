package ConquerSpace.game.universe.ships;

import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.Vector;
import java.util.ArrayList;

/**
 *
 * @author Zyun
 */
public class Fleet implements SpaceShip{
    private ArrayList<Division> divisions;
    private String name;
    private int id;
    private long X;
    private long Y;
    private Vector v;
    private UniversePath location;
    
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

    @Override
    public UniversePath getLocation() {
        return location;
    }

    @Override
    public long getX() {
        return X;
    }

    @Override
    public long getY() {
        return Y;
    }

    @Override
    public Vector getVector() {
        return v;
    }

    public void setVector(Vector v) {
        this.v = v;
    }

    public void setX(long X) {
        this.X = X;
    }

    public void setY(long Y) {
        this.Y = Y;
    }

    public void setLocation(UniversePath location) {
        this.location = location;
    }
}
