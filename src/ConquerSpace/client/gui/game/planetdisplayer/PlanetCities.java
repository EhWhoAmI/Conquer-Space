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
import ConquerSpace.client.gui.ObjectListModel;
import ConquerSpace.client.gui.game.planetdisplayer.areas.AreaInformationPanel;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.city.CityType;
import ConquerSpace.common.game.city.area.Area;
import ConquerSpace.common.game.city.modifier.CityModifier;
import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.common.game.population.Population;
import ConquerSpace.common.game.population.jobs.JobType;
import ConquerSpace.common.game.population.jobs.Workable;
import ConquerSpace.common.game.universe.bodies.Galaxy;
import ConquerSpace.common.game.universe.bodies.Planet;
import ConquerSpace.common.util.Utilities;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

/**
 *
 * @author EhWhoAmI
 */
public class PlanetCities extends JPanel {

    private JTabbedPane tabs;

    private JPanel growthPanel;
    private JPanel currentStats;
    private JLabel populationCount;
    private JLabel averagePlanetPopGrowthLabel;
    private JPanel cityListPanel;
    private DefaultListModel<City> cityListModel;
    JScrollPane cityListScrollPane;
    private JList<City> cityList;
    //private DefaultListModel<AreaWrapper> areaListModel;
    //private JList<AreaWrapper> areaList;

    private JTabbedPane cityInfoTabs;

    private JTable jobTable;
    private JobTableModel jobTableModel;
    private final String[] jobListTableColunmNames = {
        LOCALE_MESSAGES.getMessage("game.planet.cities.table.jobname"),
        LOCALE_MESSAGES.getMessage("game.planet.cities.table.count"),
        LOCALE_MESSAGES.getMessage("game.planet.cities.table.percentage")};

    private JTable availableJobTable;
    private DefaultTableModel availableJobModel;
    private final String[] availableJobColunmNames = {
        LOCALE_MESSAGES.getMessage("game.planet.cities.table.jobname"),
        LOCALE_MESSAGES.getMessage("game.planet.cities.table.count")};

    private JPanel cityData;

    private AreaConstructionPanel areaConstructionPanel;

    private Planet planet;

    private long population = 1;

    private int citySelectedTab = 0;
    //So that the tab for the employment and things stay the same as you change your selection
    boolean isBuildingUi = false;

    private City currentlySelectedCity;

    private DefaultListModel<Area> areaListModel;
    private JList<Area> areaList;

    private Galaxy universe;
    private Civilization owner;

    private PlanetInfoSheet parent;
    private GameState gameState;

    private int cityContainerHeight = 800;

    private AreaInformationPanel areaInformationPanel;

