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
import ConquerSpace.common.game.universe.UniversePath;
import ConquerSpace.common.game.universe.bodies.StarSystem;

/**
 *
 * @author
 */
public class ExitStarSystemAction extends ShipAction {

    private int starSystem;

    public ExitStarSystemAction(Ship s) {
        super(s);
        done = false;
    }

    @Override
    public void doAction(GameState gameState) {
        Ship shipObject = gameState.getObject(ship, Ship.class);

        //Get out of star system
        int id = shipObject.getLocation().getSystemIndex();
        if (id > -1) {
            ObjectReference systemId = gameState.getUniverse().getStarSystem(id);
            StarSystem sys = gameState.getObject(systemId, StarSystem.class);
            //Set location
            shipObject.setLocation(new UniversePath());

            sys.spaceShips.remove(shipObject.getReference());
            shipObject.setX(sys.getX());
            shipObject.setY(sys.getY());
            if (!gameState.getUniverse().spaceShips.contains(shipObject.getReference())) {
                gameState.getUniverse().spaceShips.add(shipObject.getReference());
            }
        }
        done = true;
    }

    @Override
    public void initAction(GameState gameState) {
        //Empty
    }

    @Override
    public boolean checkIfDone(GameState gameState) {
        return done;
    }

    @Override
    public boolean isPossible(GameState gameState) {
        return true;
    }
}
