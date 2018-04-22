package ConquerSpace;

import ConquerSpace.game.universe.spaceObjects.Universe;
import java.util.Properties;

/**
 * Global variables of the game.
 * @author Zyun
 */
public class Globals {
    /**
     * This is the whole universe.
     */
    public static Universe universe;
    
    /**
     * This is the settings of the game.
     */
    public static Properties settings;
    
    /**
     * Current turn.
     */
    public static int turn;
    
    /**
     * How long 1 year is in turns.
     * For now, 12, so there are 12 turns in a year.
     * (in case you failed in math, that's 1 month per turn)
     */
    public static final int YEAR_IN_TURNS = 12;
}
