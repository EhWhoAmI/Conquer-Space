package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.Globals;
import ConquerSpace.game.ui.renderers.PlanetDrawStats;
import ConquerSpace.game.ui.renderers.SystemInternalsDrawer;
import ConquerSpace.game.ui.renderers.SystemRenderer;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.util.CQSPLogger;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Zyun
 */
public class SystemDisplayer extends JInternalFrame implements MouseListener {

    SystemInternalsDrawer stats;
    private static final Logger LOGGER = CQSPLogger.getLogger(SystemDisplayer.class.getName());
    int sectorID;
    int systemID;

    private JPopupMenu menu;

    private Universe universe;
    private StarSystem system;

    public SystemDisplayer(StarSystem sys, Universe universe) {
        this.universe = universe;
        this.system = sys;
        //Init popup menu
        menu = new JPopupMenu();

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
        setResizable(true);
        setClosable(true);

        setSize(500, 500);
        setMaximumSize(new Dimension(1500, 1500));
        setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //Double click opens system.
        if (e.getClickCount() == 2 || SwingUtilities.isLeftMouseButton(e)) {
            //Get which system clicked.
            for (PlanetDrawStats pstats : stats.stats.planetDrawStats) {
                if (Math.hypot(pstats.getPos().x - e.getX(), pstats.getPos().y - e.getY()) < pstats.getSize()) {
                    LOGGER.trace("Mouse clicked in planet " + pstats.getID() + "!");
                    PlanetInfoSheet d = new PlanetInfoSheet(universe.getSector(sectorID).getStarSystem(systemID).getPlanet(pstats.getID()));
                    getDesktopPane().add(d);
                    break;
                }
            }
        }
//        } else if (SwingUtilities.isRightMouseButton(e)) {
//            //Show right click...
//            Planet clicked = null;
//            for (PlanetDrawStats pstats : stats.stats.planetDrawStats) {
//                if (Math.hypot(pstats.getPos().x - e.getX(), pstats.getPos().y - e.getY()) < pstats.getSize()) {
//                    LOGGER.trace("Mouse clicked in planet " + pstats.getID() + "!");
//                    clicked = system.getPlanet(pstats.getID());
//                    break;
//                }
//            }
//            menu.add("Planet " + clicked.getId());
//        }
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
