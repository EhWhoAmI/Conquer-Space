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
import ConquerSpace.client.gui.GraphicsUtil;
import ConquerSpace.client.gui.ObjectListModel;
import ConquerSpace.client.gui.game.planetdisplayer.areas.AreaInformationPanelBuilder;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.city.CityType;
import ConquerSpace.common.game.city.area.Area;
import ConquerSpace.common.game.city.area.AreaClassification;
import ConquerSpace.common.game.city.area.SpacePortArea;
import ConquerSpace.common.game.city.modifier.CityModifier;
import ConquerSpace.common.game.logistics.SupplyNode;
import ConquerSpace.common.game.logistics.SupplySegment;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.population.Population;
import ConquerSpace.common.game.population.PopulationSegment;
import ConquerSpace.common.game.population.jobs.JobType;
import ConquerSpace.common.game.resources.GoodReference;
import ConquerSpace.common.game.resources.ResourceStockpile;
import ConquerSpace.common.game.resources.StoreableReference;
import ConquerSpace.common.game.ships.Ship;
import ConquerSpace.common.game.ships.ShipClass;
import ConquerSpace.common.game.universe.GeographicPoint;
import ConquerSpace.common.game.universe.Vector;
import ConquerSpace.common.game.universe.bodies.Planet;
import ConquerSpace.common.util.DoubleHashMap;
import ConquerSpace.common.util.Utilities;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
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
public class CityInformationPanel extends JPanel {

    City selectedCity;
    Civilization civilization;
    GameState gameState;
    PlanetMapProvider provider;
    GridBagLayout layout;
    Planet planet;
    PlanetCities parent;

    CitySkylinePanel citySkylinePanel;
    CityOverviewPanel cityOverviewPanel;
    MapMinimap mapMinimap;
    JobInformationPanel jobInformationPanel;
    CityIndustryPanel cityIndustryPanel;
    CityEconomyPanel cityEconomyPanel;

    public CityInformationPanel(GameState gameState, PlanetCities parent, City selectedCity, Planet planet, Civilization owner, PlanetMapProvider provider) {
        this.selectedCity = selectedCity;
        this.gameState = gameState;
        this.provider = provider;
        this.planet = planet;
        this.civilization = owner;
        this.parent = parent;

        layout = new GridBagLayout();
        setLayout(layout);

        long start = System.currentTimeMillis();

        //Init components
        citySkylinePanel = new CitySkylinePanel();
        cityOverviewPanel = new CityOverviewPanel();
        mapMinimap = new MapMinimap();
        //Minimap needs a container because of how the drawing works
        JPanel minimapContainer = new JPanel(new BorderLayout());
        minimapContainer.add(mapMinimap, BorderLayout.CENTER);
        minimapContainer.setBorder(new TitledBorder(new LineBorder(Color.gray), "Position on Map"));

        jobInformationPanel = new JobInformationPanel();
        cityIndustryPanel = new CityIndustryPanel();
        cityEconomyPanel = new CityEconomyPanel();

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0;
        constraints.weighty = 0;
        add(citySkylinePanel, constraints);

        constraints.gridy = 0;
        constraints.gridx = 1;
        add(minimapContainer, constraints);

        constraints.gridy = 1;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        add(cityOverviewPanel, constraints);

        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0;
        constraints.weighty = 0;
        constraints.gridy = 2;
        constraints.gridx = 2;
        constraints.gridwidth = 1;
        add(jobInformationPanel, constraints);

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridy = 0;
        constraints.gridx = 2;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.gridheight = 2;
        add(cityIndustryPanel, constraints);

        constraints.gridy = 2;
        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        add(cityEconomyPanel, constraints);

        long end = System.currentTimeMillis();
    }

