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

/**
 *
 * @author EhWhoAmI
 */
public class PowerPlantAreaFactory extends AreaFactory {
    
    private StorableReference usesResource;
    //Amount of units needed to get each time
    private int maxVolume;
    
    private int currentCapacity;

    //In megawatts
    private int production;
    
    public PowerPlantAreaFactory(Civilization builder) {
        super(builder);
    }
    
    public void setMaxVolume(int maxVolume) {
        this.maxVolume = maxVolume;
    }
    
    public void setProduction(int production) {
        this.production = production;
    }
    
    public void setUsesResource(StorableReference usesResource) {
        this.usesResource = usesResource;
    }
    
    public int getMaxVolume() {
        return maxVolume;
    }
    
    public int getProduction() {
        return production;
    }
    
    public StorableReference getUsesResource() {
        return usesResource;
    }
    
    @Override
    public Area build(GameState gameState) {
        PowerPlantArea area = new PowerPlantArea(gameState);
        area.setProduction(production);
        area.setMaxVolume(maxVolume);
        area.setUsedResource(usesResource);
        setDefaultInformation(gameState, area);
        return area;
    }
}
