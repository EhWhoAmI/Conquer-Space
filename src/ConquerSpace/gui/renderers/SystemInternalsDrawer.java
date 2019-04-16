package ConquerSpace.gui.renderers;

import ConquerSpace.game.universe.GalacticLocation;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.Ship;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.Star;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.StarTypes;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.util.CQSPLogger;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Zyun
 */
public class SystemInternalsDrawer {

    public SystemInternalDrawStats stats;
    private static final Logger LOGGER = CQSPLogger.getLogger(SystemInternalsDrawer.class.getName());
    public int sizeofAU;
    public ArrayList<Ship> ships;
    private Universe universe;

    public SystemInternalsDrawer(StarSystem sys, Universe u, Dimension bounds) {
        universe = u;
        stats = new SystemInternalDrawStats();
        
        ships = new ArrayList<>();
        //Get size of the star system
        long size = 0;
        for (int i = 0; i < sys.getPlanetCount(); i++) {
            if (sys.getPlanet(i).getOrbitalDistance() > size) {
                size = sys.getPlanet(i).getOrbitalDistance();
            }
        }
        //then find larger bounds
        int systemDrawnSize = (((bounds.height < bounds.width) ? bounds.height : bounds.width) / 2);
        LOGGER.trace("System size: " + size);
        sizeofAU = 1;
        if (size != 0) {
            sizeofAU = (int) (Math.floor(systemDrawnSize / (int) (size / 10000000) + 1));
        }

        LOGGER.trace("Size of 1 AU: " + sizeofAU + " px");
        //Draw it
        // As of version indev, there is only one star.
        //Star will be in the center
        Star star = sys.getStar(0);
        int starSize = star.starSize/50000;
        int type = star.type;
        Color c;
        switch (type) {
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

        Point starPos = RendererMath.polarCoordToCartesianCoord(new GalacticLocation(0, 0), new Point(bounds.width / 2, bounds.height / 2), sizeofAU);
        StarDrawStats sds = new StarDrawStats(star.getId(), starPos, starSize * 5, c);
        stats.addStarDrawStats(sds);
        LOGGER.trace("System " + sys.getId() + " has " + sys.getPlanetCount() + " planets");
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
            LOGGER.trace("Planet " + p.getId() + " is type " + p.getPlanetType());
            Random rand = new Random();
            int degs = rand.nextInt(361);
            LOGGER.trace("Degrees: " + degs + " distance " + p.getOrbitalDistance());
            //With the new system, we are supposed to calculate this all properly, maybe
            //Increasing the accuracy as we go, but for now, lets do it by the ten millions.
            Point point = RendererMath.polarCoordToCartesianCoord(new GalacticLocation(p.getPlanetDegrees(), (int) (p.getOrbitalDistance() / 10000000)),
                    new Point(bounds.width / 2, bounds.height / 2), sizeofAU);

            String playerSymbol = "";
            Color co = null;
            if (p.getOwnerID() != -1) {
                Civilization civ = universe.getCivilization(p.getOwnerID());
                playerSymbol = civ.getName();
                co = civ.getColor();
            }
            PlanetDrawStats pds = new PlanetDrawStats(p.getId(), point, cl,
                    (int) (p.getOrbitalDistance() / 10000000) * sizeofAU, p.getPlanetSize(), playerSymbol,
                    co, p.getName());

            LOGGER.trace("Distance : " + Math.hypot(point.x - bounds.width / 2, point.y - bounds.height / 2));
            this.stats.addPlanetDrawStats(pds);
        }
    }

}
