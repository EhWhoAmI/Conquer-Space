package ConquerSpace.game.universe.ships.components;

/**
 *
 * @author Zyun
 */
public class EngineComponent extends ShipComponent{

    public EngineComponent() {
        super(0, 0, null);
    }

    @Override
    public String getRatingType() {
        return "Thrust (mn)";
    }
}
