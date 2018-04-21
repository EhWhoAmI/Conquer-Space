package ConquerSpace.game.universe.civilizations.stats;

import java.util.ArrayList;

/**
 *
 * @author Zyun
 */
public class Population {
    public ArrayList<Long> population;
    public ArrayList<Integer> populationGrowth;
    public ArrayList<Byte> happiness;

    public Population() {
        population = new ArrayList<>();
        populationGrowth = new ArrayList<>();
        happiness = new ArrayList<>();
    }
    
}
