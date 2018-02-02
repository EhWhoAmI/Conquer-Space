package ConquerSpace.game.ui;

import ConquerSpace.Globals;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * Displays the universe in a window.
 * @author Zyun
 */
public class UniverseDisplayer extends JFrame{

    public UniverseDisplayer() {
        setTitle("Conquer Space");
        //Create universe renderer
        UniverseRenderer renderer = new UniverseRenderer(new Dimension(750, 750), Globals.universe, new Point(0, 0));
        renderer.setPreferredSize(new Dimension(750, 750));
        //Place renderer into a scroll pane. taken out for now
        JScrollPane scrollPane = new JScrollPane(renderer);
        //scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        //scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        add(renderer);
        pack();
        setVisible(true);
    }
    
}
