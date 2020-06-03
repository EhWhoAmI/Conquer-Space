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
package ConquerSpace.game.population;

import ConquerSpace.game.StarDate;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author EhWhoAmI
 */
public class Population {

    public final ArrayList<PopulationSegment> populations;

    private long populationSize = 0;

    public Population() {
        populations = new ArrayList<>();
    }

    public PopulationSegment getSegment(int id) {
        return populations.get(id);
    }

    public void addSegment(PopulationSegment seg) {
        populations.add(seg);
    }

    public long getPopulationSize() {
        populationSize = 0;
        for (PopulationSegment seg : populations) {
            populationSize += seg.size;
        }
        return populationSize;
    }

    public Iterator<PopulationSegment> getIterator() {
        return populations.iterator();
    }

    public void incrementPopulation(StarDate date, long delta) {
        for (PopulationSegment seg : populations) {
            double fraction = ((double) delta) / 10000d;
            seg.size = (long) ((double) seg.size * ((1 + seg.populationIncrease * fraction)));
        }
    }
}
