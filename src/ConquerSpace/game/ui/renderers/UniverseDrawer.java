package ConquerSpace.game.ui.renderers;

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
            
            
            Point sectorPos = RendererMath.polarCoordToCartesianCoord(s.getGalaticLocation(), new Point(universeDrawnSize/2, universeDrawnSize/2), sizeOfLtyr);
            
            //Also for debugging, ensure the center of the circle is in the screen
            double i = Math.hypot(sectorPos.getX() - universeDrawnSize/2, sectorPos.getY() - universeDrawnSize/2);
            LOGGER.info("Distance is " + i);
            if (i > (universeDrawnSize/2)){
                LOGGER.warn("Sector " + s.getID() + " Outside the box!");
                placedOutside++;
            }
            SectorDrawStats stats = new SectorDrawStats(new Point((int) sectorPos.getX(), (int) sectorPos.getY()), (int) longest);
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
