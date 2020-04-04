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
package ConquerSpace.game.buildings;

import ConquerSpace.game.population.jobs.Job;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.resources.Good;
import ConquerSpace.game.universe.resources.StorageNeeds;
import ConquerSpace.game.universe.resources.ResourceStockpile;
import ConquerSpace.game.universe.bodies.Planet;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author EhWhoAmI
 */
public class ResourceStorage extends Building implements ResourceStockpile {

    private int upkeep;
    private int maximumStorage;

    private HashMap<Good, Double> resources;
    public ArrayList<StorageNeeds> needs;
    private int system;
    private int planet;

    public ResourceStorage(Planet parent) {
        resources = new HashMap<>();
        upkeep = 0;
        planet = parent.getId();
        system = parent.getParentStarSystem();
        maximumStorage = 0;
    }

    @Override
    public void addResourceTypeStore(Good type) {
        resources.put(type, 0d);
    }

    public boolean getHasResource(int type) {
        return resources.containsKey(type);
    }

    @Override
    public Double getResourceAmount(Good type) {
        return resources.get(type);
    }

    @Override
    public void addResource(Good type, Double amount) {
        if(!resources.containsKey(type)) {
            resources.put(type, 0d);
        }
        resources.put(type, resources.get(type) + amount);
    }

    public void setUpkeep(int upkeep) {
        this.upkeep = upkeep;
    }

    public int getUpkeep() {
        return upkeep;
    }

    @Override
    public UniversePath getUniversePath() {
        return new UniversePath(system, planet);
    }

    @Override
    public boolean canStore(Good type) {
        return true;//(resources.containsKey(type));
    }

    @Override
    public Good[] storedTypes() {
        Iterator<Good> res = resources.keySet().iterator();
        Good[] arr = new Good[resources.size()];
        int i = 0;
        while (res.hasNext()) {
            Good next = res.next();
            arr[i] = next;
            i++;
        }
        return arr;
    }

    @Override
    public Color getColor() {
        return Color.CYAN;
    }

    @Override
    public boolean removeResource(Good type, Double amount) {
        //Get the amount in the place
        Double currentlyStored = resources.get(type);
        if(amount > currentlyStored)
            return false;
        
        resources.put(type, currentlyStored-amount);
        return true;
    }

    public int getMaximumStorage() {
        return maximumStorage;
    }

    public void setMaximumStorage(int maximumStorage) {
        this.maximumStorage = maximumStorage;
    }
    
    @Override
    public String getType() {
        return "Resource Storage";
    }
    
    @Override
    public Job[] jobsNeeded() {
        return new Job[0];
    }
}
