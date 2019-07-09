package ConquerSpace.game.universe.ships;

import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.Vector;
import java.util.ArrayList;

/**
 *
 * @author Zyun
 */
public class Division extends SpaceShip{
    private int id;
    private ArrayList<Ship> ships;
    private Vector v;
    private UniversePath location;
    private int parentfleetID;

    public Division(int id, long X, long Y, Vector v, UniversePath location, int parentfleetID) {
        this.id = id;
        this.ships = new ArrayList<>();;
        this.X = X;
        this.Y = Y;
        this.v = v;
        this.location = location;
        this.parentfleetID = parentfleetID;
    }

    



    public int getId() {
        return id;
    }


    public int getParentfleetID() {
        return parentfleetID;
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }
    
    public void addShip(Ship s) {
        ships.add(s);
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

    @Override
    public UniversePath getLocation() {
        return location;
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

    @Override
    public long getSpeed() {
        return 0;
    }

    @Override
    public UniversePath getOrbiting() {
        return null;
    }
    
    
}
