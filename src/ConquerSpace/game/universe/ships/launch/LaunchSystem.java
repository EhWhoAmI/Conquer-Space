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
package ConquerSpace.game.universe.ships.launch;

import ConquerSpace.game.tech.Technology;

/**
 *
 * @author Zyun
 */
public class LaunchSystem {

    private String name;
    private Technology tech;
    private int size;
    private int safety;
    private int launchCost;
    private boolean reusability;
    private int reusabilityCost;
    private int maxCargo;
    private int constructCost;
    private int id;
    
    private static int idcounter = 0;

    /**
     * For reusable launch system.
     *
     * @param name
     * @param tech
     * @param size
     * @param safety
     * @param cost
     * @param constructCost
     * @param reusabilityCost
     * @param maxCargo
     */
    public LaunchSystem(String name, Technology tech, int size, int safety, int cost, int constructCost, int reusabilityCost, int maxCargo) {
        this.name = name;
        this.tech = tech;
        this.size = size;
        this.safety = safety;
        this.launchCost = cost;
        this.constructCost = constructCost;
        this.reusability = true;
        this.reusabilityCost = reusabilityCost;
        this.maxCargo = maxCargo;
        id = idcounter++;
    }

    public LaunchSystem(String name, Technology tech, int size, int safety, int cost, int constructCost, int maxCargo) {
        this.name = name;
        this.tech = tech;
        this.size = size;
        this.safety = safety;
        this.launchCost = cost;
        this.constructCost = constructCost;
        this.reusability = false;
        this.maxCargo = maxCargo;
        id = idcounter++;
    }

    
    public int getLaunchCost() {
        return launchCost;
    }

    public int getMaxCargo() {
        return maxCargo;
    }

    public String getName() {
        return name;
    }

    public int getReusabilityCost() {
        return reusabilityCost;
    }

    public int getSafety() {
        return safety;
    }

    public int getSize() {
        return size;
    }

    public Technology getTech() {
        return tech;
    }

    public int getConstructCost() {
        return constructCost;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
