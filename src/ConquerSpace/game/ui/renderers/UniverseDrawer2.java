package ConquerSpace.game.ui.renderers;

import ConquerSpace.game.universe.civilization.controllers.LimitedStarSystem;
import ConquerSpace.game.universe.civilization.controllers.LimitedUniverse;
import ConquerSpace.game.universe.civilization.vision.VisionTypes;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.util.CQSPLogger;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Zyun
 */
public class UniverseDrawer2 {

    private static final Logger LOGGER = CQSPLogger.getLogger(UniverseDrawer2.class.getName());

    public int universeDimensionsLTYR;
    public int universeDrawnSize;
    public int sizeOfLtyr;

    public ArrayList<SystemDrawStats> systemDrawings;
    
    LimitedUniverse universe;
    //We need some way of updating it
    public UniverseDrawer2(LimitedUniverse universe, Dimension bounds) {
        systemDrawings = new ArrayList<>();
        this.universe = universe;
        
        //Get furthest star system
        int universeRadius = 0;
        ArrayList<LimitedStarSystem> visibleStarSystems = universe.getVisibleStarSystems();
        for (int i = 0; i < visibleStarSystems.size(); i++) {
            LimitedStarSystem s = visibleStarSystems.get(i);
            if(s == null) {
                continue;
            }
            //if it is null, then it is not visible
            if (s.getGalaticLocation() != null && s.getGalaticLocation().getDistance() > universeRadius) {
                universeRadius = s.getGalaticLocation().getDistance();
            }
        }
        //One more light year because why not.
        
        universeRadius++;

        universeDimensionsLTYR = universeRadius;
        LOGGER.info("Universe diameter : " + universeDimensionsLTYR + " ltyr");

        universeDrawnSize = (bounds.height > bounds.width) ? bounds.height : bounds.width;

        //Do fancy math to calculate the size of 1 light year. Divide the universe drawn size with universe details' diameter
        //Multiply by 2 because it is a radius
        sizeOfLtyr = (int) Math.floor(universeDrawnSize/(universeDimensionsLTYR *2 ));
        LOGGER.info("Size of light year " + sizeOfLtyr + "px, actual is " + ((float) universeDrawnSize / (float) universeDimensionsLTYR));
        //sizeOfLtyr = 1;

        for (int i = 0; i < visibleStarSystems.size(); i++) {
            //Do star systems
            LimitedStarSystem sys = visibleStarSystems.get(i);
            if(sys.getVisionType() < VisionTypes.KNOWS_DETAILS)
                continue;
            Point pt = RendererMath.polarCoordToCartesianCoord(sys.getGalaticLocation(), new Point(universeDrawnSize / 2, universeDrawnSize / 2), sizeOfLtyr);

            //Color of star system
            Color c;
            switch (sys.getStar(0).getType()) {
                case 0:
                    c = new Color(104, 64, 0);
                    break;
                case 1:
                    c = Color.YELLOW;
                    break;
                case 2:
                    c = Color.RED;
                    break;
                case 3:
                    c = Color.CYAN;
                    break;
                default:
                    c = Color.BLACK;
            }
            SystemDrawStats sysStats = new SystemDrawStats(pt, c, sys.getID(), sys.getUniversePath());
            systemDrawings.add(sysStats);
        }
    }
    
    //Redo all math
    //Definitely not copied from constructor...
    public void refresh() {
        ArrayList<LimitedStarSystem> visibleStarSystems = universe.getVisibleStarSystems();
        for (int i = 0; i < visibleStarSystems.size(); i++) {
            //Do star systems
            LimitedStarSystem sys = visibleStarSystems.get(i);
            if(sys.getVisionType() < VisionTypes.KNOWS_DETAILS)
                continue;
            Point pt = RendererMath.polarCoordToCartesianCoord(sys.getGalaticLocation(), new Point(universeDrawnSize / 2, universeDrawnSize / 2), sizeOfLtyr);

            //Color of star system
            Color c;
            switch (sys.getStar(0).getType()) {
                case 0:
                    c = new Color(104, 64, 0);
                    break;
                case 1:
                    c = Color.YELLOW;
                    break;
                case 2:
                    c = Color.RED;
                    break;
                case 3:
                    c = Color.CYAN;
                    break;
                default:
                    c = Color.BLACK;
            }
            SystemDrawStats sysStats = new SystemDrawStats(pt, c, sys.getID(), sys.getUniversePath());
            synchronized(systemDrawings) {
                systemDrawings.add(sysStats);
            }
        }
    }
}
