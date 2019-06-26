package ConquerSpace.game.actions;

import ConquerSpace.game.universe.ships.Ship;
import ConquerSpace.game.universe.ships.SpaceShip;

/**
 *
 * @author zyunl
 */
public class ShipAction {

    protected SpaceShip ship;
    
    protected boolean done;
    

    public ShipAction(SpaceShip ship) {
        this.ship = ship;
    }

    public void doAction() {
    }
    
    public boolean checkIfDone() {
        return true;
    }
}
