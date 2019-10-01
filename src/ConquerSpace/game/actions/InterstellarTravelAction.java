package ConquerSpace.game.actions;

import ConquerSpace.game.universe.ships.Ship;

/**
 *
 * @author zyunl
 */
public class InterstellarTravelAction extends ShipAction {

    private double positionX;
    private double positionY;
    private int starSystem;
    private static final int KM_IN_AU = 149_598_000;

    private boolean done = false;

    public InterstellarTravelAction(Ship ship) {
        super(ship);
    }

    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
    }

    @Override
    public void doAction() {

        double x = positionX - ship.getX();
        double y = positionY - ship.getY();

        //Normalize
        double len = Math.sqrt(x * x + y * y);
        if (len > 0) {
            x /= len;
            y /= len;
        }
        //Travel the fraction
        double distance = Math.sqrt(Math.pow(positionX - ship.getX(), 2) + Math.pow(positionY - ship.getY(), 2));
        double speed = ship.getSpeed() / ((double) KM_IN_AU);
        double objX = (x * speed);
        double objY = (y * speed);

        if (Math.sqrt(Math.pow(objX, 2) + Math.pow(objY, 2)) >= distance) {
            objX = positionX;
            objY = positionY;
            ship.setX(objX);
            ship.setY(objY);
        } else {
            ship.translate(objX, objY);
        }
    }

    @Override
    public boolean checkIfDone() {
        return (ship.getX() == positionX && ship.getY() == positionY);
    }

    @Override
    public void initAction() {
    }

    public void setStarSystem(int starSystem) {
        this.starSystem = starSystem;
    }

    public int getStarSystem() {
        return starSystem;
    }
}
