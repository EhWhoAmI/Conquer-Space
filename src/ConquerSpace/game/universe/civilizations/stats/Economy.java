package ConquerSpace.game.universe.civilizations.stats;

import java.util.ArrayList;

/**
 * Economy for a planet or something like that. Everything is in arraylists so that
 * we can refrence them later.
 * @author Zyun
 */
public class Economy {
    //Search wikipedia for those things
    /**
     * GDP.
     */
    public ArrayList<Long> gdp;
    /**
     * Unemployment rate.
     */
    public ArrayList<Float> unemploymentRate;
    /**
     * Amount of money flowing in and out.
     */
    public ArrayList<Long> tradeVolume;
    /**
     * Average wage of all those people.
     */
    public ArrayList<Integer> averageWage;
    /**
     * Number of jobs.
     */
    public ArrayList<Long> jobs;

    public Economy() {
        gdp = new ArrayList<>();
        unemploymentRate = new ArrayList<>();
        tradeVolume = new ArrayList<>();
        averageWage = new ArrayList<>();
        jobs = new ArrayList<>();
    }
    
    
}
