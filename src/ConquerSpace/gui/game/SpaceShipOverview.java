package ConquerSpace.gui.game;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.spaceObjects.Universe;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author zyunl
 */
public class SpaceShipOverview extends JPanel {
    private ShipListManager shipListManager;
    private JTabbedPane tabs;

    public SpaceShipOverview(Civilization civ, Universe u) {
        tabs = new JTabbedPane();
        shipListManager = new ShipListManager(u, civ);
        
        tabs.add("Ship List", shipListManager);

        tabs.addChangeListener(l -> {
        });
        add(tabs);
    }

    public void update() {
        //shipDesigner.update();
        shipListManager.update();
    }
}
