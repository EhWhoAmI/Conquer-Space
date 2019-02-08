package ConquerSpace;

import ConquerSpace.game.StarDate;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.util.ResourceLoader;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Scanner;
import javax.script.ScriptEngineManager;
import org.python.util.PythonInterpreter;

/**
 * Global variables of the game.
 *
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

    public static PythonInterpreter pythonEngine;
    static {
        ScriptEngineManager manager = new ScriptEngineManager();
        Globals.pythonEngine = new PythonInterpreter();
        //Load python methods
        try {
            Scanner scriptReader = new Scanner(ResourceLoader.getResourceByFile("script.python.processing.files"));
            int count = 0;
            while (scriptReader.hasNextLine()) {
                String script = scriptReader.nextLine();
                Globals.pythonEngine.execfile(ResourceLoader.loadResource(script));
                count++;
            }
        } catch (FileNotFoundException ex) {
        }
    }
}
