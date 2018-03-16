package ConquerSpace.game.ui;

import ConquerSpace.Globals;
import ConquerSpace.game.universe.civilizations.Civilization;
import ConquerSpace.game.universe.spaceObjects.Sector;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Breakdown of the universe.
 * @author Zyun
 */
public class UniverseBreakdown extends JFrame {

    private static UniverseBreakdown instance;

    //Hide constructor
    private UniverseBreakdown() {
        JPanel pan = new JPanel();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Universe");
        DefaultMutableTreeNode sectorNodes = new DefaultMutableTreeNode("Sectors");
        //Parse all the sectors in the universe
        for (int i = 0; i < Globals.universe.getSectorCount(); i++) {
            Sector s = Globals.universe.getSector(i);
            //Parse star systems
            DefaultMutableTreeNode sectorNode = new DefaultMutableTreeNode("Sector " + s.getID());

            //Planets
            for (int b = 0; b < s.getStarSystemCount(); b++) {
                StarSystem sys = s.getStarSystem(b);
                DefaultMutableTreeNode starSystem = new DefaultMutableTreeNode("Star System" + sys.getId());
                DefaultMutableTreeNode starsNode = new DefaultMutableTreeNode("Stars");
                DefaultMutableTreeNode starNode = new DefaultMutableTreeNode("Star");
                starsNode.add(starNode);
                starSystem.add(starsNode);
                //Planets
                DefaultMutableTreeNode planetsNode = new DefaultMutableTreeNode("Planets");
                for (int k = 0; k < sys.getPlanetCount(); k++) {
                    DefaultMutableTreeNode planetNode = new DefaultMutableTreeNode("Planet");
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
        for (int i = 0; i < Globals.universe.getCivilizationCount(); i++) {
            Civilization civ = Globals.universe.getCivilization(i);
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
     * @return Instance of universe breakdown class.
     */
    public static UniverseBreakdown getInstance() {
        if (instance == null) {
            instance = new UniverseBreakdown();
        }
        instance.setVisible(true);
        return instance;
    }
}
