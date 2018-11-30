package ConquerSpace.game.universe.spaceObjects.pSectors;

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
    
    public PlanetSector() {
        economy = new Economy();
        resources = new ArrayList<>();
    }
    
    public String toReadableString() {
        return (this.getClass().getName());
    }

    public int getId() {
        return id;
    }
    
    //Creates the info panel, and then do
    //Copy this for the subclass panels.
    public JPanel getInfoPanel() {
        JPanel root = new JPanel();
        JTabbedPane tabbedPane = new JTabbedPane();
        
        JPanel infoPanel = new JPanel();
        JLabel idLabel = new JLabel("ID: " + this.getId());
        
        infoPanel.add(idLabel);
        
        tabbedPane.add("Info", infoPanel);
        root.add(tabbedPane);
        return root;
    }
    
    public void addResource(int type, int amount) {
        resources.add(new Resource(type, amount));
    }
}