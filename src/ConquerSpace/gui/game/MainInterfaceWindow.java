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
package ConquerSpace.gui.game;

import ConquerSpace.game.organizations.civilization.Civilization;
import ConquerSpace.game.events.Event;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.gui.game.engineering.BuildSpaceShipAutomationMenu;
import ConquerSpace.gui.game.engineering.LaunchSystemDesigner;
import ConquerSpace.gui.game.engineering.SatelliteDesigner;
import ConquerSpace.gui.game.engineering.ShipComponentDesigner;
import ConquerSpace.gui.game.planetdisplayer.PlanetInfoSheet;
import ConquerSpace.util.ResourceLoader;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author EhwhoAmI
 */
public class MainInterfaceWindow extends JInternalFrame implements MouseListener, InternalFrameListener {

    //Hold all the panels
    private JTabbedPane tabs;

    private ResearchViewer researchViewer;

    private JPanel planetInfoSheetContainer;
    private PlanetInfoSheet planetInfoSheet;
    private ShrinkedPlanetSheet shrinkedPlanetSheet;
    private UnownedPlanetInfoMenu unownedPlanetInfoMenu;
    private SpaceShipOverview spaceShipOverview;
    private HullCreator hullCreator;
    private EconomyWindow economyWindow;

    JTabbedPane shipsComponentsOverviewPanel;
    private BuildSpaceShipAutomationMenu buildSpaceShipAutomationMenu;
    private SatelliteDesigner satelliteDesigner;
    private LaunchSystemDesigner launchSystemDesigner;

    private CivInfoOverview civInfoOverview;

    private PersonWindow personWindow;
    private RecruitingPerson recruitingPerson;

    private EventViewer eventViewer;

    private ResourceManager resourceManager;

    private Civilization c;
    private Universe u;

    private JTree universeBreakdown;
    private DefaultMutableTreeNode universeBreakdownTreeModel;

    private Planet selectedPlanet = null;

    private boolean toShowResources = true;

    private final int planetinfotab = 1;

    public MainInterfaceWindow(Civilization c, Universe u) {
        this.c = c;
        this.u = u;
        init();

        //Set selected planet
        setSelectedPlanet(c.getCapitalPlanet(), true);
        setSelectedTab(0);

        update();
        expandAllNodes(universeBreakdown, 0, universeBreakdown.getRowCount());

        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setVisible(false);

        pack();
        Dimension d = getToolkit().getScreenSize();
        setSize(d.width - 100, d.height - 100);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setTitle("Main Window");
    }

