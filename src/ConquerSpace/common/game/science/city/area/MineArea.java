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
package ConquerSpace.common.game.science.city.area;

import ConquerSpace.server.GameController;
import ConquerSpace.common.game.population.jobs.JobType;
import ConquerSpace.common.game.resources.Good;
import ConquerSpace.common.game.resources.Stratum;
import java.util.HashMap;

/**
 *
 * @author EhWhoAmI
 */
public class MineArea extends Area {

    private Stratum mining;
    private float productivity;
    private HashMap<Integer, Double> necessaryGoods;
    private Integer resourceMined;

    public MineArea(Stratum mining, Integer resourceMined, float productivity) {
        this.mining = mining;
        this.productivity = productivity;
        this.resourceMined = resourceMined;
        necessaryGoods = new HashMap<>();
    }

    @Override
    public AreaClassification getAreaType() {
        return AreaClassification.Mine;
    }

    public float getProductivity() {
        return productivity;
    }

    public Stratum getMining() {
        return mining;
    }

    public HashMap<Integer, Double> getNecessaryGoods() {
        return necessaryGoods;
    }
    
    public Integer getResourceMinedId() {
        return resourceMined;
    }

    @Override
    public String toString() {
        return "Mine";
    }

    @Override
    public JobType getJobClassification() {
        return (JobType.Miner);
    }
}
