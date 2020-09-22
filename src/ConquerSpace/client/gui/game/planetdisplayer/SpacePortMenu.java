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
import ConquerSpace.common.game.ships.ShipClass;
import ConquerSpace.common.game.ships.launch.LaunchSystem;
import ConquerSpace.common.game.universe.Vector;
import ConquerSpace.common.game.universe.bodies.Planet;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.util.UUID;
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
    JLabel selectedShipButton;

    public SpacePortMenu(GameState gameState, Planet planet, Civilization civilization) {
        setLayout(new VerticalFlowLayout());
        this.gameState = gameState;
        this.planet = planet;
        this.civilization = civilization;
        spacePortCount = new JLabel();

        launchableListModel = new ObjectListModel<>();
        launchableListModel.setElements(civilization.shipClasses);
        launchableListModel.setHandler(l -> {
            return gameState.getObject(l, ShipClass.class).toString();
        });

        launchableList = new JList<>(launchableListModel);
        launchableList.setFixedCellWidth(250);
        launchableList.addListSelectionListener(l -> {
            ObjectReference reference = launchableListModel.getObject(launchableList.getSelectedIndex());
            ShipClass shipClass = gameState.getObject(reference, ShipClass.class);
            selectedShipButton.setText(shipClass.getName());
            launchButton.setEnabled(true);
        });

        selectedShipButton = new JLabel("");
        launchButton = new JButton("Launch Ship!");
        launchButton.setEnabled(false);
        launchButton.addActionListener(l -> {
            ObjectReference reference = launchableListModel.getObject(launchableList.getSelectedIndex());
            ShipClass shipClass = gameState.getObject(reference, ShipClass.class);
            //Launch ship somehow
            Ship ship = new Ship(gameState, shipClass, planet.getY(), planet.getX(), new Vector(0, 0), planet.getUniversePath());
            //Set random name for now
            ship.setName(UUID.randomUUID().toString());
            //Then stuff it onto launch system and launch into space
            //Get launch system
//            if (civilization.launchSystems.isEmpty()) {
//                //No launch system, exit
//                JOptionPane.showInternalMessageDialog(SpacePortMenu.this, "You need to design a launch system before you can launch your space ship!");
//                return;
//            }
            //Then stuff to launchsystem
            LaunchSystem launchSystem = gameState.getObject(civilization.launchSystems.get(0), LaunchSystem.class);
            
            
            //Add to civ
            civilization.spaceships.add(ship.getReference());
            Actions.launchLaunchable(ship, planet);
        });

        add(spacePortCount);

        JPanel launchableInformationPanel = new JPanel(new HorizontalFlowLayout());
        launchableInformationPanel.add(new JScrollPane(launchableList));

        JPanel shipInfoandLaunchPanel = new JPanel(new VerticalFlowLayout());
        shipInfoandLaunchPanel.add(selectedShipButton);
        shipInfoandLaunchPanel.add(launchButton);
        launchableInformationPanel.add(shipInfoandLaunchPanel);

        add(launchableInformationPanel);
        updateComponent();
    }

    private void updateComponent() {
        //Get the amount of launch pads
        int launchPadCount = 0;
        for (ObjectReference cityIndex : planet.cities) {
            City city = gameState.getObject(cityIndex, City.class);
            for (ObjectReference areaIndex : city.areas) {
                Area area = gameState.getObject(areaIndex, Area.class);
                if (area instanceof SpacePortArea) {
                    SpacePortArea port = (SpacePortArea) area;
                    launchPadCount += port.launchPads.size();
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
}
