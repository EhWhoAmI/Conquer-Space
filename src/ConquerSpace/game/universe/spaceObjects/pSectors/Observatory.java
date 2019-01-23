package ConquerSpace.game.universe.spaceObjects.pSectors;

import ConquerSpace.game.universe.civilization.vision.VisionPoint;

/**
 *
 * @author Zyun
 */
public class Observatory extends PlanetSector implements VisionPoint{

    private int range;
    private int owner = -1;
    public Observatory(int range) {
        this.range = range;
    }
    
    @Override
    public int getRange() {
        return range;
    }

    @Override
    public int getCivilization() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }
}
