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
package ConquerSpace.common.actions;

import ConquerSpace.common.GameState;
import ConquerSpace.common.game.ships.Ship;
import ConquerSpace.common.game.universe.bodies.Body;
import ConquerSpace.common.game.universe.bodies.Planet;
import ConquerSpace.common.game.universe.bodies.StarSystem;

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
    public void doAction(GameState gameState) {
        Ship shipObject = gameState.getObject(ship, Ship.class);
        double x = position.getX() - shipObject.getX();
        double y = position.getY() - shipObject.getY();

        //Normalize
        double len = Math.sqrt(x * x + y * y);
        if (len > 0) {
            x /= len;
            y /= len;
        }
        double distance = Math.sqrt(Math.pow(position.getX() - shipObject.getX(), 2) + Math.pow(position.getY() - shipObject.getY(), 2));
        double objX = (x * shipObject.getSpeed());
        double objY = (y * shipObject.getSpeed());
        if (Math.sqrt(Math.pow(objX, 2) + Math.pow(objY, 2)) >= distance) {
            objX = position.getX();
            objY = position.getY();
            shipObject.setX((long) objX);
            shipObject.setY((long) objY);
            //Enter orbit
            shipObject.setIsOrbiting(true);
            shipObject.setLocation(position.getUniversePath());
            //Remove...
            StarSystem body = gameState.getObject(gameState.getUniverse().getStarSystem(position.getParentIndex()), StarSystem.class);
            body.spaceShips.remove(ship);
            position.putShipInOrbit(shipObject);
        } else {
            shipObject.translate((long) (objX), (long) (objY));
        }
    }

    @Override
    public boolean checkIfDone(GameState gameState) {
        Ship shipObject = gameState.getObject(ship, Ship.class);

        return (shipObject.getX() == position.getX() && shipObject.getY() == position.getY() && shipObject.getOrbiting().equals(position.getUniversePath()));
    }

    @Override
    public void initAction(GameState gameState) {
        Ship shipObject = gameState.getObject(ship, Ship.class);

        if (shipObject.isOrbiting()) {
            //Exit orbit
            Body object = gameState.getObject(gameState.getUniverse().getSpaceObject(shipObject.getOrbiting()), Body.class);
            if (object instanceof Planet) {
                Planet p = (Planet) object;
                //Remove from orbit
                p.getSatellites().remove(ship);

                shipObject.setX(p.getX());
                shipObject.setY(p.getY());
                shipObject.setIsOrbiting(false);

                //Add to star system
                gameState.getObject(gameState.getUniverse().getStarSystem(p.getParentIndex()), StarSystem.class).addSpaceShip(ship);
            }
        }
        //Predict going to location...
        //Get changing degrexes per turn, and get distance from planet

        shipObject.setGoingToX(position.getX());
        shipObject.setGoingToY(position.getY());
    }

    public Planet getPosition() {
        return position;
    }
}
