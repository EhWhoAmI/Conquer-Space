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

import ConquerSpace.common.ConstantStarDate;
import ConquerSpace.common.GameState;
import ConquerSpace.common.game.population.jobs.JobType;
import ConquerSpace.common.game.resources.ProductionProcess;
import ConquerSpace.common.game.resources.ResourceStockpile;
import ConquerSpace.common.game.resources.StoreableReference;
import ConquerSpace.common.save.SerializeClassName;

/**
 * Resources in and out, quickly.
 *
 * @author EhWhoAmI
 */
@SerializeClassName("manufacturer-area")
public class ManufacturerArea extends Area implements ResourceStockpile {

    protected ProductionProcess process;
    //How much of that production process per round...
    float productivity;


    private ConstantStarDate lastStarDateExtracted;

    ManufacturerArea(GameState gameState, ProductionProcess process, float productivity) {
        super(gameState);
        this.process = process;
        this.productivity = productivity;
        lastStarDateExtracted = new ConstantStarDate(0);
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

    @Override
    public void accept(AreaDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    @Override
    public void addResourceTypeStore(StoreableReference type) {
        //Ignore lul
    }

    @Override
    public Double getResourceAmount(StoreableReference type) {
        if (process.getOutput().containsKey(type)) {
            return process.getOutput().get(type) * (double) (gameState.date.getDate() - lastStarDateExtracted.getDate());
        }
        return 0d;
    }

    @Override
    public void addResource(StoreableReference type, Double amount) {
        //Fills up the resources or something, ignore for now
    }

    @Override
    public boolean canStore(StoreableReference type) {
        //If in recipe, then ok
        return process.getInput().containsKey(type);
    }

    @Override
    public boolean hasResource(StoreableReference type) {
        return process.getOutput().containsKey(type);
    }

    @Override
    public StoreableReference[] storedTypes() {
        return process.getOutput().keySet().toArray(new StoreableReference[0]);
    }

    @Override
    public boolean removeResource(StoreableReference type, Double amount) {
        //Check for the last time it produced, and remove all resources that are generated. For now, because most recipes only have one output in general, we should be good, for now
        if (process.getInput().containsKey(type) && amount <= getResourceAmount(type)) {
            lastStarDateExtracted = gameState.date.getConstantDate();
            return true;
        }
        return false;
    }

    @Override
    public void preResourceTransfer(StoreableReference type, Double amount, ResourceStockpile toWhere) {
        //Do nothing
    }

    @Override
    public void postResourceTransfer(StoreableReference type, Double amount, ResourceStockpile toWhere) {
        //Do nothing
    }
}
