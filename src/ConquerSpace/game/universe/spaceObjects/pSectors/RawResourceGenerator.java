package ConquerSpace.game.universe.spaceObjects.pSectors;

import ConquerSpace.game.buildings.Buildable;

/**
 * Placed on a raw resource, mines raw resources.
 * @author Zyun
 */
public class RawResourceGenerator extends IndustryBuilding implements Buildable{
    private int resourceMined;
    private int amountMinedPerTurn;
    
    public RawResourceGenerator(int resourceMined, int diffulcity, int amountMinedPerTurn) {
        this.resourceMined = resourceMined;
        this.amountMinedPerTurn = amountMinedPerTurn;
    }

    public int getResourceMined() {
        return resourceMined;
    }

    public int getAmountMinedPerTurn() {
        return amountMinedPerTurn;
    }
}
