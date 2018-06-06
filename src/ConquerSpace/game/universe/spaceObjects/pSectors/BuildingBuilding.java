package ConquerSpace.game.universe.spaceObjects.pSectors;

import javax.swing.JPanel;

/**
 *
 * @author Zyun
 */
public class BuildingBuilding extends PlanetSector{
    private int turns;
    private PlanetSector sector;
    public BuildingBuilding(int id, int turns, PlanetSector sector, int owner) {
        super(id, owner);
        this.turns = turns;
        this.sector = sector;
    }

    public PlanetSector getSector() {
        return sector;
    }

    public int getTurns() {
        return turns;
    }

    @Override
    public void processTurn(int turn) {
        turns--;
        if(turns == 0) {
            //Replace self with sector
        }
    }
}
