package ConquerSpace.gui.renderers;

import ConquerSpace.game.UniversePath;
import java.awt.Color;
import java.awt.Point;

/**
 *
 * @author Zyun
 */
public class SystemDrawStats {

    private Point pos;
    private Color color;
    private int id;
    private UniversePath path;

    public SystemDrawStats(Point pos, Color color, int id, UniversePath path) {
        this.pos = pos;
        this.color = color;
        this.id = id;
        this.path = path;
    }

    public Point getPosition() {
        return pos;
    }

    public Color getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public UniversePath getPath() {
        return path;
    }
}
