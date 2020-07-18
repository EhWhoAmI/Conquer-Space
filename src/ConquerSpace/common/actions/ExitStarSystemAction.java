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
import ConquerSpace.common.game.universe.UniversePath;
import ConquerSpace.common.game.universe.bodies.StarSystem;

/**
 *
 * @author
 */
public class ExitStarSystemAction extends ShipAction {

    private int starSystem;
    boolean done = false;

    public ExitStarSystemAction(Ship s) {
        super(s);
    }

    @Override
    public void doAction(GameState gameState) {
        //Get out of star system
        int id = ship.getLocation().getSystemID();
        if (id > -1) {
            Integer systemId = gameState.getUniverse().getStarSystem(id);
            StarSystem sys = gameState.getObject(systemId, StarSystem.class);
            //Set location
            ship.setLocation(new UniversePath());

            sys.spaceShips.remove(ship.getId());
            ship.setX(sys.getX());
            ship.setY(sys.getY());
            if (!gameState.getUniverse().spaceShips.contains(ship.getId())) {
                gameState.getUniverse().spaceShips.add(ship.getId());
            }
        }
        done = true;
    }

    @Override
    public void initAction(GameState gameState) {

    }

    @Override
    public boolean checkIfDone(GameState gameState) {
        return done;
    }
}
