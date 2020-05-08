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
package ConquerSpace.game.buildings.area;

import ConquerSpace.game.life.Species;
import ConquerSpace.game.universe.resources.ProductionProcess;

/**
 * A farm field
 *
 * @author EhWhoAmI
 */
public class FarmFieldArea extends TimedManufacturerArea {

    private Species grown;
    private int time;

    public FarmFieldArea(Species grownSpecies) {
        super(new ProductionProcess(grownSpecies.getFoodGood()));
        grown = grownSpecies;
        //Because you can only grow one.
        setLimit(1);
    }

    public Species getGrown() {
        return grown;
    }

    public void setGrown(Species grown) {
        this.grown = grown;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void grow() {
        if (queue.isEmpty()) {
            addQueue(time);
        }
    }

    @Override
    public String toString() {
        return "Farm Area";
    }
    
    public AreaClassification getAreaType() {
        return AreaClassification.Farm;
    }
}
