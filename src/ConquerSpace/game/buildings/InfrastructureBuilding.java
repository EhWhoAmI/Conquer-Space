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
        return Color.orange;
    }

    @Override
    public String getType() {
        return "Infrastructure Hub";
    }
    
    public void addBuilding(Building b) {
        b.infrastructure.add(this);
        connectedTo.add(b);
    }
}
