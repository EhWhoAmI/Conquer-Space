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
package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.game.districts.District;
import ConquerSpace.game.districts.SpacePort;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.civilization.Civilization;
import ConquerSpace.game.ships.launch.SpacePortLaunchPad;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.gui.game.LaunchSatelliteMenu;
import ConquerSpace.gui.game.LaunchSpaceShipMenu;
import com.alee.extended.layout.HorizontalFlowLayout;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 *
 * @author Ehwhoami
 */
public class SpacePortMenu extends JPanel {

    private ArrayList<SpacePortLaunchPad> launchPads;

    private JList<SpacePortLaunchPad> launchPadList;

    private DefaultListModel<SpacePortLaunchPad> spaceLPModel;
    private Planet p;
    private Civilization c;

    public SpacePortMenu(Planet p, Civilization c) {
        this.p = p;
        this.c = c;
        setLayout(new HorizontalFlowLayout());
        launchPads = new ArrayList<>();
        //Do stuff
        //Compile all the launch pads on the planet

        spaceLPModel = new DefaultListModel<>();

        for (Map.Entry<GeographicPoint, District> entry : p.buildings.entrySet()) {
            GeographicPoint key = entry.getKey();
            District value = entry.getValue();

            //Do stuff...
            if (value instanceof SpacePort) {
                SpacePort port = (SpacePort) value;
                //Do things
                for (SpacePortLaunchPad lp : port.launchPads) {
                    launchPads.add(lp);
                    spaceLPModel.addElement(lp);
                }
            }
        }
        //Add components and stuff

        launchPadList = new JList<>(spaceLPModel);
        JScrollPane launchPadListScrollPane = new JScrollPane(launchPadList);
        
        JTabbedPane pane = new JTabbedPane();
        JPanel launchSatelliteMenuContainer = new JPanel(new HorizontalFlowLayout());
        pane.add("Satellite", launchSatelliteMenuContainer);
        JPanel launchSpaceShipJPanelMenuContainer = new JPanel(new HorizontalFlowLayout());
        pane.add("Space Ship", launchSpaceShipJPanelMenuContainer);

        //LaunchSatelliteMenu launchSatelliteMenu = new LaunchSatelliteMenu(pad, c, p, sys);
        launchPadList.addListSelectionListener(a -> {
            //Show the thingy
            launchSatelliteMenuContainer.removeAll();
            LaunchSatelliteMenu launchSatelliteMenu = new LaunchSatelliteMenu(launchPadList.getSelectedValue(), c, p);
            launchSatelliteMenuContainer.add(launchSatelliteMenu);
            launchSpaceShipJPanelMenuContainer.removeAll();
            LaunchSpaceShipMenu launchSpaceShipMenu = new LaunchSpaceShipMenu(launchPadList.getSelectedValue(), c, p);
            launchSpaceShipJPanelMenuContainer.add(launchSpaceShipMenu);
        });

        add(launchPadListScrollPane);
        add(pane);
    }

    public void update() {
        for (Map.Entry<GeographicPoint, District> entry : p.buildings.entrySet()) {
            GeographicPoint key = entry.getKey();
            District value = entry.getValue();

            //Do stuff...
            if (value instanceof SpacePort) {
                SpacePort port = (SpacePort) value;
                //Do things
                for (SpacePortLaunchPad lp : port.launchPads) {
                    if (!launchPads.contains(lp)) {
                        launchPads.add(lp);
                        spaceLPModel.addElement(lp);
                    }
                }
            }
        }
    }
}
