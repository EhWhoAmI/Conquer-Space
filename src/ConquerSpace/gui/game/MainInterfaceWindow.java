package ConquerSpace.gui.game;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.Universe;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.Timer;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author EhwhoAmI
 */
public class MainInterfaceWindow extends JInternalFrame {

    //Hold all the panels
    private JTabbedPane tabs;

    private ResearchViewer researchViewer;

    private JPanel planetInfoSheetContainer;
    private PlanetInfoSheet planetInfoSheet;
    private ShrinkedPlanetSheet shrinkedPlanetSheet;
    private SpaceShipOverview spaceShipOverview;
    private HullCreator hullCreator;
    
    private CivInfoOverview civInfoOverview;

    private PersonWindow personWindow;
    private RecruitingPerson recruitingPerson;

    private Civilization c;
    private Universe u;

    private JTree universeBreakdown;
    private DefaultMutableTreeNode universeBreakdownTreeModel;

    private Planet selectedPlanet = null;

    private boolean toShowResources = true;

    public MainInterfaceWindow(Civilization c, Universe u) {
        this.c = c;
        this.u = u;
        init();
        update();
        expandAllNodes(universeBreakdown, 0, universeBreakdown.getRowCount());
        
        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        //setVisible(true);
        pack();
        Dimension d = getToolkit().getScreenSize();
        setSize(d.width - 100, d.height - 100);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
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
                    setSelectedPlanet(p, p.scanned.contains(c.getID()));
                    setSelectedTab(1);
                }
                //process
            }
        });

        researchViewer = new ResearchViewer(c);

        planetInfoSheetContainer = new JPanel();
        planetInfoSheetContainer.setLayout(new BorderLayout());

        spaceShipOverview = new SpaceShipOverview(c, u);

        JPanel shipComponentsOverview = new JPanel(new BorderLayout());

        JTabbedPane shipsComponentsOverviewPanel = new JTabbedPane();
        ShipComponentDesigner shipComponentDesigner = new ShipComponentDesigner(c);
        shipsComponentsOverviewPanel.add("Ship Components", shipComponentDesigner);

        hullCreator = new HullCreator(c);
        shipsComponentsOverviewPanel.add("Hulls", hullCreator);

        shipComponentsOverview.add(shipsComponentsOverviewPanel, BorderLayout.CENTER);

        JTabbedPane peopleTabs = new JTabbedPane();
        personWindow = new PersonWindow(c);
        peopleTabs.add("Person List", personWindow);

        recruitingPerson = new RecruitingPerson(c);
        peopleTabs.add("Recruitment", recruitingPerson);
        
        civInfoOverview = new CivInfoOverview(c, u);

        tabs.add("Research and Science", researchViewer);
        tabs.add("Planet", planetInfoSheetContainer);
        tabs.add("Ships", spaceShipOverview);
        tabs.add("Engineering", shipComponentsOverview);
        tabs.add("People", peopleTabs);
        tabs.add("My Civilization", civInfoOverview);

        add(universeBreakdown, BorderLayout.WEST);
        add(tabs, BorderLayout.CENTER);

        // Updating code
        Timer t = new Timer(500, a -> {
            update();
        });
        t.setRepeats(true);
        t.start();
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

        spaceShipOverview.update();
        recruitingPerson.update();
        personWindow.update();
        researchViewer.update();
        
        if(planetInfoSheet != null) {
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
            planetInfoSheet = new PlanetInfoSheet(u, selectedPlanet, c);
            planetInfoSheetContainer.add(planetInfoSheet, BorderLayout.CENTER);
        } else {
            shrinkedPlanetSheet = new ShrinkedPlanetSheet(u, p, c);
            planetInfoSheetContainer.add(shrinkedPlanetSheet, BorderLayout.CENTER);
        }
        setSelectedTab(1);
        setVisible(true);
    }
}
