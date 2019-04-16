package ConquerSpace.gui.game;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.controllers.PlayerController.PlayerController;
import ConquerSpace.game.universe.ships.Ship;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

/**
 *
 * @author Zyun
 */
public class ShipInformationMenu extends JInternalFrame {

    private JLabel shipNameLabel;
    private JLabel shipNameText;
    
    private JButton gotoPositionButton;
    private JButton selectButton;

    //Actions buttons. 
    public ShipInformationMenu(Ship ship, Civilization c) {
        setLayout(new GridLayout(2, 2));
        shipNameLabel = new JLabel("Name: ");
        add(shipNameLabel);
        shipNameText = new JLabel(ship.getName());
        add(shipNameText);

        if (ship.isOrbiting()) {
            JLabel currentlyOrbitingLabel = new JLabel("Currently orbiting: ");
            JLabel currentlyOrbitingText = new JLabel(ship.getOrbiting().toString());
            add(currentlyOrbitingLabel);
            add(currentlyOrbitingText);
        }
        else {
            JLabel currentPositionLabel = new JLabel("Current Position: ");
            JLabel currentPositionText = new JLabel(ship.getLocation().toString());
            add(currentPositionLabel);
            add(currentPositionText);
        }
        selectButton = new JButton("Select this Ship");
        selectButton.addActionListener(a -> {
            ((PlayerController)c.controller).selectedShips.add(ship);
        });
        add(selectButton);
        setClosable(true);
        setVisible(true);
        setResizable(true);
        //setSize(100, 100);
        pack();
    }

}
