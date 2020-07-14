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
import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.common.game.ships.launch.SpacePortLaunchPad;
import ConquerSpace.common.game.ships.satellites.Satellite;
import ConquerSpace.common.game.ships.satellites.Satellites;
import ConquerSpace.common.game.ships.satellites.templates.SatelliteTemplate;
import ConquerSpace.common.game.universe.bodies.Planet;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.GridLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author EhWhoAmI
 */
public class LaunchSatelliteMenu extends JPanel {

    private JPanel satelliteSelectPanel;
    private JList<SatelliteWrapper> satelliteSelectList;
    private DefaultListModel<SatelliteWrapper> listModel;
    private JLabel title;

    private JPanel statsPanel;
    private JLabel satelliteName;
    private JLabel satelliteMass;
    private JButton buildAndLaunchButton;

    @SuppressWarnings("unchecked")
    public LaunchSatelliteMenu(GameState gameState, SpacePortLaunchPad pad, Civilization c, Planet p) {
        //The launch pad type and stuff as title
        //title = new JLabel("Launch Satellite");
        setLayout(new GridLayout(1, 2));

        //Satellite select content
        satelliteSelectPanel = new JPanel();

        listModel = new DefaultListModel<>();

        for (SatelliteTemplate s : c.satelliteTemplates) {
            //Process satellite
            SatelliteWrapper wrap = new SatelliteWrapper(s.getId(), s.getName());
            listModel.addElement(wrap);
        }
        satelliteSelectList = new JList(listModel);
        //So that one is selected every time
        satelliteSelectList.setSelectedIndex(0);
        satelliteSelectList.addListSelectionListener((e) -> {
            //Reload all the things
            //Get selected object
            SatelliteWrapper selected = satelliteSelectList.getSelectedValue();
            int id = selected.getId();
            //Get compatable
            SatelliteTemplate selectedObject = null;
            for (SatelliteTemplate s : c.satelliteTemplates) {
                //Process satellite
                if (id == s.getId()) {
                    selectedObject = s;
                    break;
                }
            }
            if (selectedObject != null) {
                satelliteMass.setText("Mass: " + selectedObject.getMass());
                satelliteName.setText(selectedObject.getName());
            }
        });
        satelliteSelectPanel.add(satelliteSelectList);

        //Satellite stats content
        statsPanel = new JPanel();
        statsPanel.setLayout(new VerticalFlowLayout());
        satelliteName = new JLabel();
        if (!c.satelliteTemplates.isEmpty()) {
            satelliteName.setText(satelliteSelectList.
                    getSelectedValue().toString());
        }

        statsPanel.add(satelliteName);

        satelliteMass = new JLabel("Mass: ");
        statsPanel.add(satelliteMass);
        buildAndLaunchButton = new JButton("Launch!");
        buildAndLaunchButton.setFocusable(false);
        buildAndLaunchButton.addActionListener(e -> {
            if (satelliteSelectList.getSelectedValue() != null) {
                //Launch satellite
                SatelliteWrapper selected = satelliteSelectList.getSelectedValue();
                int id = selected.getId();
                //Get compatable
                SatelliteTemplate selectedObject = null;
                for (SatelliteTemplate s : c.satelliteTemplates) {
                    //Process satellite
                    if (id == s.getId()) {
                        selectedObject = s;
                        Satellite sat = Satellites.parseSatellite(gameState, selectedObject, c.multipliers, c.values);
                        //Check if it orbits a planet
                        //if(sat instanceof SpaceTelescope) {
                        //((SpaceTelescope) sat).setPosition(new Point(sys.getX(), sys.getY()));
                        //}
                        sat.setOwner(c.getId());
                        //Actions.launchSatellite(sat, p, 100, c);
                        JOptionPane.showInternalMessageDialog(getParent(), "Launched satellite");
                        break;
                    }
                }
            }
        });
        statsPanel.add(buildAndLaunchButton);

        //Add update code for the satellite selection
        satelliteSelectList.addListSelectionListener(e -> {
            //satelliteName.setText(satelliteSelectList.getSelectedValue().getName());
            //satelliteMass.setText("Mass: " + satelliteSelectList.getSelectedValue().getMass());
        });

        add(satelliteSelectList);
        add(statsPanel);
        setVisible(true);

        //Bring to front
    }

    private static class SatelliteWrapper {

        int id;
        String name;

        public SatelliteWrapper(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return name;
        }

    }
}