    public void init() {
        setLayout(new BorderLayout());
        tabs = new JTabbedPane();

        universeBreakdownTreeModel = new DefaultMutableTreeNode("Owned Star Systems");
        universeBreakdown = new JTree(universeBreakdownTreeModel);

        universeBreakdown.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent arg0) {
                DefaultMutableTreeNode selectedNode
                        = (DefaultMutableTreeNode) universeBreakdown.getLastSelectedPathComponent();
                if (selectedNode.getChildCount() == 0 && selectedNode.getUserObject() instanceof Planet) {
                    //Do stuff
                    Planet p = (Planet) selectedNode.getUserObject();
                    //Selected planet
                    setSelectedPlanet(p, p.scanned.contains(c.getId()));
                }
                //process
            }
        });

        researchViewer = new ResearchViewer(c);

        planetInfoSheetContainer = new JPanel();
        planetInfoSheetContainer.setLayout(new BorderLayout());

        //Space ship tabs
        spaceShipOverview = new SpaceShipOverview(c, u);

        //Engineering tabs
        JPanel shipComponentsOverview = new JPanel(new BorderLayout());

        shipsComponentsOverviewPanel = new JTabbedPane();

        buildSpaceShipAutomationMenu = new BuildSpaceShipAutomationMenu(c);
        shipsComponentsOverviewPanel.add("Design Ship", buildSpaceShipAutomationMenu);

        satelliteDesigner = new SatelliteDesigner(c);
        shipsComponentsOverviewPanel.add("Design Satellite", satelliteDesigner);
        ImageIcon map = new ImageIcon("assets/img/icons/satellite.png");

        shipsComponentsOverviewPanel.setIconAt(1, map);
        launchSystemDesigner = new LaunchSystemDesigner(c);
        JPanel launchWrapper = new JPanel();
        launchWrapper.setLayout(new VerticalFlowLayout());
        launchWrapper.add(launchSystemDesigner);
        shipsComponentsOverviewPanel.add("Design Launch System", launchWrapper);

        updateComponents();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                updateComponents();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                updateComponents();
            }
        });

        addPropertyChangeListener(l -> {
            updateComponents();
        });

        ShipComponentDesigner shipComponentDesigner = new ShipComponentDesigner(c);
        shipsComponentsOverviewPanel.add("Ship Components", shipComponentDesigner);

        hullCreator = new HullCreator(c);
        shipsComponentsOverviewPanel.add("Hulls", hullCreator);

        shipComponentsOverview.add(shipsComponentsOverviewPanel, BorderLayout.CENTER);

        JTabbedPane peopleTabs = new JTabbedPane();
        personWindow = new PersonWindow(c, u);
        peopleTabs.add("Person List", personWindow);

        recruitingPerson = new RecruitingPerson(c, u);
        peopleTabs.add("Recruitment", recruitingPerson);

        civInfoOverview = new CivInfoOverview(c, u);

        resourceManager = new ResourceManager(c);

        economyWindow = new EconomyWindow(c, u);

        eventViewer = new EventViewer();
        tabs.add("Research and Science", researchViewer);
        tabs.add("Planet", planetInfoSheetContainer);
        tabs.add("Ships", spaceShipOverview);
        tabs.add("Engineering", shipComponentsOverview);
        tabs.add("People", peopleTabs);
        tabs.add("My Civilization", civInfoOverview);
        tabs.add("Resources", resourceManager);
        tabs.add("Economy", economyWindow);
        tabs.add("Events", eventViewer);

        ImageIcon tab1Icon = ResourceLoader.getIcon("science.icon");
        ImageIcon econ = ResourceLoader.getIcon("economy.icon");
        ImageIcon engineering = ResourceLoader.getIcon("engineering.icon");
        ImageIcon spaceshps = ResourceLoader.getIcon("spaceships.icon");
        ImageIcon planet = ResourceLoader.getIcon("eclipse.icon");
        ImageIcon person = ResourceLoader.getIcon("person.icon");
        ImageIcon civ = ResourceLoader.getIcon("people.icon");
        ImageIcon goods = ResourceLoader.getIcon("goods.icon");
        ImageIcon events = ResourceLoader.getIcon("alert.icon");

        tabs.setIconAt(0, tab1Icon);
        tabs.setIconAt(1, planet);
        tabs.setIconAt(2, spaceshps);
        tabs.setIconAt(3, engineering);
        tabs.setIconAt(4, person);
        tabs.setIconAt(5, civ);
        tabs.setIconAt(6, goods);
        tabs.setIconAt(7, econ);
        tabs.setIconAt(8, events);

        add(universeBreakdown, BorderLayout.WEST);
        add(tabs, BorderLayout.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                update();
            }

        });
    }

    public void update() {
        //Add the stuff
        universeBreakdownTreeModel.removeAllChildren();
        //get owned star systems

        for (Planet p : c.habitatedPlanets) {
            DefaultMutableTreeNode system = new DefaultMutableTreeNode(p.getParentStarSystem() + "");
            DefaultMutableTreeNode dm = new DefaultMutableTreeNode(p);
            system.add(dm);
            universeBreakdownTreeModel.add(system);
        }

        if (spaceShipOverview.isVisible()) {
            spaceShipOverview.update();
        }
        if (recruitingPerson.isVisible()) {
            recruitingPerson.update();
        }
        if (personWindow.isVisible()) {
            personWindow.update();
        }
        if (researchViewer.isVisible()) {
            researchViewer.update();
        }
        if (resourceManager.isVisible()) {
            resourceManager.update();
        }
        if (economyWindow.isVisible()) {
            economyWindow.update();
        }

        if (planetInfoSheet != null) {
            planetInfoSheet.update();
        }
    }

    private void expandAllNodes(JTree tree, int startingIndex, int rowCount) {
        for (int i = startingIndex; i < rowCount; ++i) {
            tree.expandRow(i);
        }

        if (tree.getRowCount() != rowCount) {
            expandAllNodes(tree, rowCount, tree.getRowCount());
        }
    }

    public void setSelectedTab(int tab) {
        tabs.setSelectedIndex(tab);
    }

    public void setSelectedPlanet(Planet p, boolean toShowResources) {
        selectedPlanet = p;
        planetInfoSheetContainer.removeAll();
        if (toShowResources) {
            //Check if owned or not
            if (p.getOwnerID() == (c.getId())) {
                planetInfoSheet = new PlanetInfoSheet(u, selectedPlanet, c);
                planetInfoSheetContainer.add(planetInfoSheet, BorderLayout.CENTER);
            } else {
                unownedPlanetInfoMenu = new UnownedPlanetInfoMenu(u, selectedPlanet, c);
                planetInfoSheetContainer.add(unownedPlanetInfoMenu, BorderLayout.CENTER);
            }
        } else {
            shrinkedPlanetSheet = new ShrinkedPlanetSheet(u, p, c);
            planetInfoSheetContainer.add(shrinkedPlanetSheet, BorderLayout.CENTER);
        }
        setSelectedTab(planetinfotab);
        setVisible(true);
    }

    public void passEvent(Event e) {
        //Add thing
        eventViewer.passEvent(e);
    }

    private void updateComponents() {
        shipsComponentsOverviewPanel.setEnabledAt(2, false);
        shipsComponentsOverviewPanel.setToolTipTextAt(2, "You need to research a launch system in the Research Tab!");
        if (c.values.containsKey("haslaunch") && c.values.get("haslaunch") == 1) {
            shipsComponentsOverviewPanel.setEnabledAt(2, true);
            shipsComponentsOverviewPanel.setToolTipText("");
        }
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
        update();
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
        update();
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        update();
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
