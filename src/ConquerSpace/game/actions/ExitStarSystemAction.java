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
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.ships.Ship;
import ConquerSpace.game.universe.bodies.StarSystem;

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
    public void doAction() {
        //Get out of star system
        int id = ship.getLocation().getSystemID();
        if (id > -1) {
            StarSystem sys = Globals.universe.getStarSystem(id);
            //Set location
            ship.setLocation(new UniversePath());

            sys.spaceShips.remove(ship);
            ship.setX(sys.getX());
            ship.setY(sys.getY());
            if (!Globals.universe.spaceShips.contains(ship)) {
                Globals.universe.spaceShips.add(ship);
            }
        }
        done = true;
    }

    @Override
    public void initAction() {

    }

    @Override
    public boolean checkIfDone() {
        return done;
    }
}
