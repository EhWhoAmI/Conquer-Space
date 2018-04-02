package ConquerSpace.game.ui.renderers;

import ConquerSpace.Globals;
import ConquerSpace.game.UniversePath;
import ConquerSpace.game.universe.civilizations.Civilization;
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
    public ArrayList<ControlDrawStats> controlDrawStats;
    public int sectorDrawnSize;
    public int sizeOfLtyr;
    public Point center;

    public SectorDrawer(Sector sector, Dimension bounds) {
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
            SystemDrawStats sds = new SystemDrawStats(p, c, sector.getStarSystem(i).getId());
            stats.add(sds);
        }
        for(int n = 0; n < Globals.universe.getCivilizationCount(); n++) {
            Civilization c = Globals.universe.getCivilization(n);
            for(UniversePath p : c.control) {
                
                if (p.getSystemID() > -1 && p.getSectorID() == sector.getID()) {
                    //Calculate the thingy
                    //Get sector
                    ControlDrawStats cds = new ControlDrawStats(stats.get(p.getSystemID()).getPosition(), c.getColor());
                    System.err.println(cds.getPos().toString());
                    controlDrawStats.add(cds);
                }
            }
        }
        //Done!
    }
}
