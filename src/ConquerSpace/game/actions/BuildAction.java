package ConquerSpace.game.actions;

import ConquerSpace.game.UniversePath;
import ConquerSpace.game.buildings.Buildable;
import ConquerSpace.game.universe.spaceObjects.pSectors.PlanetSector;

/**
 *
 * @author Zyun
 */
public class BuildAction extends Action{
    private Buildable building;
    private int turns;

    public BuildAction(Buildable building, int turns, UniversePath selected) {
        super(selected);
        this.building = building;
        this.turns = turns;
    }

    public Buildable getBuilding() {
        return building;
    }

    public int getTurns() {
        return turns;
    }
    
    public void processTurn(int turn) {
        turns --;
        if (turns == 0) {
            //
        }
    }
    
}
