package ConquerSpace.game.ui;

import ConquerSpace.game.ui.renderers.UniverseRenderer;
import ConquerSpace.Globals;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.Point;

/**
 * Displays the universe in a window.
 * @author Zyun
 */
public class UniverseDisplayer extends JFrame{

    public UniverseDisplayer() {
        setTitle("Conquer Space");
        setLayout(new BorderLayout());
        //Create universe renderer
        UniverseRenderer renderer = new UniverseRenderer(new Dimension(1500, 1500), Globals.universe);
        JPanel pan = new JPanel();
        pan.add(renderer);
        
        //Place renderer into a scroll pane.

        JScrollPane scrollPane = new JScrollPane(pan);
		scrollPane.getViewport().setViewPosition(new Point(100, 100));
        System.out.println(scrollPane.getViewport().getViewPosition().toString());
        add(scrollPane);

        setSize(500, 500);
        setVisible(true);
    }
    
}
