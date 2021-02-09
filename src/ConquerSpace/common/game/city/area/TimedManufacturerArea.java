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
import ConquerSpace.common.util.DoubleHashMap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Takes in an input, and pukes it out some time later.
 *
 * @author EhWhoAmI
 */
@SerializeClassName("timed-manufacturer-area")
public class TimedManufacturerArea extends Area implements ResourceStockpile {

    /**
     * Limit of number of processes.
     */
    private int limit = 0;
    protected ArrayList<ProductionTimer> queue;
    private ProductionProcess process;

    private DoubleHashMap<StoreableReference> resources;
    private ConstantStarDate lastTick;

    TimedManufacturerArea(GameState gameState, ProductionProcess process) {
        super(gameState);
        queue = new ArrayList<>();
        resources = new DoubleHashMap<>();
        this.process = process;
        lastTick = gameState.date.getConstantDate();
    }

    public int tick() {
        //Get difference
        long change = gameState.date.getDate() - lastTick.getDate();
        for (int i = 0; i < queue.size(); i++) {
            queue.get(i).decrement((int) change);
        }
        Iterator<ProductionTimer> it = queue.iterator();
        int removed = 0;
        while (it.hasNext()) {
            ProductionTimer time = it.next();
            if (time.timeLeft <= 0) {
                it.remove();
                //Add the resources
                process.getOutput().keySet().stream().forEach(key -> {
                    resources.addValue(key, process.getOutput().get(key));
                });
                removed++;
            }
        }
        lastTick = gameState.date.getConstantDate();
        return removed;
    }

    public void addQueue(int time) {
        queue.add(new ProductionTimer(time));
    }

    public ProductionProcess getProcess() {
        return process;
    }

    public ArrayList<ProductionTimer> getQueue() {
        return queue;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public void addResourceTypeStore(StoreableReference type) {
        //Do nothing
    }

    @Override
    public Double getResourceAmount(StoreableReference type) {
        return resources.get(type);
    }

    @Override
    public void addResource(StoreableReference type, Double amount) {
    }

    @Override
    public boolean canStore(StoreableReference type) {
        return true;
    }

    @Override
    public boolean hasResource(StoreableReference type) {
        return resources.containsKey(type);
    }

    @Override
    public StoreableReference[] storedTypes() {
        return resources.keySet().toArray(new StoreableReference[0]);
    }

    @Override
    public boolean removeResource(StoreableReference type, Double amount) {
        if (resources.containsKey(type) && resources.get(type) > amount) {
            //Check if it's enough and add. 
            resources.addValue(type, -amount);

        }
        return false;
    }

    public static class ProductionTimer implements Serializable {

        int timeLeft;

        public ProductionTimer(int timeLeft) {
            this.timeLeft = timeLeft;
        }

        void decrement(int gameTickSpeed) {
            timeLeft -= gameTickSpeed;
        }

        public int getTimeLeft() {
            return timeLeft;
        }

        @Override
        public String toString() {
            return Integer.toString(timeLeft);
        }
    }

    @Override
    public String toString() {
        return process.name + " Factory";
    }

    @Override
    public void accept(AreaDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    @Override
    public AreaClassification getAreaType() {
        return AreaClassification.Manufacturing;
    }

    @Override
    public JobType getJobClassification() {
        return (JobType.FactoryWorker);
    }
}
