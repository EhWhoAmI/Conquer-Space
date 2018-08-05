package ConquerSpace.game.universe.spaceObjects.pSectors;

/**
 *
 * @author Zyun
 */
public class LaunchPadTypes {
    public static final int ROCKET = 0;
    public static final int SPACE_PLANE = 1;
    
    public static String getLaunchPadTypeName(int name) {
        switch(name) {
            case ROCKET:
                return "rocket";
            case SPACE_PLANE:
                return "space plane";
        }
        return "";
    }
}