package ConquerSpace.game.universe.spaceObjects.pSectors;

import ConquerSpace.game.universe.civilizations.stats.Economy;

/**
 * AKA building
 * @author Zyun
 */
public class PlanetSector {
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
