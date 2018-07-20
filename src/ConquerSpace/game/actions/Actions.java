package ConquerSpace.game.actions;

import ConquerSpace.game.tech.Techonology;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.pSectors.BuildingBuilding;
import ConquerSpace.game.universe.spaceObjects.pSectors.PlanetSector;

/**
 * This is like a driver to do all the actions. All methods must be static.
 * @author Zyun
 */
public class Actions {
    /*
     * Hide constructor
     */
    private Actions() {  
    }
    
    /**
     * Builds a building on the planet.
     * @param p planet you want to build on.
     * @param sectorID ID of planet sector
     * @param what What do you want to build?
     * @param owner You the owner
     * @param turns number of months...
     * @return Success or not
     */
    public static boolean buildBuilding(Planet p, int sectorID, PlanetSector what, int owner, int turns) {
        if(p.getOwnerID() == owner) {
            //Pass
            p.planetSectors[sectorID] = new BuildingBuilding(turns, what, p);
            return true;
        } else
            return false;
    }
    
    public static void researchTech(Civilization c, Techonology t) {
        
    }
}
