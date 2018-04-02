package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.ui.renderers.SectorDrawer;
import ConquerSpace.game.ui.renderers.SectorRenderer;
import ConquerSpace.game.ui.renderers.SystemDrawStats;
import ConquerSpace.game.universe.spaceObjects.Sector;
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
    public SectorDisplayer(Sector s) {

        setTitle("Sector " + s.getID());
        setLayout(new BorderLayout());
        //Create universe renderer
        SectorRenderer renderer = new SectorRenderer(new Dimension(1500, 1500), s);
        renderer.addMouseListener(this);
        drawStats = renderer.drawer;
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
            LOGGER.info("Double clicked. Opening system");
            for (SystemDrawStats stats : drawStats.stats) {
                if (Math.hypot(stats.getPosition().getX() - e.getX(), stats.getPosition().getY() - e.getY()) < 25) {
                    LOGGER.info("Mouse clicked in system " + stats.getId() + "!");
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
