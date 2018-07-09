package ConquerSpace.game.tech;

import ConquerSpace.util.CQSPLogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Zyun
 */
public class Techonologies {

    private static final Logger LOGGER = CQSPLogger.getLogger(Techonologies.class.getName());
    public static ArrayList<Techonology> techonologies = new ArrayList<>();
    public static ArrayList<String> fields = new ArrayList<>();

    public static final int RESEARCHED = 101;

    public static void readTech() {
        File techFolder = new File(System.getProperty("user.dir") + "/assets/tech/techs");
        File[] tempFiles = techFolder.listFiles();
        for (File f : tempFiles) {
            try {
                readTechFromFile(f);
            } catch (IOException ex) {
                LOGGER.warn("IOException", ex);
            } catch (JSONException jsone) {
                LOGGER.warn("JSONException", jsone);
            }
        }
    }

    public static void readTechFromFile(File file) throws FileNotFoundException, IOException, JSONException {
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        String text = new String(data);
        JSONObject root = new JSONObject(text);
        JSONArray techList = root.getJSONArray("techonologies");

        //Iterate over the techonologies
        for (int i = 0; i < techList.length(); i++) {
            JSONObject techonology = techList.getJSONObject(i);
            String name = techonology.getString("name");
            JSONArray deplist = techonology.getJSONArray("deps");
            //Loop over deps
            String[] deps = new String[deplist.length()];
            for (int j = 0; j < deplist.length(); j++) {
                deps[j] = deplist.getString(j);
            }

            //Tech level
            int level = techonology.getInt("level");

            int type = -1;
            switch (techonology.getString("type")) {
                case "UNLOCK":
                    type = TechonologyTypes.UNLOCK;
                    break;
                case "UPGRADE":
                    type = TechonologyTypes.UPGRADE;
                    break;
            }

            JSONArray fieldsArray = techonology.getJSONArray("fields");

            //Loop over fields
            String[] fields = new String[fieldsArray.length()];
            for (int j = 0; j < fieldsArray.length(); j++) {
                fields[j] = fieldsArray.getString(j);
            }

            JSONArray tagsArray = techonology.getJSONArray("tags");

            String[] tags = new String[tagsArray.length()];
            for (int j = 0; j < tagsArray.length(); j++) {
                tags[j] = tagsArray.getString(j);
            }

            //Floor
            int floor = techonology.getInt("floor");

            Techonology t = new Techonology(name, deps, type, level, fields, tags, floor);
            techonologies.add(t);
        }
    }

    public static Techonology getTechByName(String s) {
        return (techonologies.stream().filter(e -> e.getName().toLowerCase().equals(s.toLowerCase()))).findFirst().get();
    }

    public static Techonology[] getTechsByTag(String tag) {
        Object[] techList = techonologies.stream().filter(e -> Arrays.asList(e.getTags()).contains(tag)).toArray();
        return (Arrays.copyOf(techList, techList.length, Techonology[].class));
    }
}
