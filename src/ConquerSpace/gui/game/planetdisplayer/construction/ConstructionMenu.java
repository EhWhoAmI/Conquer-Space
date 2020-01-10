package ConquerSpace.gui.game.planetdisplayer.construction;

import ConquerSpace.game.GameController;
import ConquerSpace.game.GameUpdater;
import ConquerSpace.game.actions.Actions;
import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.BuildingCost;
import ConquerSpace.game.buildings.BuildingCostGetter;
import ConquerSpace.game.buildings.Observatory;
import ConquerSpace.game.buildings.CityDistrict;
import ConquerSpace.game.buildings.ResourceMinerDistrict;
import ConquerSpace.game.buildings.ResourceStorage;
import ConquerSpace.game.buildings.SpacePort;
import ConquerSpace.game.population.PopulationUnit;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.resources.ResourceVein;
import ConquerSpace.game.universe.ships.launch.LaunchSystem;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.game.universe.spaceObjects.terrain.Terrain;
import ConquerSpace.gui.renderers.TerrainRenderer;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author zyunl
 */
public class ConstructionMenu extends JPanel {

    private Planet p;

    private JPanel planetSectors;

    private JPanel buildingThingPanel;

    private JPanel buildingInfoPanel;
    private JComboBox<String> buildingType;
    private DefaultComboBoxModel<String> buildingModel;
    private JPanel buildLogisticsPanel;
    private JLabel xPosLabel;
    private JLabel yPosLabel;
    private JSpinner xposSpinner;
    private JSpinner yPosSpinner;
    private JPanel costPanel;
    private JTable costTable;
    private DefaultTableModel costTableModel;

    //private 
    //private JTable resourceCost;
    private CardLayout buildCardLayout;

    private BuildPopulationStorage popStoragePanel;

    private BuildSpaceLaunchSite buildSpaceLaunchSite;

    private BuildObservatoryMenu buildObservatoryMenu;

    private BuildResourceStorageMenu buildResourceStorageMenu;

    private BuildResourceGenerationMenu buildMiningStorageMenu;

    private BuildIndustrialAreaMenu buildIndustrialAreaMenu;

    private JButton buildButton;

    private final String RESIDENTIAL = "Residential area";
    private final String LAUNCH = "Launch Systems";
    private final String OBSERVATORY = "Observatory";
    private final String RESOURCE_STOCKPILE = "Resource Storage";
    private final String RESOURCE_MINER = "Resource Miner";
    private final String INDUSTRIAL_DISTRICT = "Industrial Area";

    private Civilization c;