    //Draws the borders of the borderlayout
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (false) {
            int[][] dims = layout.getLayoutDimensions();
            g.setColor(Color.BLUE);
            int x = 0;
            for (int add : dims[0]) {
                x += add;
                g.drawLine(x, 0, x, getHeight());
            }
            int y = 0;
            for (int add : dims[1]) {
                y += add;
                g.drawLine(0, y, getWidth(), y);
            }
        }
    }

    private class CitySkylinePanel extends JPanel {

        Image bg = null;
        int imageCounter = 0;

        public CitySkylinePanel() {
            int height = 150;
            if (imageCount > 0) {
                int id = selectedCity.getReference().getId() % imageCount;

                double ratio = height / (double) citySkylineImageMap.get(id).getHeight(null);
                int width = (int) ((double) citySkylineImageMap.get(id).getWidth(null) * ratio);
                bg = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D graphics = (Graphics2D) bg.getGraphics();
                graphics.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.drawImage(citySkylineImageMap.get(id).getScaledInstance(width, height, Image.SCALE_DEFAULT), 0, 0, null);
                GraphicsUtil.paintTextWithOutline(selectedCity.getName(), graphics, 30, 0, 150 / 2);

            }
            setPreferredSize(new Dimension(500, height));
        }

        @Override
        protected void paintComponent(Graphics g) {
            //Draw background
            if (bg != null) {
                g.drawImage(bg, 0, 0, null);
            } else {
                //Load image...
            }

        }
    }

    private class CityOverviewPanel extends JPanel {

        public CityOverviewPanel() {
            setLayout(new VerticalFlowLayout());
            //Check if capital city
            for (int i = 0; i < gameState.getCivilizationCount(); i++) {
                Civilization civilization = gameState.getCivilizationObject(i);
                if (civilization.getCapitalCity().equals(selectedCity.getReference())) {
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
            
            JLabel mainProduction = new JLabel("");
            StringJoiner joiner = new StringJoiner(", ");

            for(StoreableReference ref : selectedCity.primaryProduction) {
                joiner.add(gameState.getGood(ref).toString());
            }
            mainProduction.setText("Produces Goods: " + joiner.toString());
            add(mainProduction);

            //Get the number of powerplants leading to it
            //Energy usage
//            JLabel energyUsageLabel = new JLabel(
//                    LOCALE_MESSAGES.getMessage("game.planet.cities.energyusage", energyUsage, energyProvided));
//            add(energyUsageLabel);
            //Growth
            JLabel growthAmount = new JLabel(
                    LOCALE_MESSAGES.getMessage("game.planet.cities.growth", 0));//new JLabel("Growth: " + (selected.getPopulationUnitPercentage()) + "% done, " + increment + "% within the next 40 days.");
            add(growthAmount);

            double unemploymentRate = selectedCity.getUnemploymentRate();
            JLabel unemployment = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.cities.unempoymentrate", Math.round(unemploymentRate * 100)));
            if (unemploymentRate < 1f && unemploymentRate > 0f) {
                unemployment.setForeground(new Color((float) unemploymentRate, 0f, 0f));
            }
            add(unemployment);

            //Max population
//            JLabel maxPopulation = new JLabel(
//                    LOCALE_MESSAGES.getMessage("game.planet.cities.popcap", Utilities.longToHumanString(maxPop)));
//            add(maxPopulation);
            //Check for govenor
            if (selectedCity.getGovernor() != null) {
                JLabel governorLabel = new JLabel(
                        LOCALE_MESSAGES.getMessage("game.planet.cities.governor", selectedCity.getGovernor().getName()));
                add(governorLabel);
            }

            setBorder(new TitledBorder(new LineBorder(Color.gray), LOCALE_MESSAGES.getMessage("game.planet.cities.overview")));
        }
    }

    private class MapMinimap extends JComponent {

        double translateX;
        double translateY;
        boolean isLoaded;
        int enlargementSize = 2;
        private Image planetSurfaceMap = null;
        private double tileSize = 8;

        public MapMinimap() {
            setPreferredSize(new Dimension(150, 150));
            translateX = -selectedCity.getInitialPoint().getX() * tileSize + getWidth() / 2;
            translateY = -selectedCity.getInitialPoint().getY() * tileSize + getWidth() / 2;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2d = (Graphics2D) g;

            //Turn on AA.
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);

            translateX = -selectedCity.getInitialPoint().getX() * tileSize + getWidth() / 2;
            translateY = -selectedCity.getInitialPoint().getY() * tileSize + getWidth() / 2;
            g2d.translate(translateX, translateY);

            //Terrain
            if (provider.isLoaded() && !isLoaded) {
                Image img = provider.getImage();
                planetSurfaceMap = img.getScaledInstance(img.getWidth(null) * enlargementSize, img.getHeight(null) * enlargementSize, Image.SCALE_DEFAULT);
                isLoaded = true;
            }

            if (isLoaded) {
                g2d.drawImage(planetSurfaceMap, 0, 0, null);
            }

            //g2d
            Graphics2D mapGraphics = g2d;

            Iterator<GeographicPoint> distIterator = planet.cityDistributions.keySet().iterator();

            //for (int i = 0; i < size(); i++) {
            Rectangle r = g.getClipBounds();
            while (distIterator.hasNext()) {
                GeographicPoint point = distIterator.next();
                City c = planet.getCity(point);
                if (c != null) {
                    int xPos = (int) (point.getX() * tileSize);
                    int yPos = (int) (point.getY() * tileSize);
                    if (r.contains(new Point(xPos, yPos))) {
                        //Draw supply lines
                        ArrayList<ObjectReference> ref = c.getSupplyConnections();
                        for (int i = 0; i < ref.size(); i++) {
                            SupplySegment segment = gameState.getObject(ref.get(i), SupplySegment.class);
                            SupplyNode pt1 = gameState.getObject(segment.getPoint1(), SupplyNode.class);
                            SupplyNode pt2 = gameState.getObject(segment.getPoint2(), SupplyNode.class);
                            GeographicPoint city1Pt = null;
                            GeographicPoint city2Pt = null;
                            if (pt1 instanceof City) {
                                City originCity = (City) pt1;
                                city1Pt = originCity.getInitialPoint();
                            }
                            if (pt2 instanceof City) {
                                City destCity = (City) pt2;
                                city2Pt = destCity.getInitialPoint();
                            }
                            if (city1Pt != null && city2Pt != null) {
                                //Draw line
                                Line2D.Double line = new Line2D.Double(
                                        city1Pt.getX() * tileSize + tileSize / 2,
                                        city1Pt.getY() * tileSize + tileSize / 2,
                                        city2Pt.getX() * tileSize + tileSize / 2,
                                        city2Pt.getY() * tileSize + tileSize / 2);
                                g2d.setColor(Color.red);
                                g2d.draw(line);
                            }

                        }

                        //Draw city
                        Rectangle2D.Double rect = new Rectangle2D.Double(xPos, yPos, tileSize, tileSize);

                        //Draw tile color
                        mapGraphics.setColor(CityType.getDistrictColor(c.getCityType()));
                        mapGraphics.fill(rect);

                        float fontSize = 16;
                        Font derivedFont = getFont().deriveFont(fontSize);
                        int width = getFontMetrics(derivedFont).stringWidth(c.getName());

                        //Draw city name
                        //Draw fancy text
                        GraphicsUtil.paintTextWithOutline(c.getName(), g, fontSize, xPos - width / 2, yPos + getFontMetrics(derivedFont).getHeight() / 2);
                    }
                }
            }
        }
    }

    private class JobInformationPanel extends JPanel {

        HashMap<JobType, Integer> populationCount;
        private long currentlyWorking;
        private long populationLaborForceSize;

        JTabbedPane tabs;

        public JobInformationPanel() {
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

            checkBoxPanels.add(new JLabel("Labor Force: " + new DecimalFormat("###,###").format(gameState.getObject(selectedCity.population, Population.class).getWorkableSize())));

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

            JFreeChart chart = ChartFactory.createPieChart(LOCALE_MESSAGES.getMessage("game.planet.cities.chart.jobs"), dataset, true, true, false);
            PiePlot piePlot = (PiePlot) chart.getPlot();

            PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(
                    "{0}: {1} ({2})", new DecimalFormat("###,###"), new DecimalFormat("0%"));
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
            tabs.add(LOCALE_MESSAGES.getMessage("game.planet.cities.tab.chart"), jobTablePanel);
            tabs.add(LOCALE_MESSAGES.getMessage("game.planet.cities.tab.table"), new JScrollPane(jobTable));
            tabs.add("Segments", segmentChartPanel);
            add(tabs, BorderLayout.CENTER);
            setBorder(new TitledBorder(new LineBorder(Color.gray), LOCALE_MESSAGES.getMessage("game.planet.cities.chart.jobs")));
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

            private final String[] jobListTableColunmNames = {
                LOCALE_MESSAGES.getMessage("game.planet.cities.table.jobname"),
                LOCALE_MESSAGES.getMessage("game.planet.cities.table.count"),
                LOCALE_MESSAGES.getMessage("game.planet.cities.table.percentage")};

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
    }

    private class CityIndustryPanel extends JPanel {

        JScrollPane scrollPane;
        boolean hasChangedSize = false;
        AreaConstructionPanel areaConstructionPanel;
        JTabbedPane tabs;

        public CityIndustryPanel() {
            setLayout(new BorderLayout());
            //Do industry and areas
            JXTaskPaneContainer industriesList = new JXTaskPaneContainer();
            //Get list of industries

            HashMap<AreaClassification, Integer> industriesMap = new HashMap<>();
            for (int i = 0; i < selectedCity.areas.size(); i++) {
                Area area = gameState.getObject(selectedCity.areas.get(i), Area.class);
                if (area != null) {
                    if (industriesMap.containsKey(area.getAreaType())) {
                        industriesMap.put(area.getAreaType(), industriesMap.get(area.getAreaType()) + 1);
                    } else {
                        industriesMap.put(area.getAreaType(), 1);
                    }
                }
            }

            for (Map.Entry<AreaClassification, Integer> entry : industriesMap.entrySet()) {
                AreaClassification key = entry.getKey();
                Integer val = entry.getValue();

                JXTaskPane pane = new JXTaskPane(key.toString() + " - " + val.toString());
                //List all areas of this type
                for (int i = 0; i < selectedCity.areas.size(); i++) {
                    Area area = gameState.getObject(selectedCity.areas.get(i), Area.class);
                    if (area.getAreaType().equals(key)) {
                        //Then add to list I guess
                        AreaInformationPanelBuilder builder = new AreaInformationPanelBuilder(gameState);
                        area.accept(builder);
                        pane.add(builder.getPanel(area));
                    }
                }

                pane.setCollapsed(true);
                pane.setAnimated(false);
                industriesList.add(pane);
            }
            //Set to better colors  
            industriesList.setBackground(UIManager.getDefaults().getColor("Panel.background"));
            industriesList.setForeground(UIManager.getDefaults().getColor("Label.foreground"));
            industriesList.setFont(UIManager.getDefaults().getFont("Label.font"));

            scrollPane = new JScrollPane(industriesList);
            scrollPane.setPreferredSize(new Dimension(831, 312));
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            areaConstructionPanel = new AreaConstructionPanel(gameState, planet, civilization, selectedCity);

            CityProductionPanel production = new CityProductionPanel();
            tabs = new JTabbedPane();

            tabs.add(LOCALE_MESSAGES.getMessage("game.planet.cities.tab.industries"), scrollPane);
            tabs.add(LOCALE_MESSAGES.getMessage("game.planet.cities.tab.construction"), areaConstructionPanel);
            tabs.add(LOCALE_MESSAGES.getMessage("game.planet.cities.production"), production);
            add(tabs, BorderLayout.CENTER);

            setBorder(new TitledBorder(new LineBorder(Color.gray), LOCALE_MESSAGES.getMessage("game.planet.cities.industries")));
            setPreferredSize(new Dimension(831, 312));
        }
    }

    private class CityEconomyPanel extends JPanel {

        JTabbedPane tabs;
        JTable resourceTable;
        JTable resourceDemandTable;
        JTable resourceInputTable;
        JTable resourceOutputTable;
        JList<String> modifierList;
        JList<String> connectedCityList;

        public CityEconomyPanel() {
            setLayout(new BorderLayout());
            tabs = new JTabbedPane();

            ObjectListModel<CityModifier> modifierListModel = new ObjectListModel<>();
            modifierListModel.setElements(selectedCity.cityModifiers);
            modifierListModel.setHandler(l -> {
                return (l.toString());
            });

            modifierList = new JList<>(modifierListModel);

            ObjectListModel<ObjectReference> connectedCityListModel = new ObjectListModel<>();
            for (ObjectReference connection : selectedCity.getSupplyConnections()) {
                SupplySegment seg = gameState.getObject(connection, SupplySegment.class);
                if (!seg.getPoint1().equals(selectedCity.getReference())) {
                    connectedCityListModel.addElement(seg.getPoint1());
                } else {
                    connectedCityListModel.addElement(seg.getPoint2());
                }
            }
            connectedCityListModel.setHandler(l -> {
                return gameState.getObject(l, City.class).toString();
            });

            connectedCityList = new JList<>(connectedCityListModel);
            connectedCityList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                        //Goto the city selected
                        City city
                                = gameState.getObject(connectedCityListModel.getObject(connectedCityList.getSelectedIndex()), City.class);
                        parent.showCity(city);
                    }
                }

            });

            resourceTable = new JTable(new StockpileStorageModel());
            resourceTable.getColumnModel().getColumn(2).setCellRenderer(new StockpileResourceDeltaCellRenderer());

            resourceDemandTable = new JTable(new ResourceDemandModel());

            resourceInputTable = new JTable(new StockpileInModel());
            resourceOutputTable = new JTable(new StockpileOutModel());

            tabs.add(LOCALE_MESSAGES.getMessage("game.planet.cities.modifiers"), new JScrollPane(modifierList));
            tabs.add(LOCALE_MESSAGES.getMessage("game.planet.cities.linked"), new JScrollPane(connectedCityList));
            tabs.add(LOCALE_MESSAGES.getMessage("game.planet.cities.ledger"), new JScrollPane(resourceTable));
            tabs.add("Resource Demands", new JScrollPane(resourceDemandTable));
            tabs.add("Input", new JScrollPane(resourceInputTable));
            tabs.add("Output", new JScrollPane(resourceOutputTable));

            //Show table of resources
            add(tabs, BorderLayout.CENTER);
            setBorder(new TitledBorder(new LineBorder(Color.gray), LOCALE_MESSAGES.getMessage("game.planet.cities.economy")));
        }

        private class StockpileStorageModel extends AbstractTableModel {

            String[] colunmNames = {
                LOCALE_MESSAGES.getMessage("game.planet.resources.table.good"),
                LOCALE_MESSAGES.getMessage("game.planet.resources.table.count"),
                LOCALE_MESSAGES.getMessage("game.planet.resources.table.delta", gameState.GameRefreshRate)};

            @Override
            public int getRowCount() {
                if (selectedCity == null) {
                    return 0;
                } else {
                    return selectedCity.storedTypes().length;
                }
            }

            @Override
            public int getColumnCount() {
                return colunmNames.length;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                StoreableReference storedValue = selectedCity.storedTypes()[rowIndex];
                DecimalFormat df = new DecimalFormat("###.##");
                if (selectedCity != null) {
                    switch (columnIndex) {
                        case 0:
                            return gameState.getGood(storedValue);
                        case 1:

                            String unitCount = df.format(selectedCity.getResourceAmount(storedValue));

                            if (selectedCity.getResourceAmount(storedValue) > 10000) {
                                unitCount = Utilities.longToHumanString(selectedCity.getResourceAmount(storedValue).longValue());
                            }

                            return unitCount
                                    + " units/"
                                    //Get mass in kg...
                                    + Utilities.longToHumanString((long) ((selectedCity.getResourceAmount(storedValue) * gameState.getGood(storedValue).getMass()) / 1000))
                                    + " tons";
                        case 2:
                            HashMap<String, Double> ledger = selectedCity.resourceLedger.get(storedValue);
                            double change = 0;
                            if (ledger != null) {
                                for (Double d : ledger.values()) {
                                    change += d;
                                }
                            }
                            return change;
                    }
                }
                return 0;
            }

            @Override
            public String getColumnName(int column) {
                return colunmNames[column];
            }
        }

        private class ResourceDemandModel extends AbstractTableModel {

            String[] colunmNames = {
                LOCALE_MESSAGES.getMessage("game.planet.resources.table.good"),
                LOCALE_MESSAGES.getMessage("game.planet.resources.table.count")
            };

            @Override
            public int getRowCount() {
                if (selectedCity == null) {
                    return 0;
                } else {
                    return selectedCity.resourceDemands.size();
                }
            }

            @Override
            public int getColumnCount() {
                return colunmNames.length;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                //Lol needs to be efficient
                StoreableReference storedValue = new ArrayList<StoreableReference>(selectedCity.resourceDemands.keySet()).get(rowIndex);

                if (selectedCity != null) {
                    switch (columnIndex) {
                        case 0:
                            return gameState.getGood(storedValue);
                        case 1:
                            return selectedCity.resourceDemands.get(storedValue).toString()
                                    + "u/"
                                    //Get mass in kg...
                                    + (selectedCity.resourceDemands.get(storedValue) * gameState.getGood(storedValue).getMass())
                                    + " kg";
                    }
                }
                return 0;
            }

            @Override
            public String getColumnName(int column) {
                return colunmNames[column];
            }
        }

        class StockpileResourceDeltaCellRenderer extends DefaultTableCellRenderer {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
                //Cells are by default rendered as a JLabel.
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                //Get the status for the current row.
                if (col == 2) {
                    String stringValue = String.valueOf(value);
                    try {
                        double doubleValue = Double.parseDouble(stringValue);
                        //Set bold...
                        if (isSelected) {
                            if (doubleValue > 0) {
                                //Stonks
                                l.setForeground(Color.green);
                                l.setFont(l.getFont().deriveFont(Font.BOLD));
                                l.setText("+" + l.getText() + "\u21e7"); //Stonks!
                            } else if (doubleValue < 0) {
                                //Not stonks
                                l.setForeground(Color.red);
                                l.setFont(l.getFont().deriveFont(Font.BOLD));
                                l.setText(l.getText() + "\u21e9"); //Add downwards arrow :(
                            }
                        } else {
                            if (doubleValue > 0) {
                                //Stonks
                                l.setForeground(new Color(15, 157, 88));
                                l.setFont(l.getFont().deriveFont(Font.BOLD));
                                l.setText("+" + l.getText() + "\u21e7"); //Stonks!
                            } else if (doubleValue < 0) {
                                //Not stonks
                                l.setForeground(new Color(230, 74, 25));
                                l.setFont(l.getFont().deriveFont(Font.BOLD));
                                l.setText(l.getText() + "\u21e9"); //Add downwards arrow :(
                            }
                        }

                        DoubleHashMap<String> map = selectedCity.resourceLedger.get(selectedCity.storedTypes()[row]);
                        if (map != null) {
                            String text = "<html>";
                            for (Map.Entry<String, Double> object : map.entrySet()) {
                                Object key = object.getKey();
                                Double val = object.getValue();

                                text += key + ": ";
                                if (Math.signum(val) == -1) {
                                    text += "-";
                                }
                                text += Utilities.longToHumanString(Math.abs(val.longValue()));
                                text += "<br/>";
                            }
                            text += "</html>";
                            l.setToolTipText(text);
                        }
                    } catch (NumberFormatException nfe) {

                    }
                }
                //Return the JLabel which renders the cell.
                return l;
            }
        }

        private class StockpileOutModel extends AbstractTableModel {

            String[] colunmNames = {"City", "Good", "Amount"};

            ArrayList<ResourcesChangeTuple> tuple;

            public StockpileOutModel() {
                tuple = new ArrayList<>();
                //Get the city
                for (Map.Entry<ResourceStockpile, DoubleHashMap<StoreableReference>> entry : selectedCity.resourcesSentTo.entrySet()) {
                    ResourceStockpile resourceStockpile = entry.getKey();
                    DoubleHashMap<StoreableReference> val = entry.getValue();

                    for (Map.Entry<StoreableReference, Double> entry1 : val.entrySet()) {
                        StoreableReference reference = entry1.getKey();
                        Double goodCount = entry1.getValue();
                        tuple.add(new ResourcesChangeTuple(resourceStockpile, reference, goodCount));
                    }
                }
            }

            @Override
            public int getRowCount() {
                return tuple.size();
            }

            @Override
            public int getColumnCount() {
                return colunmNames.length;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                //get thingy
                switch (columnIndex) {
                    case 0:
                        return tuple.get(rowIndex).stock;
                    case 1:
                        return gameState.getGood(tuple.get(rowIndex).ref).getName();
                    case 2:
                        return tuple.get(rowIndex).amount;
                }
                return 0;
            }

            @Override
            public String getColumnName(int column) {
                return colunmNames[column];
            }

        }

        private class StockpileInModel extends AbstractTableModel {

            String[] colunmNames = {"City", "Good", "Amount"};

            ArrayList<ResourcesChangeTuple> tuple;

            public StockpileInModel() {
                tuple = new ArrayList<>();
                //Get the city
                for (Map.Entry<ResourceStockpile, DoubleHashMap<StoreableReference>> entry : selectedCity.resourcesGainedFrom.entrySet()) {
                    ResourceStockpile resourceStockpile = entry.getKey();
                    DoubleHashMap<StoreableReference> val = entry.getValue();

                    for (Map.Entry<StoreableReference, Double> entry1 : val.entrySet()) {
                        StoreableReference reference = entry1.getKey();
                        Double goodCount = entry1.getValue();
                        tuple.add(new ResourcesChangeTuple(resourceStockpile, reference, goodCount));
                    }
                }
            }

            @Override
            public int getRowCount() {
                return tuple.size();
            }

            @Override
            public int getColumnCount() {
                return colunmNames.length;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                //get thingy
                switch (columnIndex) {
                    case 0:
                        return tuple.get(rowIndex).stock;
                    case 1:
                        return gameState.getGood(tuple.get(rowIndex).ref).getName();
                    case 2:
                        return tuple.get(rowIndex).amount;
                }
                return 0;
            }

            @Override
            public String getColumnName(int column) {
                return colunmNames[column];
            }

        }

        class ResourcesChangeTuple {

            ResourceStockpile stock;
            StoreableReference ref;
            Double amount;

            public ResourcesChangeTuple(ResourceStockpile stock, StoreableReference ref, Double amount) {
                this.stock = stock;
                this.ref = ref;
                this.amount = amount;
            }

            public ResourcesChangeTuple() {
            }

        }
    }

    /**
     * Panel to order the production of certain goods or something.
     */
    public class CityProductionPanel extends JPanel {

        JList<String> shipClassList;

        public CityProductionPanel() {
            setLayout(new VerticalFlowLayout());
            add(new JLabel(LOCALE_MESSAGES.getMessage("game.planet.cities.production")));

            //Create new space ship menu
            add(new JLabel(LOCALE_MESSAGES.getMessage("game.planet.cities.spaceship")));
            ObjectListModel<ObjectReference> shipList = new ObjectListModel<>();
            shipList.setElements(civilization.shipClasses);
            shipList.setHandler(l -> {
                return gameState.getObject(l, ShipClass.class).getName();
            });
            shipClassList = new JList<>(shipList);
            add(new JScrollPane(shipClassList));
            JButton createButton = new JButton(LOCALE_MESSAGES.getMessage("game.planet.cities.create"));
            createButton.addActionListener(l -> {
                //Find the space ports on this planet... 
                //Get selected ship
                if (shipClassList.getSelectedIndex() < -1) {
                    JOptionPane.showInternalMessageDialog(this, "You need to select a ship");
                    return;
                }
                int launchPadCount = 0;
                for (ObjectReference cityIndex : planet.cities) {
                    City city = gameState.getObject(cityIndex, City.class);
                    for (ObjectReference areaIndex : city.areas) {
                        Area area = gameState.getObject(areaIndex, Area.class);
                        if (area instanceof SpacePortArea) {
                            SpacePortArea port = (SpacePortArea) area;
                            //Add selected ship class
                            //UI to create ship
                            ShipClass shipClass = gameState.getObject(shipList.getObject(shipClassList.getSelectedIndex()), ShipClass.class);
                            Ship ship = new Ship(gameState, shipClass, planet.getY(), planet.getX(), new Vector(0, 0), planet.getUniversePath());
                            //Set random name for now
                            ship.setName(UUID.randomUUID().toString());
                            port.landedShips.add(ship.getReference());
                            civilization.spaceships.add(ship.getReference());

                            JOptionPane.showInternalMessageDialog(this, LOCALE_MESSAGES.getMessage("game.planet.cities.launch.alert", shipClass.getName(), ship.getName()));

                            //Deselect
                            shipClassList.clearSelection();
                            return;
                        }
                    }
                }
                JOptionPane.showInternalMessageDialog(this, LOCALE_MESSAGES.getMessage("game.planet.cities.needport"));
            });
            add(createButton);
        }

        @Override
        public void setVisible(boolean aFlag) {
            super.setVisible(aFlag);
            if (aFlag) {
                shipClassList.updateUI();
            }
        }
    }

    private static final HashMap<Integer, Image> citySkylineImageMap = new HashMap<>();
    private static int imageCount = 0;

    static {
        int height = 150;
        Thread t = new Thread(() -> {
            try {
                //Choose random image from dir
                File panoFiles = new File("assets/img/pano/");
                File[] imageList = panoFiles.listFiles();
                int i = 0;
                for (File imgfile : imageList) {
                    Image img = ImageIO.read(imgfile);

                    double ratio = height / (double) img.getHeight(null);
                    int width = (int) ((double) img.getWidth(null) * ratio);
                    Image bg = new BufferedImage(500, height, BufferedImage.TYPE_INT_ARGB);
                    long end = System.currentTimeMillis();
                    Graphics2D graphics = (Graphics2D) bg.getGraphics();
                    graphics.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                    graphics.drawImage(img.getScaledInstance(width, height, Image.SCALE_DEFAULT), 0, 0, null);

                    //Save image
                    citySkylineImageMap.put(i, bg);
                    i++;
                }
                imageCount = i;

            } catch (IOException ex) {
                //System.out.println("no file");
                //Ignore for now 
                //FIXME
            }
            long end = System.currentTimeMillis();

        });
        t.start();
    }

}
