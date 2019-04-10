package ConquerSpace.game.universe.ships.components;

/**
 *
 * @author Zyun
 */
public class ControlComponent extends ShipComponent{
    
    public ControlComponent(int mass, int cost, String name) {
        super(mass, cost, name);
    }
    
    @Override
    public String getRatingType() {
        return "Control Power";
    }
}
