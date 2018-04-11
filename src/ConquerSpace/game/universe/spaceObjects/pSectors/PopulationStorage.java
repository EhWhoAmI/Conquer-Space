package ConquerSpace.game.universe.spaceObjects.pSectors;

/**
 * AKA apartments
 * @author Zyun
 */
public class PopulationStorage extends PlanetSector{
    private int maxStorage;
    private int currentStorage;
    private byte happiness;

    public PopulationStorage(int maxStorage, int currentStorage, byte happiness, int id) {
        super(id);
        this.maxStorage = maxStorage;
        this.currentStorage = currentStorage;
        this.happiness = happiness;
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
