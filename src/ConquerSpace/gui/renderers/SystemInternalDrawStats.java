package ConquerSpace.gui.renderers;

import java.util.ArrayList;

/**
 *
 * @author Zyun
 */
public class SystemInternalDrawStats {
    public ArrayList<StarDrawStats> starDrawStats;
    public ArrayList<PlanetDrawStats> planetDrawStats;

    public SystemInternalDrawStats() {
        starDrawStats = new ArrayList<>();
        planetDrawStats = new ArrayList<>();
    }
    
    public void addPlanetDrawStats(PlanetDrawStats p) {
        planetDrawStats.add(p);
    }
    public void addStarDrawStats(StarDrawStats s) {
        starDrawStats.add(s);
    }
}
