package ConquerSpace.game.universe.ships;

import ConquerSpace.game.actions.ShipAction;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.Vector;
import java.util.ArrayList;

/**
 * A space travelling object.
 *
 * @author Zyun
 */
public abstract class SpaceShip implements Orbitable{

    private static int ticker = 0;
    protected String name = "";
    protected double X;
    protected double Y;
    protected Vector v;
    protected UniversePath location;
    protected boolean isOrbiting = false;
    protected long mass;
    protected int goingToStarSystem = -1;
    protected double goingToX;
    protected double goingToY;

    protected int id;
    protected long maxSpeed = 1_0000_000;
    protected int throttle = 0;

    protected long estimatedThrust = 0;

    public ArrayList<ShipAction> commands = new ArrayList<ShipAction>();

    public abstract UniversePath getLocation();

    public abstract Vector getVector();

    public final ShipAction popAction() {
        return commands.remove(0);
    }

    public final ShipAction getActionAndPopIfDone() {
        if (commands.size() > 0) {
            ShipAction act = commands.get(0);
            if (act.checkIfDone()) {
                return commands.remove(0);
                //return act;
            } else {
                return act;
            }
        }
        //Return no action
        return new ShipAction(this);
    }

    public void addAction(ShipAction act) {
        //If empty, init
        if(commands.isEmpty()) {
            act.initAction();
        }
        commands.add(act);
    }

    public void setIsOrbiting(boolean isOrbiting) {
        this.isOrbiting = isOrbiting;
    }

    public boolean isOrbiting() {
        return isOrbiting;
    }

    public double getGoingToX() {
        return goingToX;
    }

    public double getGoingToY() {
        return goingToY;
    }

    public void setGoingToX(double goingToX) {
        this.goingToX = goingToX;
    }

    public void setGoingToY(double goingToY) {
        this.goingToY = goingToY;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Ship && ((SpaceShip) obj).id == this.id);
    }

    public void setMaxSpeed(long maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public long getMaxSpeed() {
        return estimatedThrust / mass;
    }

    public long getMass() {
        return mass;
    }

    public void translate(double x, double y) {
        this.X += x;
        this.Y += y;
    }

    public int getThrottle() {
        return throttle;
    }

    public void setThrottle(int throttle) {
        this.throttle = throttle;
    }

    public void setEstimatedThrust(long estimatedThrust) {
        this.estimatedThrust = estimatedThrust;
    }

    public long getEstimatedThrust() {
        return estimatedThrust;
    }

    public int getId() {
        return id;
    }
    
    public void setX(double X) {
        this.X = X;
    }

    public void setY(double Y) {
        this.Y = Y;
    }
    
    public abstract long getSpeed();
    
    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }
    
    public void setLocation(UniversePath location) {
        this.location = location;
        goingToStarSystem = location.getSystemID();
    }

    public void setGoingToStarSystem(int goingToStarSystem) {
        this.goingToStarSystem = goingToStarSystem;
    }

    public int getGoingToStarSystem() {
        return goingToStarSystem;
    }
}
