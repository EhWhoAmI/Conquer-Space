package ConquerSpace.game.ui.renderers;

import ConquerSpace.game.universe.GalaticLocation;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.Star;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.util.CQSPLogger;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Zyun
 */
public class SystemInternalsDrawer {

    public SystemInternalDrawStats stats;
    private static final Logger LOGGER = CQSPLogger.getLogger(SystemInternalsDrawer.class.getName());
    public SystemInternalsDrawer(StarSystem sys, Dimension bounds) {
        stats = new SystemInternalDrawStats();
        //Get size of the star system
        int size = 0;
        for (int i = 0; i < sys.getPlanetCount(); i++) {
            if (sys.getPlanet(i).getOrbitalDistance() > size) {
                size = (int) sys.getPlanet(i).getOrbitalDistance();
            }
        }
        //then find larger bounds
        int systemDrawnSize = (((bounds.height < bounds.width) ? bounds.height : bounds.width) / 2);
        LOGGER.info("System size: " + size);
        int sizeofAU = (int) (Math.floor(systemDrawnSize / (size + (size/2))));
        LOGGER.info("Size of 1 AU: " + sizeofAU + " px");
        //Draw it
        // As of version indev, there is only one star.
        //Star will be in the center
        Star star = sys.getStar(0);
        int starSize = star.starSize;
        int type = star.type;
        Color c;
        switch (type) {
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

        Point starPos = RendererMath.polarCoordToCartesianCoord(new GalaticLocation(0, 0), new Point(bounds.width / 2, bounds.height / 2), sizeofAU);
        StarDrawStats sds = new StarDrawStats(star.id, starPos, starSize * 10, c);
        stats.addStarDrawStats(sds);
        LOGGER.info("System " + sys.getId() + " has " + sys.getPlanetCount() + " planets");
        for (int n = 0; n < sys.getPlanetCount(); n++) {
            Planet p = sys.getPlanet(n);
            type = p.getPlanetType();
            Color cl;
            switch (type) {
                case 0:
                    cl = Color.ORANGE;
                    break;
                case 1:
                    cl = Color.MAGENTA;
                    break;
                default:
                    cl = Color.BLACK;
            }
            LOGGER.info("Planet " + p.getId() + " is type " + p.getPlanetType());
            Random rand = new Random();
            int degs = rand.nextInt(361);
            LOGGER.info("Degrees: " + degs + " distance " + p.getOrbitalDistance());
            Point point = RendererMath.polarCoordToCartesianCoord(new GalaticLocation(degs, p.getOrbitalDistance()), new Point(bounds.width / 2, bounds.height / 2), sizeofAU);
            PlanetDrawStats pds = new PlanetDrawStats(p.getId(), point, cl);
            LOGGER.info(Math.hypot(point.x - bounds.width/2, point.y - bounds.height/2));
            this.stats.addPlanetDrawStats(pds);
        }
    }

}
