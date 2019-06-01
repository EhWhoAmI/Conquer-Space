package ConquerSpace.game.buildings;

import ConquerSpace.game.universe.Point;
import ConquerSpace.game.universe.civilization.vision.VisionPoint;
import java.awt.Color;

/**
 *
 * @author zyunl
 */
public class Observatory extends Building implements VisionPoint {

    private int range;
    private int lensSize;
    private int civ;
    private Point point;

    public Observatory(int range, int lensSize, int civ, Point point) {
        this.range = range;
        this.lensSize = lensSize;
        this.civ = civ;
        this.point = point;
    }

    @Override
    public int getRange() {
        return range;
    }

    @Override
    public Color getColor() {
        return Color.CYAN;
    }

    public int getLensSize() {
        return lensSize;
    }

    @Override
    public int getCivilization() {
        return civ;
    }

    public void setPosition(Point point) {
        this.point = point;
    }

    @Override
    public Point getPosition() {
        return point;
    }
}
