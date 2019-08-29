package ConquerSpace.game.buildings;

import ConquerSpace.game.population.PopulationUnit;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.resources.ResourceVein;
import java.awt.Color;
import java.util.ArrayList;

/**
 * More like a miner.
 * Desc: A district dedicated to the mining of a resource.
 * @author zyunl
 */
public class ResourceMinerDistrict extends Building implements PopulationStorage {
    
    private ResourceVein veinMining;
    private double amount;
    private Resource resourceMining;
    private int maxStorage;
    /*
    The population of the area...
    */
    public ArrayList<PopulationUnit> population;

    public ResourceMinerDistrict(ResourceVein vein, double amount) {
        this.veinMining = vein;
        if (vein != null) {
            resourceMining = vein.getResourceType();
        }
        this.amount = amount;
        population = new ArrayList<>();
    }

    public ResourceVein getVeinMining() {
        return veinMining;
    }

    public void setVeinMining(ResourceVein veinMining) {
        if (veinMining != null) {
            resourceMining = veinMining.getResourceType();
        }
        this.veinMining = veinMining;
    }

    public double getAmountMined() {
        return amount;
    }

    public Resource getResourceMining() {
        return resourceMining;
    }

    @Override
    public Color getColor() {
        return Color.YELLOW;
    }

    @Override
    public ArrayList<PopulationUnit> getPopulationArrayList() {
        return population;
    }

    @Override
    public int getMaxStorage() {
        return maxStorage;
    }
}
