package ConquerSpace.gui.game;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.pSectors.SpacePortLaunchPad;
import javax.swing.JInternalFrame;

/**
 *
 * @author Zyun
 */
public class LaunchSpaceShipMenu extends JInternalFrame {

    public LaunchSpaceShipMenu(SpacePortLaunchPad pad, Civilization c, Planet p) {
        setTitle("Launch a space ship using " + pad.getType().getName());
        
        setResizable(true);
        setVisible(true);
        setClosable(true);
        pack();

        //Bring to front
        toFront();
    }

}
