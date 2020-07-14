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
import ConquerSpace.common.game.science.ScienceLab;
import java.util.HashMap;

/**
 * For science labs and stuff
 */
public class ResearchArea extends ConsumerArea implements ScienceLab {

    private String name;

    public HashMap<String, Integer> focusFields;

    public ResearchArea(GameState gameState) {
        super(gameState);
        focusFields = new HashMap<>();
    }

    @Override
    public String toString() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public HashMap<String, Integer> scienceProvided() {
        return focusFields;
    }

    @Override
    public JobType getJobClassification() {
        return (JobType.Educator);
    }
}
