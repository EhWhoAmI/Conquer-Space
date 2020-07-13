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

import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.common.game.people.Person;
import ConquerSpace.common.game.universe.bodies.Universe;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * All the info and charts and stuff of the civ.
 *
 * @author EhWhoAmI
 */
public class CivInfoOverview extends JPanel {

    private JPanel mainPanel;
    private JPanel resourcesPanel;
    private JPanel populationPanel;
    private JPanel governmentPanel;
    private JTabbedPane mainTabs;

    public CivInfoOverview(Civilization c, Universe u) {
        setLayout(new BorderLayout());
        //Civ name
        mainTabs = new JTabbedPane(JTabbedPane.BOTTOM);

        mainPanel = new JPanel();
        mainPanel.setLayout(new VerticalFlowLayout());

        JLabel civName = new JLabel(c.getName());
        JLabel civHomePlanet = new JLabel("From " + c.getHomePlanetName());
        JLabel civTechLevel = new JLabel("Tech level: " + c.getTechLevel());
        JLabel civFoundingSpecies = new JLabel("Founding Species: " + c.getFoundingSpecies().getName());
        JLabel capital = new JLabel("Capital: " + c.getCapitalCity().getName() + " on " + c.getCapitalPlanet().getName());
        JLabel currency = new JLabel("National currency: " + c.getNationalCurrency().getName());

        mainPanel.add(civName);
        mainPanel.add(civHomePlanet);
        mainPanel.add(civTechLevel);
        mainPanel.add(civFoundingSpecies);
        mainPanel.add(capital);
        mainPanel.add(currency);

        resourcesPanel = new JPanel();

        populationPanel = new JPanel();
        //Add population count and stuff
//        JLabel populationLabel = new JLabel("Population: " + (c.population.size() * 10) + " million");
//        populationPanel.add(populationLabel);

        governmentPanel = new JPanel(new VerticalFlowLayout());

        Person p = c.government.officials.get(c.government.headofState);
        JLabel civLeader = new JLabel(c.government.headofState.getName() + " " + p.getName());
        governmentPanel.add(civLeader);

        mainTabs.addTab("Civilization", mainPanel);
        //mainTabs.addTab("Resources", resourcesPanel);
        mainTabs.addTab("Population", populationPanel);
        mainTabs.addTab("Government", governmentPanel);
        add(mainTabs, BorderLayout.CENTER);

        //Updating code
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                //Update info...
                Person p = c.government.officials.get(c.government.headofState);

                //populationLabel.setText("Population: " + (c.population.size() * 10) + " million");
                civLeader.setText(c.government.headofState.getName() + " " + p.getName());
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                //Update info...
                Person p = c.government.officials.get(c.government.headofState);

                //populationLabel.setText("Population: " + (c.population.size() * 10) + " million");
                civLeader.setText(c.government.headofState.getName() + " " + p.getName());
            }
        });
        setVisible(true);
    }

}
