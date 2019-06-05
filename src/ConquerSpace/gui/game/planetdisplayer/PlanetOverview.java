package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.game.GameController;
import ConquerSpace.game.GameUpdater;
import ConquerSpace.game.actions.Actions;
import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.Observatory;
import ConquerSpace.game.buildings.SpacePort;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.resources.ResourceVein;
import ConquerSpace.game.universe.ships.launch.LaunchSystem;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.game.universe.spaceObjects.terrain.Terrain;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.AlphaComposite;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;

/**
 * Display sectors and stuff.
 *
 * @author Zyun
 */
public class PlanetOverview extends JPanel {

    private static final int TILE_SIZE = 7;
    private JPanel planetOverview;
    private JPanel planetSectors;
    //private JList<Orbitable> satelliteList;
    private JLabel planetName;
    private JLabel planetPath;
    private JLabel planetType;
    private JLabel ownerLabel;
    private JLabel orbitDistance;
    private Planet p;
    private JButton switchButton;
    private ButtonGroup resourceButtonGroup;
    private JRadioButton[] showResources;

    private JPanel buildingThingPanel;

    private JPanel buildingInfoPanel;
    private JComboBox<String> buildingType;
    private JPanel buildPanelXYPosContainer;
    private JLabel xPosLabel;
    private JLabel yPosLabel;
    private JSpinner xposSpinner;
    private JSpinner yPosSpinner;

    private CardLayout buildCardLayout;

    private BuildPopulationStorage popStoragePanel;

    private BuildSpaceLaunchSite buildSpaceLaunchSite;

    private BuildObservatoryMenu buildObservatoryMenu;

    private JButton buildButton;

