package ConquerSpace.game;

import ConquerSpace.game.ui.renderers.RendererMath;
import ConquerSpace.game.ui.renderers.SystemDrawStats;
import ConquerSpace.game.universe.civilization.controllers.LimitedStarSystem;
import ConquerSpace.game.universe.civilization.controllers.LimitedUniverse;
import ConquerSpace.game.universe.civilization.vision.VisionTypes;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.Universe;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

/**
 * This is a much simpler vision of UniverseDrawer2, as it does not process data
 * for the user, it only processes the position of star systems in the universe.
 * Otherwise, it is just copied and pasted from UniverseDrawer2.
 * @author Zyun
 */
public class SimplifiedUniverseDrawer {
    public int universeDimensionsLTYR;
    public int universeDrawnSize;
    public int sizeOfLtyr;

    public ArrayList<ReducedSystemStats> systemDrawings;
    
    Universe universe;
    //We need some way of updating it
    public SimplifiedUniverseDrawer(Universe universe, Dimension bounds) {
        systemDrawings = new ArrayList<>();
        this.universe = universe;
        
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

        universeDrawnSize = (bounds.height > bounds.width) ? bounds.height : bounds.width;

        //Do fancy math to calculate the size of 1 light year. Divide the universe drawn size with universe details' diameter
        //Multiply by 2 because it is a radius
        sizeOfLtyr = (int) Math.floor(universeDrawnSize/(universeDimensionsLTYR *2 ));
        //sizeOfLtyr = 1;

        for (int i = 0; i < universe.getStarSystemCount(); i++) {
            //Do star systems
            StarSystem sys = universe.getStarSystem(i);
            Point pt = RendererMath.polarCoordToCartesianCoord(sys.getGalaticLocation(), new Point(universeDrawnSize / 2, universeDrawnSize / 2), sizeOfLtyr);
            ReducedSystemStats sysStats = new ReducedSystemStats(pt, sys.getId(), sys.getUniversePath(), universe.getCivilizationCount());
            systemDrawings.add(sysStats);
        }
    }
    
    //Redo all math
    //Definitely not copied from constructor...
    //Private for now because we have no need to use it, but who knows?
    private void refresh() {
        //Do fancy math to calculate the size of 1 light year. Divide the universe drawn size with universe details' diameter
        //Multiply by 2 because it is a radius
        sizeOfLtyr = (int) Math.floor(universeDrawnSize/(universeDimensionsLTYR *2 ));
        //sizeOfLtyr = 1;

        for (int i = 0; i < universe.getStarSystemCount(); i++) {
            //Do star systems
            StarSystem sys = universe.getStarSystem(i);
            Point pt = RendererMath.polarCoordToCartesianCoord(sys.getGalaticLocation(), new Point(universeDrawnSize / 2, universeDrawnSize / 2), sizeOfLtyr);
            ReducedSystemStats sysStats = new ReducedSystemStats(pt, sys.getId(), sys.getUniversePath(), universe.getCivilizationCount());
            systemDrawings.add(sysStats);
        }
    }
    
    public void resetVisionValues() {
        for(ReducedSystemStats a : systemDrawings) {
           a.clear();
        }
    }
    
    //A reduced drawstat that only shows gets the position and things like that.
    public static class ReducedSystemStats {
        private Point pos;
        private int id;
        private UniversePath path;
        //This one will be incremented over time. A certain value will mean the various
        //vision types.
        private int[] timesSeen;
        public ReducedSystemStats(Point pos, int id, UniversePath path, int civs) {
            this.pos = pos;
            this.id = id;
            this.path = path;
            timesSeen = new int[civs];
        }

        public int getSystemID() {
            return id;
        }

        public UniversePath getPath() {
            return path;
        }

        public Point getPos() {
            return pos;
        }
        
        public void incrementFor(int civ) {
            timesSeen[civ]++;
        }
        
        public void increment(int civ, int amount) {
            timesSeen[civ]+=amount;
        }
        
        public int getTimesSeen(int civ) {
            return (timesSeen[civ]);
        }
        
        private void clear() {
            for(int i = 0; i < timesSeen.length; i++) {
                timesSeen[i] = 0;
            }
        }
        
        public int getCivs() {
            return (timesSeen.length);
        }
    }
}
