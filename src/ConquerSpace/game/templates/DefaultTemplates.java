package ConquerSpace.game.templates;

import ConquerSpace.util.CQSPLogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Zyun
 */
public class DefaultTemplates {

    private static final Logger LOGGER = CQSPLogger.getLogger(DefaultTemplates.class.getName());

    public static Template[] createDefaultTemplates() {
        //Load from template folder
        File templateFolder = new File(System.getProperty("user.dir") + "/assets/templates/");
        File[] files = templateFolder.listFiles();
        ArrayList<Template> templates = new ArrayList<>();
        for (File file : files) {
            try {
                Template[] temp = loadTemplates(file.getAbsolutePath());
                templates.addAll(Arrays.asList(temp));
            } catch (IOException ex) {
                LOGGER.warn("Unable to process file " + file + ".", ex);
            } catch (ClassNotFoundException ex) {
                LOGGER.warn("Unable to process file " + file + ".", ex);
            } catch (JSONException ex) {
                LOGGER.warn("Unable to process file " + file + ".", ex);
            }
        }
        return (templates.toArray(new Template[templates.size()]));
    }

    private static Template[] loadTemplates(String filename) throws FileNotFoundException, IOException, ClassNotFoundException, JSONException {
        Template[] templates;
        File file = new File(filename);
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        String text = new String(data);
        JSONObject root = new JSONObject(text);
        JSONArray templateList = root.getJSONArray("templates");
        templates = new Template[templateList.length()];
        for (int i = 0; i < templateList.length(); i++) {
            JSONObject temp = templateList.getJSONObject(i);
            String name = temp.getString("name");
            String obj_class = temp.getString("class");

            //Param classes
            JSONArray params = temp.getJSONArray("params");
            String[] types = new String[params.length()];
            for (int n = 0; n < params.length(); n++) {
                types[n] = params.getString(n);
            }

            //args
            Object[] args;
            JSONArray argsArray = temp.getJSONArray("args");
            args = new Object[argsArray.length()];

            for (int n = 0; n < argsArray.length(); n++) {
                switch (types[n]) {
                    //Int
                    case "i":
                        args[n] = argsArray.getInt(n);
                        break;
                    //Long
                    case "l":
                        args[n] = argsArray.getLong(n);
                        break;
                    //Byte
                    case "b":
                        args[n] = (Integer.valueOf(argsArray.getInt(n)).byteValue());
                        break;
                    //Bool
                    case "bool":
                        args[n] = argsArray.getBoolean(n);
                        break;
                    //String
                    case "s":
                        args[n] = argsArray.getString(n);
                        break;
                    //Double
                    case "d":
                        args[n] = argsArray.getDouble(n);
                        break;
                    //Float
                    case "f":
                        args[n] = argsArray.getFloat(n);
                        break;
                    //Object
                    case "o":
                        args[n] = argsArray.get(n);
                        break;
                    //Or else, just get it as object
                    default:
                        args[n] = argsArray.get(n);
                }
            }
            Template template = new Template(obj_class, args, name);
            templates[i] = template;
        }
        return templates;
    }
}
