package ConquerSpace.gui.game;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.PlanetTypes;
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
        
    }

    /**
     * Get one instance of the UniverseBreakdown class.
     *
     * @param u Universe
     * @return Instance of universe breakdown class.
     */
    private static UniverseBreakdown getInstance(Universe u) {
        if (instance == null) {
            instance = new UniverseBreakdown(u);
        }
        instance.setVisible(true);
        return instance;
    }
}
