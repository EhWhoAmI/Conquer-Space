package ConquerSpace.game.universe.ships;

import ConquerSpace.game.universe.GalaticLocation;

/**
 *
 * @author Zyun
 */
public class Satellite implements Launchable{
    private GalaticLocation location;
    private int type;

    public Satellite(int type, int distance) {
        this.type = type;
        location = new GalaticLocation(0, distance);
    }
    
    public int getType() {
        return type;
    }
}
