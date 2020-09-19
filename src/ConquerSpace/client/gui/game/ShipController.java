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

import ConquerSpace.client.gui.ObjectListModel;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.ships.Ship;
import ConquerSpace.common.game.ships.components.ShipComponent;
import com.alee.extended.layout.VerticalFlowLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author EhWhoAmI
 */
public class ShipController extends JPanel {

    JLabel shipNameLabel;
    JLabel shipClassLabel;
    JList<String> shipcomponents;

    ObjectListModel<ObjectReference> shipComponentModel;
    JList<String> shipComponentToAdd;

    ObjectListModel<ObjectReference> shipComponentListModel;

    Civilization civilization;
    Ship currentShip;

    public ShipController(GameState gameState, Civilization civilization) {
        this.civilization = civilization;
        currentShip = null;
        setLayout(new VerticalFlowLayout());
        shipNameLabel = new JLabel();
        shipClassLabel = new JLabel();
        //Show ship components and other happiness
        shipComponentModel = new ObjectListModel<>();
        shipComponentModel.setHandler(l -> {
            return gameState.getObject(l, ShipComponent.class).getName();
        });

        shipcomponents = new JList<>(shipComponentModel);

        //Add UI to increase size
        //Another panel to add random ship components
        JPanel addRandomShipComponentPanel = new JPanel();
        shipComponentListModel = new ObjectListModel<>();
        shipComponentListModel.setElements(civilization.shipComponentList);
        shipComponentListModel.setHandler(l -> {
            return gameState.getObject(l, ShipComponent.class).getName();
        });

        shipComponentToAdd = new JList<>(shipComponentListModel);

        //Add button to add component
        JButton addSelectedComponentButton = new JButton("Add Component");
        addSelectedComponentButton.addActionListener(l -> {
            int selected = shipComponentToAdd.getSelectedIndex();
            if (selected >= 0) {
                //Then get the component and add it to the thing
                ObjectReference reference = shipComponentListModel.getObject(selected);
                //Add to ship
                if (currentShip != null) {
                    currentShip.components.add(reference);
                }
                shipcomponents.updateUI();
            }
        });

        addRandomShipComponentPanel.add(new JScrollPane(shipComponentToAdd));
        addRandomShipComponentPanel.add(addSelectedComponentButton);

        add(shipNameLabel);
        add(shipClassLabel);
        add(new JScrollPane(shipcomponents));
        add(addRandomShipComponentPanel);
    }

    public void showShip(Ship ship) {
        currentShip = ship;

        shipNameLabel.setText(ship.getName());
        shipClassLabel.setText(ship.getShipClassName());
        shipComponentModel.setElements(ship.components);
        //shipComponentListModel.setElements(civilization.shipComponentList);
        shipcomponents.updateUI();
        shipComponentToAdd.updateUI();
    }
}
