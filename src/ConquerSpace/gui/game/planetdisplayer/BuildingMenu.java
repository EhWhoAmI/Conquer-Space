package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.game.GameController;
import ConquerSpace.game.GameUpdater;
import ConquerSpace.game.actions.Actions;
import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.Observatory;
import ConquerSpace.game.buildings.ResourceGatherer;
import ConquerSpace.game.buildings.ResourceStorage;
import ConquerSpace.game.buildings.SpacePort;
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
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

/**
 *
 * @author zyunl
 */
public class BuildingMenu extends JPanel {

    private Planet p;

    private JPanel planetSectors;

    private JPanel buildingThingPanel;

    private JPanel buildingInfoPanel;
    private JComboBox<String> buildingType;
    private DefaultComboBoxModel<String> buildingModel;
    private JPanel buildPanelXYPosContainer;
    private JLabel xPosLabel;
    private JLabel yPosLabel;
    private JSpinner xposSpinner;
    private JSpinner yPosSpinner;

    private CardLayout buildCardLayout;

    private BuildPopulationStorage popStoragePanel;

    private BuildSpaceLaunchSite buildSpaceLaunchSite;

    private BuildObservatoryMenu buildObservatoryMenu;

    private BuildResourceStorageMenu buildResourceStorageMenu;

    private BuildResourceGenerationMenu buildMiningStorageMenu;

    private JButton buildButton;

    private final String RESIDENTIAL = "Residential area";
    private final String LAUNCH = "Launch Systems";
    private final String OBSERVATORY = "Observatory";
    private final String RESOURCE_STOCKPILE = "Resource Storage";
    private final String RESOURCE_MINER = "Resource Miner";

    private Civilization c;

