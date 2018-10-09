package ConquerSpace.game;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.VisionTypes;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.Universe;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Universe from which the player plays from. This controls a large chunk of the game,
 * and will be an interface from where the game things are accessed. It keeps track of
 * what the player owns, what it does not, what it sees... All that good stuff
 * @author Zyun
 */
public class GameUniverse {
    private Civilization self;
    private Universe universe;
    
    public GameUniverse(Universe u, Civilization civ) {
        self = civ;
        universe = u;
    }
    
    // initialize
    void initialize() {
        
    }
    
    void update() {
        
    }
    
    public int getVisibleStarSystemCount() {
        //Loop through all visible stuff. 
        int i = 0;
        
        for (UniversePath p : self.vision.keySet()) {
            int v = self.vision.get(p); 
            if(v > VisionTypes.UNDISCOVERED) {
                i++;
            }
        }
        return i;
    }
    
    public ArrayList<StarSystem> getVisibleStarSystems() {
        ArrayList<StarSystem> list = new ArrayList<>();
        //Loop through all visible star systems
        for (UniversePath p : self.vision.keySet()) {
            int v = self.vision.get(p); 
            if(v > VisionTypes.UNDISCOVERED) {
                //Get star system
                StarSystem sys = (StarSystem)universe.getSpaceObject(p);
                list.add(sys);
            }
        }
        return list;
    }
}