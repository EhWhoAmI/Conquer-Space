package ConquerSpace.game.universe.civilization.controllers.PlayerController.planetdisplayer;

import ConquerSpace.game.universe.spaceObjects.Planet;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.Color;
import java.awt.GridLayout;
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
    private JLabel populationCount;
    private JLabel populationGrowth;
    private JLabel birthRate;
    private JLabel deathRate;
    
    public PlayerPopulation(Planet p, int turn) {
        setLayout(new VerticalFlowLayout());
        currentStats = new JPanel(new VerticalFlowLayout());
        
        populationCount = new JLabel();
        populationGrowth = new JLabel();
        birthRate = new JLabel();
        deathRate = new JLabel();
        
        currentStats.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Current population stats"));
        
        populationCount.setText("Population: " + p.getPopulation());
        populationGrowth.setText("Population growth: " + p.population.getLastYearsPopulationGrowth(turn) + "% this year");
        birthRate.setText("Birth rate: " + p.population.getLastYearsbirthsPer1K(turn) + " per 1000 people this year");
        deathRate.setText("Death rate: " + p.population.getLastYearsMortalityRate(turn) + " per 1000 people this year");
        
        currentStats.add(populationCount);
        currentStats.add(populationGrowth);
        currentStats.add(birthRate);
        currentStats.add(deathRate);
        
        add(currentStats);
    }
}
