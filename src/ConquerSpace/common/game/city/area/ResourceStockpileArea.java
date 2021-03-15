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
import ConquerSpace.common.game.logistics.ResourcePermissions;
import ConquerSpace.common.game.population.jobs.JobType;
import ConquerSpace.common.game.resources.ResourceStockpile;
import ConquerSpace.common.game.resources.StorageNeeds;
import ConquerSpace.common.game.resources.StoreableReference;
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
    private HashMap<ObjectReference, ResourcePermissions> allPermissions;

    private HashMap<StoreableReference, Double> resources;
    private DoubleHashMap<StoreableReference> resourceDemands;

    private ArrayList<StorageNeeds> storageNeeds;
    private HashMap<StoreableReference, DoubleHashMap<String>> resourceLedger;
    private UniversePath path;

    ResourceStockpileArea(GameState gameState) {
        super(gameState);
        allPermissions = new HashMap<>();
        defaultPermissions = new ResourcePermissions(false, false, false);
        resourceLedger = new HashMap<>();
        resourceDemands = new DoubleHashMap<>();
    }

    public ResourcePermissions getPermission(ObjectReference person) {
        if (allPermissions.containsKey(person)) {
            return allPermissions.get(person);
        }
        return defaultPermissions;
    }

    public void addPermission(ObjectReference person, ResourcePermissions permission) {
        allPermissions.put(person, permission);
    }

    @Override
    public void addResourceTypeStore(StoreableReference type) {
        getResources().put(type, 0d);
    }

    @Override
    public Double getResourceAmount(StoreableReference type) {
        return getResources().get(type);
    }

    @Override
    public void addResource(StoreableReference type, Double amount) {
        if (!resources.containsKey(type)) {
            getResources().put(type, 0d);
        }
        getResources().put(type, getResources().get(type) + amount);
        //Add to ledger
        if (getResourceLedger().containsKey(type)) {
            DoubleHashMap<String> resource = getResourceLedger().get(type);
            resource.addValue("added", (amount));
        } else {
            DoubleHashMap<String> resource = new DoubleHashMap<>();
            resource.put("added", amount);
            getResourceLedger().put(type, resource);
        }
    }

    @Override
    public boolean canStore(StoreableReference type) {
        return true;//(resources.containsKey(type));
    }

    @Override
    public StoreableReference[] storedTypes() {
        Iterator<StoreableReference> res = getResources().keySet().iterator();
        StoreableReference[] arr = new StoreableReference[getResources().size()];
        int i = 0;
        while (res.hasNext()) {
            StoreableReference next = res.next();
            arr[i] = next;
            i++;
        }
        return arr;
    }

    @Override
    public boolean removeResource(StoreableReference type, Double amount) {
        //Get the amount in the place
        if (!resources.containsKey(type)) {
            //Remove stuff for now
            //resources.put(type, amount);
            return false;
        }
        Double currentlyStored = getResources().get(type);

        if (amount > currentlyStored) {
            return false;
        }

        getResources().put(type, (currentlyStored - amount));
        //Add to ledger
        if (getResourceLedger().containsKey(type)) {
            DoubleHashMap<String> resource = getResourceLedger().get(type);
            resource.addValue("removed", -amount);
            getResourceLedger().put(type, resource);
        } else {
            DoubleHashMap<String> resource = new DoubleHashMap<>();
            resource.addValue("removed", -amount);
            getResourceLedger().put(type, resource);
        }
        return true;
    }

    @Override
    public void accept(AreaDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    @Override
    public JobType getJobClassification() {
        return JobType.Infrastructure;
    }

    @Override
    public AreaClassification getAreaType() {
        return AreaClassification.Infrastructure;
    }

    @Override
    public boolean hasResource(StoreableReference type) {
        return getResources().containsKey(type);
    }

    /**
     * @return the resources
     */
    public HashMap<StoreableReference, Double> getResources() {
        return resources;
    }

    /**
     * @return the resourceDemands
     */
    public DoubleHashMap<StoreableReference> getResourceDemands() {
        return resourceDemands;
    }

    /**
     * @return the storageNeeds
     */
    public ArrayList<StorageNeeds> getStorageNeeds() {
        return storageNeeds;
    }

    /**
     * @return the resourceLedger
     */
    public HashMap<StoreableReference, DoubleHashMap<String>> getResourceLedger() {
        return resourceLedger;
    }

    /**
     * @return the path
     */
    public UniversePath getPath() {
        return path;
    }

    @Override
    public void preResourceTransfer(StoreableReference type, Double amount, ResourceStockpile toWhere) {
        //Do nothing
    }

    @Override
    public void postResourceTransfer(StoreableReference type, Double amount, ResourceStockpile toWhere) {
        //Do nothing
    }
}
