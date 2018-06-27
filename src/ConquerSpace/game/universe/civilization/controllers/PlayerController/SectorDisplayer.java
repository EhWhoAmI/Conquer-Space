package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.ui.renderers.SectorDrawer;
import ConquerSpace.game.ui.renderers.SectorRenderer;
import ConquerSpace.game.ui.renderers.SystemDrawStats;
import ConquerSpace.game.universe.civilizations.VisionTypes;
import ConquerSpace.game.universe.spaceObjects.Sector;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.util.CQSPLogger;
import java.awt.BorderLayout;
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
public class SectorDisplayer extends JFrame implements MouseListener{
    private static final Logger LOGGER = CQSPLogger.getLogger(SectorDisplayer.class.getName());
    SectorDrawer drawStats;
    int id;
    
    private Universe universe;
    public SectorDisplayer(Sector s, Universe universe) {
        this.universe = universe;
        setTitle("Sector " + s.getID());
        setLayout(new BorderLayout());
        //Create universe renderer
        SectorRenderer renderer = new SectorRenderer(new Dimension(1500, 1500), s, universe);
        renderer.addMouseListener(this);
        drawStats = renderer.drawer;
        JPanel pan = new JPanel();
        pan.add(renderer);
        
        //Place renderer into a scroll pane.
        JScrollPane scrollPane = new JScrollPane(pan);
        add(scrollPane);
        
        //Prevent close
        setSize(500, 500);
        setMaximumSize(new Dimension(1500, 1500));
        setVisible(true);
        id = s.getID();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //Double click opens system.
        if (e.getClickCount() == 2) {
            //Get which system clicked.
            LOGGER.info("Double clicked. Opening system");
            for (SystemDrawStats stats : drawStats.stats) {
                if (Math.hypot(stats.getPosition().getX() - e.getX(), stats.getPosition().getY() - e.getY()) < 25 && universe.getCivilization(0).vision.get(stats.getPath()) > VisionTypes.UNDISCOVERED) {
                    LOGGER.info("Mouse clicked in system " + stats.getId() + "!");
                    SystemDisplayer d = new SystemDisplayer(universe.getSector(id).getStarSystem(stats.getId()), universe);
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