    public BuildingMenu(Universe u, Planet p, Civilization c) {
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

        JPanel mainItemContainer = new JPanel();
        buildCardLayout = new CardLayout();
        mainItemContainer.setLayout(buildCardLayout);

        popStoragePanel = new BuildPopulationStorage();

        buildSpaceLaunchSite = new BuildSpaceLaunchSite(c);

        buildObservatoryMenu = new BuildObservatoryMenu();

        buildResourceStorageMenu = new BuildResourceStorageMenu();

        buildMiningStorageMenu = new BuildResourceGenerationMenu();

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

        buildCardLayout.show(mainItemContainer, RESIDENTIAL);

        //Do whatever, add action listeners
        buildingType = new JComboBox<String>(buildingModel);
        buildingType.addActionListener(a -> {
            if (buildingType.getSelectedIndex() > -1) {
                switch ((String) buildingType.getSelectedItem()) {
                    case RESIDENTIAL:
                        buildCardLayout.show(mainItemContainer, RESIDENTIAL);
                        break;
                    case LAUNCH:
                        buildCardLayout.show(mainItemContainer, LAUNCH);
                        break;
                    case OBSERVATORY:
                        buildCardLayout.show(mainItemContainer, OBSERVATORY);
                        break;
                    case RESOURCE_STOCKPILE:
                        buildCardLayout.show(mainItemContainer, RESOURCE_STOCKPILE);
                        break;
                    case RESOURCE_MINER:
                        buildCardLayout.show(mainItemContainer, RESOURCE_MINER);
                        break;
                }
            }
        });

        buildButton = new JButton("Build!");
        buildButton.addActionListener(a -> {
            String item = (String) buildingType.getSelectedItem();
            boolean toReset = false;
            if (item.equals(RESIDENTIAL)) {
                ConquerSpace.game.buildings.PopulationStorage storage = new ConquerSpace.game.buildings.PopulationStorage();
                Actions.buildBuilding(p, new ConquerSpace.game.universe.Point((int) xposSpinner.getValue(), (int) yPosSpinner.getValue()), storage, 0, 1);
                //Reset...
                toReset = true;
            } else if (item.equals(LAUNCH)) {
                //Get civ launching type...
                //SpacePortBuilding port = new SpacePortBuilding(0, (Integer)buildSpaceLaunchSite.maxPopulation.getValue(), (LaunchSystem) buildSpaceLaunchSite.launchTypesValue.getSelectedItem(), p);
                //Actions.buildBuilding(p, new ConquerSpace.game.universe.Point((int)xposSpinner.getValue(), (int)yPosSpinner.getValue()), port, 0, 1);
                SpacePort port = new SpacePort((LaunchSystem) buildSpaceLaunchSite.launchTypesValue.getSelectedItem(), (Integer) buildSpaceLaunchSite.maxPopulation.getValue());
                Actions.buildBuilding(p, new ConquerSpace.game.universe.Point((int) xposSpinner.getValue(), (int) yPosSpinner.getValue()), port, 0, 1);
                toReset = true;
            } else if (item.equals(OBSERVATORY)) {
                StarSystem sys = u.getStarSystem(p.getParentStarSystem());
                Observatory observatory = new Observatory(
                        GameUpdater.Calculators.Optics.getRange(1, (int) buildObservatoryMenu.lensSizeSpinner.getValue()),
                        (Integer) buildObservatoryMenu.lensSizeSpinner.getValue(),
                        c.getID(), new ConquerSpace.game.universe.Point(sys.getX(), sys.getY()));
                //Add visionpoint to civ
                c.visionPoints.add(observatory);
                Actions.buildBuilding(p, new ConquerSpace.game.universe.Point((int) xposSpinner.getValue(), (int) yPosSpinner.getValue()), observatory, 0, 1);
                toReset = true;
            } else if (item.equals(RESOURCE_STOCKPILE)) {
                ResourceStorage stor = new ResourceStorage(p);
                //Add the stuff...
                Iterator<Resource> res = buildResourceStorageMenu.resourceInsertedListModel.elements().asIterator();
                while (res.hasNext()) {
                    Resource next = res.next();
                    stor.addResourceTypeStore(next);
                }
                c.resourceStorages.add(stor);
                Actions.buildBuilding(p, new ConquerSpace.game.universe.Point((int) xposSpinner.getValue(), (int) yPosSpinner.getValue()), stor, 0, 1);
                toReset = true;
            } else if (item.equals(RESOURCE_MINER)) {
                ResourceGatherer miner = new ResourceGatherer(null, (double) buildMiningStorageMenu.miningSpeedSpinner.getValue());
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

                Actions.buildBuilding(p, new ConquerSpace.game.universe.Point((int) xposSpinner.getValue(), (int) yPosSpinner.getValue()), miner, 0, 1);
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
        });

        buildingInfoPanel.setLayout(new BorderLayout());
        buildingInfoPanel.add(buildingType, BorderLayout.NORTH);
        buildingInfoPanel.add(mainItemContainer, BorderLayout.CENTER);
        buildingInfoPanel.add(buildButton, BorderLayout.SOUTH);

        buildPanelXYPosContainer = new JPanel();
        buildPanelXYPosContainer.setLayout(new GridLayout(2, 2));

        xPosLabel = new JLabel("X");
        SpinnerNumberModel xSpinnerMod = new SpinnerNumberModel(0, 0, p.getPlanetSize() * 2, -1);
        xposSpinner = new JSpinner(xSpinnerMod);

        buildPanelXYPosContainer.add(xPosLabel);
        buildPanelXYPosContainer.add(xposSpinner);

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

        buildPanelXYPosContainer.add(yPosLabel);
        buildPanelXYPosContainer.add(yPosSpinner);

        buildingThingPanel.add(buildingInfoPanel);
        buildingThingPanel.add(buildPanelXYPosContainer);

        JScrollPane sectorsScrollPane = new JScrollPane(wrapper);
        planetSectors.add(sectorsScrollPane);
        add(planetSectors, BorderLayout.NORTH);
        add(buildingThingPanel, BorderLayout.CENTER);
        //Add empty panel
        //add(new JPanel());
    }

    public void update() {
        if (!buildingType.isPopupVisible()) {
            //Then update
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

            buildingType.setSelectedIndex(selected);
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
        private Image img;
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
            img = renderer.getImage(2d);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
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
                for (ResourceVein v : p.resourceVeins) {
                    //Draw...
                    if (resourceToShow == SHOW_ALL || resourceToShow == v.getResourceType().getId()) {
                        Ellipse2D.Float circe = new Ellipse2D.Float(v.getX() * size, v.getY() * size, v.getRadius() * size, v.getRadius() * size);
                        g2d.setColor(v.getResourceType().getColor());
                        g2d.fill(circe);
                    }
                }
            }
            if (whatToShow == PLANET_BUILDINGS || whatToShow == SHOW_ALL_RESOURCES) {
                //Draw buildings
                for (Map.Entry<ConquerSpace.game.universe.Point, Building> en : p.buildings.entrySet()) {
                    ConquerSpace.game.universe.Point p = en.getKey();
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

    private class BuildPopulationStorage extends JPanel implements ActionListener {

        long maxPopulation;
        private JLabel amount;
        JSpinner maxPopulationTextField;

        public BuildPopulationStorage() {
            GridBagLayout gridbag = new GridBagLayout();

            setLayout(gridbag);
            GridBagConstraints constraints = new GridBagConstraints();
            amount = new JLabel("Max Population");

            SpinnerNumberModel model = new SpinnerNumberModel(10000l, 0l, Long.MAX_VALUE, 1000);
            maxPopulationTextField = new JSpinner(model);
            ((JSpinner.DefaultEditor) maxPopulationTextField.getEditor()).getTextField().setEditable(false);

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            add(amount, constraints);

            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            add(maxPopulationTextField, constraints);
        }

        //Determine price
        //Add this 
        @Override
        public void actionPerformed(ActionEvent e) {
            //Calculate the cost...
            try {
                maxPopulation = (Long) maxPopulationTextField.getValue();

            } catch (NumberFormatException | ArithmeticException nfe) {
                //Because who cares!

            }
        }
    }

    private class BuildSpaceLaunchSite extends JPanel implements ActionListener {

        private JLabel amount;

        private JSpinner maxPopulation;
        private JLabel launchTypes;
        private JComboBox<LaunchSystem> launchTypesValue;

        public BuildSpaceLaunchSite(Civilization c) {
            setLayout(new GridLayout(2, 2));
            amount = new JLabel("Amount of launch ports");

            launchTypes = new JLabel("Launch types");

            SpinnerNumberModel model = new SpinnerNumberModel(3, 0, 5000, 1);

            maxPopulation = new JSpinner(model);
            ((JSpinner.DefaultEditor) maxPopulation.getEditor()).getTextField().setEditable(false);

            launchTypesValue = new JComboBox<LaunchSystem>();

            for (LaunchSystem t : c.launchSystems) {
                launchTypesValue.addItem(t);
            }
            add(amount);
            add(maxPopulation);
            add(launchTypes);
            add(launchTypesValue);
        }

        //Determine price
        //Add this 
        @Override
        public void actionPerformed(ActionEvent e) {
            //Calculate the cost...
            long pop = 0;
            try {
                pop = (Long) maxPopulation.getValue();

            } catch (NumberFormatException | ArithmeticException nfe) {
                //Because who cares!
            }
        }
    }

    private class BuildObservatoryMenu extends JPanel {

        private JLabel lensSizeLabel;
        private JSpinner lensSizeSpinner;
        private JLabel telescopeRangeLabel;
        private JLabel telescopeRangeValueLabel;

        public BuildObservatoryMenu() {
            GridBagLayout layout = new GridBagLayout();
            setLayout(layout);
            GridBagConstraints constraints = new GridBagConstraints();

            lensSizeLabel = new JLabel("Lens Size(cm)");

            SpinnerNumberModel model = new SpinnerNumberModel(50, 0, 5000, 1);

            lensSizeSpinner = new JSpinner(model);
            ((JSpinner.DefaultEditor) lensSizeSpinner.getEditor()).getTextField().setEditable(false);

            lensSizeSpinner.addChangeListener(a -> {
                //Calculate
                telescopeRangeValueLabel.setText(GameUpdater.Calculators.Optics.getRange(1, (int) lensSizeSpinner.getValue()) + " light years");

            });

            telescopeRangeLabel = new JLabel("Range: ");
            telescopeRangeValueLabel = new JLabel("0 light years");

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.ipady = 5;
            add(lensSizeLabel, constraints);

            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.ipady = 5;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            add(lensSizeSpinner, constraints);

            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            add(telescopeRangeLabel, constraints);

            constraints.gridx = 1;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            add(telescopeRangeValueLabel, constraints);
        }
    }

    public class BuildResourceStorageMenu extends JPanel {

        private JLabel resourceSize;
        private JSpinner resourceSizeSpinner;
        private JButton insertResourceButton;
        private JButton removeResourceButton;
        private JList<Resource> resourceToPut;
        private JList<Resource> resourceInserted;
        private DefaultListModel<Resource> resourceToPutListModel;
        private DefaultListModel<Resource> resourceInsertedListModel;

        public BuildResourceStorageMenu() {
            setLayout(new BorderLayout());
            JPanel ResourceAmountStorage = new JPanel();
            resourceSize = new JLabel("Amount of Resources:");
            SpinnerNumberModel mod = new SpinnerNumberModel(1000, 0, Integer.MAX_VALUE, 100);
            resourceSizeSpinner = new JSpinner(mod);

            ResourceAmountStorage.add(resourceSize);
            ResourceAmountStorage.add(resourceSizeSpinner);

            JPanel resourcePanel = new JPanel();
            resourcePanel.setLayout(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();

            removeResourceButton = new JButton("Remove Resource");
            removeResourceButton.addActionListener(a -> {
                if (resourceToPut.getSelectedIndex() > -1) {
                    resourceInsertedListModel.addElement(resourceToPut.getSelectedValue());
                    resourceToPutListModel.remove(resourceToPut.getSelectedIndex());
                }
            });

            resourceToPutListModel = new DefaultListModel<>();
            resourceToPut = new JList(resourceToPutListModel);

            JScrollPane resourceToPutScrollPane = new JScrollPane(resourceToPut);

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            resourcePanel.add(removeResourceButton, constraints);

            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            resourcePanel.add(resourceToPutScrollPane, constraints);

            insertResourceButton = new JButton("Add resource");

            insertResourceButton.addActionListener(a -> {
                if (resourceInserted.getSelectedIndex() > -1) {
                    resourceToPutListModel.addElement(resourceInserted.getSelectedValue());
                    resourceInsertedListModel.remove(resourceInserted.getSelectedIndex());
                }
            });
            resourceInsertedListModel = new DefaultListModel<>();
            for (Resource res : GameController.resources) {
                resourceInsertedListModel.addElement(res);
            }
            resourceInserted = new JList<>(resourceInsertedListModel);
            JScrollPane resourceInsertedScrollPane = new JScrollPane(resourceInserted);

            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            resourcePanel.add(insertResourceButton, constraints);

            constraints.gridx = 1;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            resourcePanel.add(resourceInsertedScrollPane, constraints);

            add(ResourceAmountStorage, BorderLayout.NORTH);
            add(resourcePanel, BorderLayout.SOUTH);
        }
    }

    private class BuildResourceGenerationMenu extends JPanel {

        private JComboBox<Resource> resourceToMine;
        private JLabel resourceToMineLabel;
        private JLabel miningSpeed;
        private JSpinner miningSpeedSpinner;

        public BuildResourceGenerationMenu() {
            resourceToMineLabel = new JLabel("Mining resource: ");

            DefaultComboBoxModel<Resource> resourceComboBoxModel = new DefaultComboBoxModel<>();
            //Add the resources
            for (Resource res : GameController.resources) {
                resourceComboBoxModel.addElement(res);
            }
            resourceToMine = new JComboBox<>(resourceComboBoxModel);

            miningSpeed = new JLabel("Mining speed, units per month");
            SpinnerNumberModel miningSpeedSpinnerNumberModel = new SpinnerNumberModel(10f, 0f, 50000f, 0.5f);
            miningSpeedSpinner = new JSpinner(miningSpeedSpinnerNumberModel);

            setLayout(new GridBagLayout());

            GridBagConstraints constraints = new GridBagConstraints();

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            add(resourceToMineLabel, constraints);

            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            add(resourceToMine, constraints);

            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            add(miningSpeed, constraints);

            constraints.gridx = 1;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            add(miningSpeedSpinner, constraints);
        }
    }
}
