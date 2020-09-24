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
import ConquerSpace.common.game.resources.ProductionProcess;
import ConquerSpace.common.save.SerializeClassName;

/**
 * Resources in and out, quickly.
 *
 * @author EhWhoAmI
 */
@SerializeClassName("manufacturer-area")
public class ManufacturerArea extends Area {
    protected ProductionProcess process;
    //How much of that production process per round...
    float productivity;
    
    private boolean producedLastTick;

    ManufacturerArea(GameState gameState, ProductionProcess process, float productivity) {
        super(gameState);
        this.producedLastTick = false;
        this.process = process;
        this.productivity = productivity;
    }

    public ProductionProcess getProcess() {
        return process;
    }

    public float getProductivity() {
        return productivity;
    }

    public void setProductivity(float productivity) {
        this.productivity = productivity;
    }

    @Override
    public String toString() {
        return process.name + " Factory";
    }

    @Override
    public AreaClassification getAreaType() {
        return AreaClassification.Manufacturing;
    }
    
    @Override
    public JobType getJobClassification() {
        return (JobType.FactoryWorker);
    }

    public boolean producedLastTick() {
        return producedLastTick;
    }

    public void setProducedLastTick(boolean producedLastTick) {
        this.producedLastTick = producedLastTick;
    }
}
