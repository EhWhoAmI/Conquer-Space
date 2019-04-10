package ConquerSpace.gui.game;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.pSectors.SpacePortLaunchPad;
import java.awt.GridLayout;
import javax.swing.DefaultListModel;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import org.json.JSONObject;

/**
 *
 * @author Zyun
 */
public class LaunchSpaceShipMenu extends JInternalFrame {
    private DefaultListModel<SpaceShipWrapper> spaceShipListModel;
    private JList<SpaceShipWrapper> spaceShipList;
    
    private JPanel launchSpaceShipMenu;
    public LaunchSpaceShipMenu(SpacePortLaunchPad pad, Civilization c, Planet p) {
        setTitle("Launch a space ship using " + pad.getType().getName());
        setLayout(new GridLayout(1, 3));
        
        spaceShipListModel = new DefaultListModel<>();
        spaceShipList = new JList(spaceShipListModel);
        
        launchSpaceShipMenu = new JPanel();
        
        add(spaceShipList);
        add(launchSpaceShipMenu);
        setResizable(true);
        setVisible(true);
        setClosable(true);
        pack();

        //Bring to front
        toFront();
    }
    
    private class SpaceShipWrapper {
        JSONObject obj;

        public SpaceShipWrapper(JSONObject obj) {
            this.obj = obj;
        }

        @Override
        public String toString() {
            return super.toString(); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
