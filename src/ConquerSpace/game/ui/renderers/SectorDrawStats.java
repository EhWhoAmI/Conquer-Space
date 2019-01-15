package ConquerSpace.game.ui.renderers;

import java.awt.Point;
import java.util.ArrayList;

/**
 *
 * @author Zyun
 */
public class SectorDrawStats {

    private Point pos;
    private int radius;
    public ArrayList<SystemDrawStats> systems;
    private int id;

    public SectorDrawStats(Point pos, int circumference, int id) {
        this.pos = pos;
        this.id = id;
        this.radius = circumference;
        this.systems = new ArrayList<>();
    }

    public int getRadius() {
        return radius;
    }

    public Point getPosition() {
        return pos;
    }

    public void addSystemStats(SystemDrawStats s) {
        systems.add(s);
    }

    public int getId() {
        return id;
    }
}
