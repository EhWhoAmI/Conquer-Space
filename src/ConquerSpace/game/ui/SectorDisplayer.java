package ConquerSpace.game.ui;

import ConquerSpace.game.ui.renderers.SectorDrawer;
import ConquerSpace.game.ui.renderers.SectorRenderer;
import ConquerSpace.game.universe.spaceObjects.Sector;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Zyun
 */
public class SectorDisplayer extends JFrame{
    private SectorDrawer drawer;
    public SectorDisplayer(Sector s) {

        setTitle("Conquer Space");
        setLayout(new BorderLayout());
        //Create universe renderer
        SectorRenderer renderer = new SectorRenderer(new Dimension(1500, 1500), s);
        JPanel pan = new JPanel();
        pan.add(renderer);
        
        //Place renderer into a scroll pane.
        JScrollPane scrollPane = new JScrollPane(pan);
        add(scrollPane);
        
        setSize(500, 500);
        setVisible(true);
    
    }
}
