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

import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.common.GameState;
import ConquerSpace.common.game.characters.Person;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.universe.bodies.Galaxy;
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
    private JPanel populationPanel;
    private JPanel governmentPanel;
    private JTabbedPane mainTabs;
    
    private JLabel civLeaderLabel;

    Civilization civ;

    public CivInfoOverview(GameState gameState, Civilization civ) {
        this.civ = civ;
        setLayout(new BorderLayout());
        //Civ name
        mainTabs = new JTabbedPane(JTabbedPane.BOTTOM);

        mainPanel = new JPanel();
        mainPanel.setLayout(new VerticalFlowLayout());

        JLabel civName = new JLabel(LOCALE_MESSAGES.getMessage("game.civinfo.name", civ.getName()));
        JLabel civHomePlanet = new JLabel(LOCALE_MESSAGES.getMessage("game.civinfo.homeplanet", civ.getHomePlanetName()));
        JLabel civTechLevel = new JLabel(LOCALE_MESSAGES.getMessage("game.civinfo.tech", civ.getTechLevel()));
        JLabel civFoundingSpecies = new JLabel(LOCALE_MESSAGES.getMessage("game.civinfo.founding", civ.getFoundingSpecies().getName()));
        City capitalCity = gameState.getObject(civ.getCapitalCity(), City.class);
        JLabel capital = new JLabel(LOCALE_MESSAGES.getMessage("game.civinfo.capital", capitalCity.getName(), civ.getCapitalPlanet().getName()));
        JLabel currency = new JLabel(LOCALE_MESSAGES.getMessage("game.civinfo.currency", civ.getNationalCurrency().getName()));

        mainPanel.add(civName);
        mainPanel.add(civHomePlanet);
        mainPanel.add(civTechLevel);
        mainPanel.add(civFoundingSpecies);
        mainPanel.add(capital);
        mainPanel.add(currency);

        populationPanel = new JPanel();
        //Add population count and stuff
//        JLabel populationLabel = new JLabel("Population: " + (c.population.size() * 10) + " million");
//        populationPanel.add(populationLabel);

        governmentPanel = new JPanel(new VerticalFlowLayout());

        Person p = civ.government.officials.get(civ.government.headofState);
        civLeaderLabel = new JLabel(civ.government.headofState.getTitleName() + " " + p.getName());
        governmentPanel.add(civLeaderLabel);

        mainTabs.addTab(LOCALE_MESSAGES.getMessage("game.civinfo.tabs.civ"), mainPanel);
        //mainTabs.addTab("Resources", resourcesPanel);
        mainTabs.addTab(LOCALE_MESSAGES.getMessage("game.civinfo.tabs.pops"), populationPanel);
        mainTabs.addTab(LOCALE_MESSAGES.getMessage("game.civinfo.tabs.gov"), governmentPanel);
        add(mainTabs, BorderLayout.CENTER);

        //Updating code
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                updateLeaderLabel();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                updateLeaderLabel();
            }
        });
        setVisible(true);
    }

    private void updateLeaderLabel() {
        Person p = civ.government.officials.get(civ.government.headofState);

        //populationLabel.setText("Population: " + (c.population.size() * 10) + " million");
        civLeaderLabel.setText(LOCALE_MESSAGES.getMessage("game.civinfo.headofstate", civ.government.headofState.getTitleName(), p.getName()));
    }
}
