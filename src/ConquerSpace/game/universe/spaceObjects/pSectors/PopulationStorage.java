package ConquerSpace.game.universe.spaceObjects.pSectors;

import ConquerSpace.game.universe.civilizations.stats.Economy;
import ConquerSpace.game.universe.civilizations.stats.Population;

/**
 * AKA apartments
 * @author Zyun
 */
public class PopulationStorage extends PlanetSector{
    private int maxStorage;
    private int currentStorage;
    private byte happiness;
    public Population pop;

    public PopulationStorage(int maxStorage, int currentStorage, byte happiness, int id) {
        super(id);
        this.maxStorage = maxStorage;
        this.currentStorage = currentStorage;
        this.happiness = happiness;
        pop = new Population();
        economy = new Economy();
    }

    public byte getHappiness() {
        return happiness;
    }
    
    public int getMaxPopulation() {
        return maxStorage;
    }
    
    public int getCurrentPopulation() {
        return currentStorage;
    }
    
    public void setCurrentPopulation(int pop) {
        currentStorage = pop;
    }
    
    public void setHappiness(byte happiness) {
        this.happiness = happiness;
    }
}
