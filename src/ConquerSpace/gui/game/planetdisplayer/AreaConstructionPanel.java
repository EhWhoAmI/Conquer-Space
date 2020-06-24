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

import ConquerSpace.game.organizations.civilization.Civilization;
import ConquerSpace.game.city.City;
import ConquerSpace.game.city.area.Area;
import ConquerSpace.game.city.area.ConstructingArea;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.gui.game.planetdisplayer.construction.AreaDesignPanel;
import ConquerSpace.gui.game.planetdisplayer.construction.IndustrialFactoryConstructionPanel;
import ConquerSpace.gui.game.planetdisplayer.construction.MinerAreaConstructionPanel;
import ConquerSpace.gui.game.planetdisplayer.construction.ObservatoryConstructionPanel;
import ConquerSpace.gui.game.planetdisplayer.construction.SpacePortConstructionPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

/**
 *
 * @author EhWhoAmI
 */
public class AreaConstructionPanel extends JPanel {

    private Planet planet;
    private City city;

    private DefaultListModel<String> areaTypeListModel;
    private JList<String> areaTypeList;

    private JButton constructButton;

    private JPanel areaConstructionInfo;

    private static final String MINE_STRING = "Mine";
    private static final String MANUFACTURER_STRING = "Manufacturer";
    private static final String POWER_PLANT_STRING = "Power Plant";
    private static final String SPACE_PORT_STRING = "Space Port";
    private static final String OBSERVARTORY_STRING = "Observatory";

    private static String[] AREA_LIST_NAMES = {MINE_STRING, MANUFACTURER_STRING, POWER_PLANT_STRING, OBSERVARTORY_STRING, SPACE_PORT_STRING};

    private AreaDesignPanel areaDesignPanel = null;

    public AreaConstructionPanel(Planet planet, Civilization c, City city) {
        setLayout(new BorderLayout());

        areaTypeListModel = new DefaultListModel<>();
        for (int i = 0; i < AREA_LIST_NAMES.length; i++) {
            //Has launch capability
            if (AREA_LIST_NAMES[i].equals(SPACE_PORT_STRING)) {
                if (c.values.containsKey("haslaunch") && c.values.get("haslaunch") == 1) {
                    areaTypeListModel.addElement(AREA_LIST_NAMES[i]);
                }
            } else {
                areaTypeListModel.addElement(AREA_LIST_NAMES[i]);
            }
        }
        areaTypeList = new JList<>(areaTypeListModel);
        areaTypeList.addListSelectionListener(l -> {
            areaConstructionInfo.removeAll();
            //Get selected area type
            switch (areaTypeList.getSelectedValue()) {
                case MINE_STRING:
                    areaDesignPanel = new MinerAreaConstructionPanel(planet, city);
                    break;
                case SPACE_PORT_STRING:
                    areaDesignPanel = new SpacePortConstructionPanel(planet, city, c);
                    break;
                case MANUFACTURER_STRING:
                    areaDesignPanel = new IndustrialFactoryConstructionPanel(planet, city, c);
                    break;
                case OBSERVARTORY_STRING:
                    areaDesignPanel = new ObservatoryConstructionPanel(planet, city, c);
                    break;
                default:
                    areaDesignPanel = new AreaDesignPanel(planet, city);
                    break;
            }
            areaConstructionInfo.add(areaDesignPanel, BorderLayout.PAGE_START);
        });

        areaConstructionInfo = new JPanel(new BorderLayout());

        constructButton = new JButton("Construct!");
        constructButton.addActionListener(l -> {
            Area areaToBuild = areaDesignPanel.getAreaToConstruct();
            if (areaDesignPanel != null && areaToBuild != null) {
                //Then construct area
                city.addArea(new ConstructingArea(10_000, areaToBuild));
                JOptionPane.showInternalMessageDialog(AreaConstructionPanel.this, "Created Area!");
            }
        });
        constructButton.setFocusable(false);

        areaConstructionInfo.setBorder(new LineBorder(Color.gray));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;

        add(new JScrollPane(areaTypeList), BorderLayout.WEST);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        add(areaConstructionInfo, BorderLayout.CENTER);

        add(constructButton, BorderLayout.SOUTH);
        areaTypeList.setSelectedIndex(0);
    }
}
