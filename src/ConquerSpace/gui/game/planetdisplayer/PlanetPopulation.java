package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.City;
import ConquerSpace.game.buildings.CityDistrict;
import ConquerSpace.game.buildings.PopulationStorage;
import ConquerSpace.game.buildings.area.Area;
import ConquerSpace.game.buildings.area.ResearchArea;
import ConquerSpace.game.life.Job;
import ConquerSpace.game.population.PopulationUnit;
import ConquerSpace.game.universe.Point;
import ConquerSpace.game.universe.spaceObjects.Planet;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
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

    private JPanel cityData;

    private JPanel jobContainer;
    private PopulationTableModel jobListModel;
    private JTable jobList;

    private Planet p;

    public PlanetPopulation(Planet p, int turn) {
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
        for (Map.Entry<Point, Building> entry : p.buildings.entrySet()) {
            Point key = entry.getKey();
            Building value = entry.getValue();
            float increment = 0;
            if (value instanceof CityDistrict) {
                CityDistrict storage = (CityDistrict) value;
                pop += storage.population.size();
                //process pops
                for (PopulationUnit unit : storage.population) {
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
        currentStats.add(averagePlanetPopGrowthLabel);

        cityListPanel = new JPanel();
        cityListPanel.setLayout(new BorderLayout());

        cityListModel = new DefaultListModel<>();
        for (City c : p.cities) {
            cityListModel.addElement(c);
        }
        cityList = new JList<>(cityListModel);
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
            JLabel popCount = new JLabel("Population: " + (popcount * 10) + " million people");
            cityData.add(popCount);

            //Growth
            JLabel growthAmount = new JLabel("Growth: " + (cityList.getSelectedValue().getPopulationUnitPercentage()) + "% done, " + increment + "% within the next 40 days.");
            cityData.add(growthAmount);

            //Max population
            JLabel maxPopulation = new JLabel("Population cap: " + (maxPop * 10) + " million people");
            cityData.add(maxPopulation);

            //Areas
            //areaListModel = new DefaultListModel<>();
            //for(Area a : cityList.getSelectedValue().areas) {
            //AreaWrapper wrap = new AreaWrapper(a);
            //areaListModel.addElement(wrap);
            //}
            //areaList = new JList<>(areaListModel);
            //JScrollPane areascrollPane = new JScrollPane(areaList);
            //cityData.add(areascrollPane);
        });

        JScrollPane scrollPane = new JScrollPane(cityList);

        cityListPanel.add(scrollPane, BorderLayout.WEST);
        cityListPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Cities"));

        //City info
        cityData = new JPanel();
        cityData.setLayout(new VerticalFlowLayout());
        cityListPanel.add(cityData, BorderLayout.CENTER);

        jobContainer = new JPanel();
        jobContainer.setLayout(new BorderLayout());
        jobListModel = new PopulationTableModel();
        for (City city : p.cities) {
            for (PopulationStorage stor : city.storages) {
                for (PopulationUnit unit : stor.getPopulationArrayList()) {
                    jobListModel.addModel(new PopulationModel(unit, city));
                }
            }
        }
        jobList = new JTable(jobListModel);
        jobList.setAutoCreateRowSorter(true);
        JScrollPane jobscrollPane = new JScrollPane(jobList);
        jobContainer.add(jobscrollPane, BorderLayout.WEST);

        growthPanel.add(currentStats);
        growthPanel.add(cityListPanel);
        tabs.add(growthPanel, "Population Growth");
        tabs.add(jobContainer, "Jobs");
        add(tabs);
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
        int selectedRow = jobList.getSelectedRow();
        jobListModel.clear();
        for (City city : p.cities) {
            for (PopulationStorage stor : city.storages) {
                for (PopulationUnit unit : stor.getPopulationArrayList()) {
                    jobListModel.addModel(new PopulationModel(unit, city));
                }
            }
        }
        jobListModel.fireTableDataChanged();
        if (selectedRow > -1) {
            jobList.setRowSelectionInterval(selectedRow, selectedRow);
        }
        //jobList.setR(selectedJob);
    }

    private class PopulationModel {

        PopulationUnit unit;
        City container;

        public PopulationModel(PopulationUnit unit, City container) {
            this.unit = unit;
            this.container = container;
        }
    }

    private class PopulationTableModel extends AbstractTableModel {

        private String[] colunms = {"Species", "Happiness", "Job", "Job Rank", "City"};
        private ArrayList<PopulationModel> data;

        public PopulationTableModel() {
            data = new ArrayList<>();
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return colunms.length;
        }

        @Override
        public Object getValueAt(int row, int col) {
            PopulationModel unit = data.get(row);
            switch (col) {
                case 0:
                    return unit.unit.getSpecies().getName();
                case 1:
                    return unit.unit.getHappiness();
                case 2:
                    return unit.unit.getJob().getJobType().getName();
                case 3:
                    return unit.unit.getJob().getJobRank();
                case 4:
                    return unit.container.getName();
            }
            return "";
        }

        @Override
        public String getColumnName(int column) {
            return colunms[column];
        }

        public void addModel(PopulationModel mod) {
            data.add(mod);
        }

        public void clear() {
            data.clear();
        }
    }
}
