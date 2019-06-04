package ConquerSpace.gui.game;

import ConquerSpace.game.actions.Actions;
import ConquerSpace.game.universe.Vector;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.Ship;
import ConquerSpace.game.universe.ships.ShipClass;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.ships.launch.SpacePortLaunchPad;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Zyun
 */
public class LaunchSpaceShipMenu extends JPanel {

    private DefaultListModel<ShipClass> spaceShipListModel;
    private JList<ShipClass> spaceShipList;

    private JPanel launchSpaceShipMenu;

    private JLabel massLabel;
    private JButton launchButton;

    public LaunchSpaceShipMenu(SpacePortLaunchPad pad, Civilization c, Planet p) {
        setLayout(new GridLayout(1, 3));

        spaceShipListModel = new DefaultListModel<>();
        spaceShipList = new JList(spaceShipListModel);

        for (ShipClass sc : c.shipClasses) {
            spaceShipListModel.addElement(sc);
        }

        spaceShipList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //Get selected number and display
                ShipClass sc = spaceShipList.getSelectedValue();
                massLabel.setText("Mass: " + sc.getMass());
            }

        });

        launchSpaceShipMenu = new JPanel();
        launchSpaceShipMenu.setLayout(new VerticalFlowLayout());

        massLabel = new JLabel("Mass: ");
        launchButton = new JButton("Launch");

        launchButton.addActionListener(a -> {
            //Create ship and launch
            //get ship
            if (p != null) {
                System.out.println(p.getUniversePath().toString());
                Ship ship = new Ship(spaceShipList.getSelectedValue(),
                        0, 0, new Vector(0, 0),
                        p.getUniversePath());
                
                Actions.launchShip(ship, p, c);
            }
            else {
                JOptionPane.showMessageDialog(null, "Planet is null!");
            }
        });

        launchSpaceShipMenu.add(massLabel);
        launchSpaceShipMenu.add(launchButton);
        add(spaceShipList);
        add(launchSpaceShipMenu);
        setVisible(true);
    }
}