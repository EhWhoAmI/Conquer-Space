package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.City;
import ConquerSpace.game.buildings.PopulationStorage;
import ConquerSpace.game.buildings.area.Area;
import ConquerSpace.game.buildings.area.ResearchArea;
import ConquerSpace.game.population.JobType;
import ConquerSpace.game.population.PopulationUnit;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.Universe;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
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

/**
 *
 * @author Zyun
 */
public class PlanetPopulation extends JPanel {

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

    private JTable jobTable;
    private JobTableModel jobTableModel;
    private final String[] jobListTableColunmNames = {"Job Name", "Count", "Percentage"};

    private JPanel cityData;

    private Planet p;

    private int population = 1;

    private City currentlySelectedCity;

    public PlanetPopulation(Universe u, Planet p, int turn) {
        this.p = p;
        tabs = new JTabbedPane();
        setLayout(new VerticalFlowLayout());

        growthPanel = new JPanel();
        growthPanel.setLayout(new VerticalFlowLayout());
        currentStats = new JPanel(new VerticalFlowLayout());

        currentStats.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Current population stats"));
        int pop = 0;
        //Get average growth
        float averageGrowthSum = 0;
        for (Map.Entry<GeographicPoint, Building> entry : p.buildings.entrySet()) {
            GeographicPoint key = entry.getKey();
            Building value = entry.getValue();
            float increment = 0;
            if (value instanceof PopulationStorage) {
                PopulationStorage storage = (PopulationStorage) value;
                pop += storage.getPopulationArrayList().size();
                //process pops
                for (PopulationUnit unit : storage.getPopulationArrayList()) {
                    //Fraction it so it does not accelerate at a crazy rate
                    //Do subtractions here in the future, like happiness, and etc.
                    increment += (unit.getSpecies().getBreedingRate() / 50);
                }
            }
            averageGrowthSum += increment;
        }

        populationCount = new JLabel("Population: " + (pop * 10) + " million");
        currentStats.add(populationCount);

        averageGrowthSum /= p.cities.size();
        averagePlanetPopGrowthLabel = new JLabel("Average Growth: " + averageGrowthSum + "% every 40 days");

        cityListPanel = new JPanel();
        cityListPanel.setLayout(new BorderLayout());

        cityListModel = new DefaultListModel<>();
        for (City city : p.cities) {
            cityListModel.addElement(city);
        }
        cityList = new JList<>(cityListModel);
        cityList.setSelectedIndex(0);
        cityList.addListSelectionListener(l -> {
            cityData.removeAll();
            int popcount = 0;
            float increment = 0;
            int maxPop = 0;
            for (PopulationStorage stor : cityList.getSelectedValue().storages) {
                popcount += stor.getPopulationArrayList().size();
                for (PopulationUnit unit : stor.getPopulationArrayList()) {
                    //Fraction it so it does not accelerate at a crazy rate
                    //Do subtractions here in the future, like happiness, and etc.
                    increment += (unit.getSpecies().getBreedingRate() / 50);
                }
                maxPop += stor.getMaxStorage();
            }

            //Check if capital city
            for (int i = 0; i < u.getCivilizationCount(); i++) {
                if (u.getCivilization(i).getCapitalCity().equals(cityList.getSelectedValue())) {
                    JLabel isCapital = new JLabel("Capital City of " + u.getCivilization(i).getName());
                    cityData.add(isCapital);
                    break;
                }
            }

            //Population
            JLabel popCount = new JLabel("Population: " + (popcount * 10) + " million people");
            cityData.add(popCount);

            //Growth
            JLabel growthAmount = new JLabel("Growth: " + (cityList.getSelectedValue().getPopulationUnitPercentage()) + "% done, " + increment + "% within the next 40 days.");
            cityData.add(growthAmount);
            
            //JLabel unemployment = new JLabel("Unemployment: " + );
                    
            //Max population
            JLabel maxPopulation = new JLabel("Population cap: " + (maxPop * 10) + " million people");
            cityData.add(maxPopulation);

            //Check for govenor
            if (cityList.getSelectedValue().getGovernor() != null) {
                JLabel governorLabel = new JLabel("Governor: " + cityList.getSelectedValue().getGovernor().getName());
                cityData.add(governorLabel);
            }
            //Areas
            //areaListModel = new DefaultListModel<>();
            //for(Area a : cityList.getSelectedValue().areas) {
            //AreaWrapper wrap = new AreaWrapper(a);
            //areaListModel.addElement(wrap);
            //}
            //areaList = new JList<>(areaListModel);
            //JScrollPane areascrollPane = new JScrollPane(areaList);
            //cityData.add(areascrollPane);
            cityData.add(new JLabel("Jobs"));
            currentlySelectedCity = cityList.getSelectedValue();
            jobTableModel = new JobTableModel();
            jobTable = new JTable(jobTableModel);
            cityData.add(new JScrollPane(jobTable));
        });

        JScrollPane scrollPane = new JScrollPane(cityList);

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
            //Process the things
            for (PopulationStorage value : currentlySelectedCity.storages) {
                for (PopulationUnit unit : value.getPopulationArrayList()) {
                    JobType job = unit.getJob().getJobType();
                    if (populationCount.containsKey(job)) {
                        //Add to it
                        int i = (populationCount.get(job) + 1);
                        populationCount.put(job, i);
                    } else {
                        populationCount.put(job, 1);
                    }
                    population++;
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
                    return (i * 10) + " million people";
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
            for (PopulationStorage value : currentlySelectedCity.storages) {
                for (PopulationUnit unit : value.getPopulationArrayList()) {
                    JobType job = unit.getJob().getJobType();
                    if (populationCount.containsKey(job)) {
                        //Add to it
                        int i = (populationCount.get(job) + 1);
                        populationCount.put(job, i);
                    } else {
                        populationCount.put(job, 1);
                    }
                }
            }
        }
    }

}
