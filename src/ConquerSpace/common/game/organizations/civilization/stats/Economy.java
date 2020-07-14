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
package ConquerSpace.common.game.organizations.civilization.stats;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Economy for a planet or something like that. Everything is in arraylists so that
 * we can refrence them later.
 * @author EhWhoAmI
 */
public class Economy extends Stats{
    //Search wikipedia for those things
    /**
     * GDP.
     */
    public ArrayList<BigInteger> gdp;
    /**
     * Unemployment rate.
     */
    public ArrayList<Float> unemploymentRate;
    /**
     * Amount of money flowing in and out.
     */
    public ArrayList<BigInteger> tradeVolume;
    /**
     * Average wage of all those people.
     */
    public ArrayList<Integer> averageWage;
    /**
     * Number of jobs.
     */
    public ArrayList<Long> jobs;
    
    /**
     * Net worth of the thingy.
     */
    public ArrayList<BigInteger> netWorth;
    
    public Economy() {
        gdp = new ArrayList<>();
        unemploymentRate = new ArrayList<>();
        tradeVolume = new ArrayList<>();
        averageWage = new ArrayList<>();
        jobs = new ArrayList<>();
        netWorth = new ArrayList<>();
    }
    
    public BigInteger getLastYearsgdp(int turn) {
        //Get index
        //int year = (int) Math.floor(turn/Globals.YEAR_IN_TURNS);
        return(BigInteger.ZERO);
    }
    
    public float getLastYearsUnemploymentRate(int turn) {
        //int year = (int) Math.floor(turn/Globals.YEAR_IN_TURNS);
        return (0);
    }
    
    public BigInteger getLastYearsNetWorth(int turn) {
        //int year = (int) Math.floor(turn/Globals.YEAR_IN_TURNS);
        return (BigInteger.ZERO);
    }
    
    public int getLastYearsAverageWage(int turn) {
        //int year = (int) Math.floor(turn/Globals.YEAR_IN_TURNS);
        return (0);
    }
    
    public long getLastYearsJobs(int turn) {
        //int year = (int) Math.floor(turn/Globals.YEAR_IN_TURNS);
        return (0);
    }
    
    public BigInteger getLastYearsTradeVolume(int turn) {
        //int year = (int) Math.floor(turn/Globals.YEAR_IN_TURNS);
        return (BigInteger.ZERO);
    }
}
