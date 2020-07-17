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
public abstract class ShipAction {

    protected SpaceShip ship;
    
    protected boolean done;
    

    public ShipAction(SpaceShip ship) {
        this.ship = ship;
    }

    public void doAction(GameState gameState) {
    }
    
    public boolean checkIfDone(GameState gameState) {
        return true;
    }
    
    public void initAction(GameState gameState) {
        
    }
}
