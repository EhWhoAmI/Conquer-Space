package ConquerSpace.game.ui.renderers;

import ConquerSpace.game.universe.GalaticLocation;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.Star;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Random;

/**
 *
 * @author Zyun
 */
public class SystemInternalsDrawer {

    public SystemInternalDrawStats stats;

    public SystemInternalsDrawer(StarSystem sys, Dimension bounds) {
        //Get size of the star system
        int size = 0;
        for (int i = 0; i < sys.getPlanetCount(); i++) {
            if (sys.getPlanet(i).getOrbitalDistance() > size) {
                size = (int) sys.getPlanet(i).getOrbitalDistance();
            }
        }
        //then find larger bounds
        int systemDrawnSize = (bounds.height < bounds.width) ? bounds.height : bounds.width;
        int sizeofAU = (int) Math.floor(systemDrawnSize / size);

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
        StarDrawStats sds = new StarDrawStats(star.id, starPos, size, c);
        stats.addStarDrawStats(sds);

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
            Random rand = new Random();
            Point point = RendererMath.polarCoordToCartesianCoord(new GalaticLocation(rand.nextFloat()%360, p.getOrbitalDistance()), new Point(bounds.width / 2, bounds.height / 2), sizeofAU);
            PlanetDrawStats pds = new PlanetDrawStats(p.getId(), point, cl);
            this.stats.addPlanetDrawStats(pds);
        }
    }

}
