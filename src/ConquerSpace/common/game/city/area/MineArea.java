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
import ConquerSpace.common.game.population.jobs.JobType;
import ConquerSpace.common.game.resources.StorableReference;
import ConquerSpace.common.save.SerializeClassName;
import java.util.HashMap;

/**
 *
 * @author EhWhoAmI
 */
@SerializeClassName("mine-area")
public class MineArea extends Area {

    private ObjectReference miningStratum;
    private float productivity;
    private HashMap<StorableReference, Double> necessaryGoods;
    private StorableReference resourceMined;

    MineArea(GameState gameState, ObjectReference mining, StorableReference resourceMined, float productivity) {
        super(gameState);
        this.miningStratum = mining;
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

    public ObjectReference getStratumMining() {
        return miningStratum;
    }

    public HashMap<StorableReference, Double> getNecessaryGoods() {
        return necessaryGoods;
    }

    public StorableReference getResourceMinedId() {
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

    @Override
    public void accept(AreaDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }
}
