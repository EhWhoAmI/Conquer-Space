package ConquerSpace.game.buildings;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author zyunl
 */
public class InfrastructureBuilding extends Building{
    public ArrayList<Building> connectedTo;
    //private int 

    public InfrastructureBuilding() {
        connectedTo = new ArrayList<>();
    }

    @Override
    public Color getColor() {
        return Color.WHITE;
    }

    @Override
    public String getType() {
        return "Infrastructure Hub";
    }
}
