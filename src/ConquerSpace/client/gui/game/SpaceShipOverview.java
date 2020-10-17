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

import ConquerSpace.common.GameState;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.ships.Ship;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author EhWhoAmI
 */
public class SpaceShipOverview extends JPanel {

    private ShipManager shipListManager;
    private ShipControllerPanel controller;
    private JTabbedPane tabs;

    public SpaceShipOverview(GameState gameState, Civilization civ, PlayerRegister register) {
        setLayout(new BorderLayout());
        tabs = new JTabbedPane();
        
        shipListManager = new ShipManager(gameState, civ, register, this);
        controller = new ShipControllerPanel(gameState, civ);

        tabs.add("Ship List", shipListManager);
        tabs.add("Ship information", controller);

        tabs.addChangeListener(l -> {
            update();
        });
        add(tabs, BorderLayout.CENTER);
    }

    public void update() {
        //shipDesigner.update();
        shipListManager.update();
    }

    //Update list
    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            update();
        }
    }

    public void showShip(Ship ship) {
        tabs.setSelectedIndex(1);
        controller.showShip(ship);
    }
}