    public PlanetOverview(Universe u, Planet p, Civilization c) {
        this.p = p;
        setLayout(new GridLayout(2, 1));

        planetOverview = new JPanel();
        planetOverview.setLayout(new VerticalFlowLayout(5, 3));
        planetOverview.setBorder(new TitledBorder("Planet Info"));
        //If name is nothing, then call it unnamed planet
        planetName = new JLabel();
        planetPath = new JLabel();
        planetType = new JLabel("Planet type: " + p.getPlanetType());
        ownerLabel = new JLabel();
        orbitDistance = new JLabel("Distance: " + p.getOrbitalDistance() + " km");

        //Init planetname
        if (p.getName().equals("")) {
            planetName.setText("Unnamed Planet");
        } else {
            planetName.setText(p.getName());
        }

        //Init planetPath
        StringBuilder name = new StringBuilder();
        name.append("Star System ");
        name.append("" + p.getParentStarSystem());
        name.append(" Planet id " + p.getId());
        planetPath.setText(name.toString());

        //Init owner
        if (p.getOwnerID() > -1) {
            ownerLabel.setText("Owner: " + c.getName());
        } else {
            ownerLabel.setText("No owner");
        }

        planetSectors = new JPanel();
        PlanetSectorDisplayer sectorDisplayer = new PlanetSectorDisplayer(p, c);
        JPanel wrapper = new JPanel();
        wrapper.add(sectorDisplayer);
        JTabbedPane buildingPanel = new JTabbedPane();

        JPanel buttonsWrapper = new JPanel();
        switchButton = new JButton("Change view");
        switchButton.addActionListener(a -> {
            if (sectorDisplayer.whatToShow == 0) {
                sectorDisplayer.setWhatToShow(1);
            } else {
                sectorDisplayer.setWhatToShow(0);
            }
        });
        JPanel buttonGroupWrapper = new JPanel();
        buttonGroupWrapper.setLayout(new VerticalFlowLayout());
        resourceButtonGroup = new ButtonGroup();
        showResources = new JRadioButton[GameController.resources.size() + 1];
        showResources[0] = new JRadioButton("All Resources");
        showResources[0].addActionListener(a -> {
            sectorDisplayer.resourceToShow = -1;
        });
        buttonGroupWrapper.add(showResources[0]);
        resourceButtonGroup.add(showResources[0]);
        for (int i = 1; i < GameController.resources.size() + 1; i++) {
            showResources[i] = new JRadioButton(GameController.resources.get(i - 1).getName());
            int val = i - 1;
            showResources[i].addActionListener(a -> {
                sectorDisplayer.setResourceViewing(val);
            });
            resourceButtonGroup.add(showResources[i]);
            buttonGroupWrapper.add(showResources[i]);
        }

        buttonsWrapper.add(switchButton);
        buttonsWrapper.add(buttonGroupWrapper);
        buildingPanel.add("Map", buttonsWrapper);

        buildingPanel.addChangeListener(a -> {
            if (buildingPanel.getSelectedIndex() == 1) {
                sectorDisplayer.whatToShow = PlanetSectorDisplayer.SHOW_ALL_RESOURCES;
            } else if (buildingPanel.getSelectedIndex() == 0) {
                sectorDisplayer.whatToShow = PlanetSectorDisplayer.PLANET_BUILDINGS;
            }
        });
        buildingThingPanel = new JPanel();
        buildingThingPanel.setLayout(new GridLayout(2, 1));
        //Format build tab
        buildingInfoPanel = new JPanel();
        DefaultComboBoxModel<String> buildingModel = new DefaultComboBoxModel<>();
        buildingModel.addElement("Residential area");
        if (c.values.containsKey("haslaunch") && c.values.get("haslaunch") == 1) {
            //Do things
            buildingModel.addElement("Launch Systems");
        }
        buildingModel.addElement("Observatory");

        JPanel mainItemContainer = new JPanel();
        buildCardLayout = new CardLayout();
        mainItemContainer.setLayout(buildCardLayout);

        popStoragePanel = new BuildPopulationStorage();

        buildSpaceLaunchSite = new BuildSpaceLaunchSite(c);

        buildObservatoryMenu = new BuildObservatoryMenu();

        mainItemContainer.add(popStoragePanel, "Residential area");
        mainItemContainer.add(buildSpaceLaunchSite, "Launch Systems");
        mainItemContainer.add(buildObservatoryMenu, "Observatory");

        buildCardLayout.show(mainItemContainer, "Residential area");

        //Do whatever, add action listeners
        buildingType = new JComboBox<String>(buildingModel);
        buildingType.addActionListener(a -> {
            switch ((String) buildingType.getSelectedItem()) {
                case "Residential area":
                    buildCardLayout.show(mainItemContainer, "Residential area");
                    break;
                case "Launch Systems":
                    //System.out.println("showing");
                    buildCardLayout.show(mainItemContainer, "Launch Systems");
                    break;
                case "Observatory":
                    buildCardLayout.show(mainItemContainer, "Observatory");
            }
        });

        buildButton = new JButton("Build!");
        buildButton.addActionListener(a -> {
            String item = (String) buildingType.getSelectedItem();
            boolean toReset = false;
            if (item.equals("Residential area")) {
                ConquerSpace.game.buildings.PopulationStorage storage = new ConquerSpace.game.buildings.PopulationStorage();
                Actions.buildBuilding(p, new ConquerSpace.game.universe.Point((int) xposSpinner.getValue(), (int) yPosSpinner.getValue()), storage, 0, 1);
                //Reset...
                toReset = true;
            } else if (item.equals("Launch Systems")) {
                //Get civ launching type...
                //SpacePortBuilding port = new SpacePortBuilding(0, (Integer)buildSpaceLaunchSite.maxPopulation.getValue(), (LaunchSystem) buildSpaceLaunchSite.launchTypesValue.getSelectedItem(), p);
                //Actions.buildBuilding(p, new ConquerSpace.game.universe.Point((int)xposSpinner.getValue(), (int)yPosSpinner.getValue()), port, 0, 1);
                SpacePort port = new SpacePort((LaunchSystem) buildSpaceLaunchSite.launchTypesValue.getSelectedItem(), (Integer) buildSpaceLaunchSite.maxPopulation.getValue());
                Actions.buildBuilding(p, new ConquerSpace.game.universe.Point((int) xposSpinner.getValue(), (int) yPosSpinner.getValue()), port, 0, 1);
                toReset = true;
            } else if (item.equals("Observatory")) {
                StarSystem sys = u.getStarSystem(p.getParentStarSystem());
                Observatory observatory = new Observatory(
                        GameUpdater.Calculators.Optics.getRange(1, (int) buildObservatoryMenu.lensSizeSpinner.getValue()),
                        (Integer) buildObservatoryMenu.lensSizeSpinner.getValue(),
                        c.getID(), new ConquerSpace.game.universe.Point(sys.getX(), sys.getY()));
                //Add visionpoint to civ
                c.visionPoints.add(observatory);
                Actions.buildBuilding(p, new ConquerSpace.game.universe.Point((int) xposSpinner.getValue(), (int) yPosSpinner.getValue()), observatory, 0, 1);
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

        buildingInfoPanel.setLayout(new VerticalFlowLayout());
        buildingInfoPanel.add(buildingType);
        buildingInfoPanel.add(mainItemContainer);
        buildingInfoPanel.add(buildButton);

        buildingThingPanel.add(buildingInfoPanel);
        buildingPanel.add("Build", buildingThingPanel);

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

        buildingThingPanel.add(buildPanelXYPosContainer);

        wrapper.add(buildingPanel);

        JScrollPane sectorsScrollPane = new JScrollPane(wrapper);
        planetSectors.add(sectorsScrollPane);

        //Add components
        planetOverview.add(planetName);
        planetOverview.add(planetPath);
        planetOverview.add(planetType);
        planetOverview.add(ownerLabel);
        planetOverview.add(orbitDistance);

        add(planetOverview);
        add(planetSectors);
        //Add empty panel
        //add(new JPanel());
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

        public PlanetSectorDisplayer(Planet p, Civilization c) {
            this.c = c;
            terrain = p.terrain;
            setPreferredSize(
                    new Dimension(p.terrain.terrainColor.length * 2, p.terrain.terrainColor[0].length * 2));
            menu = new JPopupMenu();
            addMouseListener(this);
            BufferedImage planetDisplaying = new BufferedImage(p.terrain.terrainColor.length, p.terrain.terrainColor[0].length, BufferedImage.TYPE_3BYTE_BGR);

            if (p.terrain != null) {
                for (int x = 0; x < p.terrain.terrainColor.length; x++) {
                    for (int y = 0; y < p.terrain.terrainColor[x].length; y++) {
                        //System.out.println(x + " " + y + ";" + p.terrain.terrainColor[x][y]);
                        planetDisplaying.setRGB(x, y, p.terrain.terrainColor[x][y].color.getRGB());
                    }
                }
                img = planetDisplaying.getScaledInstance(p.terrain.terrainColor.length * 2, p.terrain.terrainColor[0].length * 2, Image.SCALE_DEFAULT);
            }
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
                        Ellipse2D.Float circe = new Ellipse2D.Float(v.getX() * 2, v.getY() * 2, v.getRadius() * 2, v.getRadius() * 2);
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
                    Rectangle2D.Float rect = new Rectangle2D.Float(p.getX() * 2, p.getY() * 2, 2, 2);
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
                Rectangle2D.Float bgRect = new Rectangle2D.Float((float) point.getX() * 2 - 2, (float) point.getY() * 2 - 2, 6, 6);
                g2d.setColor(invc);
                g2d.fill(bgRect);
                Rectangle2D.Float rect = new Rectangle2D.Float((float) point.getX() * 2, (float) point.getY() * 2, 2, 2);
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

    //Various menus for building stats
    private class BuildPopulationStorage extends JPanel implements ActionListener {

        long maxPopulation;
        private JLabel amount;
        JSpinner maxPopulationTextField;

        public BuildPopulationStorage() {
            setLayout(new GridLayout(1, 2));
            amount = new JLabel("Max Population");

            SpinnerNumberModel model = new SpinnerNumberModel(10000l, 0l, Long.MAX_VALUE, 1000);
            maxPopulationTextField = new JSpinner(model);
            ((JSpinner.DefaultEditor) maxPopulationTextField.getEditor()).getTextField().setEditable(false);

            add(amount);
            add(maxPopulationTextField);
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
            setLayout(new GridLayout(2, 2));
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
            add(lensSizeLabel);
            add(lensSizeSpinner);
            add(telescopeRangeLabel);
            add(telescopeRangeValueLabel);
        }
    }
}
