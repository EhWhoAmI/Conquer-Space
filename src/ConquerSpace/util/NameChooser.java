package ConquerSpace.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;

/**
 *
 * @author Zyun
 */
public class NameChooser {
    private File file;
    private JSONObject object;
    private HashMap<String, ArrayList<String>> syllables;
    public NameChooser() {
    }

    public NameChooser(File file) {
        this.file = file;
    }
    
    public NameChooser(String file) {
        this.file = new File(file);
    }
    
    public String getRandomName() {
        return "";
    }
}
