package ConquerSpace.game.ui;

import ConquerSpace.game.universe.Sector;
import ConquerSpace.game.universe.StarSystem;
import ConquerSpace.game.universe.Universe;
import ConquerSpace.util.CQSPLogger;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import org.apache.logging.log4j.Logger;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author Zyun
 */
public class UniverseRenderer extends JPanel{
    private static final Logger LOGGER = CQSPLogger.getLogger(UniverseRenderer.class.getName());
    private Dimension bounds;
    private Universe universe;
    private Point translation; 
    private UniverseDetails details;
    
    public UniverseRenderer(Dimension bounds, Universe universe, Point translation) {
        this.bounds = bounds;
        this.universe = universe;
        this.translation = translation;
        
        details = new UniverseDetails(universe);
        
        LOGGER.info("Displaying universe " + universe.toReadableString());
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Paint bounds dark blue.
        Graphics2D g2d = (Graphics2D) g;
        Rectangle2D.Float bg = new Rectangle2D.Float(0, 0, bounds.width, bounds.height);
        g2d.setColor(new Color(0, 0, 255));
        g2d.fill(bg);
        //We have the universe diameter, then find the size in pixels the universe has to be.
        int universeDrawnSize = (bounds.height < bounds.width)? bounds.height : bounds.width;
        
        //Draw a circle to show the universe
        Ellipse2D.Float universeCircle = new Ellipse2D.Float(0, 0, universeDrawnSize, universeDrawnSize);
        g2d.setColor(Color.BLACK);
        g2d.fill(universeCircle);
        //Load all the sectors.
        ArrayList<Sector> sectors = new ArrayList<>();
        for (int i = 0; i < universe.getSectorCount(); i ++) {
            sectors.add(universe.getSector(i));
        }
        
        //Iterate over sectors and find size
    }
    
    //Details for the universe. Size, etc, etc...
    private class UniverseDetails {
        float diameter;
        
        public UniverseDetails(Universe univ) {
            //Find radus of the universe. Outermost system of the outermost sector + 1 ltyr.
            Sector largest = null;
            for (int i = 0; i < univ.getSectorCount(); i ++) {
                Sector s = univ.getSector(i);
                if (largest == null || s.getGalaticLocation().getDistance() > largest.getGalaticLocation().getDistance()) {
                    largest = s;
                }
            }
            
            //Use the same process for the star systems.
            StarSystem largeStarSystem = null;
            for (int i = 0; i < largest.getStarSystemCount(); i ++) {
                StarSystem s = largest.getStarSystem(i);
                if (largeStarSystem == null || s.getGalaticLocation().getDistance() > largeStarSystem.getGalaticLocation().getDistance())
                    largeStarSystem = s;
            }
            
            // Then add the two distances together.
            diameter = (largeStarSystem.getGalaticLocation().getDistance() + largest.getGalaticLocation().getDistance() + 1);
        }
        
    }
    
}
