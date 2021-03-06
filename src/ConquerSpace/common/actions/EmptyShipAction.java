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
import ConquerSpace.common.game.ships.SpaceShip;

/**
 *
 * @author EhWhoAmI
 */
public class EmptyShipAction extends ShipAction {

    public EmptyShipAction(SpaceShip ship) {
        super(ship);
    }

    @Override
    public boolean checkIfDone(GameState gameState) {
        return true;
    }

    @Override
    public void doAction(GameState gameState) {
        //Leave empty
    }

    @Override
    public void initAction(GameState gameState) {
        //Leave empty
    }

    @Override
    public boolean isPossible(GameState gameState) {
        return true;
    }
}
