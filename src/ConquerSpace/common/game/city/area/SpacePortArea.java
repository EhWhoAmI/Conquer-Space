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
package ConquerSpace.common.game.city.area;

import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.universe.bodies.Planet;
import ConquerSpace.common.save.SerializeClassName;
import java.util.ArrayList;

/**
 * A place where you can start to Conquer Space!
 *
 * @author EhWhoAmI
 */
@SerializeClassName("space-port-area")
public class SpacePortArea extends Area {

    int launchPadCount;
    public ArrayList<ObjectReference> landedShips;
    private ObjectReference launchSystem;

    SpacePortArea(GameState gameState, ObjectReference system, int amount) {
        super(gameState);
        this.launchSystem = system;

        launchPadCount = amount;
        landedShips = new ArrayList<>();
    }

    public ObjectReference getLaunchSystem() {
        return launchSystem;
    }

    @Override
    public String toString() {
        return "Space Port";
    }

    public int getLaunchPadCount() {
        return launchPadCount;
    }

    @Override
    public void accept(AreaDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
