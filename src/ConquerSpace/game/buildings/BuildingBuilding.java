package ConquerSpace.game.buildings;

import ConquerSpace.game.universe.Point;
import java.awt.Color;

/**
 *
 * @author zyunl
 */
public class BuildingBuilding extends Building{
    private Building toBuild;
    private Point pt;
    private int length;

    public BuildingBuilding(Building toBuild, Point pt, int length) {
        this.toBuild = toBuild;
        this.pt = pt;
        this.length = length;
    }
    

    public void setToBuild(Building toBuild) {
        this.toBuild = toBuild;
    }

    public Point getPt() {
        return pt;
    }

    public Building getToBuild() {
        return toBuild;
    }

    @Override
    public Color getColor() {
        return Color.GREEN;
    }

    public void setPt(Point pt) {
        this.pt = pt;
    }

    public int getLength() {
        return length;
    }
    
    public void decrementLength(int amount) {
        length -= amount;
    }
}
