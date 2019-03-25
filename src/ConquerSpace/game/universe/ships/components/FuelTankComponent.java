package ConquerSpace.game.universe.ships.components;

/**
 *
 * @author Zyun
 */
public class FuelTankComponent extends ShipComponent{

    public FuelTankComponent(int mass, int cost, String name) {
        super(mass, cost, name);
    }
    
    @Override
    public String getRatingType() {
        return "Storage (kiloliters, or 1000 liters)";
    }
}
