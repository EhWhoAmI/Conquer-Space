package ConquerSpace.game.universe.spaceObjects.pSectors;

import ConquerSpace.Globals;
import ConquerSpace.game.universe.civilization.stats.Economy;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.spaceObjects.SpaceObject;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * AKA building
 * @author Zyun
 */
public class PlanetSector extends SpaceObject{
    public ArrayList<Resource> resources;
    public Economy economy;
    public int id;
    
    @Deprecated
    private int owner = -1;
    
    public PlanetSector() {
        economy = new Economy();
        resources = new ArrayList<>();
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
    
    public void addResource(int type, int amount) {
        resources.add(new Resource(type, amount));
    }
}