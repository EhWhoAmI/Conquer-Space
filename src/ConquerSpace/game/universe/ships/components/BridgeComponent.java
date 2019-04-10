package ConquerSpace.game.universe.ships.components;

/**
 *
 * @author Zyun
 */
public class BridgeComponent extends ControlComponent{
    public BridgeComponent(int mass, int cost, String name) {
        super(mass, cost, name);
    }

    @Override
    public String getSecondaryRatingType() {
        return "Crew";
    }
}
