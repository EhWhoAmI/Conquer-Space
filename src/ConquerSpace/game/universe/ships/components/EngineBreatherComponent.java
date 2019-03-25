package ConquerSpace.game.universe.ships.components;

/**
 *
 * @author Zyun
 */
public class EngineBreatherComponent extends ShipComponent{
    
    public EngineBreatherComponent(int mass, int cost, String name) {
        super(mass, cost, name);
    }
    
    @Override
    public String getRatingType() {
        return "Intake";
    }
}
