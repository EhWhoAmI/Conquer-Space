package ConquerSpace.game.universe.ships.components;

/**
 *
 * @author Zyun
 */
public class ScienceComponent extends ShipComponent{
    private int scienceContribution = 0;
    
    public ScienceComponent(int mass, int cost, String name) {
        super(mass, cost, name);
    }

    @Override
    public Object clone() {
        ScienceComponent sc =  new ScienceComponent(mass, cost, name);
        sc.scienceContribution = scienceContribution;
        return sc;
    }
    
    
}
