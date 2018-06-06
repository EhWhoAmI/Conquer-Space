package ConquerSpace.game.ui.renderers;

import ConquerSpace.game.UniversePath;
import java.awt.Color;
import java.awt.Point;

/**
 *
 * @author Zyun
 */
public class ControlDrawStats {

    private Point pos;
    private Color color;
    private UniversePath path;

    public ControlDrawStats(Point pos, Color color, UniversePath path) {
        this.pos = pos;
        this.color = color;
        this.path = path;
    }

    public Point getPos() {
        return pos;
    }

    public Color getColor() {
        return color;
    }

    public int getSectorId() {
        return path.getSectorID();
    }

    public UniversePath getUniversePath() {
        return path;
    }
}
