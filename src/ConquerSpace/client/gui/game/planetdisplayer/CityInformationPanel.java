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
import ConquerSpace.common.game.city.area.Area;
import ConquerSpace.common.game.city.modifier.CityModifier;
import ConquerSpace.common.game.logistics.SupplySegment;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.population.Population;
import ConquerSpace.common.game.population.jobs.JobType;
import ConquerSpace.common.game.universe.bodies.Planet;
import ConquerSpace.common.util.Utilities;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author EhWhoAmI
 */
public class CityInformationPanel extends JPanel {

    private DefaultListModel<Area> areaListModel;
    private JList<Area> areaList;

    private AreaInformationPanel areaInformationPanel;

    private JTabbedPane cityInfoTabs;
    private JTable jobTable;

    private JTable availableJobTable;
    private final String[] availableJobColunmNames = {
        LOCALE_MESSAGES.getMessage("game.planet.cities.table.jobname"),
        LOCALE_MESSAGES.getMessage("game.planet.cities.table.count")};
    private DefaultTableModel availableJobModel;

    private AreaConstructionPanel areaConstructionPanel;

    private JobTableModel jobTableModel;

    private GameState gameState;

    public CityInformationPanel(GameState gameState, City selectedCity, Planet planet, Civilization owner) {
        this.gameState = gameState;
        setLayout(new VerticalFlowLayout());
        cityInfoTabs = new JTabbedPane();
        int maxPop = 0;
        int energyUsage = selectedCity.getEnergyNeeded();
        int energyProvided = selectedCity.getEnergyProvided();

        JLabel cityName = new JLabel(selectedCity.getName());
        add(cityName);

        //Check if capital city
        for (int i = 0; i < gameState.getCivilizationCount(); i++) {
            Civilization civilization = gameState.getCivilizationObject(i);
            if (civilization.getCapitalCity().equals(selectedCity)) {
                JLabel isCapital = new JLabel(
                        LOCALE_MESSAGES.getMessage("game.planet.cities.capital", civilization.getName()));
                add(isCapital);
                break;
            }
        }

        //Population
        JLabel popCount = new JLabel(
                LOCALE_MESSAGES.getMessage("game.planet.cities.population", Utilities.longToHumanString(gameState.getObject(selectedCity.population, Population.class).getPopulationSize())));
        add(popCount);

        JLabel priindustry = new JLabel(
                LOCALE_MESSAGES.getMessage("game.planet.cities.table.priindustry", selectedCity.getCityType()));
        add(priindustry);

        //Get the number of powerplants leading to it
        //Energy usage
        JLabel energyUsageLabel = new JLabel(
                LOCALE_MESSAGES.getMessage("game.planet.cities.energyusage", energyUsage, energyProvided));
        add(energyUsageLabel);

        //Growth
        JLabel growthAmount = new JLabel(
                LOCALE_MESSAGES.getMessage("game.planet.cities.growth", 0));//new JLabel("Growth: " + (selected.getPopulationUnitPercentage()) + "% done, " + increment + "% within the next 40 days.");
       add(growthAmount);

        double unemploymentRate = selectedCity.getUnemploymentRate();
        JLabel unemployment = new JLabel("Unemployment rate: " + Math.round(unemploymentRate * 100) + "%");
        unemployment.setForeground(new Color((float) unemploymentRate, 0f, 0f));
        add(unemployment);

        //Max population
        JLabel maxPopulation = new JLabel(
                LOCALE_MESSAGES.getMessage("game.planet.cities.popcap", Utilities.longToHumanString(maxPop)));
        add(maxPopulation);

        add(maxPopulation);

        //Check for govenor
        if (selectedCity.getGovernor() != null) {
            JLabel governorLabel = new JLabel(
                    LOCALE_MESSAGES.getMessage("game.planet.cities.governor", selectedCity.getGovernor().getName()));
            add(governorLabel);
        }

        ObjectListModel<CityModifier> modifierListModel = new ObjectListModel<>();
        modifierListModel.setElements(selectedCity.cityModifiers);
        modifierListModel.setHandler(l -> {
            return (l.toString());
        });

        JTabbedPane cityLists = new JTabbedPane();
        JList<String> modifierList = new JList<>(modifierListModel);
        cityLists.add(new JScrollPane(modifierList), "Modifiers");

        DefaultListModel<String> connectedCityListModel = new DefaultListModel<>();
        for (ObjectReference connection : selectedCity.getSupplyConnections()) {
            SupplySegment seg = gameState.getObject(connection, SupplySegment.class);
            if (!seg.getPoint1().equals(selectedCity.getReference())) {
                connectedCityListModel.addElement(gameState.getObject(seg.getPoint1()).toString());
            } else {
                connectedCityListModel.addElement(gameState.getObject(seg.getPoint2()).toString());
            }
        }
        JList<String> connectedCityList = new JList<>(connectedCityListModel);
        cityLists.add(new JScrollPane(connectedCityList), "Linked Cities");

        add(cityLists);
        JButton viewResourceButton = new JButton(
                LOCALE_MESSAGES.getMessage("game.planet.cities.viewresources"));
        viewResourceButton.addActionListener(l -> {
            //parent.setSelectedTab(8);
            //parent.planetResources.jTabbedPane1.setSelectedIndex(1);
            //parent.planetResources.selectStockpile(currentlySelectedCity);
        });
        add(viewResourceButton);

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
            validate();
            repaint();
        });

        areaInfoPanel.add(areascrollPane);
        areaInfoPanel.add(areaInfoContainerPanel);

        jobTableModel = new JobTableModel(selectedCity);
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
        cityInfoTabs.add(LOCALE_MESSAGES.getMessage("game.planet.cities.resourcedemand"), new JScrollPane(new JTable(new StockpileStorageModel(selectedCity))));

        add(cityInfoTabs);

        //Select first area
        if (!selectedCity.areas.isEmpty()) {
            //Select first area
            areaList.setSelectedIndex(0);
        }
    }

    void setSelectedTab(int tab) {
        cityInfoTabs.setSelectedIndex(tab);
    }

    int getSelectedTab() {
        return cityInfoTabs.getSelectedIndex();
    }

    private class JobTableModel extends AbstractTableModel {

        private long currentlyWorking;
        private long population;
        HashMap<JobType, Integer> populationCount;
        City city;
        private final String[] jobListTableColunmNames = {
            LOCALE_MESSAGES.getMessage("game.planet.cities.table.jobname"),
            LOCALE_MESSAGES.getMessage("game.planet.cities.table.count"),
            LOCALE_MESSAGES.getMessage("game.planet.cities.table.percentage")};

        public JobTableModel(City city) {
            populationCount = new HashMap<>();
            this.city = city;

            currentlyWorking = 0;
            populationCount.clear();
            population = gameState.getObject(city.population, Population.class).getPopulationSize();
            //Get population job
            for (ObjectReference areaId : city.areas) {
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

        final String[] colunmNames = {
            LOCALE_MESSAGES.getMessage("game.planet.cities.table.good"),
            LOCALE_MESSAGES.getMessage("game.planet.cities.table.count")};

        City city;

        public StockpileStorageModel(City city) {
            this.city = city;
        }

        @Override
        public int getRowCount() {
            if (city == null) {
                return 0;
            } else {
                return city.resourceDemands.size();
            }
        }

        @Override
        public int getColumnCount() {
            return colunmNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            int demandId = 0;
            if (city.resourceDemands.keySet().toArray()[rowIndex] instanceof Integer) {
                demandId = (Integer) city.resourceDemands.keySet().toArray()[rowIndex];
            }
            if (city != null) {
                switch (columnIndex) {
                    case 0:
                        return gameState.getGood(demandId).getName();
                    case 1:
                        return (city.resourceDemands.values().toArray()[rowIndex]);
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
