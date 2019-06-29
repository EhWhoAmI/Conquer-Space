package ConquerSpace.gui.game;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.Ship;
import javax.swing.JInternalFrame;

/**
 *
 * @author zyunl
 */
public class ShipDetailsSideWindow extends JInternalFrame {

    private ShipInformationMenu men;

    public ShipDetailsSideWindow(Ship s, Civilization c) {
        //Init...
        men = new ShipInformationMenu(s, c);
        add(men);
        setVisible(true);
        setClosable(true);
        setResizable(true);
        pack();
        toFront();
        //Get window size
        
    }
}
