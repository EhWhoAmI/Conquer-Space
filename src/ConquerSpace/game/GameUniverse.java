package ConquerSpace.game;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.spaceObjects.Universe;

/**
 * Universe from which the player plays from. This controls a large chunk of the game,
 * and will be an interface from where the game things are accessed. It keeps track of
 * what the player owns, what it does not, what it sees... All that good stuff
 * @author Zyun
 */
public class GameUniverse {
    private Civilization self;
    
    public GameUniverse(Universe u, Civilization civ) {
        self = civ;
    }
    
    // initialize
    void initialize() {
        
    }
}