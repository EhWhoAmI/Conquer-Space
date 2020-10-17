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

package ConquerSpace.common.game.ships.components;

import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;

/**
 * An engine for launch vehicles.
 * @author EhWhoAmI
 */
public class ToOrbitEngineComponent extends ShipComponent {

    ObjectReference launchSystemType;

    int thrust;

    public ToOrbitEngineComponent(GameState gameState) {
        super(gameState);
    }

    @Override
    public ShipComponentType getShipComponentType() {
        return ShipComponentType.ToOrbit;
    }

    public ObjectReference getLaunchSystemType() {
        return launchSystemType;
    }

    public void setLaunchSystemType(ObjectReference launchSystemType) {
        this.launchSystemType = launchSystemType;
    }

    public int getThrust() {
        return thrust;
    }

    public void setThrust(int thrust) {
        this.thrust = thrust;
    }
}
