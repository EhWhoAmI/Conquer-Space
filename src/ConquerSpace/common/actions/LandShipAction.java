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
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.ships.SpaceShip;
import ConquerSpace.common.game.universe.bodies.Planet;

/**
 * Land ship on city
 *
 * @author EhWhoAmI
 */
public class LandShipAction extends ShipAction {

    ObjectReference positionToLand;

    public LandShipAction(SpaceShip ship) {
        super(ship);
    }

    @Override
    public void doAction(GameState gameState) {
    }

    @Override
    public void initAction(GameState gameState) {
    }

    @Override
    public boolean isPossible(GameState gameState) {
        return true;
    }

    @Override
    public boolean checkIfDone(GameState gameState) {
        //Get type of body landing on
        Object toLand = gameState.getObject(positionToLand);
        if (toLand instanceof City) {
            City city = (City) toLand;
            Planet p = gameState.getObject(city.getLocation(), Planet.class);
            SpaceShip ship = gameState.getObject(this.ship, SpaceShip.class);
            return (ship.isOrbiting() && ship.getOrbiting().equals(p.getUniversePath()));
        }
        return false;
    }

    public void setPositionToLand(ObjectReference positionToLand) {
        this.positionToLand = positionToLand;
    }
}
