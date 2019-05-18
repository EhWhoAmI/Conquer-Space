package ConquerSpace.game.universe.spaceObjects.pSectors;

import ConquerSpace.Globals;
import ConquerSpace.game.StarDate;
import ConquerSpace.game.actions.Alert;
import ConquerSpace.game.universe.spaceObjects.Planet;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Zyun
 */
//Note: this uses Globals.universe
public class BuildingBuilding extends PlanetSector{
    private int ticks;
    private PlanetSector sector;
    private Planet parent;
    private int owner;
    
    public BuildingBuilding(int turns, PlanetSector sector, Planet parent, int owner, int sectorid) {
        this.ticks = turns;
        this.sector = sector;
        this.parent = parent;
        this.owner = owner;
        this.id = sectorid;
    }

    public PlanetSector getSector() {
        return sector;
    }

    public int getTicks() {
        return ticks;
    }

    @Override
    public JPanel getInfoPanel() {
        JPanel root = new JPanel();
        JLabel tofinish = new JLabel("Finishing in " + (ticks/720) + " months");
        root.add(tofinish);
        return root;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }
    
    public void incrementTick(int ticks) {
        this.ticks += ticks;
    }
    
    public void decrementTick(int ticks) {
        this.ticks -= ticks;
    }
}
