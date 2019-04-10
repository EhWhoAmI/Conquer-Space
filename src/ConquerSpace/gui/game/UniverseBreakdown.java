package ConquerSpace.gui.game;

import ConquerSpace.game.universe.spaceObjects.Universe;
import javax.swing.JInternalFrame;

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
