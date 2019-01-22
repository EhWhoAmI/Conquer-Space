package ConquerSpace.gui.renderers;

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
    private String owner;
    private String name = "";
    private Color ownerColor;
    
    //NOTE: planet size has to be exxagurated in order to see the planet.
    //Space is too dang big!
    private int size;
    
    public PlanetDrawStats(int id, Point pos, Color color, int orbitPath, int size, String owner, Color ownColor, String name) {
        this.id = id;
        this.pos = pos;
        this.color = color;
        this.orbitPath = orbitPath;
        this.size = size;
        this.owner = owner;
        this.ownerColor = ownColor;
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public int getID() {
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

    public String getOwner() {
        return owner;
    }

    public Color getOwnerColor() {
        return ownerColor;
    }
    
    public String getName() {
        return name;
    }
}