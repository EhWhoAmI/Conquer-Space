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
package ConquerSpace.game.actions;

import ConquerSpace.Globals;
import ConquerSpace.game.universe.Point;
import ConquerSpace.game.ships.Ship;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.StarSystem;

/**
 *
 * @author EhWhoAmI
 */
public class ShipMoveAction extends ShipAction {

    private Point position;
    private int starSystem;

    private boolean done = false;

    public ShipMoveAction(Ship ship) {
        super(ship);
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public void doAction() {

        double x = ship.getGoingToX() - ship.getX();
        double y = ship.getGoingToY() - ship.getY();

        //Normalize
        double len = Math.sqrt(x * x + y * y);
        if (len > 0) {
            x /= len;
            y /= len;
        }
        double distance = Math.sqrt(Math.pow(ship.getGoingToX() - ship.getX(), 2) + Math.pow(ship.getGoingToY() - ship.getY(), 2));
        double speed = ship.getSpeed();

        double objX = (x * speed);
        double objY = (y * speed);

        if (Math.sqrt(Math.pow(objX, 2) + Math.pow(objY, 2)) >= distance) {
            objX = ship.getGoingToX();
            objY = ship.getGoingToY();
            ship.setX((long) objX);
            ship.setY((long) objY);
        } else {
            ship.translate((long) (objX), (long) (objY));
        }
    }

    @Override
    public boolean checkIfDone() {
        //if out of star system
        StarSystem sys = Globals.universe.getStarSystem(ship.getLocation().getSystemID());
        if (Math.sqrt(Math.pow(ship.getX(), 2) + Math.pow(ship.getY(), 2)) >= (sys.bodies.get(sys.bodies.size()- 1).orbit.toPolarCoordinate().getDistance() + 10)) {
            return true;
        }
        return (ship.getX() == position.getX() && ship.getY() == position.getY());
    }

    public Point getPosition() {
        return position;
    }

    @Override
    public void initAction() {
        if (ship.isOrbiting()) {
            //Exit orbit
            if (Globals.universe.getSpaceObject(ship.getOrbiting()) instanceof Planet) {
                Planet p = (Planet) Globals.universe.getSpaceObject(ship.getOrbiting());
                //Remove from orbit
                p.getSatellites().remove(ship);

                ship.setX(p.getX());
                ship.setY(p.getY());
                ship.setIsOrbiting(false);

                //Add
                Globals.universe.getStarSystem(p.getParentStarSystem()).addSpaceShip(ship);
            }
        }
        ship.setGoingToX(position.getX());
        ship.setGoingToY(position.getY());
    }

    public void setStarSystem(int starSystem) {
        this.starSystem = starSystem;
    }

    public int getStarSystem() {
        return starSystem;
    }
}
