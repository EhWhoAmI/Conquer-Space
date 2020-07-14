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
package ConquerSpace.common.game.ships.launch;

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameState;

/**
 * Class of launch vehicle 
 * @author EhWhoAmI
 */
public class LaunchVehicle extends ConquerSpaceGameObject{
    public LaunchSystem systemType;
    public int costPerLaunch;
    public boolean reusability;
    public int reuseCost;
    public String name;
    public float reliability;
    public int maximumMass;

    public LaunchVehicle(GameState gameState) {
        super(gameState);
    }

    public void setCostPerLaunch(int costPerLaunch) {
        this.costPerLaunch = costPerLaunch;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setReliability(float reliability) {
        this.reliability = reliability;
    }

    public void setReusability(boolean reusability) {
        this.reusability = reusability;
    }

    public void setReuseCost(int reuseCost) {
        this.reuseCost = reuseCost;
    }

    public void setSystemType(LaunchSystem systemType) {
        this.systemType = systemType;
    }

    public int getCostPerLaunch() {
        return costPerLaunch;
    }

    public String getName() {
        return name;
    }

    public float getReliability() {
        return reliability;
    }

    public int getReuseCost() {
        return reuseCost;
    }

    public LaunchSystem getSystemType() {
        return systemType;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setMaximumMass(int maximumMass) {
        this.maximumMass = maximumMass;
    }

    public int getMaximumMass() {
        return maximumMass;
    }
}
