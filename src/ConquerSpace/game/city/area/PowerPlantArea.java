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
package ConquerSpace.game.city.area;

import ConquerSpace.game.population.jobs.JobType;
import ConquerSpace.game.resources.Good;

/**
 *
 * @author EhWhoAmI
 */
public class PowerPlantArea extends ConsumerArea {
    //Needs the attribute 'energy'
    private Integer usesResource;
    //Amount of units needed to get each time
    private int maxVolume;
    
    private int currentCapacity;
    
    //In megawatts
    private int production;
    
    @Override
    public String toString() {
        return "Power Plant";
    }

    public Integer getUsedResource() {
        return usesResource;
    }

    public void setUsedResource(Integer usesResource) {
        this.usesResource = usesResource;
    }

    public int getMaxVolume() {
        return maxVolume;
    }

    public void setMaxVolume(int maxVolume) {
        this.maxVolume = maxVolume;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }
       
    @Override
    public JobType getJobClassification() {
        return (JobType.PowerPlantTechnician);
    }
}