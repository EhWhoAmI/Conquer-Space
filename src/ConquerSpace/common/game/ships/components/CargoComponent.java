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

/**
 * This can store resources, and is the same as a resource stockpile. It's just that you cannot
 * connect to it via logistics
 *
 * @author EhWhoAmI
 */
public class CargoComponent extends ShipComponent {

    int storageVolume;

    public CargoComponent(GameState gameState) {
        super(gameState);
    }

    public void setStorageVolume(int storageVolume) {
        this.storageVolume = storageVolume;
    }

    public int getStorageVolume() {
        return storageVolume;
    }

    @Override
    public ShipComponentType getShipComponentType() {
        return ShipComponentType.Cargo;
    }
}
