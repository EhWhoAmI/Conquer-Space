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
import ConquerSpace.common.game.population.jobs.JobType;
import ConquerSpace.common.game.resources.StoreableReference;
import ConquerSpace.common.save.SerializeClassName;

/**
 *
 * @author EhWhoAmI
 */
@SerializeClassName("power-plant-area")
public class PowerPlantArea extends ConsumerArea {

    //Needs the attribute 'energy'
    private StoreableReference usesResource;
    //Amount of units needed to get each time
    private int maxVolume;

    private int currentCapacity;

    //In megawatts
    private int production;

    PowerPlantArea(GameState gameState) {
        super(gameState);
    }

    @Override
    public String toString() {
        return "Power Plant";
    }

    /**
     * Resources needed to generate power.
     *
     * @return
     */
    public StoreableReference getUsedResource() {
        return usesResource;
    }

    public void setUsedResource(StoreableReference usesResource) {
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

    /**
     * Production: max amount can generate.
     *
     * @return
     */
    public int getProduction() {
        return production;
    }

    public void setProduction(int production) {
        this.production = production;
    }

    @Override
    public void accept(AreaDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    @Override
    public AreaClassification getAreaType() {
        return AreaClassification.Infrastructure;
    }
}
