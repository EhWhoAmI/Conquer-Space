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

import ConquerSpace.server.GameController;
import ConquerSpace.common.GameState;
import ConquerSpace.common.game.ships.Ship;
import ConquerSpace.common.game.universe.SpacePoint;
import ConquerSpace.common.game.universe.bodies.Body;
import ConquerSpace.common.game.universe.bodies.Planet;
import ConquerSpace.common.game.universe.bodies.StarSystem;

/**
 *
 * @author EhWhoAmI
 */
public class ShipMoveAction extends ShipAction {

    private SpacePoint position;
    private int starSystem;

    private boolean done = false;

    public ShipMoveAction(Ship ship) {
        super(ship);
    }

    public void setPosition(SpacePoint position) {
        this.position = position;
    }

    @Override
    public void doAction(GameState state) {

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
    public boolean checkIfDone(GameState gameState) {
        //if out of star system
        StarSystem sys = gameState.getObject(gameState.universe.getStarSystem(ship.getLocation().getSystemID()), StarSystem.class);
        if (Math.sqrt(Math.pow(ship.getX(), 2) + Math.pow(ship.getY(), 2)) >= (sys.getBodyObject(sys.getBodyCount()- 1).orbit.toPolarCoordinate().getDistance() + 10)) {
            return true;
        }
        return (ship.getX() == position.getX() && ship.getY() == position.getY());
    }

    public SpacePoint getPosition() {
        return position;
    }

    @Override
    public void initAction(GameState gameState) {
        if (ship.isOrbiting()) {
            //Exit orbit
            Body body = gameState.getObject(gameState.universe.getSpaceObject(ship.getOrbiting()), Body.class);
            if (body instanceof Planet) {
                Planet p = (Planet) body;
                //Remove from orbit
                p.getSatellites().remove(ship);

                ship.setX(p.getX());
                ship.setY(p.getY());
                ship.setIsOrbiting(false);

                //Add
                gameState.universe.getStarSystemObject(p.getParentStarSystem()).addSpaceShip(ship.getId());
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
