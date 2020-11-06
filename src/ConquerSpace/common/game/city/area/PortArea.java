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

/**
 * Area that allows for the transport of goods
 *
 * @author EhWhoAmI
 */
public class PortArea extends Area {

    /**
     * Amount of tons of goods able to ship per month or something.
     */
    int volume;

    public PortArea(GameState gameState) {
        super(gameState);
    }

    public int getVolume() {
        return volume;
    }

    @Override
    public void accept(AreaDispatcher dispatcher) {
        dispatcher.dispatch(this);
    }

    @Override
    public AreaClassification getAreaType() {
        return AreaClassification.Transport;
    }

    @Override
    public JobType getJobClassification() {
        return JobType.Transport;
    }
}
