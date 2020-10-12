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
import ConquerSpace.common.game.ships.SpaceShip;
import ConquerSpace.common.game.universe.bodies.Body;
import ConquerSpace.common.game.universe.bodies.Planet;

/**
 *
 * @author EhWhoAmI
 */
public class ShipUnloadAction extends ShipAction {

    public ShipUnloadAction(SpaceShip ship) {
        super(ship);
        done = false;
    }

    @Override
    public void doAction(GameState gameState) {
        //Get planet that the thing is orbiting
        SpaceShip spaceShip = gameState.getObject(ship, SpaceShip.class);
        //Get orbiting stuff
        ObjectReference orbitingref = gameState.getUniverse().getSpaceObject(spaceShip.getOrbiting());
        Body body = gameState.getObject(orbitingref, Body.class);
        System.out.println(body);
        if (body instanceof Planet) {
            //Place into orbit
            ((Planet) body).getSatellites().add(ship);
            System.out.println("done!");
            this.done = true;

        }
    }

    @Override
    public void initAction(GameState gameState) {
    }

    @Override
    public boolean isPossible(GameState gameState) {
        return this.done;
    }

}
