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
package ConquerSpace.client.gui.game.planetdisplayer;

import ConquerSpace.client.gui.ObjectListModel;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.actions.Actions;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.city.area.Area;
import ConquerSpace.common.game.city.area.SpacePortArea;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.ships.Ship;
import ConquerSpace.common.game.ships.ShipCapability;
import ConquerSpace.common.game.universe.bodies.Planet;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author EhWhoAmI
 */
public class SpacePortMenu extends JPanel {

    GameState gameState;
    Planet planet;
    Civilization civilization;

    JLabel spacePortCount;
    ObjectListModel<ObjectReference> launchableListModel;
    JList<String> launchableList;
    JButton launchButton;
    JLabel selectedShipLabel;

    public SpacePortMenu(GameState gameState, Planet planet, Civilization civilization) {
        setLayout(new VerticalFlowLayout());
        this.gameState = gameState;
        this.planet = planet;
        this.civilization = civilization;
        spacePortCount = new JLabel();

        launchableListModel = new ObjectListModel<>();
        //launchableListModel.setElements(civilization.shipClasses);
        launchableListModel.setHandler(l -> {
            return gameState.getObject(l, Ship.class).toString();
        });

        launchableList = new JList<>(launchableListModel);
        launchableList.setFixedCellWidth(250);
        launchableList.addListSelectionListener(l -> {
            ObjectReference reference = launchableListModel.getObject(launchableList.getSelectedIndex());
            Ship shipClass = gameState.getObject(reference, Ship.class);
            selectedShipLabel.setText(shipClass.getName() + " " + shipClass.getShipClassName() + "-class " + shipClass.getShipType().getName());
            launchButton.setEnabled(true);
        });

        selectedShipLabel = new JLabel("");
        launchButton = new JButton("Launch Ship!");
        launchButton.setEnabled(false);
        launchButton.addActionListener(l -> {
            //Add to civ
            ObjectReference shipToLaunch = launchableListModel.getObject(launchableList.getSelectedIndex());
            Ship ship = gameState.getObject(shipToLaunch, Ship.class);
            if (ship.shipCapabilities.contains(ShipCapability.ToOrbit)) {
                Actions.launchLaunchable(ship, planet);
                //Remove from planet
                removeShipFromPlanet(shipToLaunch);
                //Update UI
            } else {
                boolean needLaunch = true;

                //Check if has enough launch stuff
                if (civilization.civValueIsGreaterThan("launch", (int) ship.getMass())) {
                    removeShipFromPlanet(shipToLaunch);
                    Actions.launchLaunchable(ship, planet);
                    needLaunch = false;
                }

                if (needLaunch) {
                    JOptionPane.showInternalMessageDialog(this, "The ship needs to be orbit capable, or you need a launch vehicle in the space port!");
                }
            }
            updateComponent();
        });

        add(spacePortCount);

        JPanel launchableInformationPanel = new JPanel(new HorizontalFlowLayout());
        launchableInformationPanel.add(new JScrollPane(launchableList));

        JPanel shipInfoandLaunchPanel = new JPanel(new VerticalFlowLayout());
        shipInfoandLaunchPanel.add(selectedShipLabel);
        shipInfoandLaunchPanel.add(launchButton);
        launchableInformationPanel.add(shipInfoandLaunchPanel);

        add(launchableInformationPanel);
        updateComponent();
    }

    private void updateComponent() {
        launchableListModel.clear();
        //Get the amount of launch pads
        int launchPadCount = 0;
        for (ObjectReference cityIndex : planet.cities) {
            City city = gameState.getObject(cityIndex, City.class);
            for (ObjectReference areaIndex : city.areas) {
                Area area = gameState.getObject(areaIndex, Area.class);
                if (area instanceof SpacePortArea) {
                    SpacePortArea port = (SpacePortArea) area;
                    launchPadCount += port.getLaunchPadCount();
                    launchableListModel.addAllElements(port.landedShips);
                }
            }
        }
        spacePortCount.setText("Launch pads: " + Integer.toString(launchPadCount));
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag); //To change body of generated methods, choose Tools | Templates.
        if (aFlag) {
            launchableList.updateUI();
            updateComponent();
        }
    }

    public void removeShipFromPlanet(ObjectReference reference) {
        planetloop:
        for (ObjectReference cityIndex : planet.cities) {
            City city = gameState.getObject(cityIndex, City.class);
            for (ObjectReference areaIndex : city.areas) {
                Area area = gameState.getObject(areaIndex, Area.class);
                if (area instanceof SpacePortArea) {
                    SpacePortArea port = (SpacePortArea) area;
                    if (port.landedShips.contains(reference)) {
                        port.landedShips.remove(reference);

                        break planetloop;
                    }
                }
            }
        }
    }
}
