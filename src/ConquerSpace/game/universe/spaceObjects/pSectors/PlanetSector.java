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
    private int owner = -1;
    
    public PlanetSector(int id, int owner) {
        this.id = id;
        economy = new Economy();
        this.owner = owner;
    }
    
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

    public int getOwner() {
        return owner;
    }
}