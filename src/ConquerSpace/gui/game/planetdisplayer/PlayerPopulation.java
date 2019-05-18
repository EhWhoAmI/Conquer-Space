package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.game.universe.spaceObjects.Planet;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 *
 * @author Zyun
 */
public class PlayerPopulation extends JPanel{
    private JPanel currentStats;
    
    public PlayerPopulation(Planet p, int turn) {
        setLayout(new VerticalFlowLayout());
        currentStats = new JPanel(new VerticalFlowLayout());
        
        currentStats.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Current population stats"));
        
        
        add(currentStats);
    }
}
