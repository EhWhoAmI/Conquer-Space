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

import ConquerSpace.game.jobs.Job;
import ConquerSpace.game.population.PopulationUnit;
import ConquerSpace.game.jobs.Workable;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author zyunl
 */
public class CityDistrict extends Building implements PopulationStorage, Workable{
    private int maxStorage;
    public ArrayList<PopulationUnit> population;

    public CityDistrict() {
        population = new ArrayList<>();
    }

    public Color getColor() {
        return Color.BLUE;
    }

    public int getPopulations() {
        return population.size();
    }

    public int getMaxStorage() {
        return maxStorage;
    }

    public void setMaxStorage(int maxStorage) {
        this.maxStorage = maxStorage;
    }

    @Override
    public ArrayList<PopulationUnit> getPopulationArrayList() {
        return population;
    }

    @Override
    public void processJob(Job j) {
    }

    @Override
    public String getType() {
        return "City District";
    }
}
