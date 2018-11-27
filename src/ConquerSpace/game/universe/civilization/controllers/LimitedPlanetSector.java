package ConquerSpace.game.universe.civilization.controllers;

import ConquerSpace.game.universe.civilization.vision.VisionTypes;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.spaceObjects.pSectors.BuildingBuilding;
import ConquerSpace.game.universe.spaceObjects.pSectors.PlanetSector;
import ConquerSpace.game.universe.spaceObjects.pSectors.PlanetSectors;
import ConquerSpace.game.universe.spaceObjects.pSectors.PopulationStorage;
import ConquerSpace.game.universe.spaceObjects.pSectors.RawResource;
import ConquerSpace.game.universe.spaceObjects.pSectors.SpacePortBuilding;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Zyun
 */
public class LimitedPlanetSector {

    private PlanetSector sector;
    private int visionType;

    public LimitedPlanetSector(PlanetSector sector, int visionType) {
        this.sector = sector;
        this.visionType = visionType;
    }

    //And all the actions to do to the sector. I think this will have to be extended
    //for other planet sector types. How troublesome.
    public ArrayList<Resource> getResources() {
        ArrayList<Resource> resources = new ArrayList<>();
        switch (visionType) {
            case VisionTypes.EXISTS:
            case VisionTypes.UNDISCOVERED:
            case VisionTypes.KNOWS_INTERIOR:

                return resources;
            case VisionTypes.KNOWS_ALL:
            case VisionTypes.KNOWS_DETAILS:
                return sector.resources;
            default:
                return resources;
        }
    }

    public int getType() {
        switch (visionType) {
            case VisionTypes.EXISTS:
            case VisionTypes.UNDISCOVERED:
            case VisionTypes.KNOWS_INTERIOR:
                return -1;
            case VisionTypes.KNOWS_ALL:
            case VisionTypes.KNOWS_DETAILS:
                //Check the class of the sector and then return it.
                if (sector instanceof RawResource) {
                    return PlanetSectors.PlanetSectorTypes.RAW_RESOURCE;
                }
                if (sector instanceof PopulationStorage) {
                    return PlanetSectors.PlanetSectorTypes.POPULATION_STORAGE;
                }
                if (sector instanceof BuildingBuilding) {
                    return PlanetSectors.PlanetSectorTypes.BUILDING_BUILDING;
                }
                if (sector instanceof SpacePortBuilding) {
                    return PlanetSectors.PlanetSectorTypes.SPACE_PORT_BUILDING;
                }
            default:
                return -1;
        }
    }

    //Get info panel, have to implement it so that it shows limited info, perhaps make a method
    //to parse all the data
    public JPanel getInfoPanel() {
        return sector.getInfoPanel();
    }
}
