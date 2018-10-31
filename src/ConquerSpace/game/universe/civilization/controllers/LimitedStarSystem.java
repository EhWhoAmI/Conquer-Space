package ConquerSpace.game.universe.civilization.controllers;

import ConquerSpace.game.universe.GalacticLocation;
import ConquerSpace.game.universe.civilization.VisionTypes;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.Star;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import java.util.ArrayList;

/**
 * Limits the type of vision of a star system.
 * Like, what the person can edit, etc...
 * @author Zyun
 */
public class LimitedStarSystem {
    private StarSystem system;
    private int visionType;

    public LimitedStarSystem(StarSystem system, int visionType) {
        this.system = system;
    }
    
    public int getPlanetCount() {
        switch(visionType) {
            case VisionTypes.EXISTS:
            case VisionTypes.UNDISCOVERED:
                return -1;
            case VisionTypes.KNOWS_ALL:
            case VisionTypes.KNOWS_DETAILS:
            case VisionTypes.KNOWS_INTERIOR:
                return system.getPlanetCount();
            default:
                return -1;
        }
    }
    
    public LimitedPlanet getPlanet(int i) {
        switch(visionType) {
            case VisionTypes.EXISTS:
            case VisionTypes.UNDISCOVERED:
                return null;
            case VisionTypes.KNOWS_ALL:
            case VisionTypes.KNOWS_DETAILS:
            case VisionTypes.KNOWS_INTERIOR:
                return new LimitedPlanet(system.getPlanet(i), visionType);
            default:
                return null;
        }
    }
    
    public int getStarCount() {
        switch(visionType) {
            case VisionTypes.EXISTS:
            case VisionTypes.UNDISCOVERED:
                return -1;
            case VisionTypes.KNOWS_ALL:
            case VisionTypes.KNOWS_DETAILS:
            case VisionTypes.KNOWS_INTERIOR:
                return system.getStarCount();
            default:
                return -1;
        }
    }
    
    public LimitedStar getStar(int i) {
        switch(visionType) {
            case VisionTypes.EXISTS:
            case VisionTypes.UNDISCOVERED:
                return null;
            case VisionTypes.KNOWS_ALL:
            case VisionTypes.KNOWS_DETAILS:
            case VisionTypes.KNOWS_INTERIOR:
                return new LimitedStar(system.getStar(i), visionType);
            default:
                return null;
        }
    }
    public int getID() {
        return system.getId();
    }
    
    public GalacticLocation getGalaticLocation() {
        switch(visionType) {
            case VisionTypes.EXISTS:
            case VisionTypes.UNDISCOVERED:
                return null;
            case VisionTypes.KNOWS_ALL:
            case VisionTypes.KNOWS_DETAILS:
            case VisionTypes.KNOWS_INTERIOR:
                return system.getGalaticLocation();
            default:
                return null;
        }
    }
    
    public int getVisionType() {
        return visionType;
    }
    
    public ArrayList<LimitedStar> getStars() {
        ArrayList<LimitedStar> list = new ArrayList<>();
        switch(visionType) {
            case VisionTypes.EXISTS:
            case VisionTypes.UNDISCOVERED:
                return list;
            case VisionTypes.KNOWS_ALL:
            case VisionTypes.KNOWS_DETAILS:
            case VisionTypes.KNOWS_INTERIOR:
                for (Star star:system.getStars()) {
                    list.add(new LimitedStar(star, visionType));
                }
                return list;
            default:
                return null;
        }
    }
    
    public ArrayList<LimitedPlanet> getPlanets() {
        ArrayList<LimitedPlanet> list = new ArrayList<>();
        switch(visionType) {
            case VisionTypes.EXISTS:
            case VisionTypes.UNDISCOVERED:
                return list;
            case VisionTypes.KNOWS_ALL:
            case VisionTypes.KNOWS_DETAILS:
            case VisionTypes.KNOWS_INTERIOR:
                for (Planet planet:system.getPlanets()) {
                    list.add(new LimitedPlanet(planet, visionType));
                }
                return list;
            default:
                return null;
        }
    }
}