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
import ConquerSpace.common.game.population.jobs.JobType;
import ConquerSpace.common.util.Utilities;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;

/**
 * Does demographics and information about jobs
 *
 * @author EhWhoAmI
 */
class JobInformationPanel extends JPanel {

    private HashMap<JobType, Integer> populationCount;
    private long currentlyWorking;
    private long populationLaborForceSize;
    private JTabbedPane tabs;

    private JPanel jobInformationPanel;

    private static int selectedTab = 0;

    private City selectedCity;
    private GameState gameState;

    public JobInformationPanel(City selectedCity, GameState gameState) {
        this.selectedCity = selectedCity;
        this.gameState = gameState;
        setBorder(new TitledBorder(new LineBorder(Color.gray), ConquerSpace.LOCALE_MESSAGES.getMessage("game.planet.cities.chart.jobs")));

        setLayout(new BorderLayout());
        populationCount = new HashMap<>();
        initPopulationInfo();
        tabs = new JTabbedPane();

        //Job pie chart
        JPanel jobChartPanel = createJobChartPanel();

        //Population segments chart
        tabs.add(ConquerSpace.LOCALE_MESSAGES.getMessage("game.planet.cities.tab.chart"), jobChartPanel);

        tabs.setSelectedIndex(selectedTab);
        tabs.addChangeListener(l -> {
            selectedTab = tabs.getSelectedIndex();
        });

        add(tabs, BorderLayout.CENTER);
    }

    private void initPopulationInfo() {
        currentlyWorking = 0;
        populationCount.clear();
        populationLaborForceSize = gameState.getObject(selectedCity.getPopulation(), Population.class).getWorkableSize();
        //Get population job
        for (ObjectReference areaId : selectedCity.getAreas()) {
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

    private JPanel createJobChartPanel() {
        JPanel jobChartPanel = new JPanel(new BorderLayout());

        JMenu checkBoxPanels = new JMenu("Selected");
        checkBoxPanels.setLayout(new VerticalFlowLayout());

        DefaultCategoryDataset catdataset = new DefaultCategoryDataset();

        HashMap<JobType, JRadioButtonMenuItem> checkBoxes = new HashMap<>(); //Checkboxes to set the various segments visible or not
        for (Map.Entry<JobType, Integer> entry : populationCount.entrySet()) {
            JobType key = entry.getKey();
            Integer val = entry.getValue();
            if (val > 0) {
                catdataset.setValue(val, key, "");
                JRadioButtonMenuItem box = new JRadioButtonMenuItem(key.getName());
                box.addActionListener(l -> {
                    if (box.isSelected()) {
                        //Add thing back
                        catdataset.setValue(val, key, "");
                    } else {
                        //Remove thing
                        catdataset.removeValue(key, "");
                    }
                });
                box.setSelected(true);

                //Add icon
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

        JFreeChart barchart = ChartFactory.createStackedBarChart("", "Domain axis", "Range Axis", catdataset);
        barchart.removeLegend();
        CategoryPlot plot = (CategoryPlot) barchart.getPlot();

        //Remove all space and labels
        plot.setOrientation(PlotOrientation.HORIZONTAL);
        plot.getRangeAxis().setVisible(false);
        plot.getDomainAxis().setVisible(false);
        plot.setInsets(RectangleInsets.ZERO_INSETS);
        plot.getDomainAxis().setCategoryMargin(0);
        plot.getRangeAxis().setLowerMargin(0);
        plot.getRangeAxis().setUpperMargin(0);

        StackedBarRenderer renderer = (StackedBarRenderer) plot.getRenderer();

        renderer.setBarPainter(new StandardBarPainter());
        renderer.setMaximumBarWidth(1);
        renderer.setItemMargin(-2);
        renderer.setRenderAsPercentages(true);
        renderer.setDrawBarOutline(false);
        renderer.setBaseItemLabelsVisible(true);

        //Empty label
        StandardCategoryItemLabelGenerator gen = new StandardCategoryItemLabelGenerator("", new DecimalFormat("###,###"), new DecimalFormat("0%"));
        renderer.setBaseItemLabelGenerator(gen);
        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator("{0}: {2} ({3})", new DecimalFormat("###,###")));

        for (int i = 0; i < catdataset.getRowCount(); i++) {
            Comparable<?> rowKey = catdataset.getRowKey(i);
            //Get a custom paint for our series key
            Color p = ((JobType) rowKey).getColor();
            //set the new paint to the renderer
            renderer.setSeriesPaint(i, p);
        }

        JPanel chartPanelContainer = new JPanel(new VerticalFlowLayout());
        JLabel currentHoverLabel = new JLabel();
        chartPanelContainer.add(currentHoverLabel);
        
        ChartPanel chartPanel = new ChartPanel(barchart);

        chartPanel.setPopupMenu(null);
        chartPanel.addChartMouseListener(new ChartMouseListener() {
            @Override
            public void chartMouseClicked(ChartMouseEvent cme) {
                ChartEntity entity = cme.getEntity();
                if (entity instanceof CategoryItemEntity) {
                    JobType jobType = (JobType) ((CategoryItemEntity) entity).getRowKey();
                    //Add UI
                    jobInformationPanel.removeAll();
                    jobInformationPanel.add(new JLabel(jobType.getName()));
                    jobInformationPanel.add(new JLabel("Workers: " + Utilities.longToHumanString(catdataset.getValue(jobType, "").longValue())));
                    long laborForceSize = gameState.getObject(selectedCity.getPopulation(), Population.class).getWorkableSize();
                    //Get percentage
                    double percentage = ((double) catdataset.getValue(jobType, "").longValue() / (double) laborForceSize) * 100;
                    jobInformationPanel.add(new JLabel("Percentage: " + percentage + "%"));
                }
            }

            @Override
            public void chartMouseMoved(ChartMouseEvent cme) {
                //Empty
                ChartEntity entity = cme.getEntity();
                if (entity instanceof CategoryItemEntity) {
                    JobType jobType = (JobType) ((CategoryItemEntity) entity).getRowKey();
                    currentHoverLabel.setText(jobType.getName());
                } else {
                    currentHoverLabel.setText("");
                }
            }
        });
        chartPanel.setDomainZoomable(false);
        chartPanel.setRangeZoomable(false);
        chartPanel.setMaximumDrawHeight(40);
        chartPanel.setPreferredSize(new Dimension(chartPanel.getPreferredSize().width, 40));

        //Other panel for more details
        JPanel containerPanel = new JPanel(new VerticalFlowLayout());
        long laborForceSize = gameState.getObject(selectedCity.getPopulation(), Population.class).getWorkableSize();
        containerPanel.add(new JLabel("Labor Force: " + Utilities.longToHumanString(laborForceSize)));
        containerPanel.add(new JLabel("Economic Complexity: "));
        containerPanel.add(new JLabel("Total Economic Output: "));
        containerPanel.add(new JLabel("Economic Output per worker: "));
        containerPanel.add(new JSeparator());
        jobInformationPanel = new JPanel(new VerticalFlowLayout());
        containerPanel.add(jobInformationPanel);

        chartPanelContainer.add(chartPanel);

        jobChartPanel.add(containerPanel, BorderLayout.NORTH);
        jobChartPanel.add(chartPanelContainer, BorderLayout.SOUTH);
        return jobChartPanel;
    }
}
