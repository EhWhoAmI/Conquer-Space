package ConquerSpace.game.ui.renderers;

import ConquerSpace.game.universe.spaceObjects.Sector;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

/**
 * Pretty much the same as <code>UniverseRenderer</code>, just that its for
 * sectors. Based on that.
 *
 * @see UniverseRenderer
 * @author Zyun
 */
public class SectorDrawer {

    public ArrayList<SystemDrawStats> stats;
    public int sectorDrawnSize;
    public int sizeOfLtyr;
    public Point center;

    public SectorDrawer(Sector sector, Dimension bounds) {
        stats = new ArrayList<>();
        //Calculate center.
        sectorDrawnSize = (bounds.height < bounds.width) ? bounds.height : bounds.width;

        //Center is half of sectorDrawn size.
        center = new Point(sectorDrawnSize / 2, sectorDrawnSize / 2);

        sizeOfLtyr = (int) Math.floor(sectorDrawnSize / sector.getSize());
        for (int i = 0; i < sector.getStarSystemCount(); i++) {
            Point p = RendererMath.polarCoordToCartesianCoord(sector.getStarSystem(i).getGalaticLocation(), center, sizeOfLtyr);
            Color c;
            switch (sector.getStarSystem(i).getStar(0).type) {
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
            SystemDrawStats sds = new SystemDrawStats(p, c);
            stats.add(sds);
        }
        //Done!
    }
}
