package ConquerSpace.game.universe.spaceObjects.pSectors;

import ConquerSpace.game.StarDate;
import ConquerSpace.game.buildings.Buildable;
import ConquerSpace.game.universe.civilization.stats.Economy;
import ConquerSpace.game.universe.civilization.stats.Population;

/**
 * AKA apartments
 *
 * @author Zyun
 */
public class PopulationStorage extends PlanetSector  implements Buildable{

    private long maxStorage;
    private long currentStorage;
    private byte happiness;
    public Population pop;

    public PopulationStorage(long maxStorage, long currentStorage, byte happiness) {
        this.maxStorage = maxStorage;
        this.currentStorage = currentStorage;
        this.happiness = happiness;
        pop = new Population();
        economy = new Economy();
    }
    
    /**
     * Compatability purposes
     * @param maxStorage
     * @param currentStorage
     * @param happiness 
     */
    public PopulationStorage(Long maxStorage, Long currentStorage, Byte happiness) {
        this.maxStorage = maxStorage;
        this.currentStorage = currentStorage;
        this.happiness = happiness;
        pop = new Population();
        economy = new Economy();
    }


    public byte getHappiness() {
        return happiness;
    }

    public long getMaxPopulation() {
        return maxStorage;
    }

    public long getCurrentPopulation() {
        return currentStorage;
    }

    public void setCurrentPopulation(int pop) {
        currentStorage = pop;
    }

    public void setHappiness(byte happiness) {
        this.happiness = happiness;
    }

}
