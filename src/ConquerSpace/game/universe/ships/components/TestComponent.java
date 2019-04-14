package ConquerSpace.game.universe.ships.components;

/**
 * A component used for testing ships.
 * @author Zyun
 */
public class TestComponent extends ShipComponent{
    public TestComponent() {
        super(0, 0, "Test");
    }

    @Override
    public String getRatingType() {
        return "Testing Value";
    }

    @Override
    public int getRating() {
        return 0;
    }

    @Override
    public Object clone() {
        TestComponent ts =  new TestComponent();
        ts.mass = mass;
        ts.rating = rating;
        ts.cost = cost;
        ts.name = name;
        ts.id = id;
        return ts;
    }
}
