package ConquerSpace.game.universe.ships;

import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.Vector;

/**
 * A space travelling object.
 * @author Zyun
 */
public interface SpaceShip {
    public UniversePath getLocation();
    public long getX();
    public long getY();
    public Vector getVector();
}
