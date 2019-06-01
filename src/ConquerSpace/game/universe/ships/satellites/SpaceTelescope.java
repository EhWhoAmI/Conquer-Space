package ConquerSpace.game.universe.ships.satellites;

import ConquerSpace.game.universe.Point;
import ConquerSpace.game.universe.civilization.vision.VisionPoint;

/**
 *
 * @author Zyun
 */
public class SpaceTelescope extends Satellite implements VisionPoint{

    private int civilization = -1;
    private int range = 0;
    private Point position;
    public SpaceTelescope(int dist, int mass) {
        super(dist, mass);
    }

    public void setCivilization(int civ) {
        owner = civ;
    }
    
    @Override
    public int getCivilization() {
        return getOwner();
    }

    public void setRange(int range) {
        this.range = range;
    }

    
    @Override
    public int getRange() {
        return range;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public Point getPosition() {
        return position;
    }
}