    public PlanetCities(GameState gameState, Planet p, Civilization civ, PlanetInfoSheet parent) {
        this.universe = gameState.getUniverse();
        this.gameState = gameState;
        this.planet = p;
        this.owner = civ;
        this.parent = parent;
        tabs = new JTabbedPane();
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
        index.setSpecial(true);  // actions can be added, a hyperlink will be created 

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
        cityInfoTabs = new JTabbedPane();
        cityInfoTabs.addChangeListener(c -> {
            if (cityInfoTabs.getSelectedIndex() > -1 && !isBuildingUi) {
                citySelectedTab = cityInfoTabs.getSelectedIndex();
            }
            if (areaListModel != null && cityList.getSelectedValue() != null) {
                for (ObjectReference areaId : cityList.getSelectedValue().areas) {
                    Area area = gameState.getObject(areaId, Area.class);
                    if (!areaListModel.contains(area)) {
                        areaListModel.addElement(area);
                    }
                }

            }
        });
        cityList.setCellRenderer(new CityListRenderer());

        //Initialize table
        jobTableModel = new JobTableModel();

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
        cityData.setLayout(new VerticalFlowLayout());
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
        cityData.removeAll();

        City selectedCity = cityList.getSelectedValue();

        int popcount = 0;
        float increment = 0;
        int maxPop = 0;
        int energyUsage = selectedCity.getEnergyNeeded();
        int energyProvided = selectedCity.getEnergyProvided();

        JLabel cityName = new JLabel(selectedCity.getName());
        cityData.add(cityName);

        //Check if capital city
        for (int i = 0; i < gameState.getCivilizationCount(); i++) {
            Civilization civilization = gameState.getCivilizationObject(i);
            if (civilization.getCapitalCity().equals(cityList.getSelectedValue())) {
                JLabel isCapital = new JLabel(
                        LOCALE_MESSAGES.getMessage("game.planet.cities.capital", civilization.getName()));
                cityData.add(isCapital);
                break;
            }
        }

        //Population
        JLabel popCount = new JLabel(
                LOCALE_MESSAGES.getMessage("game.planet.cities.population", Utilities.longToHumanString(gameState.getObject(selectedCity.population, Population.class).getPopulationSize())));
        cityData.add(popCount);

        JLabel priindustry = new JLabel(
                LOCALE_MESSAGES.getMessage("game.planet.cities.table.priindustry", selectedCity.getCityType()));
        cityData.add(priindustry);

        //Get the number of powerplants leading to it
        //Energy usage
        JLabel energyUsageLabel = new JLabel(
                LOCALE_MESSAGES.getMessage("game.planet.cities.energyusage", energyUsage, energyProvided));
        cityData.add(energyUsageLabel);

        //Growth
        JLabel growthAmount = new JLabel(
                LOCALE_MESSAGES.getMessage("game.planet.cities.growth", 0));//new JLabel("Growth: " + (selected.getPopulationUnitPercentage()) + "% done, " + increment + "% within the next 40 days.");
        cityData.add(growthAmount);

        double unemploymentRate = selectedCity.getUnemploymentRate();
        JLabel unemployment = new JLabel("Unemployment rate: " + Math.round(unemploymentRate * 100) + "%");
        unemployment.setForeground(new Color((float) unemploymentRate, 0f, 0f));
        cityData.add(unemployment);

        //Max population
        JLabel maxPopulation = new JLabel(
                LOCALE_MESSAGES.getMessage("game.planet.cities.popcap", Utilities.longToHumanString(maxPop)));
        cityData.add(maxPopulation);

        cityData.add(maxPopulation);

        //Check for govenor
        if (cityList.getSelectedValue().getGovernor() != null) {
            JLabel governorLabel = new JLabel(
                    LOCALE_MESSAGES.getMessage("game.planet.cities.governor", selectedCity.getGovernor().getName()));
            cityData.add(governorLabel);
        }

        ObjectListModel<CityModifier> modifierListModel = new ObjectListModel<>();
        modifierListModel.setElements(selectedCity.cityModifiers);
        modifierListModel.setHandler(l -> {
            return (l.toString());
        });

        JList<String> modifierList = new JList<>(modifierListModel);
        cityData.add(new JScrollPane(modifierList));

        JButton viewResourceButton = new JButton(
                LOCALE_MESSAGES.getMessage("game.planet.cities.viewresources"));
        viewResourceButton.addActionListener(l -> {
            parent.setSelectedTab(8);
            parent.planetResources.jTabbedPane1.setSelectedIndex(1);
            parent.planetResources.selectStockpile(currentlySelectedCity);
        });
        cityData.add(viewResourceButton);

        JPanel areaInfoPanel = new JPanel(new HorizontalFlowLayout());

        //Areas
        areaListModel = new DefaultListModel<>();
        for (ObjectReference areaId : selectedCity.areas) {
            Area area = gameState.getObject(areaId, Area.class);
            areaListModel.addElement(area);
        }

        areaList = new JList<>(areaListModel);
        JScrollPane areascrollPane = new JScrollPane(areaList);

        JPanel areaInfoContainerPanel = new JPanel();

        areaList.addListSelectionListener(o -> {
            areaInfoContainerPanel.removeAll();
            areaInformationPanel = AreaInformationPanel.getPanel(gameState, areaList.getSelectedValue());
            if (areaInformationPanel != null) {
                areaInfoContainerPanel.add(areaInformationPanel);
            }
            areaInfoContainerPanel.validate();
            areaInfoContainerPanel.repaint();
            cityData.validate();
            cityData.repaint();
        });

        areaInfoPanel.add(areascrollPane);
        areaInfoPanel.add(areaInfoContainerPanel);

        currentlySelectedCity = cityList.getSelectedValue();
        jobTableModel.newCitySelection();
        jobTable = new JTable(jobTableModel);

        availableJobModel = new DefaultTableModel(availableJobColunmNames, 0);
        //Fill up
        availableJobTable = new JTable(availableJobModel);

        areaConstructionPanel = new AreaConstructionPanel(gameState, planet, owner, selectedCity);

        cityInfoTabs.removeAll();
        cityInfoTabs.add(LOCALE_MESSAGES.getMessage("game.planet.cities.areas"), areaInfoPanel);
        cityInfoTabs.add(LOCALE_MESSAGES.getMessage("game.planet.cities.employment"), new JScrollPane(jobTable));
        cityInfoTabs.add(LOCALE_MESSAGES.getMessage("game.planet.cities.jobs"), new JScrollPane(availableJobTable));
        cityInfoTabs.add(LOCALE_MESSAGES.getMessage("game.planet.cities.construction"), areaConstructionPanel);
        cityInfoTabs.add(LOCALE_MESSAGES.getMessage("game.planet.cities.resourcedemand"), new JScrollPane(new JTable(new StockpileStorageModel())));

        cityInfoTabs.setSelectedIndex(citySelectedTab);
        cityData.add(cityInfoTabs);

        //Select first area
        if (!selectedCity.areas.isEmpty()) {
            //Select first area
            areaList.setSelectedIndex(0);
        }
        isBuildingUi = false;
        revalidate();
        repaint();
    }

