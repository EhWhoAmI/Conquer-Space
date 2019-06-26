package ConquerSpace.game.actions;

import ConquerSpace.Globals;
import ConquerSpace.game.universe.Point;
import ConquerSpace.game.universe.ships.Ship;
import ConquerSpace.game.universe.spaceObjects.Planet;

/**
 *
 * @author zyunl
 */
public class ToOrbitAction extends ShipAction{
    
    private Planet position;
    public ToOrbitAction(Ship ship) {
        super(ship);
    }

    public void setPlanet(Planet position) {
        this.position = position;
        if (ship.isOrbiting()) {
            //Exit orbit
            if (Globals.universe.getSpaceObject(ship.getOrbiting()) instanceof Planet) {
                Planet p = (Planet) Globals.universe.getSpaceObject(ship.getOrbiting());
                //Remove from orbit
                p.getSatellites().remove(ship);

                ship.setX(p.getX());
                ship.setY(p.getY());
                ship.setIsOrbiting(false);

                //Add
                Globals.universe.getStarSystem(p.getParentStarSystem()).addSpaceShip(ship);
            }
        }
        ship.setGoingToX(position.getX());
        ship.setGoingToY(position.getY());
    }

    @Override
    public void doAction() {
        double x = ship.getGoingToX() - ship.getX();
        double y = ship.getGoingToY() - ship.getY();

        //Normalize
        double len = Math.sqrt(x * x + y * y);
        if (len > 0) {
            x /= len;
            y /= len;
        }
        double distance = Math.sqrt(Math.pow(ship.getGoingToX() - ship.getX(), 2) + Math.pow(ship.getGoingToY() - ship.getY(), 2));
        double objX = (x * ship.getSpeed());
        double objY = (y * ship.getSpeed());
        if (Math.sqrt(Math.pow(objX + ship.getX() - ship.getX(), 2) + Math.pow(objY + ship.getY() - ship.getY(), 2)) >= distance) {
            objX = ship.getGoingToX();
            objY = ship.getGoingToY();
            ship.setX((long) objX);
            ship.setY((long) objY);
            //Enter orbit
            ship.setIsOrbiting(true);
            ship.setLocation(position.getUniversePath());
            //Remove...
            Globals.universe.getStarSystem(position.getParentStarSystem()).spaceShips.remove(ship);
            position.putShipInOrbit(ship);
        } else {
            ship.translate((long) (objX), (long) (objY));
        }
    }

    @Override
    public boolean checkIfDone() {
        return (ship.getX() == ship.getGoingToX() && ship.getY() == ship.getGoingToY() && ship.getOrbiting().equals(position.getUniversePath()));
    }
}
