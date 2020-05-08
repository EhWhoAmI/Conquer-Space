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
package ConquerSpace.game.buildings;

import ConquerSpace.game.population.jobs.Job;
import ConquerSpace.game.population.PopulationUnit;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author EhWhoAmI
 */
public class IndustrialDistrict extends District implements PopulationStorage {

    @Override
    public Color getColor() {
        return new Color(192, 52, 235); 
    }

    public ArrayList<PopulationUnit> population;

    public IndustrialDistrict() {
        population = new ArrayList<>();
    }

    @Override
    public ArrayList<PopulationUnit> getPopulationArrayList() {
        return population;
    }

    @Override
    public void processJob(Job j) {
    }

    @Override
    public int getMaxStorage() {
        return population.size();
    }

    @Override
    public String getType() {
        return "Industrial district";
    }
    
    @Override
    public Job[] jobsNeeded() {
        return new Job[0];
    }
}
