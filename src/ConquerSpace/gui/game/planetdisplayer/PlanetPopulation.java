package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.PopulationStorage;
import ConquerSpace.game.universe.Point;
import ConquerSpace.game.universe.spaceObjects.Planet;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.Color;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

/**
 *
 * @author Zyun
 */
public class PlanetPopulation extends JPanel{
    private JPanel currentStats;
    private JLabel populationCount;
    public PlanetPopulation(Planet p, int turn) {
        setLayout(new VerticalFlowLayout());
        currentStats = new JPanel(new VerticalFlowLayout());
        
        currentStats.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Current population stats"));
        int pop = 0;
        for (Map.Entry<Point, Building> entry : p.buildings.entrySet()) {
            Point key = entry.getKey();
            Building value = entry.getValue();
                     
            if(value instanceof PopulationStorage) {
                PopulationStorage storage = (PopulationStorage) value;
                pop += storage.population.size();
            }
        }
        
        populationCount = new JLabel("Population: " + (pop*10) + " million");
        currentStats.add(populationCount);
        add(currentStats);
    }
}
