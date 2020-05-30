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

import ConquerSpace.game.districts.City;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.gui.game.planetdisplayer.construction.AreaDesignPanel;
import ConquerSpace.gui.game.planetdisplayer.construction.MinerAreaConstructionPanel;
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

    private static String[] AREA_LIST_NAMES = {"Mine", "Manufacturer", "Power Planet", "Space Port"};

    private AreaDesignPanel areaDesignPanel = null;

    public AreaConstructionPanel(Planet planet, City city) {
        setLayout(new BorderLayout());

        areaTypeListModel = new DefaultListModel<>();
        for (int i = 0; i < AREA_LIST_NAMES.length; i++) {
            areaTypeListModel.addElement(AREA_LIST_NAMES[i]);
        }
        areaTypeList = new JList<>(areaTypeListModel);
        areaTypeList.addListSelectionListener(l -> {
            areaConstructionInfo.removeAll();
            //Get selected area type
            switch (areaTypeList.getSelectedIndex()) {
                case 0:
                    areaDesignPanel = new MinerAreaConstructionPanel(planet, city);
                    break;
                default:
                    areaDesignPanel = new AreaDesignPanel(planet, city);
                    break;
            }
            areaConstructionInfo.add(areaDesignPanel, BorderLayout.CENTER);
        });

        areaConstructionInfo = new JPanel(new BorderLayout());

        constructButton = new JButton("Construct!");
        constructButton.addActionListener(l -> {
            if (areaDesignPanel != null && areaDesignPanel.getAreaToConstruct() != null) {
                //Then construct area
                city.addArea(areaDesignPanel.getAreaToConstruct());
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
