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
import ConquerSpace.common.game.logistics.ResourcePermissions;
import ConquerSpace.common.game.resources.ResourceStockpile;
import ConquerSpace.common.game.resources.StorageNeeds;
import ConquerSpace.common.game.universe.UniversePath;
import ConquerSpace.common.save.SerializeClassName;
import ConquerSpace.common.util.DoubleHashMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author EhWhoAmI
 */
@SerializeClassName("stockpile-area")
public class ResourceStockpileArea extends Area implements ResourceStockpile {

    private ResourcePermissions defaultPermissions;
    private HashMap<Integer, ResourcePermissions> allPermissions;

    public HashMap<Integer, Double> resources;
    public DoubleHashMap<Integer> resourceDemands;

    public ArrayList<StorageNeeds> storageNeeds;
    public HashMap<Integer, DoubleHashMap<String>> resourceLedger;
    public UniversePath path;
    
    public ResourceStockpileArea(GameState gameState) {
        super(gameState);
        allPermissions = new HashMap<>();
        defaultPermissions = new ResourcePermissions(false, false, false);
        resourceLedger = new HashMap<>();
        resourceDemands = new DoubleHashMap<>();
    }

    public ResourcePermissions getPermission(Integer person) {
        if (allPermissions.containsKey(person)) {
            return allPermissions.get(person);
        }
        return defaultPermissions;
    }

    public void addPermission(Integer person, ResourcePermissions permission) {
        allPermissions.put(person, permission);
    }
    
    @Override
    public UniversePath getUniversePath() {
        return path;
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
        //Add to ledger
        if (resourceLedger.containsKey(type)) {
            DoubleHashMap<String> resource = resourceLedger.get(type);
            resource.addValue("added", (amount));
        } else {
            DoubleHashMap<String> resource = new DoubleHashMap<>();
            resource.put("added", amount);
            resourceLedger.put(type, resource);
        }
    }

    @Override
    public boolean canStore(Integer type) {
        return true;//(resources.containsKey(type));
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
        //Add to ledger
        if (resourceLedger.containsKey(type)) {
            DoubleHashMap<String> resource = resourceLedger.get(type);
            resource.addValue("removed", -amount);
            resourceLedger.put(type, resource);
        } else {
            DoubleHashMap<String> resource = new DoubleHashMap<>();
            resource.addValue("removed", -amount);
            resourceLedger.put(type, resource);
        }
        return true;
    }
}
