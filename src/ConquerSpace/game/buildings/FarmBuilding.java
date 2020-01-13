package ConquerSpace.game.buildings;

import ConquerSpace.game.GameController;
import ConquerSpace.game.life.LocalLife;
import ConquerSpace.game.population.Job;
import ConquerSpace.game.population.PopulationUnit;
import ConquerSpace.game.population.Workable;
import ConquerSpace.game.universe.resources.farm.Crop;
import java.awt.Color;
import java.util.ArrayList;

/**
 * Farms have 2 types, a crop one and a livestock one, maybe a combined one.
 * We'll see.
 *
 * @author zyunl
 */
public class FarmBuilding extends Building implements PopulationStorage, Workable {

    private FarmType farmType;
    public ArrayList<Crop> crops;
    private int productivity = 0;
    public ArrayList<PopulationUnit> population;
    private int capacity = 0;
    private int maxCapacity = 0;
    private int manPower = 0;
    private int amountFarmed = 0;
    private int harvestersNeeded = 0;
    public ArrayList<Crop> harvestable;

    public FarmBuilding(FarmType ft) {
        farmType = ft;
        population = new ArrayList<>();
        crops = new ArrayList<>();
        harvestable = new ArrayList<>();
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

    @Override
    public void processJob(Job j) {
        //Harvest a little
        //Check if harvest season...
        if (!harvestable.isEmpty()) {
            Crop c = harvestable.remove(0);
            j.resources.put(GameController.foodResource, c.getYield());
            //Regrow
            c.setTimeLeft(25);
        }
    }

    public int getManpower() {
        return manPower;
    }

    public void setManpower(int manPower) {
        this.manPower = manPower;
    }

    @Override
    public String getType() {
        return "Farm";
    }

    public void setManPower(int manPower) {
        this.manPower = manPower;
    }

    public void setAmountFarmed(int amountFarmed) {
        this.amountFarmed = amountFarmed;
    }

    public int getAmountFarmed() {
        return amountFarmed;
    }

    public void setHarvestersNeeded(int harvestersNeeded) {
        this.harvestersNeeded = harvestersNeeded;
    }

    public int getHarvestersNeeded() {
        return harvestersNeeded;
    }
}
