package ConquerSpace.game.universe.civilizations.stats;

import ConquerSpace.Globals;
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
        int year = (int) Math.floor(turn/Globals.YEAR_IN_TURNS);
        return (population.get(year));
    }
    
    public float getLastYearsbirthsPer1K(int turn) {
        int year = (int) Math.floor(turn/Globals.YEAR_IN_TURNS);
        return (birthsPer1k.get(year));
    }
    
    public float getLastYearsHappiness(int turn) {
        int year = (int) Math.floor(turn/Globals.YEAR_IN_TURNS);
        return (happiness.get(year));
    }
    
    public float getLastYearsMortalityRate(int turn) {
        int year = (int) Math.floor(turn/Globals.YEAR_IN_TURNS);
        return (mortalityRate.get(year));
    }
    
    public float getLastYearsPopulationGrowth(int turn) {
        int year = (int) Math.floor(turn/Globals.YEAR_IN_TURNS);
        return (populationGrowth.get(year));
    }
}
