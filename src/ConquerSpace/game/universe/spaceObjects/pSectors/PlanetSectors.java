package ConquerSpace.game.universe.spaceObjects.pSectors;

import ConquerSpace.game.GameController;
import javax.script.Invocable;
import javax.script.ScriptException;

/**
 *
 * @author Zyun
 */
public class PlanetSectors {
    public static class PlanetSectorTypes {
        public static final int BUILDING_BUILDING = 0;
        public static final int POPULATION_STORAGE = 1;
        public static final int SPACE_PORT_BUILDING = 2;
        public static final int RAW_RESOURCE = 3;
    }
    
    public static int getPlanetSectorClass(PlanetSector sector) {
        if(sector instanceof BuildingBuilding) {
            //Building
            return PlanetSectorTypes.BUILDING_BUILDING;
        } else if (sector instanceof PopulationStorage) {
            //Population Storage
            return PlanetSectorTypes.POPULATION_STORAGE;
        } else if (sector instanceof SpacePortBuilding) {
            //Space port
            return PlanetSectorTypes.SPACE_PORT_BUILDING;
        } else if (sector instanceof RawResource) {
            //Raw resource
            return PlanetSectorTypes.RAW_RESOURCE;
        }
        return -1;
    }
    
    public static final int calculatePopulationStorageCost(int population) {
        try {
           Integer value = ((Double)((Invocable)GameController.pythonEngine).invokeFunction("", population)).intValue();
           return value;
        } catch (ScriptException ex) {
        } catch (NoSuchMethodException ex) {
        }
        return 0;
    }
}
