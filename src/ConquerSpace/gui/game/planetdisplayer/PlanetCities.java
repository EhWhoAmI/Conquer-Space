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

import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.game.GameController;
import ConquerSpace.game.organizations.civilization.Civilization;
import ConquerSpace.game.city.City;
import ConquerSpace.game.city.area.Area;
import ConquerSpace.game.population.jobs.JobType;
import ConquerSpace.game.population.jobs.Workable;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.util.Utilities;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

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

    private Planet p;

    private int population = 1;

    private int citySelectedTab = 0;
    //So that the tab for the employment and things stay the same as you change your selection
    boolean isBuildingUi = false;

    private City currentlySelectedCity;

    private DefaultListModel<Area> areaListModel;
    private JList<Area> areaList;

    private Universe u;
    private Civilization owner;

    private PlanetInfoSheet parent;

    public PlanetCities(Universe u, Planet p, Civilization civ, int turn, PlanetInfoSheet parent) {
        this.u = u;
        this.p = p;
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
        long pop = 0;
        //Get average growth
        float averageGrowthSum = 0;
//        for (Map.Entry<GeographicPoint, District> entry : p.buildings.entrySet()) {
//            GeographicPoint key = entry.getKey();
//            District value = entry.getValue();
//            float increment = 0;
//            if (value instanceof PopulationStorage) {
//                PopulationStorage storage = (PopulationStorage) value;
//                pop += storage.getPopulationArrayList().size();
//                //process pops
//                for (PopulationUnit unit : storage.getPopulationArrayList()) {
//                    //Fraction it so it does not accelerate at a crazy rate
//                    //Do subtractions here in the future, like happiness, and etc.
//                    increment += (unit.getSpecies().getBreedingRate() / 50);
//                }
//            }
//            averageGrowthSum += increment;
//        }

        populationCount = new JLabel(
                LOCALE_MESSAGES.getMessage("game.planet.cities.planetoverview.population", Utilities.longToHumanString(p.population)));
        currentStats.add(populationCount);

        averageGrowthSum /= p.cities.size();
        averagePlanetPopGrowthLabel = new JLabel(
                LOCALE_MESSAGES.getMessage("game.planet.cities.planetoverview.averagegrowth", p.populationIncrease));

        cityListPanel = new JPanel();
        cityListPanel.setLayout(new BorderLayout());

        cityListModel = new DefaultListModel<>();
        for (City city : p.cities) {
            cityListModel.addElement(city);
        }
        cityList = new JList<>(cityListModel);
        cityList.setSelectedIndex(0);
        cityInfoTabs = new JTabbedPane();
        cityInfoTabs.addChangeListener(c -> {
            if (cityInfoTabs.getSelectedIndex() > -1 && !isBuildingUi) {
                citySelectedTab = cityInfoTabs.getSelectedIndex();
            }
            if (areaListModel != null && cityList.getSelectedValue() != null) {
                for (Area area : cityList.getSelectedValue().areas) {
                    areaListModel.addElement(area);
                }

            }
        });

        //Initialize table
        jobTableModel = new JobTableModel();

        cityList.addListSelectionListener(l -> {
            selectCity();
        });

        JScrollPane scrollPane = new JScrollPane(cityList);
        cityList.setVisibleRowCount(50);

        cityListPanel.add(scrollPane, BorderLayout.WEST);
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

        City selected = cityList.getSelectedValue();

        int popcount = 0;
        float increment = 0;
        int maxPop = 0;
        int energyUsage = 0;
        int energyProvided = 0;

        JLabel cityName = new JLabel(selected.getName());
        cityData.add(cityName);

        //Check if capital city
        for (int i = 0; i < u.getCivilizationCount(); i++) {
            if (u.getCivilization(i).getCapitalCity().equals(cityList.getSelectedValue())) {
                JLabel isCapital = new JLabel(
                        LOCALE_MESSAGES.getMessage("game.planet.cities.capital", u.getCivilization(i).getName()));
                cityData.add(isCapital);
                break;
            }
        }

        //Population
        JLabel popCount = new JLabel(
                LOCALE_MESSAGES.getMessage("game.planet.cities.population", Utilities.longToHumanString(selected.population.getPopulationSize())));
        cityData.add(popCount);
        
        JLabel priindustry = new JLabel(
                LOCALE_MESSAGES.getMessage("game.planet.cities.table.priindustry", selected.getCityType()));
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

        //JLabel unemployment = new JLabel("Unemployment: " + );
        //Max population
        JLabel maxPopulation = new JLabel(
                LOCALE_MESSAGES.getMessage("game.planet.cities.popcap", Utilities.longToHumanString(maxPop)));
        cityData.add(maxPopulation);

        //Check for govenor
        if (cityList.getSelectedValue().getGovernor() != null) {
            JLabel governorLabel = new JLabel(
                    LOCALE_MESSAGES.getMessage("game.planet.cities.governor", selected.getGovernor().getName()));
            cityData.add(governorLabel);
        }

        DefaultListModel<String> tagsListModel = new DefaultListModel<>();
        for (Map.Entry<String, Integer> entry : selected.tags.entrySet()) {
            String key = entry.getKey();
            Integer val = entry.getValue();
            if (val == null) {
                tagsListModel.addElement(key);
            } else {
                tagsListModel.addElement(key + " " + val);
            }
        }
        JList<String> tagsList = new JList<String>(tagsListModel);

        cityData.add(new JScrollPane(tagsList));

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
        for (Area area : selected.areas) {
            areaListModel.addElement(area);
        }

        areaList = new JList<>(areaListModel);
        JScrollPane areascrollPane = new JScrollPane(areaList);

        JPanel areaInfoContainerPanel = new JPanel();

        areaList.addListSelectionListener(o -> {
            areaInfoContainerPanel.removeAll();
            areaInfoContainerPanel.add(new AreaInformationPanel(areaList.getSelectedValue()));
        });

        areaInfoPanel.add(areascrollPane);
        areaInfoPanel.add(areaInfoContainerPanel);

        currentlySelectedCity = cityList.getSelectedValue();
        jobTableModel.newCitySelection();
        jobTable = new JTable(jobTableModel);

        availableJobModel = new DefaultTableModel(availableJobColunmNames, 0);
        //Fill up
        availableJobTable = new JTable(availableJobModel);

        areaConstructionPanel = new AreaConstructionPanel(p, owner, selected);

        cityInfoTabs.removeAll();
        cityInfoTabs.add(LOCALE_MESSAGES.getMessage("game.planet.cities.areas"), areaInfoPanel);
        cityInfoTabs.add(LOCALE_MESSAGES.getMessage("game.planet.cities.employment"), new JScrollPane(jobTable));
        cityInfoTabs.add(LOCALE_MESSAGES.getMessage("game.planet.cities.jobs"), new JScrollPane(availableJobTable));
        cityInfoTabs.add(LOCALE_MESSAGES.getMessage("game.planet.cities.construction"), areaConstructionPanel);
        cityInfoTabs.add(LOCALE_MESSAGES.getMessage("game.planet.cities.resourcedemand"), new JScrollPane(new JTable(new StockpileStorageModel())));

        cityInfoTabs.setSelectedIndex(citySelectedTab);
        cityData.add(cityInfoTabs);

        //Select first area
        if (!selected.areas.isEmpty()) {
            //Select first area
            areaList.setSelectedIndex(0);
        }
        cityData.repaint();
        isBuildingUi = false;
    }

    public void showCity(City whichCity) {
        //Determine if on planet
        if (whichCity != null && cityListModel.contains(whichCity)) {
            cityList.setSelectedValue(whichCity, true);
            selectCity();
            jobTableModel.newCitySelection();
        }
    }

    public void update() {
    }

    private class JobTableModel extends AbstractTableModel {

        HashMap<JobType, Integer> populationCount;

        public JobTableModel() {
            populationCount = new HashMap<>();
        }

        private void newCitySelection() {
            populationCount.clear();
            population = 0;
            //Get population job
            for (Area area : currentlySelectedCity.areas) {
                if (area instanceof Workable) {
                    if (!populationCount.containsKey(area.getJobClassification())) {
                        populationCount.put(area.getJobClassification(), area.getMaxJobsProvided());
                    } else {
                        int count = populationCount.get(area.getJobClassification());
                        count += area.getMaxJobsProvided();
                        populationCount.put(area.getJobClassification(), count);
                    }
                }
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
            if (currentlySelectedCity != null) {
                switch (columnIndex) {
                    case 0:
                        return GameController.goodHashMap.get(currentlySelectedCity.resourceDemands.keySet().toArray()[rowIndex]);
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
}
