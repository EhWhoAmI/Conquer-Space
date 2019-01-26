package ConquerSpace.gui.renderers;

import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.StarTypes;
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
public class UniverseDrawer {

    private static final Logger LOGGER = CQSPLogger.getLogger(UniverseDrawer.class.getName());

    public int universeDimensionsLTYR;
    public int universeDrawnSize;
    public int sizeOfLtyr;

    public ArrayList<SystemDrawStats> systemDrawings;

    public UniverseDrawer(Universe universe, Dimension bounds) {
        systemDrawings = new ArrayList<>();

        //Get furthest star system
        int universeRadius = 0;
        for (int i = 0; i < universe.getStarSystemCount(); i++) {
            StarSystem s = universe.getStarSystem(i);
            if (s.getGalaticLocation().getDistance() > universeRadius) {
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
        sizeOfLtyr = (int) Math.floor(universeDrawnSize / (universeDimensionsLTYR * 2));
        LOGGER.info("Size of light year " + sizeOfLtyr + "px, actual is " + ((float) universeDrawnSize / (float) universeDimensionsLTYR));
        //sizeOfLtyr = 1;

        for (int i = 0; i < universe.getStarSystemCount(); i++) {
            //Do star systems
            StarSystem sys = universe.getStarSystem(i);
            Point pt = RendererMath.polarCoordToCartesianCoord(sys.getGalaticLocation(), new Point(universeDrawnSize / 2, universeDrawnSize / 2), sizeOfLtyr);

            //Color of star system
            Color c;
            switch (sys.getStar(0).type) {
                case StarTypes.TYPE_A:
                    c = Color.decode("#D5E0FF");
                    break;
                case StarTypes.TYPE_B:
                    c = Color.decode("#A2C0FF");
                    break;
                case StarTypes.TYPE_O:
                    c = Color.decode("#92B5FF");
                    break;
                case StarTypes.TYPE_F:
                    c = Color.decode("#F9F5FF");
                    break;
                case StarTypes.TYPE_G:
                    c = Color.decode("#fff4ea");
                    break;
                case StarTypes.TYPE_K:
                    c = Color.decode("#FFDAB5");
                    break;
                case StarTypes.TYPE_M:
                    c = Color.decode("#FFB56C");
                    break;
                default:
                    c = Color.BLACK;
            }
            SystemDrawStats sysStats = new SystemDrawStats(pt, c, sys.getId(), sys.getUniversePath());
            systemDrawings.add(sysStats);
        }
    }
}
