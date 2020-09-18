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
package ConquerSpace.client.gui.game.engineering;

import ConquerSpace.client.gui.ObjectListModel;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.ships.ShipClass;
import ConquerSpace.common.game.ships.components.ShipComponent;
import com.alee.extended.layout.HorizontalFlowLayout;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author EhWhoAmI
 */
public class FullShipClassInformationMenu extends JPanel {

    JList<String> shipList;
    JList<String> componentList;

    ShipClass selectedShipClass;
    ObjectListModel<ObjectReference> shipComponentList;

    public FullShipClassInformationMenu(GameState gameState, Civilization civilization) {
        setLayout(new HorizontalFlowLayout());
        ObjectListModel<ObjectReference> shipClassList = new ObjectListModel<>();
        shipClassList.setElements(civilization.shipClasses);
        shipClassList.setHandler(l -> {
            return gameState.getObject(l, ShipClass.class).getName();
        });

        shipList = new JList<>(shipClassList);
        shipList.setFixedCellWidth(250);
        
        shipList.addListSelectionListener(l ->{
            ObjectReference reference = shipClassList.getObject(shipList.getSelectedIndex());
            ShipClass shipClass = gameState.getObject(reference, ShipClass.class);
            shipComponentList.setElements(shipClass.components);
            componentList.updateUI();
        });

        shipComponentList = new ObjectListModel<>();
        shipComponentList.setHandler(l -> {
            return gameState.getObject(l, ShipComponent.class).getName();
        });
        componentList = new JList<>(shipComponentList);
        componentList.setFixedCellWidth(100);
        
        //UI to add and remove components and the like
        add(new JScrollPane(shipList));
        add(new JScrollPane(componentList));
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag); 
        if (aFlag) {
            shipList.updateUI();
        }
    }
}
