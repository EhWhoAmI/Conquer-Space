package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.ui.renderers.SystemInternalsDrawer;
import ConquerSpace.game.ui.renderers.SystemRenderer;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Zyun
 */
public class SystemDisplayer extends JFrame{
    SystemInternalsDrawer stats;

    public SystemDisplayer(StarSystem sys) {
        setTitle("Star System " + sys.getId());
        SystemRenderer renderer = new SystemRenderer(sys, new Dimension(1500, 1500));
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
}