    public void showCity(City whichCity) {
        //Determine if on planet
        if (whichCity != null && cityListModel.contains(whichCity)) {
            cityList.setSelectedValue(whichCity, true);
            selectCity();
        }
    }

    public void update() {
        if (areaInformationPanel != null) {
            areaInformationPanel.update();
        }
    }

    private class JobTableModel extends AbstractTableModel {

        private long currentlyWorking;

        HashMap<JobType, Integer> populationCount;

        public JobTableModel() {
            populationCount = new HashMap<>();
        }

        private void newCitySelection() {
            currentlyWorking = 0;
            populationCount.clear();
            population = gameState.getObject(currentlySelectedCity.population, Population.class).getPopulationSize();
            //Get population job
            for (ObjectReference areaId : currentlySelectedCity.areas) {
                Area area = gameState.getObject(areaId, Area.class);
                if (!populationCount.containsKey(area.getJobClassification())) {
                    populationCount.put(area.getJobClassification(), area.getCurrentlyManningJobs());
                    currentlyWorking += area.getCurrentlyManningJobs();
                } else {
                    int count = populationCount.get(area.getJobClassification());
                    count += area.getCurrentlyManningJobs();
                    currentlyWorking += area.getCurrentlyManningJobs();
                    populationCount.put(area.getJobClassification(), count);
                }
            }
            if (!populationCount.containsKey(JobType.Jobless)) {
                populationCount.put(JobType.Jobless, (int) (population - currentlyWorking));
            } else {
                int count = populationCount.get(JobType.Jobless);
                count += (int) (population - currentlyWorking);
                populationCount.put(JobType.Jobless, count);
            }
        }

        @Override
        public int getRowCount() {
            return populationCount.size();
        }

        @Override
        public int getColumnCount() {
            return jobListTableColunmNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            //Get the colunm index text and stuff
            int i = 0;
            JobType jobType = null;
            for (Map.Entry<JobType, Integer> entry : populationCount.entrySet()) {
                if (i == rowIndex) {
                    i = entry.getValue();
                    jobType = entry.getKey();
                    break;
                }
                i++;
            }

            switch (columnIndex) {
                case 0:
                    return jobType.getName();
                case 1:
                    return LOCALE_MESSAGES.getMessage("game.planet.cities.table.personcounter", Utilities.longToHumanString(i));
                case 2:
                    return String.format("%.2f%%", (((double) i / (double) population) * 100));
            }
            return "";
        }

        @Override
        public String getColumnName(int column) {
            return jobListTableColunmNames[column];
        }
    }

    private class StockpileStorageModel extends AbstractTableModel {

        String[] colunmNames = {
            LOCALE_MESSAGES.getMessage("game.planet.cities.table.good"),
            LOCALE_MESSAGES.getMessage("game.planet.cities.table.count")};

        @Override
        public int getRowCount() {
            if (currentlySelectedCity == null) {
                return 0;
            } else {
                return currentlySelectedCity.resourceDemands.size();
            }
        }

        @Override
        public int getColumnCount() {
            return colunmNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            int demandId = 0;
            if (currentlySelectedCity.resourceDemands.keySet().toArray()[rowIndex] instanceof Integer) {
                demandId = (Integer) currentlySelectedCity.resourceDemands.keySet().toArray()[rowIndex];
            }
            if (currentlySelectedCity != null) {
                switch (columnIndex) {
                    case 0:
                        return gameState.getGood(demandId).getName();
                    case 1:
                        return (currentlySelectedCity.resourceDemands.values().toArray()[rowIndex]);
                }
            }
            return 0;
        }

        @Override
        public String getColumnName(int column) {
            return colunmNames[column];
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
