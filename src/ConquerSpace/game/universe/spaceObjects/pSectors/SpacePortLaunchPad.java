package ConquerSpace.game.universe.spaceObjects.pSectors;

import ConquerSpace.game.universe.ships.Launchable;

/**
 * Used for building a rocket and launching it. It will remain full throughout
 * the duration.
 * @author Zyun
 */
public class SpacePortLaunchPad {
    private int launchTurn;
    private Launchable launching;
    private int type;

    public SpacePortLaunchPad(int launchTurn, Launchable launching, int type) {
        this.launchTurn = launchTurn;
        this.launching = launching;
        this.type = type;
    }
    
    
}
