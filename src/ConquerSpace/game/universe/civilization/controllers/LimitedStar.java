package ConquerSpace.game.universe.civilization.controllers;

import ConquerSpace.game.universe.civilization.vision.VisionTypes;
import ConquerSpace.game.universe.spaceObjects.Star;

/**
 *
 * @author Zyun
 */
public class LimitedStar {
    private Star star;
    private int visionType;
    
    public LimitedStar(Star star, int visionType) {
        this.star = star;
        this.visionType = visionType;
    }
    
    public int getType() {
        switch(visionType) {
            case VisionTypes.EXISTS:
            case VisionTypes.UNDISCOVERED:
                return -1;
            case VisionTypes.KNOWS_ALL:
            case VisionTypes.KNOWS_DETAILS:
            case VisionTypes.KNOWS_INTERIOR:
                return star.getType();
            default:
                return -1;
        }
    }
    
    public int getStarSize() {
        switch(visionType) {
            case VisionTypes.EXISTS:
            case VisionTypes.UNDISCOVERED:
                return -1;
            case VisionTypes.KNOWS_ALL:
            case VisionTypes.KNOWS_DETAILS:
            case VisionTypes.KNOWS_INTERIOR:
                return star.getStarSize();
            default:
                return -1;
        }
    }
    
    public int getID() {
        return star.getId();
    }
    
    public int getVisionType() {
        return visionType;
    }
}
