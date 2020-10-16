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
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.universe.bodies.Planet;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This builds more one of a kind stuff, such as ship engines, etc.
 *
 * @author EhWhoAmI
 */
public class CustomComponentFactoryManufacturerArea extends Area {

    private ArrayList<Production> productionList;

    public CustomComponentFactoryManufacturerArea(GameState gameState) {
        super(gameState);
        productionList = new ArrayList<>();
    }

    public ArrayList<ObjectReference> tick(int delta) {
        for (int i = 0; i < productionList.size(); i++) {
            productionList.get(i).decrement(delta);
        }
        Iterator<Production> it = productionList.iterator();
        ArrayList<ObjectReference> completedJobs = new ArrayList<>();
        while (it.hasNext()) {
            Production prod = it.next();
            if (prod.time <= 0) {
                completedJobs.add(prod.process);
                it.remove();
            }
        }
        return completedJobs;
    }

    public void addQueue(ObjectReference process, int time) {
        productionList.add(new Production(process, time));
    }

    public ArrayList<Production> getProductionList() {
        return productionList;
    }

    @Override
    public AreaClassification getAreaType() {
        return AreaClassification.Manufacturing;
    }

    @Override
    public void accept(AreaDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    private class Production implements Serializable {

        ObjectReference process;
        int time;

        public Production(ObjectReference process, int time) {
            this.process = process;
            this.time = time;
        }

        public void decrement(int delta) {
            time -= delta;
        }
    }
}