    public ConstructionMenu(Universe u, Planet p, Civilization c) {
        this.p = p;
        this.c = c;
        setLayout(new BorderLayout());

        planetSectors = new JPanel();
        PlanetSectorDisplayer sectorDisplayer = new PlanetSectorDisplayer(p, c);
        sectorDisplayer.setWhatToShow(PlanetSectorDisplayer.SHOW_ALL_RESOURCES);
        JPanel wrapper = new JPanel();
        wrapper.add(sectorDisplayer);

        buildingThingPanel = new JPanel();
        buildingThingPanel.setLayout(new VerticalFlowLayout());
        //Format build tab
        buildingInfoPanel = new JPanel();
        buildingModel = new DefaultComboBoxModel<>();
        buildingModel.addElement(RESIDENTIAL);
        if (c.values.containsKey("haslaunch") && c.values.get("haslaunch") == 1) {
            //Do things
            buildingModel.addElement(LAUNCH);
        }
        buildingModel.addElement(OBSERVATORY);
        buildingModel.addElement(RESOURCE_STOCKPILE);
        buildingModel.addElement(RESOURCE_MINER);
        buildingModel.addElement(INDUSTRIAL_DISTRICT);

        JPanel mainItemContainer = new JPanel();
        buildCardLayout = new CardLayout();
        mainItemContainer.setLayout(buildCardLayout);

        popStoragePanel = new BuildPopulationStorage();

        buildSpaceLaunchSite = new BuildSpaceLaunchSite(c);

        buildObservatoryMenu = new BuildObservatoryMenu();

        buildResourceStorageMenu = new BuildResourceStorageMenu();

        buildMiningStorageMenu = new BuildResourceGenerationMenu();

        buildIndustrialAreaMenu = new BuildIndustrialAreaMenu();

        JPanel container = new JPanel(new BorderLayout());
        mainItemContainer.add(popStoragePanel, RESIDENTIAL);

        mainItemContainer.add(buildSpaceLaunchSite, LAUNCH);

        container = new JPanel(new BorderLayout());
        container.add(buildObservatoryMenu, BorderLayout.CENTER);
        mainItemContainer.add(container, OBSERVATORY);

        container = new JPanel(new BorderLayout());
        container.add(buildResourceStorageMenu, BorderLayout.CENTER);
        mainItemContainer.add(container, RESOURCE_STOCKPILE);

        mainItemContainer.add(buildMiningStorageMenu, RESOURCE_MINER);

        container = new JPanel(new BorderLayout());
        container.add(buildIndustrialAreaMenu, BorderLayout.CENTER);
        mainItemContainer.add(container, INDUSTRIAL_DISTRICT);

        buildCardLayout.show(mainItemContainer, RESIDENTIAL);

        //Do whatever, add action listeners
        buildingType = new JComboBox<String>(buildingModel);
        buildingType.addActionListener(l -> {//Then update
            int selected = buildingType.getSelectedIndex();
            buildingModel.removeAllElements();
            buildingModel.addElement(RESIDENTIAL);
            if (c.values.containsKey("haslaunch") && c.values.get("haslaunch") == 1) {
                //Do things
                buildingModel.addElement(LAUNCH);
            }
            buildingModel.addElement(OBSERVATORY);
            buildingModel.addElement(RESOURCE_STOCKPILE);
            buildingModel.addElement(RESOURCE_MINER);
            buildingModel.addElement(INDUSTRIAL_DISTRICT);

            buildingType.setSelectedIndex(selected);
        });

        buildingType.addActionListener(a -> {
            if (buildingType.getSelectedIndex() > -1) {
                switch ((String) buildingType.getSelectedItem()) {
                    case RESIDENTIAL:
                        //Configure cost
                        //BuildingCostGetter.getCost("", c);
                        buildCardLayout.show(mainItemContainer, RESIDENTIAL);
                        break;
                    case LAUNCH:
                        //Configure cost
                        buildCardLayout.show(mainItemContainer, LAUNCH);
                        break;
                    case OBSERVATORY:
                        //Configure cost
                        setBuildingCost(BuildingCostGetter.getCost("observatory", c));
                        buildCardLayout.show(mainItemContainer, OBSERVATORY);
                        break;
                    case RESOURCE_STOCKPILE:
                        //Configure cost
                        buildCardLayout.show(mainItemContainer, RESOURCE_STOCKPILE);
                        break;
                    case RESOURCE_MINER:
                        //Configure cost
                        buildCardLayout.show(mainItemContainer, RESOURCE_MINER);
                        break;
                    case INDUSTRIAL_DISTRICT:
                        buildCardLayout.show(mainItemContainer, INDUSTRIAL_DISTRICT);
                        break;
                }
            }
        });

        buildButton = new JButton("Build!");
        buildButton.addActionListener(a -> {
            String item = (String) buildingType.getSelectedItem();
            boolean toReset = false;
            //Get x and y position
            GeographicPoint buildingPos = new GeographicPoint((int) xposSpinner.getValue(), (int) yPosSpinner.getValue());
            //Check if any buildings there:
            int toBuild = JOptionPane.YES_OPTION;
            if (p.buildings.containsKey(buildingPos)) {
                toBuild = JOptionPane.showConfirmDialog(null, "Do you want to demolish a " + p.buildings.get(buildingPos).toString() + " on the tile you are building?",
                        "Are you sure you want to demolish a building?", JOptionPane.YES_NO_OPTION);
            }
            if (toBuild == JOptionPane.YES_OPTION) {
                if (item.equals(RESIDENTIAL)) {
                    CityDistrict storage = new CityDistrict();
                    storage.setMaxStorage((int) popStoragePanel.maxPopulationTextField.getValue());
                    Actions.buildBuilding(p, buildingPos, storage, c, 1);
                    //Reset...
                    toReset = true;
                } else if (item.equals(LAUNCH)) {
                    //Get civ launching type...
                    //SpacePortBuilding port = new SpacePortBuilding(0, (Integer)buildSpaceLaunchSite.maxPopulation.getValue(), (LaunchSystem) buildSpaceLaunchSite.launchTypesValue.getSelectedItem(), p);
                    //Actions.buildBuilding(p, new ConquerSpace.game.universe.Point((int)xposSpinner.getValue(), (int)yPosSpinner.getValue()), port, 0, 1);
                    SpacePort port = new SpacePort((LaunchSystem) buildSpaceLaunchSite.launchTypesValue.getSelectedItem(), (Integer) buildSpaceLaunchSite.maxPopulation.getValue());
                    Actions.buildBuilding(p, buildingPos, port, c, 1);
                    toReset = true;
                } else if (item.equals(OBSERVATORY)) {
                    StarSystem sys = u.getStarSystem(p.getParentStarSystem());
                    Observatory observatory = new Observatory(
                            GameUpdater.Calculators.Optics.getRange(1, (int) buildObservatoryMenu.lensSizeSpinner.getValue()),
                            (Integer) buildObservatoryMenu.lensSizeSpinner.getValue(),
                            c.getID(), new ConquerSpace.game.universe.Point(sys.getX(), sys.getY()));
                    //Add visionpoint to civ
                    c.visionPoints.add(observatory);
                    Actions.buildBuilding(p, buildingPos, observatory, c, 1);
                    toReset = true;
                } else if (item.equals(RESOURCE_STOCKPILE)) {
                    ResourceStorage stor = new ResourceStorage(p);
                    //Add the stuff...

                    for (int i = 0; i < buildResourceStorageMenu.resourceToPut.getModel().getSize(); i++) {
                        Resource next = buildResourceStorageMenu.resourceToPut.getModel().getElementAt(i);

                        stor.addResourceTypeStore(next);
                    }
                    c.resourceStorages.add(stor);
                    Actions.buildBuilding(p, buildingPos, stor, c, 1);
                    toReset = true;
                } else if (item.equals(RESOURCE_MINER)) {
                    ResourceMinerDistrict miner = new ResourceMinerDistrict(null, (double) buildMiningStorageMenu.miningSpeedSpinner.getValue());
                    //Add the stuff...
                    //Get the map of things, and calculate the stuff
                    int x = (int) xposSpinner.getValue();
                    int y = (int) yPosSpinner.getValue();
                    Resource res = (Resource) buildMiningStorageMenu.resourceToMine.getSelectedItem();
                    for (ResourceVein v : p.resourceVeins) {
                        if (Math.hypot(x - v.getX(), y - v.getY()) < v.getRadius() && res.equals(v.getResourceType())) {
                            miner.setVeinMining(v);
                            break;
                        }
                    }
                    //Add miners
                    miner.population.add(new PopulationUnit(c.getFoundingSpecies()));
                    Actions.buildBuilding(p, buildingPos, miner, c, 1);
                    toReset = true;
                }
                if (toReset) {
                    if ((int) xposSpinner.getValue() > 0) {
                        xposSpinner.setValue(xposSpinner.getNextValue());
                    } else {
                        xposSpinner.setValue(1);
                    }
                    //yPosSpinner.setValue(0);
                    //Then show alert
                    //But like it will be annoying....
                }
            }
        });

        buildingInfoPanel.setLayout(new BorderLayout());
        buildingInfoPanel.add(buildingType, BorderLayout.NORTH);
        buildingInfoPanel.add(mainItemContainer, BorderLayout.CENTER);
        buildingInfoPanel.add(buildButton, BorderLayout.SOUTH);

        buildLogisticsPanel = new JPanel();
        buildLogisticsPanel.setLayout(new VerticalFlowLayout());

        JPanel xypositionPanel = new JPanel();

        xPosLabel = new JLabel("X");
        SpinnerNumberModel xSpinnerMod = new SpinnerNumberModel(0, 0, p.getPlanetSize() * 2, -1);
        xposSpinner = new JSpinner(xSpinnerMod);

        xypositionPanel.add(xPosLabel);
        xypositionPanel.add(xposSpinner);

        yPosLabel = new JLabel("Y");
        SpinnerNumberModel ySpinnerMod = new SpinnerNumberModel(0, 0, p.getPlanetSize(), -1);
        yPosSpinner = new JSpinner(ySpinnerMod);

        ChangeListener listener = a -> {
            //Set location
            sectorDisplayer.showLocation(new Point((int) xSpinnerMod.getValue(), (int) ySpinnerMod.getValue()), Color.RED);
            sectorDisplayer.repaint();
        };

        xposSpinner.addChangeListener(listener);
        yPosSpinner.addChangeListener(listener);

        xypositionPanel.add(yPosLabel);
        xypositionPanel.add(yPosSpinner);

        buildLogisticsPanel.add(xypositionPanel);

        costPanel = new JPanel();
        //Add the tables 
        costTableModel = new DefaultTableModel(new String[]{"Resource", "Amount"}, 0);
        costTable = new JTable(costTableModel);
        costPanel.add(new JScrollPane(costTable));
        buildLogisticsPanel.add(new JLabel("Cost per turn"));
        buildLogisticsPanel.add(costPanel);

        buildingThingPanel.add(buildingInfoPanel);
        buildingThingPanel.add(buildLogisticsPanel);

        JScrollPane sectorsScrollPane = new JScrollPane(wrapper);
        planetSectors.add(sectorsScrollPane);
        add(planetSectors, BorderLayout.NORTH);
        add(buildingThingPanel, BorderLayout.CENTER);
        //Add empty panel
        //add(new JPanel());
    }

