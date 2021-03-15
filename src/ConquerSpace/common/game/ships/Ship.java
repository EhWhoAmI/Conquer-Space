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
package ConquerSpace.common.game.ships;

import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.resources.ResourceStockpile;
import ConquerSpace.common.game.resources.StoreableReference;
import ConquerSpace.common.game.universe.UniversePath;
import ConquerSpace.common.game.universe.Vector;
import ConquerSpace.common.save.SerializeClassName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author EhWhoAmI
 */
@SerializeClassName("ship")
public class Ship extends SpaceShip implements Launchable, ResourceStockpile {

    String shipClassName;
    ShipType shipType;
    private ObjectReference hull;
    public ArrayList<ObjectReference> components;

    private ObjectReference shipClass;
    public ArrayList<ShipCapability> shipCapabilities;
    HashMap<StoreableReference, Double> resources;

    public Ship(GameState gameState, ShipClass shipClass, double X, double Y, Vector v, UniversePath location) {
        super(gameState);
        this.X = X;
        this.Y = Y;
        goingToX = X;
        goingToY = Y;
        this.v = v;
        this.location = location;
        this.shipClass = shipClass.getReference();
        //Set ship's id

        components = new ArrayList<>();
        //Get components
        if (!shipClass.components.isEmpty()) {
            components.addAll(shipClass.components);
        }

        shipCapabilities = new ArrayList<>();
        shipCapabilities.addAll(shipClass.capabilities);
        this.hull = shipClass.getHull();
        this.shipType = gameState.getObject(hull, Hull.class).getShipType();
        this.mass = shipClass.getMass();
        this.shipClassName = shipClass.getName();
        resources = new HashMap<>();
    }

    @Override
    public UniversePath getLocation() {
        return location;
    }

    @Override
    public Vector getVector() {
        return v;
    }

    public void setVector(Vector v) {
        this.v = v;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        if (getName().isEmpty()) {
            return "ship";
        }
        return (getName());
    }

    @Override
    public UniversePath getOrbiting() {
        return location;
    }

    public ObjectReference getHull() {
        return hull;
    }

    public String getShipClassName() {
        return shipClassName;
    }

    public ObjectReference getShipClass() {
        return shipClass;
    }

    @Override
    public long getSpeed() {
        return estimatedThrust / mass;
    }

    public ShipType getShipType() {
        return shipType;
    }

    @Override
    public void addResourceTypeStore(StoreableReference type) {
        resources.put(type, 0d);
    }

    @Override
    public Double getResourceAmount(StoreableReference type) {
        return resources.get(type);
    }

    @Override
    public void addResource(StoreableReference type, Double amount) {
        if (!resources.containsKey(type)) {
            resources.put(type, 0d);
        }
        resources.put(type, resources.get(type) + amount);
    }

    @Override
    public boolean canStore(StoreableReference type) {
        return true;
    }

    @Override
    public StoreableReference[] storedTypes() {
        Iterator<StoreableReference> res = resources.keySet().iterator();
        StoreableReference[] arr = new StoreableReference[resources.size()];
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
        Double currentlyStored = resources.get(type);

        if (amount > currentlyStored) {
            return false;
        }

        resources.put(type, (currentlyStored - amount));
        return true;
    }

    @Override
    public boolean hasResource(StoreableReference type) {
        return resources.containsKey(type);
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
