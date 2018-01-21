package ConquerSpace.game.ui;

import ConquerSpace.Globals;
import ConquerSpace.game.universe.spaceObjects.Sector;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
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
        JTree tree = new JTree(root);
        JScrollPane pane = new JScrollPane(tree);
        pan.add(pane);
        add(pan);
        pack();
        setVisible(true);
    }

    public static UniverseBreakdown getInstance() {
        if (instance == null) {
            instance = new UniverseBreakdown();
        }
        instance.setVisible(true);
        return instance;
    }
}
