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
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.ships.Ship;
import ConquerSpace.common.game.ships.SpaceShip;
import ConquerSpace.common.game.universe.bodies.Planet;

/**
 *
 * @author EhWhoAmI
 */
public class ShipSurveyAction extends ShipAction {

    //Progress
    private int progress = 0;
    private int finishedProgress;
    private int perTick;

    private ObjectReference civReference = ObjectReference.INVALID_REFERENCE;

    private ObjectReference toSurvey = ObjectReference.INVALID_REFERENCE;

    public ShipSurveyAction(SpaceShip ship) {
        super(ship);
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public ObjectReference getCivReference() {
        return civReference;
    }

    public void setCivReference(ObjectReference civReference) {
        this.civReference = civReference;
    }

    public void setFinishedProgress(int finishedProgress) {
        this.finishedProgress = finishedProgress;
    }

    public int getFinishedProgress() {
        return finishedProgress;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgressPerTick(int perTick) {
        this.perTick = perTick;
    }

    public int geProgresstPerTick() {
        return perTick;
    }

    public void setToSurvey(Planet toSurvey) {
        this.toSurvey = toSurvey.getReference();
    }

    public ObjectReference getToSurvey() {
        return toSurvey;
    }

    @Override
    public void doAction(GameState gameState) {
        Ship shipObject = gameState.getObject(ship, Ship.class);
        Planet toScan = gameState.getObject(toSurvey, Planet.class);
        //Check if orbiting planet...
        if (shipObject.isOrbiting() && shipObject.getOrbiting().equals(toScan.getUniversePath())) {
            //Subtract
            progress += perTick;
            if (checkIfDone(gameState)) {
                //Add to the planet
                toScan.scan(civReference);
            }
        }
    }

    @Override
    public boolean checkIfDone(GameState gameState) {
        return progress >= finishedProgress;
    }
}
