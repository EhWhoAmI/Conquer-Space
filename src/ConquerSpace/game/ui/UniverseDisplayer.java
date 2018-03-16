package ConquerSpace.game.ui;

import ConquerSpace.game.ui.renderers.UniverseRenderer;
import ConquerSpace.Globals;
import ConquerSpace.game.ui.renderers.UniverseDrawer;
import ConquerSpace.util.CQSPLogger;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import org.apache.logging.log4j.Logger;

/**
 * Displays the universe in a window.
 * @author Zyun
 */
public class UniverseDisplayer extends JFrame implements MouseListener{
    private static final Logger LOGGER = CQSPLogger.getLogger(UniverseDisplayer.class.getName());
    private UniverseDrawer drawer = new UniverseDrawer(Globals.universe, new Dimension(1500, 1500));
    public UniverseDisplayer() {
        setTitle("Conquer Space");
        setLayout(new BorderLayout());
        //Create universe renderer
        UniverseRenderer renderer = new UniverseRenderer(new Dimension(1500, 1500), Globals.universe);
        JPanel pan = new JPanel();
        pan.add(renderer);
        
        //Place renderer into a scroll pane.
        JScrollPane scrollPane = new JScrollPane(pan);
        pan.addMouseListener(this);
        System.out.println(scrollPane.getViewport().getViewPosition().toString());
        add(scrollPane);
        
        setSize(500, 500);
        setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //Double click opens sector.
        if (e.getClickCount() == 2) {
            //Get which sector clicked.
            LOGGER.info("Double clicked. Opening sector");
            int i = 0;
            for (UniverseDrawer.SectorDrawStats stats : drawer.sectorDrawings) {
                if (Math.hypot(stats.getPosition().getX() - e.getX(), stats.getPosition().getY() - e.getY()) < stats.getRadius()) {
                    LOGGER.info("Mouse clicked in sector " + i + "!");
                }
                i ++;
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
