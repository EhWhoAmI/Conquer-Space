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

import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.FarmBuilding;
import ConquerSpace.game.buildings.area.Area;
import ConquerSpace.game.jobs.Job;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.resources.farm.Crop;
import ConquerSpace.game.universe.spaceObjects.Planet;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Zyun
 */
public class PlanetIndustry extends JPanel {

    private JTabbedPane tabs;

    private JPanel areaContainer;
    private DefaultListModel<Area> areaDefaultListModel;
    private JList<Area> areaList;

    //Sorts out all the buildings and places
    private JPanel jobSortingOutPanel;
    private DefaultListModel<String> industryListModel;
    private JList<String> industryList;

    private JPanel industryInfoContainer;
    private CardLayout industryInfoCardLayout;
    private FarmingInfoPanel farmingInfoPanel;
    
    private JPanel availableJobs;
    private JobListModel availableJobListModel;
    private JList<Job> availableJobList;

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
        areaContainer.setLayout(new BorderLayout());
        areaDefaultListModel = new DefaultListModel<>();
        for (Building city : p.buildings.values()) {
            for (Area a : city.areas) {
                areaDefaultListModel.addElement(a);
            }
        }
        areaList = new JList<>(areaDefaultListModel);
        JScrollPane scrollPane = new JScrollPane(areaList);
        areaContainer.add(scrollPane, BorderLayout.WEST);
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

        farmingInfoPanel = new FarmingInfoPanel(p, c);

        industryInfoContainer.add("Farm", farmingInfoPanel);
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
        for (Building city : p.buildings.values()) {
            for (Area a : city.areas) {
                areaDefaultListModel.addElement(a);
            }
        }
        areaList.setSelectedIndex(selectedArea);
    }

    private class FarmingInfoPanel extends JPanel {

        private JTable farmTable;
        private FarmTableTableModel farmTableTableModel;
        
        private JPanel farmInfoPanel;
        private DefaultListModel<Crop> localLifeInFarmListModel;
        private JList<Crop> localLifeInFarmList;

        public FarmingInfoPanel(Planet p, Civilization c) {
            setLayout(new HorizontalFlowLayout());
            farmTableTableModel = new FarmTableTableModel();

            farmTable = new JTable(farmTableTableModel);
            
            farmInfoPanel = new JPanel();
            farmInfoPanel.setLayout(new VerticalFlowLayout());
            localLifeInFarmListModel = new DefaultListModel<>();
            localLifeInFarmList = new JList<>(localLifeInFarmListModel);
            
            farmInfoPanel.add(new JLabel("Crops"));
            farmInfoPanel.add(new JScrollPane(localLifeInFarmList));
            ListSelectionModel farmTableSelectionModel = farmTable.getSelectionModel();
            farmTableSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            farmTableSelectionModel.addListSelectionListener(l -> {
                if(farmTable.getSelectedRow() > -1) {
                    FarmBuilding fb = farmTableTableModel.getFarmBuilding(farmTable.getSelectedRow());
                    //Populate the list...
                    localLifeInFarmListModel.clear();
                    //Add the crops
                    for(Crop crop : fb.crops) {
                        localLifeInFarmListModel.addElement(crop);
                    }
                }
            });
            
            add(new JScrollPane(farmTable));
            add(farmInfoPanel);
            update();
        }

        public void update() {
            for (Map.Entry<GeographicPoint, Building> en : p.buildings.entrySet()) {
                GeographicPoint key = en.getKey();
                Building value = en.getValue();
                if (value instanceof FarmBuilding) {
                    farmTableTableModel.addFarmBuilding((FarmBuilding) value);
                }
            } 
       }

    }
    
    private class MiningInfoPanel extends JPanel {
        
    }
    
    private class FarmTableTableModel extends AbstractTableModel {

        private String[] colunms = {"Farm Type", "Productivity"};

        private ArrayList<FarmBuilding> farmBuildingArrayList;

        public FarmTableTableModel() {
            farmBuildingArrayList = new ArrayList<>();
        }

        @Override
        public int getRowCount() {
            return farmBuildingArrayList.size();
        }

        @Override
        public int getColumnCount() {
            return colunms.length;
        }

        @Override
        public Object getValueAt(int row, int column) {
            FarmBuilding fb = farmBuildingArrayList.get(row);
            if (column == 0) {
                return fb.getFarmType();
            }
            if (column == 1) {
                return (fb.getProductivity());
            }

            return "";
        }

        public void addFarmBuilding(FarmBuilding building) {
            farmBuildingArrayList.add(building);
        }

        @Override
        public String getColumnName(int column) {
            return colunms[column];
        }
        
        public FarmBuilding getFarmBuilding(int index) {
            return farmBuildingArrayList.get(index);
        }
    }
    
    private class JobListModel extends AbstractListModel<Job>{

        ArrayList<Job> jobs;

        public JobListModel(ArrayList<Job> jobs) {
            this.jobs = jobs;
        }
        
        @Override
        public int getSize() {
            return jobs.size();
        }

        @Override
        public Job getElementAt(int index) {
            return jobs.get(index);
        }
        
    }
}