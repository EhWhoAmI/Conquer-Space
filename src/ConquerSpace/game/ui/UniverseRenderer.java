package ConquerSpace.game.ui;

import ConquerSpace.game.universe.spaceObjects.Sector;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.util.CQSPLogger;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import org.apache.logging.log4j.Logger;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * Jpanel of the universe. Displays the whole of it.
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
        
        setPreferredSize(bounds);
        //LOGGER.info("Displaying universe " + universe.toReadableString());
    }
    
    /**
     * Paints the universe.
     * @param g Graphics.
     */
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
        int placedOutside = 0;
        LOGGER.info("Universe drawn size: " + universeDrawnSize);
        //Draw a circle to show the universe
        Ellipse2D.Float universeCircle = new Ellipse2D.Float(0, 0, universeDrawnSize, universeDrawnSize);
        g2d.setColor(Color.BLACK);
        g2d.fill(universeCircle);
        //Do fancy math to calculate the size of 1 light year. Divide the universe drawn size with universe details' diameter
        int sizeOfLtyr = (int) Math.floor(universeDrawnSize/details.diameter);
        LOGGER.info("Size of light year " + sizeOfLtyr + "px");
        //Load all the sectors.
        ArrayList<Sector> sectors = new ArrayList<>();
        for (int i = 0; i < universe.getSectorCount(); i ++) {
            sectors.add(universe.getSector(i));
        }
        
        //Iterate over sectors and find size of sector, make circles of them.
        Ellipse2D.Float[] circleList = new Ellipse2D.Float[universe.getSectorCount()];
        //Initalize sector circles!
        for (int n = 0; n < circleList.length; n ++) {
            long start = System.currentTimeMillis();
            //Get sector
            Sector s = sectors.get(n);
            LOGGER.info("---- [Sector " + s.getID() + "] ----");
            //Get furthest star system.
            float longest = 0;
            for (int b = 0; b < s.getStarSystemCount(); b++){
                if (s.getStarSystem(b).getGalaticLocation().getDistance() > longest)
                    longest = s.getStarSystem(b).getGalaticLocation().getDistance();
            }
            LOGGER.info("Sector " + s.getID() + " size:" + longest);
            LOGGER.info("Angle " + s.getGalaticLocation().getDegrees());
            LOGGER.info("Distance " + s.getGalaticLocation().getDistance());
            //Do math to calculate the position of the sector. 
            //Distance is to the center of the sector to center of universe.
            //So, distance is hypotenuse, we have the angle, and we need the opposite and adjectent.
            double ang = (double) s.getGalaticLocation().getDegrees();
            int rot = 0;
            while (ang > 89) {
                ang -= 90;
                rot ++;
            }
            //Then do a sine to calculate the length of the opposite. 
            int xpos;
            int ypos;
            //Math.sin and Math.cos is in radians.
            int opp = (int) Math.floor(Math.sin(Math.toRadians(ang)) * s.getGalaticLocation().getDistance());
            int adj = (int) Math.floor(Math.cos(Math.toRadians(ang)) * s.getGalaticLocation().getDistance());
            opp *= sizeOfLtyr;
            adj *= sizeOfLtyr;
            LOGGER.info("ROT: " + rot + " Angle: " + ang);
            switch (rot) {
                case 0:
                    //Xpos is opposite.
                    xpos = (int) Math.floor(universeDrawnSize/2 + opp);
                    ypos = (int) Math.floor(universeDrawnSize/2 - adj);
                    break;
                case 1:
                    //YPos is adjecant
                    xpos = (int) Math.floor(universeDrawnSize/2 + adj);
                    ypos = (int) Math.floor(universeDrawnSize/2 + opp);
                    break;
                case 2:
                    xpos = (int) Math.floor(universeDrawnSize/2 - opp);
                    ypos = (int) Math.floor(universeDrawnSize/2 + adj);
                    break;
                case 3:
                    xpos = (int) Math.floor(universeDrawnSize/2 - adj);
                    ypos = (int) Math.round(universeDrawnSize/2 - opp);
                    break;
                default:
                    xpos = 0;
                    ypos = 0;
            }
            
            LOGGER.info("Opposite = " + opp + "px; Adjacent = " + adj + "px.");
            LOGGER.info("Position: " + xpos + ", " + ypos);
            //Find position from the center of the galaxy.
            //Longest is the size of the sector.
            Ellipse2D.Float c = new Ellipse2D.Float(xpos - (longest * sizeOfLtyr/2), ypos - (longest * sizeOfLtyr/2), longest * sizeOfLtyr, longest * sizeOfLtyr);
            LOGGER.info("Rect " + c.getBounds2D().toString());
            g2d.setColor(Color.red);
            g2d.draw(c);
            
            //Debugging: draw line from xpos and y pos to the center of universe, just as test.
            Line2D.Float ln = new Line2D.Float(universeDrawnSize/2, universeDrawnSize/2, xpos, ypos);
            g2d.setColor(Color.ORANGE);
            //Uncomment for debugging
            g2d.draw(ln);
            
            //Also for debugging, ensure the center of the circle is in the screen
            double i = Math.hypot(xpos - universeDrawnSize/2, ypos - universeDrawnSize/2);
            LOGGER.info("Distance is " + i);
            if (i > (universeDrawnSize/2)){
                LOGGER.warn("Sector " + s.getID() + " Outside the box!");
                placedOutside++;
            }
            long end = System.currentTimeMillis();
            LOGGER.info("Took " + (end - start) + " milliseconds to draw sector");
            LOGGER.info("----- [End of Sector " + s.getID() + "] ----");
            
        }
        LOGGER.info(placedOutside + " sector(s) outside!");
    }
    
    /**
     * Details for the universe. Size, etc, etc...
     */
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
            int largeStarSystem = 0;
            for (int i = 0; i < largest.getStarSystemCount(); i ++) {
                if (largest.getStarSystem(i).getGalaticLocation().getDistance() > largeStarSystem)
                    largeStarSystem = (int) largest.getStarSystem(i).getGalaticLocation().getDistance();
            }
            
            // Then add the two distances together.
            diameter = (largeStarSystem + largest.getGalaticLocation().getDistance() + 1);
            LOGGER.info("Universe diameter : " + diameter + " ltyr");
        }
        
    }
    
}
