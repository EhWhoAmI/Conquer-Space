/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ConquerSpace.gui.game;

import ConquerSpace.game.actions.Actions;
import ConquerSpace.game.universe.Vector;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.Ship;
import ConquerSpace.game.universe.ships.ShipClass;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.ships.launch.SpacePortLaunchPad;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
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

    @SuppressWarnings("unchecked")
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
                if (!spaceShipList.isSelectionEmpty()) {
                    //Get selected number and display
                    ShipClass sc = spaceShipList.getSelectedValue();
                    massLabel.setText("Mass: " + sc.getMass());
                }
            }
        });

        launchSpaceShipMenu = new JPanel();
        launchSpaceShipMenu.setLayout(new VerticalFlowLayout());

        massLabel = new JLabel("Mass: ");
        launchButton = new JButton("Launch");

        launchButton.addActionListener(a -> {
            //Create ship and launch
            //get ship
            if (p != null && !spaceShipList.isSelectionEmpty()) {
                Ship ship = new Ship(spaceShipList.getSelectedValue(),
                        0, 0, new Vector(0, 0),
                        p.getUniversePath());
                ship.setEstimatedThrust(spaceShipList.getSelectedValue().getEstimatedThrust());
                Actions.launchShip(ship, p, c);
            } else {
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
