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

import ConquerSpace.game.districts.District;
import ConquerSpace.game.districts.City;
import ConquerSpace.game.districts.InfrastructureBuilding;
import ConquerSpace.game.buildings.area.Area;
import ConquerSpace.game.buildings.area.ResearchArea;
import ConquerSpace.game.population.jobs.Job;
import ConquerSpace.game.population.jobs.JobType;
import ConquerSpace.game.population.jobs.Workable;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.util.Utilities;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
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
    private final String[] jobListTableColunmNames = {"Job Name", "Count", "Percentage"};

    private JTable availableJobTable;
    private DefaultTableModel availableJobModel;
    private final String[] availableJobColunmNames = {"Job name", "Count"};

    private JPanel cityData;

    private Planet p;

    private int population = 1;

    private int citySelectedTab = 0;
    //So that the tab for the employment and things stay the same as you change your selection
    boolean isBuildingUi = false;

    private City currentlySelectedCity;

    private DefaultListModel<Area> areaListModel;
    private JList<Area> areaList;

    public PlanetCities(Universe u, Planet p, int turn) {
        this.p = p;
        tabs = new JTabbedPane();
        setLayout(new VerticalFlowLayout());

        growthPanel = new JPanel();
        growthPanel.setLayout(new VerticalFlowLayout());
        currentStats = new JPanel(new VerticalFlowLayout());

        currentStats.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Current population stats"));
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

        populationCount = new JLabel("Population: " + Utilities.longToHumanString(p.population));
        currentStats.add(populationCount);

        averageGrowthSum /= p.cities.size();
        averagePlanetPopGrowthLabel = new JLabel("Average Growth: " + p.populationIncrease + "% every 40 days");

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
        });
        
        cityList.addListSelectionListener(l -> {
            isBuildingUi = true;
            cityData.removeAll();

            City selected = cityList.getSelectedValue();

            int popcount = 0;
            float increment = 0;
            int maxPop = 0;
            int energyUsage = 0;
            int energyProvided = 0;
            ArrayList<InfrastructureBuilding> building = new ArrayList<>();

            //Get breeding rate and energy usage.
//            for (District build : selected.buildings) {
//                if (build instanceof PopulationStorage) {
//                    PopulationStorage stor = (PopulationStorage) build;
//
//                    popcount += stor.getPopulationArrayList().size();
//                    for (PopulationUnit unit : stor.getPopulationArrayList()) {
//                        //Fraction it so it does not accelerate at a crazy rate
//                        //Do subtractions here in the future, like happiness, and etc.
//                        increment += (unit.getSpecies().getBreedingRate() / 50);
//                    }
//                    maxPop += stor.getMaxStorage();
//                    if (stor instanceof District) {
//                        energyUsage += ((District) stor).getEnergyUsage();
//                        //Get the infrastructure connected to.
//                        for (InfrastructureBuilding infra : ((District) stor).infrastructure) {
//
//                            if (!building.contains(infra)) {
//                                building.add(infra);
//                                for (Area a : infra.areas) {
//                                    //energyProvided += infra
//                                    if (a instanceof PowerPlantArea) {
//                                        //Get the resource produced
//
//                                        //TODO
//                                        //Integer energy = (((PowerPlantArea) a).getUsedResource().getAttributes().get("energy"));
////                                        if (energy != null) {
////                                            energyProvided += (((PowerPlantArea) a).getMaxVolume() * energy);
////                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
            //Check if capital city
            for (int i = 0; i < u.getCivilizationCount(); i++) {
                if (u.getCivilization(i).getCapitalCity().equals(cityList.getSelectedValue())) {
                    JLabel isCapital = new JLabel("Capital City of " + u.getCivilization(i).getName());
                    cityData.add(isCapital);
                    break;
                }
            }

            //Population
            JLabel popCount = new JLabel("Population: " + Utilities.longToHumanString(selected.population.getPopulationSize()));
            cityData.add(popCount);

            //Get the number of powerplants leading to it
            //Energy usage
            JLabel energyUsageLabel = new JLabel("Energy Usage (used/provided): " + energyUsage + "/" + energyProvided);
            cityData.add(energyUsageLabel);

            //Growth
            JLabel growthAmount = new JLabel("Growth: " + 0 + "%");//new JLabel("Growth: " + (selected.getPopulationUnitPercentage()) + "% done, " + increment + "% within the next 40 days.");
            cityData.add(growthAmount);

            //JLabel unemployment = new JLabel("Unemployment: " + );
            //Max population
            JLabel maxPopulation = new JLabel("Population cap: " + Utilities.longToHumanString(maxPop) + " people");
            cityData.add(maxPopulation);

            JLabel districtCount = new JLabel("Districts: " + selected.buildings.size());
            cityData.add(districtCount);

            //Check for govenor
            if (cityList.getSelectedValue().getGovernor() != null) {
                JLabel governorLabel = new JLabel("Governor: " + selected.getGovernor().getName());
                cityData.add(governorLabel);
            }

            JPanel areaInfoPanel = new JPanel(new HorizontalFlowLayout());

            //Areas
            areaListModel = new DefaultListModel<>();
            for (District building1 : cityList.getSelectedValue().buildings) {
                for (Area area : building1.areas) {
                    areaListModel.addElement(area);
                }
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
            jobTableModel = new JobTableModel();
            jobTable = new JTable(jobTableModel);

            availableJobModel = new DefaultTableModel(availableJobColunmNames, 0);
            //Fill up
            HashMap<JobType, Integer> jobs = new HashMap<>();
            for (Job j : currentlySelectedCity.jobs) {
                JobType jt = j.getJobType();
                if (jobs.containsKey(jt)) {
                    //Add to it
                    int i = (jobs.get(jt) + 1);
                    jobs.put(jt, i);
                } else {
                    jobs.put(jt, 1);
                }
            }

            for (Map.Entry<JobType, Integer> entry : jobs.entrySet()) {
                JobType key = entry.getKey();
                Integer val = entry.getValue();

                availableJobModel.addRow(new Object[]{key, val});
            }
            availableJobTable = new JTable(availableJobModel);

            cityInfoTabs.removeAll();
            cityInfoTabs.add("Areas", areaInfoPanel);
            cityInfoTabs.add("Employment", new JScrollPane(jobTable));
            cityInfoTabs.add("Jobs", new JScrollPane(availableJobTable));
            cityInfoTabs.setSelectedIndex(citySelectedTab);
            cityData.add(cityInfoTabs);
            isBuildingUi = false;
        });

        JScrollPane scrollPane = new JScrollPane(cityList);
        cityList.setVisibleRowCount(50);

        cityListPanel.add(scrollPane, BorderLayout.WEST);
        cityListPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Cities"));

        //City info
        cityData = new JPanel();
        cityData.setLayout(new VerticalFlowLayout());
        cityListPanel.add(cityData, BorderLayout.CENTER);

        growthPanel.add(currentStats);
        growthPanel.add(cityListPanel);
        //tabs.add(jobContainer, "Jobs");
        add(growthPanel);
    }

    public void showCity(City whichCity) {
        //Determine if on planet
        if (whichCity != null && cityListModel.contains(whichCity)) {
            cityList.setSelectedValue(whichCity, true);
            jobTableModel.setSelectedCity(whichCity);
        }
    }

    private class AreaWrapper {

        Area area;

        public AreaWrapper(Area area) {
            this.area = area;
        }

        @Override
        public String toString() {
            if (area instanceof ResearchArea) {
                return "Research";
            }
            return "";
        }
    }

    public void update() {
    }

    private class JobTableModel extends AbstractTableModel {

        HashMap<JobType, Integer> populationCount;

        public JobTableModel() {
            populationCount = new HashMap<>();
            population = 0;
            //Get population job
            for (District value : currentlySelectedCity.buildings) {
                for (Area area : value.areas) {
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
                    return Utilities.longToHumanString(i) + " people";
                case 2:
                    return String.format("%.2f%%", (((double) i / (double) population) * 100));
            }
            return "";
        }

        @Override
        public String getColumnName(int column) {
            return jobListTableColunmNames[column];
        }

        public void setSelectedCity(City city) {
            populationCount.clear();
//            for (District value : currentlySelectedCity.buildings) {
//                if (value instanceof PopulationStorage) {
//                    PopulationStorage stor = (PopulationStorage) value;
//                    for (PopulationUnit unit : stor.getPopulationArrayList()) {
//                        JobType job = unit.getJob().getJobType();
//                        if (populationCount.containsKey(job)) {
//                            //Add to it
//                            int i = (populationCount.get(job) + 1);
//                            populationCount.put(job, i);
//                        } else {
//                            populationCount.put(job, 1);
//                        }
//                    }
//                }
//            }
        }
    }
}
