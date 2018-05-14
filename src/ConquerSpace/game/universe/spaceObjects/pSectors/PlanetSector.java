package ConquerSpace.game.universe.spaceObjects.pSectors;

import ConquerSpace.Globals;
import ConquerSpace.game.universe.civilizations.stats.Economy;
import ConquerSpace.game.universe.spaceObjects.SpaceObject;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * AKA building
 * @author Zyun
 */
public class PlanetSector extends SpaceObject{
    public Economy economy;
    private int id;
    private int owner = -1;
    
    public PlanetSector(int id, int owner) {
        this.id = id;
        economy = new Economy();
        this.owner = owner;
    }
    
    public PlanetSector(int id) {
        this.id = id;
        economy = new Economy();
    }
    
    public String toReadableString() {
        return ("");
    }

    public int getId() {
        return id;
    }

    public int getOwner() {
        return owner;
    }
    
    //Creates the info panel, and then do
    //Copy this for the subclass panels.
    public JPanel getInfoPanel() {
        JPanel root = new JPanel();
        JTabbedPane tabbedPane = new JTabbedPane();
        
        JPanel infoPanel = new JPanel();
        JLabel idLabel = new JLabel("ID: " + this.getId());
        JLabel ownerLabel = new JLabel("Owner: " + (owner == -1  ? "No owner" : Globals.universe.getCivilization(owner).getSpeciesName()));
        
        infoPanel.add(idLabel);
        infoPanel.add(ownerLabel);
        
        tabbedPane.add("Info", infoPanel);
        root.add(tabbedPane);
        return root;
    }
}