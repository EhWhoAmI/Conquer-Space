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

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameState;
import ConquerSpace.common.save.SerializeClassName;

/**
 *
 * @author EhWhoAmI
 */
@SerializeClassName("engine-technology")
public class EngineTechnology extends ConquerSpaceGameObject{
    
    private String name;

    /**
    * Efficiency: m/s per kg of propellant
    */
    private float efficiency;
    private float thrust_multiplier;

    public EngineTechnology(GameState gameState, String name, float efficiency, float thrust_multiplier) {
        super(gameState);
        this.name = name;
        this.efficiency = efficiency;
        this.thrust_multiplier = thrust_multiplier;
    }

    public float getEfficiency() {
        return efficiency;
    }

    public String getName() {
        return name;
    }

    public float getThrustMultiplier() {
        return thrust_multiplier;
    }

    public void setEfficiency(float efficiency) {
        this.efficiency = efficiency;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThrust_multiplier(float thrust_multiplier) {
        this.thrust_multiplier = thrust_multiplier;
    }

    @Override
    public String toString() {
        return name;
    }
}
