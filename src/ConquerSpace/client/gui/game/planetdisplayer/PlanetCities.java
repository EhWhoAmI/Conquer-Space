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
import ConquerSpace.client.gui.game.planetdisplayer.areas.AreaInformationPanel;
import ConquerSpace.client.gui.game.planetdisplayer.city.CityInformationPanel;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.city.CityType;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.universe.bodies.Galaxy;
import ConquerSpace.common.game.universe.bodies.Planet;
import ConquerSpace.common.util.Utilities;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

/**
 *
 * @author EhWhoAmI
 */
public class PlanetCities extends JPanel {

    private JPanel growthPanel;
    private JPanel currentStats;
    private JLabel populationCount;
    private JLabel averagePlanetPopGrowthLabel;
    private JPanel cityListPanel;
    private DefaultListModel<City> cityListModel;
    JScrollPane cityListScrollPane;
    private JList<City> cityList;

    private JPanel cityData;

    private Planet planet;

    CityInformationPanel cityInformationPanel;

    //private int citySelectedTab = 0;
    //So that the tab for the employment and things stay the same as you change your selection
    boolean isBuildingUi = false;

    private City currentlySelectedCity;

    private Civilization owner;

    private PlanetInfoSheet parent;
    private GameState gameState;

    private int cityContainerHeight = 800;

    private AreaInformationPanel areaInformationPanel;
    PlanetMapProvider provider;

    public PlanetCities(GameState gameState, Planet p, Civilization civ, PlanetInfoSheet parent, PlanetMapProvider provider) {
        this.gameState = gameState;
        this.planet = p;
        this.owner = civ;
        this.parent = parent;
        this.provider = provider;
        setLayout(new VerticalFlowLayout());

        growthPanel = new JPanel();
        growthPanel.setLayout(new VerticalFlowLayout());
        currentStats = new JPanel(new VerticalFlowLayout());

        currentStats.setBorder(
                BorderFactory.createTitledBorder(
                        new LineBorder(Color.GRAY),
                        LOCALE_MESSAGES.getMessage("game.planet.cities.planetoverview.currentstats")));

        populationCount = new JLabel(
                LOCALE_MESSAGES.getMessage("game.planet.cities.planetoverview.population", Utilities.longToHumanString(p.population)));
        currentStats.add(populationCount);

        averagePlanetPopGrowthLabel = new JLabel(
                LOCALE_MESSAGES.getMessage("game.planet.cities.planetoverview.averagegrowth", p.populationIncrease));

        cityListPanel = new JPanel();
        cityListPanel.setLayout(new BorderLayout(5, 5));

        cityListModel = new DefaultListModel<>();
        for (ObjectReference cityId : p.cities) {
            City city = gameState.getObject(cityId, City.class);

            cityListModel.addElement(city);
        }
        JXTaskPaneContainer taskPaneContainer = new JXTaskPaneContainer();
        JXTaskPane index = new JXTaskPane();
        index.setTitle("Legend");
        index.setSpecial(true);

        for (int i = 0; i < CityType.values().length; i++) {
            CityType cityType = CityType.values()[i];
            Color color = CityType.getDistrictColor(cityType);
            BufferedImage image = new BufferedImage(8, 8, BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D g2d = (Graphics2D) image.getGraphics();
            g2d.setColor(color);
            g2d.fill(new Rectangle2D.Float(0, 0, 8, 8));
            JLabel label = new JLabel(cityType.name(), new ImageIcon(image), JLabel.LEADING);
            index.add(label);
        }
        index.setCollapsed(true);
        index.setToolTipText(LOCALE_MESSAGES.getMessage("game.planet.cities.legend.tooltip"));

        index.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int heightForList = cityContainerHeight - taskPaneContainer.getSize().height;
                //Reduce rows
                int things = heightForList / cityList.getFixedCellHeight();
                cityList.setVisibleRowCount(things);
                cityList.revalidate();
            }
        });
        index.setAnimated(false);

        taskPaneContainer.add(index);
        taskPaneContainer.setBackground(UIManager.getDefaults().getColor("Panel.background"));

        cityList = new JList<>(cityListModel);
        cityList.setSelectedIndex(0);
        int cellHeight = 18;
        cityList.setFixedCellHeight(cellHeight);
        cityList.setFixedCellWidth(32);
        cityList.setCellRenderer(new CityListRenderer());

        cityList.addListSelectionListener(l -> {
            selectCity();
        });

        cityListScrollPane = new JScrollPane(cityList);
        cityList.setVisibleRowCount(50);

        JPanel contain = new JPanel();
        contain.setLayout(new VerticalFlowLayout());
        contain.add(taskPaneContainer);
        contain.add(cityListScrollPane);
        cityListPanel.add(contain, BorderLayout.WEST);

        cityListPanel.setBorder(
                BorderFactory.createTitledBorder(
                        new LineBorder(Color.GRAY),
                        LOCALE_MESSAGES.getMessage("game.planet.cities.citytitle")));

        //City info
        cityData = new JPanel();
        cityData.setLayout(new BorderLayout());
        cityListPanel.add(cityData, BorderLayout.CENTER);

        growthPanel.add(currentStats);
        growthPanel.add(cityListPanel);
        //tabs.add(jobContainer, "Jobs");
        add(growthPanel);

        //Select first city
        if (!cityListModel.isEmpty()) {
            showCity(cityListModel.get(0));
        }
    }

    private void selectCity() {
        isBuildingUi = true;

//        if (cityInformationPanel != null) {
//            citySelectedTab = cityInformationPanel.getSelectedTab();
//        }

        cityData.removeAll();

        currentlySelectedCity = cityList.getSelectedValue();
        if (currentlySelectedCity != null) {
            cityInformationPanel = new CityInformationPanel(gameState, this, currentlySelectedCity, planet, owner, provider);

            ///cityInformationPanel.setSelectedTab(citySelectedTab);
            cityData.add(cityInformationPanel, BorderLayout.CENTER);
            isBuildingUi = false;
            revalidate();
            repaint();
        }
    }

    public void showCity(City whichCity) {
        //Determine if on planet
        if (whichCity != null && cityListModel.contains(whichCity)) {
            cityList.setSelectedValue(whichCity, true);
            selectCity();
        }
    }

    public void update() {
        //Rewrite cities
        if (cityListModel.size() != planet.cities.size()) {
            cityListModel.clear();
            for (ObjectReference cityId : planet.cities) {
                City city = gameState.getObject(cityId, City.class);
                if (city != null) {
                    cityListModel.addElement(city);
                }
            }
        }
        if (areaInformationPanel != null) {
            areaInformationPanel.update();
        }
    }

    private class CityListRenderer extends DefaultListCellRenderer {

        public CityListRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {

            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (value instanceof City) {
                City city = (City) value;

                Color foregroundColor = CityType.getDistrictColor(city.getCityType());
                c.setForeground(foregroundColor);
            }
            return c;
        }
    }
}
