package ConquerSpace.game.universe.ships.components;

/**
 *
 * @author Zyun
 */
public class ProbeCoreComponent extends ControlComponent {
    
    public ProbeCoreComponent(int mass, int cost, String name) {
        super(mass, cost, name);
    }

    public String getSecondaryRatingType() {
        return "Control range";
    }
}
