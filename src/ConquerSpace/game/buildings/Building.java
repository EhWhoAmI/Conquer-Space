package ConquerSpace.game.buildings;

import ConquerSpace.game.buildings.area.Area;
import java.awt.Color;
import java.util.ArrayList;

/**
 * A building is defined as a series of points
 * @author zyunl
 */
public class Building {
    private Color color;
    public ArrayList<Area> areas;
    

    public Building() {
        areas = new ArrayList<>();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}