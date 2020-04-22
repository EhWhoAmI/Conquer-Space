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
package ConquerSpace.game.actions;

import ConquerSpace.game.ships.SpaceShip;
import ConquerSpace.game.universe.bodies.Planet;

/**
 *
 * @author EhWhoAmI
 */
public class ShipSurveyAction extends ShipAction{
    //Progress
    private int progress = 0;
    private int finishedProgress;
    private int perTick;
    
    private int civID = -1;
    
    private Planet toSurvey;

    public ShipSurveyAction(SpaceShip ship) {
        super(ship);
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getCivID() {
        return civID;
    }

    public void setCivID(int civID) {
        this.civID = civID;
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
        this.toSurvey = toSurvey;
    }

    public Planet getToSurvey() {
        return toSurvey;
    }
    
    @Override
    public void doAction() {
        //Check if orbiting planet...
        if (ship.isOrbiting() && ship.getOrbiting().equals(toSurvey.getUniversePath())) {
            //Subtract
            progress+=perTick;
            if(checkIfDone()) {
                //Add to the planet
                toSurvey.scanned.add(civID);
            }
        }
    }

    @Override
    public boolean checkIfDone() {
        return progress>=finishedProgress;
    }
}
