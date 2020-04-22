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
import ConquerSpace.game.ships.Ship;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.Body;

/**
 *
 * @author EhWhoAmI
 */
public class ToOrbitAction extends ShipAction {

    private Planet position;

    public ToOrbitAction(Ship ship) {
        super(ship);
    }

    public void setPlanet(Planet position) {
        this.position = position;
    }

    @Override
    public void doAction() {
        double x = position.getX() - ship.getX();
        double y = position.getY() - ship.getY();

        //Normalize
        double len = Math.sqrt(x * x + y * y);
        if (len > 0) {
            x /= len;
            y /= len;
        }
        double distance = Math.sqrt(Math.pow(position.getX() - ship.getX(), 2) + Math.pow(position.getY() - ship.getY(), 2));
        double objX = (x * ship.getSpeed());
        double objY = (y * ship.getSpeed());
        if (Math.sqrt(Math.pow(objX, 2) + Math.pow(objY, 2)) >= distance) {
            objX = position.getX();
            objY = position.getY();
            ship.setX((long) objX);
            ship.setY((long) objY);
            //Enter orbit
            ship.setIsOrbiting(true);
            ship.setLocation(position.getUniversePath());
            //Remove...
            Globals.universe.getStarSystem(position.getParentStarSystem()).spaceShips.remove(ship);
            position.putShipInOrbit(ship);
        } else {
            ship.translate((long) (objX), (long) (objY));
        }
    }

    @Override
    public boolean checkIfDone() {
        return (ship.getX() == position.getX() && ship.getY() == position.getY() && ship.getOrbiting().equals(position.getUniversePath()));
    }

    @Override
    public void initAction() {
        if (ship.isOrbiting()) {
            //Exit orbit
            Body object = Globals.universe.getSpaceObject(ship.getOrbiting());
            if (object instanceof Planet) {
                Planet p = (Planet) object;
                //Remove from orbit
                p.getSatellites().remove(ship);

                ship.setX(p.getX());
                ship.setY(p.getY());
                ship.setIsOrbiting(false);

                //Add
                Globals.universe.getStarSystem(p.getParentStarSystem()).addSpaceShip(ship);
            }   
        }
        //Predict going to location...
        //Get changing degrexes per turn, and get distance from planet
        
        
        ship.setGoingToX(position.getX());
        ship.setGoingToY(position.getY());
    }

    public Planet getPosition() {
        return position;
    }
}
