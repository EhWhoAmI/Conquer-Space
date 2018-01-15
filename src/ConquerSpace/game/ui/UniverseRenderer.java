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
        //Do fancy math to calculate the size of 1 light year. Divide the universe drawn size with universe details' diameter
        int sizeOfLtyr = (int) ((int) universeDrawnSize/details.diameter);
        
        //Load all the sectors.
        ArrayList<Sector> sectors = new ArrayList<>();
        for (int i = 0; i < universe.getSectorCount(); i ++) {
            sectors.add(universe.getSector(i));
        }
        
        //Iterate over sectors and find size of sector, make circles of them.
        Ellipse2D.Float[] circleList = new Ellipse2D.Float[universe.getSectorCount()];
        //Initalize sector circles!
        for (int n = 0; n < circleList.length; n ++) {
            //Get sector
            Sector s = sectors.get(n);
            
            //Get furthest star system.
            float longest = 0;
            for (int b = 0; b < s.getStarSystemCount(); b++){
                if (s.getStarSystem(n).getGalaticLocation().getDistance() > longest)
                    longest = s.getStarSystem(n).getGalaticLocation().getDistance();
            }
            //Do math to calculate the position of the sector. 
            //Distance is to the center of the sector to center of universe.
            //So, distance is hypotenuse, we have the angle, and we need the opposite and adjectent.
            int ang = (int) s.getGalaticLocation().getDegrees();
            if (s.getGalaticLocation().getDegrees() < 90) {
                //So the triangle is to up. ( the 90 degrees)
                
            }
            else if (s.getGalaticLocation().getDegrees() < 180) {
                //So the triangle is to right. ( the 90 degrees)
                ang -= 90;
            }
            else if (s.getGalaticLocation().getDegrees() < 270) {
                //So the triangle is to down. ( the 90 degrees)
                ang -= 180;
            }
            else if (s.getGalaticLocation().getDegrees() < 360) {
                //So the triangle is to right. ( the 90 degrees)
                ang -= 270;
            }
            //Then do a sine to calculate the length of the opposite. 
            int opp = (int) (Math.sin(ang)*(s.getGalaticLocation().getDistance() * sizeOfLtyr));
            //Then do a cosine to get the adjacent
            int adj = (int) (Math.cos(ang) * (s.getGalaticLocation().getDistance() * sizeOfLtyr));
            LOGGER.info("Opposite = " + opp + "px; Adjacent = " + adj + "px.");
            
            //Find position from the center of the galaxy.
            int xpos = (universeDrawnSize/2 + opp);
            int ypos = (universeDrawnSize/2 + adj);
            //Longest is the size of the sector.
            circleList[n] = new Ellipse2D.Float(xpos, ypos, longest * sizeOfLtyr, longest * sizeOfLtyr);
        }
        
        g2d.setColor(Color.red);
        for (Ellipse2D.Float e: circleList) {
            g2d.draw(e);
        }
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
