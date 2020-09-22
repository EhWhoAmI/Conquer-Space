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
import ConquerSpace.common.game.ships.Ship;
import ConquerSpace.common.game.ships.SpaceShip;

/**
 *
 * @author EhWhoAmI
 */
public class ShipDockAction extends ShipAction {
    ObjectReference toDockTo;
    
    public ShipDockAction(SpaceShip ship) {
        super(ship);
    }

    public void setToDockTo(SpaceShip toDockTo) {
        this.toDockTo = toDockTo.getReference();
    }

    public ObjectReference getToDockTo() {
        return toDockTo;
    }

    @Override
    public void initAction(GameState gameState) {
    }

    @Override
    public void doAction(GameState gameState) {
        //Goto the place
        
    }

    @Override
    public boolean checkIfDone(GameState gameState) {
        Ship shipDoc = gameState.getObject(ship, Ship.class);
        Ship shipDocking = gameState.getObject(toDockTo, Ship.class);
        //They're basically next to each other anyway, so good enough I guess
        return shipDocking.getOrbiting().equals(shipDoc.getOrbiting());
    }

}
