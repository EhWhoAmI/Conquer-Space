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
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.resources.StorableReference;
import java.util.HashMap;

/**
 *
 * @author EhWhoAmI
 */
public abstract class AreaFactory {

    private Civilization builder;

    private int currentlyManningJobs = 0;
    private int operatingJobs = 0;
    private int maxJobs = 0;
    private int powerUsage = 0;
    protected int priority = Integer.MAX_VALUE;
    private float workingmultiplier = 1;

    public AreaFactory(Civilization builder) {
        this.builder = builder;
    }

    public HashMap<StorableReference, Double> getCost() {
        if (builder != null) {
            //Add basic construction stuff
            HashMap<StorableReference, Double> constructionCost = new HashMap<>();
            constructionCost.put(builder.taggedGoods.get("structure"), 1000d);
            return constructionCost;
        }
        return new HashMap<>();
    }

    public void setMaxJobs(int maxJobs) {
        this.maxJobs = maxJobs;
    }

    public void setOperatingJobs(int operatingJobs) {
        this.operatingJobs = operatingJobs;
    }

    public void setPowerUsage(int powerUsage) {
        this.powerUsage = powerUsage;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setWorkingmultiplier(float workingmultiplier) {
        this.workingmultiplier = workingmultiplier;
    }
    
    public int buildTime() {
        return 50;
    }

    public abstract Area build(GameState gameState);

    public Area setDefaultInformation(GameState gameState, Area area) {
        area.setOwner(builder.getReference());
        area.setMaxJobs(maxJobs);
        area.setOperatingJobs(operatingJobs);
        area.setPowerUsage(powerUsage);
        area.setWorkingmultiplier(workingmultiplier);
        area.setPriority(priority);
        return area;
    }
}
