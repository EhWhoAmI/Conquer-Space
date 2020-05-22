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
package ConquerSpace.game.districts;

import ConquerSpace.game.universe.resources.Good;
import ConquerSpace.game.universe.resources.StorageNeeds;
import ConquerSpace.game.universe.resources.ResourceStockpile;
import ConquerSpace.game.universe.bodies.Planet;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author EhWhoAmI
 */
public class ResourceStorage extends District implements ResourceStockpile {

    private int upkeep;
    private int maximumStorage;

    private HashMap<Good, Double> resources;
    public ArrayList<StorageNeeds> needs;
    private int system;
    private int planet;

    public ResourceStorage(Planet parent) {
        resources = new HashMap<>();
        upkeep = 0;
        planet = parent.getID();
        system = parent.getParentStarSystem();
        maximumStorage = 0;
    }

    public boolean getHasResource(int type) {
        return resources.containsKey(type);
    }

    public void setUpkeep(int upkeep) {
        this.upkeep = upkeep;
    }

    public int getUpkeep() {
        return upkeep;
    }

    public int getMaximumStorage() {
        return maximumStorage;
    }

    public void setMaximumStorage(int maximumStorage) {
        this.maximumStorage = maximumStorage;
    }
}
