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
package ConquerSpace.game.universe.civilization.stats;

import java.util.ArrayList;

/**
 * Population stats. Everything is in arraylists because we want graphs later.
 * @author Zyun
 */
public class Population extends Stats{
    /**
     * Population.
     */
    public ArrayList<Long> population;
    /**
     * Population growth in a year(4 turns). In percentage
     */
    public ArrayList<Float> populationGrowth;
    /**
     * Happiness of the people -- percentage as in how sastified.
     */
    public ArrayList<Float> happiness;
    /**
     * Births per 1000 people.
     */
    public ArrayList<Float> birthsPer1k;
    /**
     * Deaths per 1000 people.
     */
    public ArrayList<Float> mortalityRate;
    
    public Population() {
        population = new ArrayList<>();
        populationGrowth = new ArrayList<>();
        happiness = new ArrayList<>();
        birthsPer1k = new ArrayList<>();
        mortalityRate = new ArrayList<>();
    }
    
    public long getLastYearsPopulation(int turn) {
//        int year = (int) Math.floor(turn/Globals.YEAR_IN_TURNS);
        return 0; //(population.get(year));
    }
    
    public float getLastYearsbirthsPer1K(int turn) {
//        int year = (int) Math.floor(turn/Globals.YEAR_IN_TURNS);
        return 0;//(birthsPer1k.get(year));
    }
    
    public float getLastYearsHappiness(int turn) {
//        int year = (int) Math.floor(turn/Globals.YEAR_IN_TURNS);
        return 0;//(happiness.get(year));
    }
    
    public float getLastYearsMortalityRate(int turn) {
//        int year = (int) Math.floor(turn/Globals.YEAR_IN_TURNS);
        return 0;//(mortalityRate.get(year));
    }
    
    public float getLastYearsPopulationGrowth(int turn) {
//        int year = (int) Math.floor(turn/Globals.YEAR_IN_TURNS);
        return 0;//(populationGrowth.get(year));
    }
}
