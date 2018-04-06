package ConquerSpace.game.ui.renderers;

import java.awt.Color;
import java.awt.Point;

/**
 *
 * @author Zyun
 */
public class StarDrawStats {
    private int id;
    private Point pos;
    private int diameter;
    private Color color;
    
    public StarDrawStats(int id, Point pos, int diameter, Color color) {
        this.id = id;
        this.pos = pos;
        this.diameter = diameter;
        this.color = color;
    }

    public int getDiameter() {
        return diameter;
    }

    public int getId() {
        return id;
    }

    public Point getPos() {
        return pos;
    }

    public Color getColor() {
        return color;
    }
    
}
