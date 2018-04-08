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
    //Orbit is the distance from the center (radius)
    private int orbitPath;
    
    //NOTE: planet size has to be exzagurated in order to see the planet.
    //Space is too dang big!
    private int size;
    public PlanetDrawStats(int id, Point pos, Color color, int orbitPath, int size) {
        this.id = id;
        this.pos = pos;
        this.color = color;
        this.orbitPath = orbitPath;
        this.size = size;
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

    public int getOrbitPath() {
        return orbitPath;
    }

    public int getSize() {
        return size;
    }
}
