/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ConquerSpace.common.actions;

import ConquerSpace.common.GameState;
import ConquerSpace.common.game.ships.Ship;

/**
 *
 * @author EhWhoAmI
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
    public void doAction(GameState gameState) {
        Ship shipObject = gameState.getObject(ship, Ship.class);

        double x = positionX - shipObject.getX();
        double y = positionY - shipObject.getY();

        //Normalize
        double len = Math.sqrt(x * x + y * y);
        if (len > 0) {
            x /= len;
            y /= len;
        }
        //Travel the fraction
        double distance = Math.sqrt(Math.pow(positionX - shipObject.getX(), 2) + Math.pow(positionY - shipObject.getY(), 2));
        double speed = shipObject.getSpeed() / ((double) KM_IN_AU);
        double objX = (x * speed);
        double objY = (y * speed);

        if (Math.sqrt(Math.pow(objX, 2) + Math.pow(objY, 2)) >= distance) {
            objX = positionX;
            objY = positionY;
            shipObject.setX(objX);
            shipObject.setY(objY);
        } else {
            shipObject.translate(objX, objY);
        }
    }

    @Override
    public boolean checkIfDone(GameState gameState) {
        Ship shipObject = gameState.getObject(ship, Ship.class);
        return (shipObject.getX() == positionX && shipObject.getY() == positionY);
    }

    @Override
    public void initAction(GameState gameState) {
    }

    @Override
    public boolean isPossible(GameState gameState) {
        return true;
    }

    public void setStarSystem(int starSystem) {
        this.starSystem = starSystem;
    }

    public int getStarSystem() {
        return starSystem;
    }
}
