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
import ConquerSpace.client.gui.game.planetdisplayer.areas.AreaInformationPanelBuilder;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.city.area.Area;
import ConquerSpace.common.game.universe.bodies.Planet;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/**
 *
 * @author EhWhoAmI
 */
public class PlanetIndustry extends JPanel {
    
    private JTabbedPane tabs;
    
    private JPanel areaContainer;
    private DefaultListModel<Area> areaDefaultListModel;
    private JList<Area> areaList;
    
    private JPanel areaInfoPanel;

    //Sorts out all the buildings and places
    private JPanel jobSortingOutPanel;
    private DefaultListModel<String> industryListModel;
    private JList<String> industryList;
    
    private JPanel industryInfoContainer;
    private CardLayout industryInfoCardLayout;
    
    private Planet p;
    
    private GameState gameState;
    
    public PlanetIndustry(GameState gameState, Planet planet) {
        this.p = planet;
        this.gameState = gameState;
        setLayout(new VerticalFlowLayout());
        add(new JLabel(LOCALE_MESSAGES.getMessage("game.planet.industry.title")));
        tabs = new JTabbedPane();
        //Industry stuff includes:
        //Labs, Foundry, Ship yard, Production line, Mill, Etc... 
        //Get the cities of the planet...

        areaContainer = new JPanel();
        areaContainer.setLayout(new GridBagLayout());
        areaDefaultListModel = new DefaultListModel<>();
        
        for (int i = 0; i < planet.cities.size(); i++) {
            City city = gameState.getObject(planet.cities.get(i), City.class);
            for (ObjectReference areaId : city.areas) {
                Area area = gameState.getObject(areaId, Area.class);
                areaDefaultListModel.addElement(area);
            }
        }
        
        areaList = new JList<>(areaDefaultListModel);
        
        areaList.addListSelectionListener(l -> {
            areaInfoPanel.removeAll();
            AreaInformationPanelBuilder builder = new AreaInformationPanelBuilder(gameState);
            areaList.getSelectedValue().accept(builder);
            areaInfoPanel.add(builder.getPanel(areaList.getSelectedValue()));
        });
        
        JScrollPane scrollPane = new JScrollPane(areaList);
        
        areaInfoPanel = new JPanel();
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.BOTH;
        areaContainer.add(scrollPane, constraints);
        constraints = new java.awt.GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        areaContainer.add(areaInfoPanel, constraints);
        
        jobSortingOutPanel = new JPanel(new HorizontalFlowLayout());
        
        industryListModel = new DefaultListModel<>();
        industryListModel.addElement("Farming");
        industryList = new JList<>(industryListModel);
        
        industryList.addListSelectionListener(l -> {
            if (industryList.getSelectedValue().equals("Farming")) {
                industryInfoCardLayout.show(industryInfoContainer, "Farm");
            }
        });
        
        industryInfoContainer = new JPanel();
        industryInfoCardLayout = new CardLayout();
        industryInfoContainer.setLayout(industryInfoCardLayout);
        
        jobSortingOutPanel.add(new JScrollPane(industryList));
        jobSortingOutPanel.add(industryInfoContainer);
        
        tabs.add(jobSortingOutPanel, LOCALE_MESSAGES.getMessage("game.planet.industry.industries"));
        add(tabs);
    }
    
    public void update() {
        int selectedArea = areaList.getSelectedIndex();
        areaDefaultListModel.clear();
        for (int i = 0; i < p.cities.size(); i++) {
            City city = gameState.getObject(p.cities.get(i), City.class);
            Iterator<ObjectReference> iterator = city.areas.iterator();
            while (iterator.hasNext()) {
                ObjectReference in = iterator.next();
                Area area = gameState.getObject(in, Area.class);
                areaDefaultListModel.addElement(area);
            }
        }
        areaList.setSelectedIndex(selectedArea);
    }
}
