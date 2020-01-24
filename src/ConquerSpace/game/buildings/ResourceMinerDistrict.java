package ConquerSpace.game.buildings;

import ConquerSpace.game.population.Job;
import ConquerSpace.game.population.PopulationUnit;
import ConquerSpace.game.population.Workable;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.resources.ResourceVein;
import java.awt.Color;
import java.util.ArrayList;

/**
 * More like a miner. Desc: A district dedicated to the mining of a resource.
 *
 * @author zyunl
 */
public class ResourceMinerDistrict extends Building implements PopulationStorage, Workable {

    /**
     * Maximum jobs in this district.
     */
    private int scale;
    private ResourceVein veinMining;
    private double amount;
    private Resource resourceMining;
    private int maxStorage;
    private City city;
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

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getScale() {
        return scale;
    }

    public double miningPerMonth() {
        return (getAmountMined() / getScale());
    }

    @Override
    public void processJob(Job j) {
        //Is mining job, now subtract the stuff..
        //subtract from resource vein
        veinMining.removeResources((int)miningPerMonth());
        j.resources.put(resourceMining, (int)miningPerMonth());
    }

    @Override
    public String getType() {
        return "Mining District";
    }

    @Override
    public City getCity() {
        return city;
    }
    
    public void setCity(City city) {
        this.city = city;
    }
}
