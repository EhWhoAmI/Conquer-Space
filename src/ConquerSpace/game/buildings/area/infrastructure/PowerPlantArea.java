package ConquerSpace.game.buildings.area.infrastructure;

import ConquerSpace.game.buildings.area.Area;
import ConquerSpace.game.universe.resources.Resource;

/**
 *
 * @author zyunl
 */
public class PowerPlantArea extends Area{
    //Needs the attribute 'energy'
    private Resource usesResource;
    //Amount of units needed to get each time
    private int maxVolume;
    
    private int currentCapacity;
    
    //In megawatts
    private int production;
    
    @Override
    public String toString() {
        return "Power Plant";
    }

    public Resource getUsedResource() {
        return usesResource;
    }

    public void setUsedResource(Resource usesResource) {
        this.usesResource = usesResource;
    }

    public int getMaxVolume() {
        return maxVolume;
    }

    public void setMaxVolume(int maxVolume) {
        this.maxVolume = maxVolume;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }
}