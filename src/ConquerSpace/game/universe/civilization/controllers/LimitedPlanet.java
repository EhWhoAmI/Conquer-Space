package ConquerSpace.game.universe.civilization.controllers;

import ConquerSpace.game.universe.civilization.vision.VisionTypes;
import ConquerSpace.game.universe.ships.satellites.Satellite;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.pSectors.PlanetSector;
import java.util.ArrayList;

/**
 *
 * @author Zyun
 */
public class LimitedPlanet {

    private Planet planet;
    private int visionType;

    public LimitedPlanet(Planet planet, int visionType) {
        this.planet = planet;
        this.visionType = visionType;
    }

    public int getID() {
        return planet.getId();
    }

    public int getVisionType() {
        return visionType;
    }

    public ArrayList<LimitedPlanetSector> getPlanetSectors() {
        ArrayList<LimitedPlanetSector> planetSectors = new ArrayList<>();
        switch (visionType) {
            case VisionTypes.EXISTS:
            case VisionTypes.UNDISCOVERED:
                return planetSectors;
            case VisionTypes.KNOWS_ALL:
            case VisionTypes.KNOWS_DETAILS:
            case VisionTypes.KNOWS_INTERIOR:
                for (PlanetSector sector : planet.planetSectors) {
                    planetSectors.add(new LimitedPlanetSector(sector, visionType));
                }
                break;
            default:
                return planetSectors;
        }
        return planetSectors;
    }

    public int getOrbitalDistance() {
        switch (visionType) {
            case VisionTypes.EXISTS:
            case VisionTypes.UNDISCOVERED:
            case VisionTypes.KNOWS_INTERIOR:
                return -1;
            case VisionTypes.KNOWS_ALL:
            case VisionTypes.KNOWS_DETAILS:
                return planet.getOrbitalDistance();
            default:
                return -1;
        }
    }
    
    

    public int getPlanetType() {
        switch (visionType) {
            case VisionTypes.EXISTS:
            case VisionTypes.UNDISCOVERED:
            case VisionTypes.KNOWS_INTERIOR:
                return -1;
            case VisionTypes.KNOWS_ALL:
            case VisionTypes.KNOWS_DETAILS:
                return planet.getPlanetType();
            default:
                return -1;
        }
    }

    public float getPlanetDegrees() {
        switch (visionType) {
            case VisionTypes.EXISTS:
            case VisionTypes.KNOWS_INTERIOR:
            case VisionTypes.KNOWS_ALL:
            case VisionTypes.KNOWS_DETAILS:
                return planet.getPlanetDegrees();
            case VisionTypes.UNDISCOVERED:
            default:
                return -1;
        }
    }
    
    public String getName() {
        switch (visionType) {
            case VisionTypes.EXISTS:
            case VisionTypes.KNOWS_INTERIOR:
            case VisionTypes.KNOWS_ALL:
            case VisionTypes.KNOWS_DETAILS:
                return planet.getName();
            case VisionTypes.UNDISCOVERED:
            default:
                return "";
        }
    }
    
    //This really depends on the interaction with the other civ, but
    //For now, it does not matter.
    public int getOwnerID() {
        switch (visionType) {
            case VisionTypes.EXISTS:
            case VisionTypes.KNOWS_INTERIOR:
            case VisionTypes.KNOWS_ALL:
            case VisionTypes.KNOWS_DETAILS:
                return planet.getOwnerID();
            case VisionTypes.UNDISCOVERED:
            default:
                return -1;
        }
    }
    
    public int getPlanetSize() {
        switch (visionType) {
            case VisionTypes.EXISTS:
            case VisionTypes.KNOWS_INTERIOR:
            case VisionTypes.KNOWS_ALL:
            case VisionTypes.KNOWS_DETAILS:
                return planet.getPlanetSize();
            case VisionTypes.UNDISCOVERED:
            default:
                return -1;
        }
    }
    
    public int getParentStarSystem() {
        return planet.getParentStarSystem();
    }
    
    //Get satellites -- will check if they are visible, one day
    public ArrayList<Satellite> getSatellites() {
        return planet.getSatellites();
    }
    
    //So that it can be converted to build. It is necessary, but will replace it in the future.
    public Planet toPlanet() {
        return planet;
    }
}
