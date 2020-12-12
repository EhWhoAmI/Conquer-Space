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

package ConquerSpace.client.gui.game.planetdisplayer.city;

import ConquerSpace.ConquerSpace;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.city.area.Area;
import ConquerSpace.common.game.population.Population;
import ConquerSpace.common.game.population.PopulationSegment;
import ConquerSpace.common.game.population.jobs.JobType;
import ConquerSpace.common.util.Utilities;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.LegendItemEntity;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author EhWhoAmI
 */
class JobInformationPanel extends JPanel {

    private HashMap<JobType, Integer> populationCount;
    private long currentlyWorking;
    private long populationLaborForceSize;
    private JTabbedPane tabs;
    private City selectedCity;
    private GameState gameState;

    private static int selectedTab = 0;

    public JobInformationPanel(City selectedCity, GameState gameState) {
        this.selectedCity = selectedCity;
        this.gameState = gameState;
        setLayout(new BorderLayout());
        populationCount = new HashMap<>();
        initPopulationInfo();
        tabs = new JTabbedPane();
        JobTableModel jobTableModel = new JobTableModel();
        JTable jobTable = new JTable(jobTableModel);
        JPanel jobTablePanel = new JPanel(new BorderLayout());
        HashMap<JobType, JCheckBox> checkBoxes = new HashMap<>();
        JPanel checkBoxPanels = new JPanel();
        checkBoxPanels.setLayout(new VerticalFlowLayout());
        long laborForceSize = gameState.getObject(selectedCity.population, Population.class).getWorkableSize();
        checkBoxPanels.add(new JLabel("Labor Force: " + new DecimalFormat("###,###").format(laborForceSize)));
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<JobType, Integer> entry : populationCount.entrySet()) {
            JobType key = entry.getKey();
            Integer val = entry.getValue();
            if (val > 0) {
                dataset.setValue(key, val);
                JCheckBox box = new JCheckBox(key.getName());
                box.addActionListener(l -> {
                    if (box.isSelected()) {
                        //Add thing back
                        dataset.setValue(key, val);
                    } else {
                        //Remove thing
                        dataset.remove(key);
                    }
                });
                box.setSelected(true);
                //Add
                JPanel boxContainerPanel = new JPanel(new HorizontalFlowLayout());
                boxContainerPanel.add(box);
                int boxHeight = getFontMetrics(box.getFont()).getHeight();
                BufferedImage img = new BufferedImage(boxHeight, boxHeight, BufferedImage.TYPE_3BYTE_BGR);
                Graphics2D g2d = (Graphics2D) img.getGraphics();
                Color keyColor = key.getColor();
                g2d.setColor(keyColor);
                g2d.fillRect(0, 0, boxHeight, boxHeight);
                g2d.setColor(Color.lightGray);
                g2d.drawRect(0, 0, boxHeight, boxHeight);
                boxContainerPanel.add(new JLabel(new ImageIcon(img)));
                checkBoxes.put(key, box);
                checkBoxPanels.add(boxContainerPanel);
            }
        }
        JFreeChart chart = ChartFactory.createPieChart(ConquerSpace.LOCALE_MESSAGES.getMessage("game.planet.cities.chart.jobs"), dataset, true, true, false);
        PiePlot piePlot = (PiePlot) chart.getPlot();
        PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator("{0}: {1} ({2})", new DecimalFormat("###,###"), new DecimalFormat("0%"));
        piePlot.setLabelGenerator(gen);
        for (JobType key : populationCount.keySet()) {
            ((PiePlot) chart.getPlot()).setSectionPaint(key, key.getColor());
        }
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPopupMenu(null);
        chartPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent cme) {
                ChartEntity entity = cme.getEntity();
                if (entity instanceof PieSectionEntity) {
                    //System.out.println(((PieSectionEntity) entity).getSectionKey());
                    //System.out.println(((PieSectionEntity) entity).getSectionKey().getClass());
                    //Remove segment
                    if (SwingUtilities.isRightMouseButton(cme.getTrigger())) {
                        dataset.remove(((PieSectionEntity) entity).getSectionKey());
                        //Uncheck checkbox
                        checkBoxes.get(((PieSectionEntity) entity).getSectionKey()).setSelected(false);
                    }
                } else if (entity instanceof LegendItemEntity) {
                }
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent cme) {
            }
        });
        jobTablePanel.add(chartPanel, BorderLayout.CENTER);
        jobTablePanel.add(checkBoxPanels, BorderLayout.EAST);
        //Population segments chart
        DefaultPieDataset populationSegmentDataset = new DefaultPieDataset();
        //Fill up
        Population cityPopulation = gameState.getObject(selectedCity.population, Population.class);
        for (ObjectReference segmentReference : cityPopulation.segments) {
            PopulationSegment segment = gameState.getObject(segmentReference, PopulationSegment.class);
            populationSegmentDataset.setValue(new Long(segment.tier), (double) segment.size);
        }
        JFreeChart segmentChart = ChartFactory.createPieChart("Segments", populationSegmentDataset, true, true, false);
        ChartPanel segmentChartPanel = new ChartPanel(segmentChart);
        segmentChartPanel.setPopupMenu(null);
        tabs.add(ConquerSpace.LOCALE_MESSAGES.getMessage("game.planet.cities.tab.chart"), jobTablePanel);
        tabs.add(ConquerSpace.LOCALE_MESSAGES.getMessage("game.planet.cities.tab.table"), new JScrollPane(jobTable));
        tabs.add("Segments", segmentChartPanel);

        tabs.setSelectedIndex(selectedTab);
        tabs.addChangeListener(l -> {
            selectedTab = tabs.getSelectedIndex();
        });

        add(tabs, BorderLayout.CENTER);
        setBorder(new TitledBorder(new LineBorder(Color.gray), ConquerSpace.LOCALE_MESSAGES.getMessage("game.planet.cities.chart.jobs")));
    }

    public void initPopulationInfo() {
        currentlyWorking = 0;
        populationCount.clear();
        populationLaborForceSize = gameState.getObject(selectedCity.population, Population.class).getWorkableSize();
        //Get population job
        for (ObjectReference areaId : selectedCity.areas) {
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
            populationCount.put(JobType.Jobless, (int) (populationLaborForceSize - currentlyWorking));
        } else {
            int count = populationCount.get(JobType.Jobless);
            count += (int) (populationLaborForceSize - currentlyWorking);
            populationCount.put(JobType.Jobless, count);
        }
    }

    private class JobTableModel extends AbstractTableModel {

        private long population;
        private final String[] jobListTableColunmNames = {ConquerSpace.LOCALE_MESSAGES.getMessage("game.planet.cities.table.jobname"), ConquerSpace.LOCALE_MESSAGES.getMessage("game.planet.cities.table.count"), ConquerSpace.LOCALE_MESSAGES.getMessage("game.planet.cities.table.percentage")};

        public JobTableModel() {
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
                    return ConquerSpace.LOCALE_MESSAGES.getMessage("game.planet.cities.table.personcounter", Utilities.longToHumanString(i));
                case 2:
                    return String.format("%.2f%%", ((double) i / (double) population) * 100);
            }
            return "";
        }

        @Override
        public String getColumnName(int column) {
            return jobListTableColunmNames[column];
        }
    }

}
