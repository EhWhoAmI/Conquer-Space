package ConquerSpace.gui.game;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.spaceObjects.Universe;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author zyunl
 */
public class SpaceShipOverview extends JPanel{

    private ShipDesigner shipDesigner;
    private ShipListManager shipListManager;
    private SatelliteDesigner satelliteDesigner;
    private JTabbedPane tabs;
    
    public SpaceShipOverview(Civilization civ, Universe u) {
        tabs = new JTabbedPane();
        shipDesigner = new ShipDesigner(civ);
        shipListManager = new ShipListManager(u, civ);
        satelliteDesigner = new SatelliteDesigner(civ);
        
        tabs.add("Ship List", shipListManager);
        tabs.add("Ship Designer", shipDesigner);
        tabs.add("Satellite Designer", satelliteDesigner);
        add(tabs);
    }
    
}
