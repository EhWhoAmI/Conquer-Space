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

import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.client.gui.game.planetdisplayer.construction.AreaDesignPanel;
import ConquerSpace.client.gui.game.planetdisplayer.construction.IndustrialFactoryConstructionPanel;
import ConquerSpace.client.gui.game.planetdisplayer.construction.MinerAreaConstructionPanel;
import ConquerSpace.client.gui.game.planetdisplayer.construction.ObservatoryConstructionPanel;
import ConquerSpace.client.gui.game.planetdisplayer.construction.SpacePortConstructionPanel;
import ConquerSpace.common.GameState;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.city.area.AreaFactory;
import ConquerSpace.common.game.city.area.ConstructingArea;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.universe.bodies.Planet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.util.HashMap;
import java.util.Map;
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

    private DefaultListModel<AreaListNames> areaTypeListModel;
    private JList<AreaListNames> areaTypeList;

    private JButton constructButton;

    private JPanel areaConstructionInfo;

    private enum AreaListNames {
        Mine("game.planet.construction.mine"),
        Manufacturer("game.planet.construction.factory"),
        PowerPlant("game.planet.construction.powerplant"),
        SpacePort("game.planet.construction.spaceport"),
        Observatory("game.planet.construction.observatory");

        //LOCALE_MESSAGES.getMessage(
        String text;

        private AreaListNames(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return LOCALE_MESSAGES.getMessage(text);
        }
    }

    private AreaDesignPanel areaDesignPanel = null;

    public AreaConstructionPanel(GameState gameState,
            Planet planet,
            Civilization civilization,
            City city) {
        setLayout(new BorderLayout());

        areaTypeListModel = new DefaultListModel<>();
        for (int i = 0; i < AreaListNames.values().length; i++) {
            //Has launch capability
            if (AreaListNames.values()[i].equals(AreaListNames.SpacePort)) {
                if (civilization.values.containsKey("haslaunch") && civilization.values.get("haslaunch") == 1) {
                    areaTypeListModel.addElement(AreaListNames.values()[i]);
                }
            } else {
                areaTypeListModel.addElement(AreaListNames.values()[i]);
            }
        }
        areaTypeList = new JList<>(areaTypeListModel);
        areaTypeList.addListSelectionListener(l -> {
            areaConstructionInfo.removeAll();
            //Get selected area type
            switch (areaTypeList.getSelectedValue()) {
                default:
                    //Show nothing
                    break;
                case Mine:
                    areaDesignPanel = new MinerAreaConstructionPanel(gameState, planet, city, civilization);
                    break;
                case SpacePort:
                    areaDesignPanel = new SpacePortConstructionPanel(gameState, planet, city, civilization);
                    break;
                case Manufacturer:
                    areaDesignPanel = new IndustrialFactoryConstructionPanel(gameState, planet, city, civilization);
                    break;
                case Observatory:
                    areaDesignPanel = new ObservatoryConstructionPanel(gameState, planet, city, civilization);
                    break;

            }
            if (areaDesignPanel != null) {
                areaConstructionInfo.add(areaDesignPanel, BorderLayout.PAGE_START);

                //Repaint so it shows immediately
                areaConstructionInfo.revalidate();
                areaConstructionInfo.repaint();
            }
        });

        areaConstructionInfo = new JPanel(new BorderLayout());

        constructButton = new JButton(LOCALE_MESSAGES.getMessage("game.planet.construction.construct"));
        constructButton.addActionListener(l -> {
            AreaFactory areaToBuild = areaDesignPanel.getAreaToConstruct();

            if (areaDesignPanel != null && areaToBuild != null) {
                //Then construct area
                ConstructingArea area = new ConstructingArea(gameState, areaToBuild.buildTime(), areaToBuild.build(gameState));
                //Set cost
                HashMap<Integer, Double> cost = new HashMap<>();
                for (Map.Entry<Integer, Double> entry : areaToBuild.getCost().entrySet()) {
                    Integer key = entry.getKey();
                    Double val = entry.getValue();
                    cost.put(key, val / areaToBuild.buildTime());
                }
                area.setCostPerTurn(cost);

                city.addArea(area.getReference());
                JOptionPane.showInternalMessageDialog(AreaConstructionPanel.this, LOCALE_MESSAGES.getMessage("game.planet.construction.created"));
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

    public void update() {

    }
}
