package ConquerSpace.game.buildings;

import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.resources.ResourceVein;
import java.awt.Color;

/**
 *
 * @author zyunl
 */
public class ResourceGatherer extends Building {

    private ResourceVein veinMining;
    private double amount;
    private Resource resourceMining;

    public ResourceGatherer(ResourceVein vein, double amount) {
        this.veinMining = vein;
        if (vein != null) {
            resourceMining = vein.getResourceType();
        }
        this.amount = amount;
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
    
    
}
