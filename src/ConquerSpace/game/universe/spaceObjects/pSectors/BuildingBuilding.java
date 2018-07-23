package ConquerSpace.game.universe.spaceObjects.pSectors;

import ConquerSpace.Globals;
import ConquerSpace.game.actions.Alert;
import ConquerSpace.game.universe.spaceObjects.Planet;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Zyun
 */
public class BuildingBuilding extends PlanetSector{
    private int turns;
    private PlanetSector sector;
    private Planet parent;
    private int owner;
    
    public BuildingBuilding(int turns, PlanetSector sector, Planet parent, int owner) {
        this.turns = turns;
        this.sector = sector;
        this.parent = parent;
        this.owner = owner;
    }

    public PlanetSector getSector() {
        return sector;
    }

    public int getTurns() {
        return turns;
    }

    @Override
    public void processTurn() {
        turns--;
        if(turns == 0) {
            //Replace self with sector
            parent.planetSectors[sector.getId()] = sector;
            Globals.universe.getCivilization(owner).controller.alert(new Alert(0, 0, "Sector finished building"));
        }
    }

    @Override
    public JPanel getInfoPanel() {
        JPanel root = new JPanel();
        JLabel tofinish = new JLabel("Finishing in " + turns + " turns");
        root.add(tofinish);
        return root;
    }
    
}
