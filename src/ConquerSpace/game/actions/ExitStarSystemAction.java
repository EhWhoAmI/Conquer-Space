package ConquerSpace.game.actions;

import ConquerSpace.Globals;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.ships.Ship;
import ConquerSpace.game.universe.spaceObjects.StarSystem;

/**
 *
 * @author
 */
public class ExitStarSystemAction extends ShipAction {

    private int starSystem;
    boolean done = false;

    public ExitStarSystemAction(Ship s) {
        super(s);
    }

    @Override
    public void doAction() {
        //Get out of star system
        int id = ship.getLocation().getSystemID();
        if (id > -1) {
            StarSystem sys = Globals.universe.getStarSystem(id);
            //Set location
            ship.setLocation(new UniversePath());

            sys.spaceShips.remove(ship);
            ship.setX(sys.getX());
            ship.setY(sys.getY());
            if (!Globals.universe.spaceShips.contains(ship)) {
                Globals.universe.spaceShips.add(ship);
            }
        }
        done = true;
    }

    @Override
    public void initAction() {

    }

    @Override
    public boolean checkIfDone() {
        return done;
    }
}
