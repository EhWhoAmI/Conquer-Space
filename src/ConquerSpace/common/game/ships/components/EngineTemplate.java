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
import ConquerSpace.common.save.SerializeClassName;

/**
 *
 * @author EhWhoAmI
 */
@SerializeClassName("engine-template")
public class EngineTemplate extends ShipComponentTemplate{
    
    private int thrust;
    private Integer engineTechnology;
    //Fuel

    public EngineTemplate(GameState gameState) {
        super(gameState);
    }
    
    public EngineTemplate(GameState gameState, int mass, int cost, String name) {
        super(gameState, mass, cost, name);
    }

    public void setThrust(int thrust) {
        this.thrust = thrust;
    }

    public int getThrust() {
        return thrust;
    }

    public EngineTechnology getEngineTechnology() {
        return gameState.getObject(engineTechnology, EngineTechnology.class);
    }

    public void setEngineTechnology(EngineTechnology engineTechnology) {
        this.engineTechnology = engineTechnology.getId();
    }

    @Override
    public ShipComponentTypes getType() {
        return ShipComponentTypes.Engine;
    }
}
