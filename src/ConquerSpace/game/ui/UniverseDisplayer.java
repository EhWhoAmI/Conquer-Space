package ConquerSpace.game.ui;

import ConquerSpace.Globals;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Displays the universe in a window.
 * @author Zyun
 */
public class UniverseDisplayer extends JFrame{

    public UniverseDisplayer() {
        setTitle("Conquer Space");
        setLayout(new BorderLayout());
        //Create universe renderer
        UniverseRenderer renderer = new UniverseRenderer(new Dimension(1000, 1000), Globals.universe, new Point(0, 0));
        JPanel pan = new JPanel();
        pan.add(renderer);
        
        //Place renderer into a scroll pane.

        JScrollPane scrollPane = new JScrollPane(pan);
        
        add(scrollPane);

        setSize(500, 500);
        setVisible(true);
    }
    
}
