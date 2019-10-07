package ConquerSpace.game.buildings;

import ConquerSpace.game.life.LocalLife;
import ConquerSpace.game.population.PopulationUnit;
import java.awt.Color;
import java.util.ArrayList;

/**
 * Farms have 2 types, a crop one and a livestock one, maybe a combined one. We'll see.
 * @author zyunl
 */
public class FarmBuilding extends Building implements PopulationStorage{
    private FarmType farmType;
    public ArrayList<LocalLife> farmCreatures;
    private int productivity = 0;
    public ArrayList<PopulationUnit> population;
    private int capacity = 0;
    private int maxCapacity = 0;
    
    public FarmBuilding(FarmType ft) {
        farmType = ft;
        farmCreatures = new ArrayList<>();
        population = new ArrayList<>();
    }

    @Override
    public ArrayList<PopulationUnit> getPopulationArrayList() {
        return population;
    }

    @Override
    public int getMaxStorage() {
        return 1;
    }
    
    public static enum FarmType {
        Livestock, 
        Crop;
    }

    public int getProductivity() {
        return productivity;
    }

    public void setProductivity(int productivity) {
        this.productivity = productivity;
    }

    public FarmType getFarmType() {
        return farmType;
    }

    public void setFarmType(FarmType farmType) {
        this.farmType = farmType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int maxCapacity) {
        this.capacity = maxCapacity;
    }
    
     @Override
    public Color getColor() {
        return Color.GREEN;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }
}
