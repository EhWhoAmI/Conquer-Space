package ConquerSpace.game.universe.spaceObjects.pSectors;

import ConquerSpace.game.universe.civilizations.stats.Economy;
import ConquerSpace.game.universe.spaceObjects.SpaceObject;

/**
 * AKA building
 * @author Zyun
 */
public class PlanetSector extends SpaceObject{
    public Economy economy;
    private int id;
    
    public PlanetSector(int id) {
        this.id = id;
        economy = new Economy();
    }
    
    public String toReadableString() {
        return ("");
    }

    public int getId() {
        return id;
    }
}
