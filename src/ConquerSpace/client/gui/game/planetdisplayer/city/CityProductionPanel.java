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

package ConquerSpace.client.gui.game.planetdisplayer.city;

import ConquerSpace.ConquerSpace;
import ConquerSpace.client.gui.ObjectListModel;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.city.area.Area;
import ConquerSpace.common.game.city.area.SpacePortArea;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.ships.Ship;
import ConquerSpace.common.game.ships.ShipClass;
import ConquerSpace.common.game.universe.Vector;
import ConquerSpace.common.game.universe.bodies.Planet;
import com.alee.extended.layout.VerticalFlowLayout;
import java.util.UUID;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Panel to order the production of certain goods or something.
 */
public class CityProductionPanel extends JPanel {

    private JList<String> shipClassList;

    public CityProductionPanel(Civilization civilization, Planet planet, GameState gameState) {
        setLayout(new VerticalFlowLayout());
        add(new JLabel(ConquerSpace.LOCALE_MESSAGES.getMessage("game.planet.cities.production")));
        //Create new space ship menu
        add(new JLabel(ConquerSpace.LOCALE_MESSAGES.getMessage("game.planet.cities.spaceship")));
        ObjectListModel<ObjectReference> shipList = new ObjectListModel<>();
        shipList.setElements(civilization.getShipClasses());
        shipList.setHandler(l -> {
            return gameState.getObject(l, ShipClass.class).getName();
        });
        shipClassList = new JList<>(shipList);
        add(new JScrollPane(shipClassList));
        JButton createButton = new JButton(ConquerSpace.LOCALE_MESSAGES.getMessage("game.planet.cities.create"));
        createButton.addActionListener(l -> {
            //Find the space ports on this planet...
            //Get selected ship
            if (shipClassList.getSelectedIndex() < -1) {
                JOptionPane.showInternalMessageDialog(this, "You need to select a ship");
                return;
            }
            int launchPadCount = 0;
            for (ObjectReference cityIndex : planet.cities) {
                City city = gameState.getObject(cityIndex, City.class);
                for (ObjectReference areaIndex : city.getAreas()) {
                    Area area = gameState.getObject(areaIndex, Area.class);
                    if (area instanceof SpacePortArea) {
                        SpacePortArea port = (SpacePortArea) area;
                        //Add selected ship class
                        //UI to create ship
                        ShipClass shipClass = gameState.getObject(shipList.getObject(shipClassList.getSelectedIndex()), ShipClass.class);
                        Ship ship = new Ship(gameState, shipClass, planet.getY(), planet.getX(), new Vector(0, 0), planet.getUniversePath());
                        //Set random name for now
                        ship.setName(UUID.randomUUID().toString());
                        port.landedShips.add(ship.getReference());
                        civilization.getSpaceships().add(ship.getReference());
                        JOptionPane.showInternalMessageDialog(this, ConquerSpace.LOCALE_MESSAGES.getMessage("game.planet.cities.launch.alert", shipClass.getName(), ship.getName()));
                        //Deselect
                        shipClassList.clearSelection();
                        return;
                    }
                }
            }
            JOptionPane.showInternalMessageDialog(this, ConquerSpace.LOCALE_MESSAGES.getMessage("game.planet.cities.needport"));
        });
        add(createButton);
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            shipClassList.updateUI();
        }
    }

}
