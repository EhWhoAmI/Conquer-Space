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
import ConquerSpace.common.ObjectReference;

/**
 *
 * @author EhWhoAmI
 */
public class EngineComponent extends ShipComponent{
    private ObjectReference engineType;
    
    /**
     * Efficiency in mass of fuel per kilonewton of thrust generated, 
     * which means that this would also be subtracted per tick.
     */
    private double efficiency;
    
    /**
     * Maximum thrust in kn.
     */
    private double thrust;
    
    /**
     * Fuel type.
     */
    private int fuel;
    public EngineComponent(GameState gameState) {
        super(gameState);
    }

    public void setEngineType(ObjectReference engineType) {
        this.engineType = engineType;
    }

    public ObjectReference getEngineType() {
        return engineType;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int fuel) {
        this.fuel = fuel;
    }

    public double getThrust() {
        return thrust;
    }

    public void setThrust(double thrust) {
        this.thrust = thrust;
    }
    
    @Override
    public ShipComponentType getShipComponentType() {
        return ShipComponentType.Engine;
    }
}
