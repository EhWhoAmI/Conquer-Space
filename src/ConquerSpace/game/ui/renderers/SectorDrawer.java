package ConquerSpace.game.ui.renderers;

import ConquerSpace.Globals;
import ConquerSpace.game.UniversePath;
import ConquerSpace.game.universe.civilizations.Civilization;
import ConquerSpace.game.universe.spaceObjects.Sector;
import ConquerSpace.game.universe.spaceObjects.Universe;
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
    public ArrayList<ControlDrawStats> controlDrawStats;
    public int sectorDrawnSize;
    public int sizeOfLtyr;
    public Point center;

    private Universe universe;

    public SectorDrawer(Sector sector, Universe u, Dimension bounds) {
        universe = u;
        stats = new ArrayList<>();
        controlDrawStats = new ArrayList<>();
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
            SystemDrawStats sds = new SystemDrawStats(p, c, sector.getStarSystem(i).getId(), new UniversePath(sector.getStarSystem(i).getParent(), sector.getStarSystem(i).getId()));
            stats.add(sds);
        }
        for (UniversePath p : universe.control.keySet()) {
            if (p.getSystemID() > -1 && p.getSectorID() == sector.getID() && universe.control.get(p) > -1) {
                //Calculate the thingy
                //Get sector
                if (universe.control.get(p) > -1) {
                    ControlDrawStats cds = new ControlDrawStats(
                            stats.get(p.getSystemID()).getPosition(),
                            universe.getCivilization(universe.control.get(p)).getColor(), p);
                    controlDrawStats.add(cds);
                }
            }
        }
        //Done!
    }
}
