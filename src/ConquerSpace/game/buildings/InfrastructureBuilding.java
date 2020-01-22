package ConquerSpace.game.buildings;

import java.awt.Color;

/**
 *
 * @author zyunl
 */
public class InfrastructureBuilding extends Building{
    private City connectedTo = null;
    //private int 

    public InfrastructureBuilding() {
    }

    @Override
    public Color getColor() {
        return Color.WHITE;
    }

    public City getConnectedTo() {
        return connectedTo;
    }

    public void setConnectedTo(City connectedTo) {
        this.connectedTo = connectedTo;
        connectedTo.infrastructure.add(this);
    }

    @Override
    public String getType() {
        return "Infrastructure Hub";
    }
}
