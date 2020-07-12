/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ConquerSpace.game.ships;

import ConquerSpace.game.GameState;
import ConquerSpace.game.actions.EmptyShipAction;
import ConquerSpace.game.actions.ShipAction;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.Vector;
import java.util.ArrayList;

/**
 * A space travelling object.
 *
 * @author EhWhoAmI
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

    public final ShipAction getActionAndPopIfDone(GameState gameState) {
        if (commands.size() > 0) {
            ShipAction act = commands.get(0);
            if (act.checkIfDone(gameState)) {
                return commands.remove(0);
                //return act;
            } else {
                return act;
            }
        }
        //Return no action
        return new EmptyShipAction(this);
    }

    public void addAction(GameState gameState, ShipAction act) {
        //If empty, init
        if(commands.isEmpty()) {
            act.initAction(gameState);
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
