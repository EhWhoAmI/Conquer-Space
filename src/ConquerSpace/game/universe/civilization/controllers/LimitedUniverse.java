package ConquerSpace.game.universe.civilization.controllers;

import ConquerSpace.game.UniversePath;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.vision.VisionTypes;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.Universe;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Universe from which the player plays from. This controls a large chunk of the game,
 * and will be an interface from where the game things are accessed. It keeps track of
 * what the player owns, what it does not, what it sees... All that good stuff
 * @author Zyun
 */
public class LimitedUniverse {
    private Civilization self;
    private Universe universe;
    
    public LimitedUniverse(Universe u, Civilization civ) {
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
    
    public ArrayList<LimitedStarSystem> getVisibleStarSystems() {
        ArrayList<LimitedStarSystem> list = new ArrayList<>();
        //Loop through all visible star systems
        for (UniversePath p : self.vision.keySet()) {
            int v = self.vision.get(p); 
            if(v > VisionTypes.UNDISCOVERED) {
                //Get star system
                LimitedStarSystem sys = new LimitedStarSystem((StarSystem)universe.getSpaceObject(p), v);
                list.add(sys);
            }
        }
        return list;
    }
    
    public LimitedStarSystem getStarSystem(int system) {
        return new LimitedStarSystem(universe.getStarSystem(system), self.vision.get(universe.getStarSystem(system).getUniversePath()));
    }
    
    //This will have to depend on contact with the civ, but for now, lets scrap it.
    public Civilization getCivilization(int i) {
        return universe.getCivilization(i);
    }
    
    //Control also relies on contact with other civ, so lets skip it
    public HashMap<UniversePath, Integer> getControl() {
        return universe.control;
    }
}