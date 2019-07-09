package ConquerSpace.game.universe.ships;

import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.Vector;
import java.util.ArrayList;

/**
 *
 * @author Zyun
 */
public class Fleet extends SpaceShip{
    private ArrayList<Division> divisions;
    private String name;
    private int id;
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
    public double getX() {
        return X;
    }

    @Override
    public double getY() {
        return Y;
    }

    @Override
    public Vector getVector() {
        return v;
    }

    public void setVector(Vector v) {
        this.v = v;
    }

    public void setX(double X) {
        this.X = X;
    }

    public void setY(double Y) {
        this.Y = Y;
    }

    public void setLocation(UniversePath location) {
        this.location = location;
    }
    
        @Override
    public long getSpeed() {
        return 0;
    }

    @Override
    public UniversePath getOrbiting() {
        return null;
    }
}
