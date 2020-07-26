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
import ConquerSpace.common.game.life.Species;
import ConquerSpace.common.game.population.jobs.JobType;
import ConquerSpace.common.game.resources.ProductionProcess;
import ConquerSpace.common.save.SerializeClassName;

/**
 * A farm field
 *
 * @author EhWhoAmI
 */
@SerializeClassName("farm-field")
public class FarmFieldArea extends TimedManufacturerArea {

    private Integer grown;
    private int time;
    private int fieldSize;

    public FarmFieldArea(GameState gameState, Species grownSpecies) {
        super(gameState, new ProductionProcess(gameState, grownSpecies.getId()));
        grown = grownSpecies.getId();
        //Because you can only grow one.
        setLimit(1);
    }

    public void setFieldSize(int fieldSize) {
        this.fieldSize = fieldSize;
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public Species getGrown() {
        return gameState.getObject(grown, Species.class);
    }

    public void setGrown(Species grown) {
        this.grown = grown.getId();
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
    
    @Override
    public JobType getJobClassification() {
        return (JobType.Farmer);
    }
}
