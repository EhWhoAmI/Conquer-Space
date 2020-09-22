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
import ConquerSpace.common.game.resources.ResourceStockpile;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This can store resources, and is the same as a resource stockpile. It's just
 * that you cannot connect to it via logistics
 *
 * @author EhWhoAmI
 */
public class CargoComponent extends ShipComponent implements ResourceStockpile {

    int storageVolume;

    HashMap<Integer, Double> resources;

    public CargoComponent(GameState gameState) {
        super(gameState);
        resources = new HashMap<>();
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

    @Override
    public void addResourceTypeStore(Integer type) {
        resources.put(type, 0d);
    }

    @Override
    public Double getResourceAmount(Integer type) {
        return resources.get(type);
    }

    @Override
    public void addResource(Integer type, Double amount) {
        if (!resources.containsKey(type)) {
            resources.put(type, 0d);
        }
        resources.put(type, resources.get(type) + amount);
    }

    @Override
    public boolean canStore(Integer type) {
        return true;
    }

    @Override
    public Integer[] storedTypes() {
        Iterator<Integer> res = resources.keySet().iterator();
        Integer[] arr = new Integer[resources.size()];
        int i = 0;
        while (res.hasNext()) {
            Integer next = res.next();
            arr[i] = next;
            i++;
        }
        return arr;
    }

    @Override
    public boolean removeResource(Integer type, Double amount) {
        //Get the amount in the place
        if (!resources.containsKey(type)) {
            //Remove stuff for now
            //resources.put(type, amount);
            return false;
        }
        Double currentlyStored = resources.get(type);

        if (amount > currentlyStored) {
            return false;
        }

        resources.put(type, (currentlyStored - amount));
        return true;
    }
}
