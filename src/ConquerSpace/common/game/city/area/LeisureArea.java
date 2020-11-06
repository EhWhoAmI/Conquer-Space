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
 * Adds amenities to a place, where people can enjoy stuff.
 * @author EhWhoAmI
 */
public class LeisureArea extends ConsumerArea{

    public LeisureArea(GameState gameState) {
        super(gameState);
    }

    @Override
    public AreaClassification getAreaType() {
        return AreaClassification.Commercial;
    }

    @Override
    public JobType getJobClassification() {
        return JobType.Leisure;
    }
    
    
}
