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
package ConquerSpace.client.gui.game;

import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.common.game.ships.Ship;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author EhWhoAmI
 */
public class ShipInformationMenu extends JPanel {

    private JLabel shipNameLabel;
    private JLabel shipNameText;

    //private JButton gotoPositionButton;
    private JButton selectButton;

    //Actions buttons. 
    public ShipInformationMenu(Ship ship, Civilization c, PlayerRegister playerRegister) {
        setLayout(new GridLayout(3, 2));
        shipNameLabel = new JLabel("Name: ");
        add(shipNameLabel);
        shipNameText = new JLabel(ship.getName());
        add(shipNameText);

        if (ship.isOrbiting()) {
            JLabel currentlyOrbitingLabel = new JLabel("Currently orbiting: ");
            JLabel currentlyOrbitingText = new JLabel(ship.getOrbiting().toString());
            add(currentlyOrbitingLabel);
            add(currentlyOrbitingText);
        } else {
            JLabel currentPositionLabel = new JLabel("Current Position: ");
            JLabel currentPositionText = new JLabel(ship.getLocation().toString());
            add(currentPositionLabel);
            add(currentPositionText);
        }
        selectButton = new JButton("Select this Ship");
        selectButton.addActionListener(a -> {
            if (!playerRegister.getSelectedShips().contains(ship)) {
                playerRegister.getSelectedShips().add(ship);
            }
        });
        add(selectButton);
        setVisible(true);
        //setSize(100, 100);
    }
}
