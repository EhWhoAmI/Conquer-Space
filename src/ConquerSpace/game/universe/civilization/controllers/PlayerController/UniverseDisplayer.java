package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.UniversePath;
import ConquerSpace.game.ui.renderers.SectorDrawStats;
import ConquerSpace.game.ui.renderers.UniverseDrawer;
import ConquerSpace.game.ui.renderers.UniverseRenderer;
import ConquerSpace.game.universe.civilization.VisionTypes;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.util.CQSPLogger;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.apache.logging.log4j.Logger;

/**
 * Displays the universe in a window.
 *
 * @author Zyun
 */
public class UniverseDisplayer extends JInternalFrame implements MouseListener {

    private static final Logger LOGGER = CQSPLogger.getLogger(UniverseDisplayer.class.getName());
    private UniverseDrawer drawer;
    private Universe universe;

    public UniverseDisplayer(Universe u) {
        this.universe = u;
        
        setTitle("Conquer Space");
        setLayout(new BorderLayout());
        //Create universe renderer
        UniverseRenderer renderer = new UniverseRenderer(new Dimension(1500, 1500), u);
        drawer = renderer.drawer;
        JPanel pan = new JPanel();
        pan.add(renderer);

        //Place renderer into a scroll pane.
        JScrollPane scrollPane = new JScrollPane(pan);
        pan.addMouseListener(this);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        add(scrollPane);
        setMaximumSize(new Dimension(1500, 1500));
        setSize(500, 500);
        setResizable(true);
        setClosable(true);

        setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //Double click opens sector.
        if (e.getClickCount() == 2) {
            //Get which sector clicked.
            LOGGER.info("Double clicked. Opening sector");
            sectorit:
            for (SectorDrawStats stats : drawer.sectorDrawings) {
                //Check for vision
                if (Math.hypot(stats.getPosition().getX() - e.getX(), stats.getPosition().getY() - e.getY()) < stats.getRadius()) {
                    for (UniversePath p : universe.getCivilization(0).vision.keySet()) {
                        if (p.getSectorID() == stats.getId() && universe.getCivilization(0).vision.get(p) > VisionTypes.UNDISCOVERED) {
                            LOGGER.info("Mouse clicked in sector " + stats.getId() + "!");
                            SectorDisplayer d = new SectorDisplayer(universe.getSector(stats.getId()), universe);
                            getDesktopPane().add(d);
                            break sectorit;
                        }
                    }

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
