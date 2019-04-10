package ConquerSpace.util;

import ConquerSpace.Globals;
import java.util.HashMap;

/**
 *
 * @author Zyun
 */
public class MultiplierProcessor {

    public static int process(String text, HashMap<String, Integer> values, HashMap<String, Integer> multipliers) throws IllegalArgumentException {
        //Split
        Globals.pythonEngine.set("values", values);
        Globals.pythonEngine.set("multipliers", multipliers);
        text.replace("#", "multiplier.");
        text.replace("@", "values.");
        Globals.pythonEngine.exec(text);
        return 0;
    }
}
