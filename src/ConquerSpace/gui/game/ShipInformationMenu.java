package ConquerSpace.gui.game;

import ConquerSpace.game.universe.ships.Ship;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

/**
 *
 * @author Zyun
 */
public class ShipInformationMenu extends JInternalFrame{
    private JLabel shipNameLabel;
    private JLabel shipNameText;
    //Actions buttons. 
    public ShipInformationMenu(Ship ship) {
        setClosable(true);
        setVisible(true);
        setResizable(true);
        //setSize(100, 100);
        pack();
    }
    
}
