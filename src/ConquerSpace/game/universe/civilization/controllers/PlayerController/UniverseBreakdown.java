package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.universe.civilizations.Civilization;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.PlanetTypes;
import ConquerSpace.game.universe.spaceObjects.Sector;
import ConquerSpace.game.universe.spaceObjects.Star;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.StarTypes;
import ConquerSpace.game.universe.spaceObjects.Universe;
import java.awt.Dimension;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Breakdown of the universe.
 * @author Zyun
 */
public class UniverseBreakdown extends JInternalFrame {

    private static UniverseBreakdown instance;

    private Universe universe;
    //Hide constructor
    private UniverseBreakdown(Universe u) {
        this.universe = u;
        
        JPanel pan = new JPanel();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Universe");
        DefaultMutableTreeNode sectorNodes = new DefaultMutableTreeNode("Sectors");
        //Parse all the sectors in the universe
        for (int i = 0; i < universe.getSectorCount(); i++) {
            Sector s = universe.getSector(i);
            //Parse star systems
            DefaultMutableTreeNode sectorNode = new DefaultMutableTreeNode("Sector " + s.getID());

            //Planets
            for (int b = 0; b < s.getStarSystemCount(); b++) {
                StarSystem sys = s.getStarSystem(b);
                DefaultMutableTreeNode starSystem = new DefaultMutableTreeNode("Star System" + sys.getId());
                DefaultMutableTreeNode starsNode = new DefaultMutableTreeNode("Stars");

                //Stars
                for (int k = 0; k < sys.getStarCount(); k++) {
                    Star star = sys.getStar(k);
                    String startype = "";
                    switch(star.type) {
                        case StarTypes.BLUE:
                            startype = "Blue";
                            break;
                        case StarTypes.BROWN:
                            startype = "Brown";
                            break;
                        case StarTypes.RED:
                            startype = "Red";
                            break;
                        case StarTypes.YELLOW:
                            startype = "Yellow";
                            break;
                    }
                    DefaultMutableTreeNode starNode = new DefaultMutableTreeNode(startype + " Star");
                    starsNode.add(starNode);

                }
                starSystem.add(starsNode);
                //Planets
                DefaultMutableTreeNode planetsNode = new DefaultMutableTreeNode("Planets");
                for (int k = 0; k < sys.getPlanetCount(); k++) {
                    Planet p = sys.getPlanet(k);
                    String name;
                    if (p.getName() == "") {
                        String planetType = "";
                        switch(p.getPlanetType()) {
                            case PlanetTypes.GAS:
                                planetType = "Gas";
                                break;
                            case PlanetTypes.ROCK:
                                planetType = "Rock";
                        }
                        name = "Unnamed " + planetType + " Planet";
                    } else {
                        name = p.getName();
                    }
                    DefaultMutableTreeNode planetNode = new DefaultMutableTreeNode(name);
                    planetsNode.add(planetNode);
                }
                if (sys.getPlanetCount() == 0) {
                    DefaultMutableTreeNode pNode = new DefaultMutableTreeNode("None");

                    planetsNode.add(pNode);
                }
                starSystem.add(planetsNode);
                sectorNode.add(starSystem);
            }
            sectorNodes.add(sectorNode);

        }
        root.add(sectorNodes);

        //Civs
        DefaultMutableTreeNode civNodes = new DefaultMutableTreeNode("Civilizations");
        for (int i = 0; i < universe.getCivilizationCount(); i++) {
            Civilization civ = universe.getCivilization(i);
            DefaultMutableTreeNode civNode = new DefaultMutableTreeNode(civ.getName());
            civNodes.add(civNode);
        }
        root.add(civNodes);
        JTree tree = new JTree(root);
        tree.addTreeSelectionListener((e) -> {

        });
        tree.setPreferredSize(new Dimension(480, 500));
        JScrollPane pane = new JScrollPane(tree);
        pane.setSize(500, 500);
        pan.add(pane);
        //pan.setPreferredSize(new Dimension(500, 500));
        add(pan);
        pack();
        setVisible(true);
    }

    /**
     * Get one instance of the UniverseBreakdown class.
     *
     * @param u Universe
     * @return Instance of universe breakdown class.
     */
    public static UniverseBreakdown getInstance(Universe u) {
        if (instance == null) {
            instance = new UniverseBreakdown(u);
        }
        instance.setVisible(true);
        return instance;
    }
}
