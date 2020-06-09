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

import ConquerSpace.game.civilization.Civilization;
import ConquerSpace.game.city.City;
import ConquerSpace.game.city.area.Area;
import ConquerSpace.game.universe.bodies.Planet;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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

    public PlanetIndustry(Planet p, Civilization c) {
        this.p = p;
        setLayout(new VerticalFlowLayout());
        add(new JLabel("Industry"));
        tabs = new JTabbedPane();
        //Industry stuff includes:
        //Labs, Foundry, Ship yard, Production line, Mill, Etc... 
        //Get the cities of the planet...

        areaContainer = new JPanel();
        areaContainer.setLayout(new GridBagLayout());
        areaDefaultListModel = new DefaultListModel<>();
        for (City city : p.cityDistributions.values()) {
            for (Area a : city.areas) {
                areaDefaultListModel.addElement(a);
            }
        }
        areaList = new JList<>(areaDefaultListModel);

        areaList.addListSelectionListener(l -> {
            areaInfoPanel.removeAll();
            areaInfoPanel.add(new AreaInformationPanel(areaList.getSelectedValue()));
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
        tabs.add(areaContainer, "Areas");
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

        tabs.add(jobSortingOutPanel, "Industries");

        //availableJobs = new JPanel();
        //availableJobListModel = new JobListModel(p.planetJobs);
        //availableJobList = new JList<>(availableJobListModel);
        //availableJobs.add(new JScrollPane(availableJobList));
        //tabs.add(availableJobs, "Available jobs");
        add(tabs);
    }

    public void update() {
        int selectedArea = areaList.getSelectedIndex();
        areaDefaultListModel.clear();
        for (City city : p.cityDistributions.values()) {
            //Throws currentmodificationexception, so need to fix in the future
            for (Area a : city.areas) {
                areaDefaultListModel.addElement(a);
            }
        }
        areaList.setSelectedIndex(selectedArea);
    }
}
