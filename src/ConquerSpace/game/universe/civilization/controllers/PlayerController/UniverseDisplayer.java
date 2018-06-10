package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.ui.renderers.UniverseRenderer;
import ConquerSpace.Globals;
import ConquerSpace.game.UniversePath;
import ConquerSpace.game.actions.Action;
import ConquerSpace.game.ui.renderers.SectorDrawStats;
import ConquerSpace.game.ui.renderers.UniverseDrawer;
import ConquerSpace.game.universe.civilizations.VisionTypes;
import ConquerSpace.util.CQSPLogger;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;

/**
 * Displays the universe in a window.
 *
 * @author Zyun
 */
public class UniverseDisplayer extends JFrame implements MouseListener {

    private static final Logger LOGGER = CQSPLogger.getLogger(UniverseDisplayer.class.getName());
    private UniverseDrawer drawer;

    public UniverseDisplayer() {
        setTitle("Conquer Space");
        setLayout(new BorderLayout());
        //Create universe renderer
        UniverseRenderer renderer = new UniverseRenderer(new Dimension(1500, 1500), Globals.universe);
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
                    for (UniversePath p : Globals.universe.getCivilization(0).vision.keySet()) {
                        if (p.getSectorID() == stats.getId() && Globals.universe.getCivilization(0).vision.get(p) > VisionTypes.UNDISCOVERED) {
                            LOGGER.info("Mouse clicked in sector " + stats.getId() + "!");
                            SectorDisplayer d = new SectorDisplayer(Globals.universe.getSector(stats.getId()));
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
