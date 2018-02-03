package ConquerSpace.game.ui.renderer.universe;

import ConquerSpace.game.universe.spaceObjects.Sector;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.util.CQSPLogger;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;

/**
 * Tries to remove all the math from the paintComponent in UniverseRenderer, by doing the math before hand.
 * @author Zyun
 */
public class UniverseDrawer {
    //Logger
    private static final Logger LOGGER = CQSPLogger.getLogger(UniverseDrawer.class.getName());
    
    public int universeDimensionsLTYR;
    public int universeDrawnSize;
    
    public ArrayList<SectorDrawStats> sectorDrawings;
    public UniverseDrawer(Universe universe, Dimension bounds) {
        sectorDrawings = new ArrayList<>();
        {
            //Find radius of the universe in light years. Outermost system of the outermost sector + 1 ltyr.
            Sector largest = null;
            for (int i = 0; i < universe.getSectorCount(); i ++) {
                Sector s = universe.getSector(i);
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
            universeDimensionsLTYR = (int) (largeStarSystem + largest.getGalaticLocation().getDistance() + 1);
            LOGGER.info("Universe diameter : " + universeDimensionsLTYR + " ltyr");
            
            
        }
        
        //Calculate drawn size
        universeDrawnSize = (bounds.height < bounds.width)? bounds.height : bounds.width;
        int placedOutside = 0;
        LOGGER.info("Universe drawn size: " + universeDrawnSize);
        //Draw a circle to show the universe

        //Do fancy math to calculate the size of 1 light year. Divide the universe drawn size with universe details' diameter
        int sizeOfLtyr = (int) Math.floor(universeDrawnSize/universeDimensionsLTYR);
        LOGGER.info("Size of light year " + sizeOfLtyr + "px");
        //Load all the sectors.
        ArrayList<Sector> sectors = new ArrayList<>();
        for (int i = 0; i < universe.getSectorCount(); i ++) {
            sectors.add(universe.getSector(i));
        }
        
        //Initalize sector circles!
        for (int n = 0; n < universe.getSectorCount(); n ++) {
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
            
            //Also for debugging, ensure the center of the circle is in the screen
            double i = Math.hypot(xpos - universeDrawnSize/2, ypos - universeDrawnSize/2);
            LOGGER.info("Distance is " + i);
            if (i > (universeDrawnSize/2)){
                LOGGER.warn("Sector " + s.getID() + " Outside the box!");
                placedOutside++;
            }
            SectorDrawStats stats = new SectorDrawStats(new Point(xpos, ypos), (int) longest);
            sectorDrawings.add(stats);
            LOGGER.info("----- [End of Sector " + s.getID() + "] ----");
            
        }
        LOGGER.info(placedOutside + " sector(s) outside!");
    }
    
    public class SectorDrawStats {
        private Point pos;
        private int circumference;

        public SectorDrawStats(Point pos, int circumference) {
            this.pos = pos;
            this.circumference = circumference;
        }

        public int getCircumference() {
            return circumference;
        }

        public Point getPosition() {
            return pos;
        }
    }
}
