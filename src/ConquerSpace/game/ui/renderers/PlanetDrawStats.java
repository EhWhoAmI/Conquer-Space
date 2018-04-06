package ConquerSpace.game.ui.renderers;

import java.awt.Color;
import java.awt.Point;

/**
 *
 * @author Zyun
 */
public class PlanetDrawStats {
    private int id;
    private Point pos;
    private Color color;

    public PlanetDrawStats(int id, Point pos, Color color) {
        this.id = id;
        this.pos = pos;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public Point getPos() {
        return pos;
    }
    
    
}
