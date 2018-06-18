package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.Globals;
import ConquerSpace.game.ui.renderers.PlanetDrawStats;
import ConquerSpace.game.ui.renderers.SystemInternalsDrawer;
import ConquerSpace.game.ui.renderers.SystemRenderer;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.util.CQSPLogger;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Zyun
 */
public class SystemDisplayer extends JFrame implements MouseListener{
    SystemInternalsDrawer stats;
    private static final Logger LOGGER = CQSPLogger.getLogger(SystemDisplayer.class.getName());
    int sectorID;
    int systemID;
    
    private Universe universe;
    
    public SystemDisplayer(StarSystem sys, Universe universe) {
        this.universe = universe;
        
        setTitle("Star System " + sys.getId());
        systemID = sys.getId();
        sectorID = sys.getParent();
        SystemRenderer renderer = new SystemRenderer(sys, universe, new Dimension(1500, 1500));
        renderer.addMouseListener(this);
        stats = renderer.drawer;
        JPanel pan = new JPanel();
        pan.add(renderer);
        
        //Place renderer into a scroll pane.
        JScrollPane scrollPane = new JScrollPane(pan);
        add(scrollPane);
        
        setSize(500, 500);
        setMaximumSize(new Dimension(1500, 1500));
        setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //Double click opens system.
        if (e.getClickCount() == 2) {
            //Get which system clicked.
            for (PlanetDrawStats pstats : stats.stats.planetDrawStats) {
                if (Math.hypot(pstats.getPos().x - e.getX(), pstats.getPos().y - e.getY()) < pstats.getSize()) {
                    LOGGER.trace("Mouse clicked in planet " + pstats.getId() + "!");
                    PlanetInfoSheet d = new PlanetInfoSheet(universe.getSector(sectorID).getStarSystem(systemID).getPlanet(pstats.getId()), Globals.turn);
                    break;
                }
            }
        }
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
}