    public void update() {
    }

    public void setBuildingCost(BuildingCost cost) {
        costTableModel.setRowCount(0);
        for (Map.Entry<Resource, Integer> entry : cost.cost.entrySet()) {
            Resource key = entry.getKey();
            Integer value = entry.getValue();
            costTableModel.addRow(new String[]{key.getName(), value.toString()});
        }
    }

    private class PlanetSectorDisplayer extends JPanel implements MouseListener {

        private final int SHOW_ALL = -1;
        int resourceToShow = SHOW_ALL;

        private int whatToShow = PLANET_BUILDINGS;
        static final int PLANET_BUILDINGS = 0;
        static final int PLANET_RESOURCES = 1;
        static final int SHOW_ALL_RESOURCES = 2;
        private JPopupMenu menu;
        private Civilization c;
        private Color color;
        private Point point;
        private Image img = null;
        private Terrain terrain;
        private Point lastClicked;
        private TerrainRenderer renderer;

        int size = 2;

        public PlanetSectorDisplayer(Planet p, Civilization c) {
            this.c = c;
            setPreferredSize(
                    new Dimension(p.getPlanetSize() * 4, p.getPlanetSize() * 2));
            menu = new JPopupMenu();
            addMouseListener(this);
            renderer = new TerrainRenderer(p);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            if (img == null) {
                img = renderer.getImage(2d);
            }
            //The thingy has to be a square number
            //Times to draw the thingy
            if (whatToShow == PLANET_RESOURCES) {
                //Set opacity
                //g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            }
            g2d.drawImage(img, 0, 0, null);

            if (whatToShow == PLANET_RESOURCES || whatToShow == SHOW_ALL_RESOURCES) {
                //Set opacity
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55f));
                //Draw the circles
                if (((String) buildingType.getSelectedItem()).equals(RESOURCE_MINER)) {
                    for (ResourceVein v : p.resourceVeins) {
                        //Draw...
                        if ((resourceToShow == SHOW_ALL || resourceToShow == v.getResourceType().getId()) && ((Resource) buildMiningStorageMenu.resourceToMine.getSelectedItem()).equals(v.getResourceType())) {
                            Ellipse2D.Float circe = new Ellipse2D.Float(v.getX() * size, v.getY() * size, v.getRadius() * size, v.getRadius() * size);
                            g2d.setColor(v.getResourceType().getColor());
                            g2d.fill(circe);
                        }
                    }
                }
            }
            if (whatToShow == PLANET_BUILDINGS || whatToShow == SHOW_ALL_RESOURCES) {
                //Draw buildings
                for (Map.Entry<GeographicPoint, Building> en : p.buildings.entrySet()) {
                    GeographicPoint p = en.getKey();
                    Building Building = en.getValue();
                    //Draw
                    Rectangle2D.Float rect = new Rectangle2D.Float(p.getX() * size, p.getY() * size, size, size);
                    g2d.setColor(Building.getColor());
                    g2d.fill(rect);
                }
            }
            if (whatToShow == SHOW_ALL_RESOURCES && point != null && color != null) {
                //Show thingy
                //Surround with yellow marker
                Color invc = new Color(255 - color.getRed(),
                        255 - color.getGreen(),
                        255 - color.getBlue());
                Rectangle2D.Float bgRect = new Rectangle2D.Float((float) point.getX() * size - size, (float) point.getY() * size - size, size * 3, size * 3);
                g2d.setColor(invc);
                g2d.fill(bgRect);
                Rectangle2D.Float rect = new Rectangle2D.Float((float) point.getX() * size, (float) point.getY() * size, size, size);
                g2d.setColor(color);
                g2d.fill(rect);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            lastClicked = e.getPoint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        public void setWhatToShow(int what) {
            whatToShow = what;
            repaint();
        }

        public void setResourceViewing(int wat) {
            resourceToShow = wat;
        }

        public Point getLastClicked() {
            return lastClicked;
        }

        public void showLocation(Point pt, Color c) {
            point = pt;
            color = c;
        }
    }
}
