package ConquerSpace.game.buildings;

import ConquerSpace.game.population.Job;
import ConquerSpace.game.population.PopulationUnit;
import ConquerSpace.game.population.Workable;
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
}
