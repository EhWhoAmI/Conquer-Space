package ConquerSpace;

import ConquerSpace.game.StarDate;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.util.DatabaseManager;
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
     * Date in the stars. Game timer.
     */
    public static StarDate date = new StarDate();
    
    public static DatabaseManager database = new DatabaseManager();
}
