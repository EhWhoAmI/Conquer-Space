package ConquerSpace.game.universe.spaceObjects.pSectors;

import ConquerSpace.game.universe.ships.Launchable;

/**
 * Used for building a rocket and launching it. It will remain full throughout
 * the duration.
 * @author Zyun
 */
public class SpacePortLaunchPad {
    private int launchTurn;
    private Launchable launching = null;
    private int type;

    public SpacePortLaunchPad(int type) {
        this.type = type;
    }
    
    public void beginLaunch(Launchable launch, int launchTurn) {
        launching = launch;
        this.launchTurn = launchTurn;
    }
    
    public boolean isLaunching() {
        if(launching != null) {
            return true;
        }
        return false;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return LaunchPadTypes.getLaunchPadTypeName(type);
    }

    
}
