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
import ConquerSpace.client.gui.game.planetdisplayer.AreaConstructionPanel;
import ConquerSpace.client.gui.game.planetdisplayer.areas.AreaInformationPanelBuilder;
import ConquerSpace.common.GameState;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.city.area.Area;
import ConquerSpace.common.game.city.area.AreaClassification;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.universe.bodies.Planet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

/**
 *
 * @author EhWhoAmI
 */
class CityIndustryPanel extends JPanel {

    JScrollPane scrollPane;
    boolean hasChangedSize = false;
    AreaConstructionPanel areaConstructionPanel;
    JTabbedPane tabs;
    private City selectedCity;
    private GameState gameState;
    private static int selectedTab = 0;

    public CityIndustryPanel(City selectedCity, Civilization civilization, Planet planet, GameState gameState) {
        this.selectedCity = selectedCity;
        this.gameState = gameState;
        setLayout(new BorderLayout());
        //Do industry and areas
        JXTaskPaneContainer industriesList = new JXTaskPaneContainer();
        //Get list of industries
        HashMap<AreaClassification, Integer> industriesMap = new HashMap<>();
        for (int i = 0; i < selectedCity.areas.size(); i++) {
            Area area = gameState.getObject(selectedCity.areas.get(i), Area.class);
            if (area != null) {
                if (industriesMap.containsKey(area.getAreaType())) {
                    industriesMap.put(area.getAreaType(), industriesMap.get(area.getAreaType()) + 1);
                } else {
                    industriesMap.put(area.getAreaType(), 1);
                }
            }
        }
        for (Map.Entry<AreaClassification, Integer> entry : industriesMap.entrySet()) {
            AreaClassification key = entry.getKey();
            Integer val = entry.getValue();
            JXTaskPane pane = new JXTaskPane(key.toString() + " - " + val.toString());
            //List all areas of this type
            for (int i = 0; i < selectedCity.areas.size(); i++) {
                Area area = gameState.getObject(selectedCity.areas.get(i), Area.class);
                if (area.getAreaType().equals(key)) {
                    //Then add to list I guess
                    AreaInformationPanelBuilder builder = new AreaInformationPanelBuilder(gameState);
                    area.accept(builder);
                    pane.add(builder.getPanel(area));
                }
            }
            pane.setCollapsed(true);
            pane.setAnimated(false);
            industriesList.add(pane);
        }
        //Set to better colors
        industriesList.setBackground(UIManager.getDefaults().getColor("Panel.background"));
        industriesList.setForeground(UIManager.getDefaults().getColor("Label.foreground"));
        industriesList.setFont(UIManager.getDefaults().getFont("Label.font"));
        scrollPane = new JScrollPane(industriesList);
        scrollPane.setPreferredSize(new Dimension(831, 312));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaConstructionPanel = new AreaConstructionPanel(gameState, planet, civilization, selectedCity);
        CityProductionPanel production = new CityProductionPanel(civilization, planet, gameState);
        tabs = new JTabbedPane();
        tabs.add(ConquerSpace.LOCALE_MESSAGES.getMessage("game.planet.cities.tab.industries"), scrollPane);
        tabs.add(ConquerSpace.LOCALE_MESSAGES.getMessage("game.planet.cities.tab.construction"), areaConstructionPanel);
        tabs.add(ConquerSpace.LOCALE_MESSAGES.getMessage("game.planet.cities.production"), production);

        tabs.setSelectedIndex(selectedTab);
        tabs.addChangeListener(l -> {
            selectedTab = tabs.getSelectedIndex();
        });

        add(tabs, BorderLayout.CENTER);
        setBorder(new TitledBorder(new LineBorder(Color.gray), ConquerSpace.LOCALE_MESSAGES.getMessage("game.planet.cities.industries")));
        setPreferredSize(new Dimension(831, 312));
    } //Do industry and areas
    //Get list of industries
    //List all areas of this type
    //Then add to list I guess
    //Set to better colors

}
