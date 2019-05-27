package ConquerSpace.game.universe.ships.launch;

import ConquerSpace.game.universe.ships.Launchable;

/**
 * Used for building a rocket and launching it. It will remain full throughout
 * the duration.
 * @author Zyun
 */
public class SpacePortLaunchPad {
    public int ticks;
    private Launchable launching = null;
    private LaunchSystem type;

    public SpacePortLaunchPad(LaunchSystem type) {
        this.type = type;
    }
    
    public void beginLaunch(Launchable launch, int ticks) {
        launching = launch;
        this.ticks = ticks;
    }
    
    public boolean isLaunching() {
        if(launching != null) {
            return true;
        }
        return false;
    }

    public LaunchSystem getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.getName();
    }
}
