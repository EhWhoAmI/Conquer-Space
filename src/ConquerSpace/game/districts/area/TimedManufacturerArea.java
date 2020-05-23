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
package ConquerSpace.game.districts.area;

import ConquerSpace.game.universe.resources.ProductionProcess;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Takes in an input, and pukes it out some time later.
 *
 * @author EhWhoAmI
 */
public class TimedManufacturerArea extends Area {    
    /**
     * Limit of number of processes.
     */
    private int limit = 0;
    protected ArrayList<ProductionTimer> queue;
    private ProductionProcess process;
    
    float productivity = 0;

    public TimedManufacturerArea(ProductionProcess process) {
        queue = new ArrayList<>();
        this.process = process;
    }

    public int tick(int delta) {
        for (int i = 0; i < queue.size(); i++) {
            queue.get(i).decrement(delta);
        }
        Iterator<ProductionTimer> it = queue.iterator();
        int removed = 0;
        while (it.hasNext()) {
            ProductionTimer time = it.next();
            if (time.timeLeft <= 0) {
                it.remove();
                removed++;
            }
        }
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

    public static class ProductionTimer {

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
}