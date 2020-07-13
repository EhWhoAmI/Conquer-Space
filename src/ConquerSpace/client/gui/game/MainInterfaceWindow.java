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
package ConquerSpace.client.gui.game;

import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.common.GameState;
import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.common.game.events.Event;
import ConquerSpace.common.game.universe.bodies.Planet;
import ConquerSpace.common.game.universe.bodies.Universe;
import ConquerSpace.client.gui.game.engineering.BuildSpaceShipAutomationMenu;
import ConquerSpace.client.gui.game.engineering.LaunchSystemDesigner;
import ConquerSpace.client.gui.game.engineering.SatelliteDesigner;
import ConquerSpace.client.gui.game.engineering.ShipComponentDesigner;
import ConquerSpace.client.gui.game.planetdisplayer.PlanetInfoSheet;
import ConquerSpace.common.util.ResourceLoader;
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

    private OrganizationsOrganizer organizationsOrganizer;

    private ResourceManager resourceManager;

    private Civilization civilization;
    private Universe universe;
    private GameState gameState;

    private JTree universeBreakdown;
    private DefaultMutableTreeNode universeBreakdownTreeModel;

    private Planet selectedPlanet = null;

    private boolean toShowResources = true;

    private final int planetinfotab = 1;

    public MainInterfaceWindow(GameState gameState, Civilization c) {
        this.civilization = c;
        this.universe = gameState.universe;
        this.gameState = gameState;
        
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
        setTitle(LOCALE_MESSAGES.getMessage("game.mainwindow.title"));
    }

    public void init() {
        setLayout(new BorderLayout());
        tabs = new JTabbedPane();

        universeBreakdownTreeModel = new DefaultMutableTreeNode(LOCALE_MESSAGES.getMessage("game.breakdown.ownedstarsystems"));
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
                    setSelectedPlanet(p, p.hasScanned(civilization.getId()));
                }
                //process
            }
        });

        researchViewer = new ResearchViewer(gameState, civilization);

        planetInfoSheetContainer = new JPanel();
        planetInfoSheetContainer.setLayout(new BorderLayout());

        //Space ship tabs
        spaceShipOverview = new SpaceShipOverview(civilization, universe);

        //Engineering tabs
        JPanel shipComponentsOverview = new JPanel(new BorderLayout());

        shipsComponentsOverviewPanel = new JTabbedPane();

        buildSpaceShipAutomationMenu = new BuildSpaceShipAutomationMenu(gameState, civilization);
        shipsComponentsOverviewPanel.add(LOCALE_MESSAGES.getMessage("game.mainwindow.engineering.tabs.shipdesign"), buildSpaceShipAutomationMenu);

        satelliteDesigner = new SatelliteDesigner(civilization);
        shipsComponentsOverviewPanel.add(LOCALE_MESSAGES.getMessage("game.mainwindow.engineering.tabs.satellite"), satelliteDesigner);
        ImageIcon map = ResourceLoader.getIcon("satellite.icon");

        shipsComponentsOverviewPanel.setIconAt(1, map);
        launchSystemDesigner = new LaunchSystemDesigner(civilization);
        JPanel launchWrapper = new JPanel();
        launchWrapper.setLayout(new VerticalFlowLayout());
        launchWrapper.add(launchSystemDesigner);
        shipsComponentsOverviewPanel.add(LOCALE_MESSAGES.getMessage("game.mainwindow.engineering.tabs.launchsystem"), launchWrapper);

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

        ShipComponentDesigner shipComponentDesigner = new ShipComponentDesigner(civilization);
        shipsComponentsOverviewPanel.add(LOCALE_MESSAGES.getMessage("game.mainwindow.engineering.tabs.shipcomponent"), shipComponentDesigner);

        hullCreator = new HullCreator(gameState, civilization);
        shipsComponentsOverviewPanel.add(LOCALE_MESSAGES.getMessage("game.mainwindow.engineering.tabs.hulls"), hullCreator);

        shipComponentsOverview.add(shipsComponentsOverviewPanel, BorderLayout.CENTER);

        JTabbedPane peopleTabs = new JTabbedPane();
        personWindow = new PersonWindow(civilization, universe);
        peopleTabs.add(LOCALE_MESSAGES.getMessage("game.mainwindow.people.list"), personWindow);

        recruitingPerson = new RecruitingPerson(civilization, universe);
        peopleTabs.add(LOCALE_MESSAGES.getMessage("game.mainwindow.people.recruitment"), recruitingPerson);

        civInfoOverview = new CivInfoOverview(civilization, universe);

        resourceManager = new ResourceManager(civilization);

        economyWindow = new EconomyWindow(civilization, universe);

        eventViewer = new EventViewer();

        organizationsOrganizer = new OrganizationsOrganizer(gameState, civilization);

        tabs.add(LOCALE_MESSAGES.getMessage("game.mainwindow.tabs.research"), researchViewer);
        tabs.add(LOCALE_MESSAGES.getMessage("game.mainwindow.tabs.planet"), planetInfoSheetContainer);
        tabs.add(LOCALE_MESSAGES.getMessage("game.mainwindow.tabs.ships"), spaceShipOverview);
        tabs.add(LOCALE_MESSAGES.getMessage("game.mainwindow.tabs.engineering"), shipComponentsOverview);
        tabs.add(LOCALE_MESSAGES.getMessage("game.mainwindow.tabs.people"), peopleTabs);
        tabs.add(LOCALE_MESSAGES.getMessage("game.mainwindow.tabs.civilization"), civInfoOverview);
        tabs.add(LOCALE_MESSAGES.getMessage("game.mainwindow.tabs.resources"), resourceManager);
        tabs.add(LOCALE_MESSAGES.getMessage("game.mainwindow.tabs.economy"), economyWindow);
        tabs.add(LOCALE_MESSAGES.getMessage("game.mainwindow.tabs.events"), eventViewer);
        tabs.add(LOCALE_MESSAGES.getMessage("game.mainwindow.tabs.orgs"), organizationsOrganizer);

        ImageIcon tab1Icon = ResourceLoader.getIcon("science.icon");
        ImageIcon econ = ResourceLoader.getIcon("economy.icon");
        ImageIcon engineering = ResourceLoader.getIcon("engineering.icon");
        ImageIcon spaceshps = ResourceLoader.getIcon("spaceships.icon");
        ImageIcon planet = ResourceLoader.getIcon("eclipse.icon");
        ImageIcon person = ResourceLoader.getIcon("person.icon");
        ImageIcon civ = ResourceLoader.getIcon("people.icon");
        ImageIcon goods = ResourceLoader.getIcon("goods.icon");
        ImageIcon events = ResourceLoader.getIcon("alert.icon");
        ImageIcon orgs = ResourceLoader.getIcon("org.icon");

        tabs.setIconAt(0, tab1Icon);
        tabs.setIconAt(1, planet);
        tabs.setIconAt(2, spaceshps);
        tabs.setIconAt(3, engineering);
        tabs.setIconAt(4, person);
        tabs.setIconAt(5, civ);
        tabs.setIconAt(6, goods);
        tabs.setIconAt(7, econ);
        tabs.setIconAt(8, events);
        tabs.setIconAt(9, orgs);

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

        for (Planet p : civilization.habitatedPlanets) {
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
            if (p.getOwnerID() == (civilization.getId())) {
                planetInfoSheet = new PlanetInfoSheet(gameState, selectedPlanet, civilization);
                planetInfoSheetContainer.add(planetInfoSheet, BorderLayout.CENTER);
            } else {
                unownedPlanetInfoMenu = new UnownedPlanetInfoMenu(universe, selectedPlanet, civilization);
                planetInfoSheetContainer.add(unownedPlanetInfoMenu, BorderLayout.CENTER);
            }
        } else {
            shrinkedPlanetSheet = new ShrinkedPlanetSheet(universe, p, civilization);
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
        if (civilization.values.containsKey("haslaunch") && civilization.values.get("haslaunch") == 1) {
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
