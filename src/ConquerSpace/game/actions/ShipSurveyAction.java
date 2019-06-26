package ConquerSpace.game.actions;

import ConquerSpace.game.universe.ships.SpaceShip;
import ConquerSpace.game.universe.spaceObjects.Planet;

/**
 *
 * @author zyunl
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
