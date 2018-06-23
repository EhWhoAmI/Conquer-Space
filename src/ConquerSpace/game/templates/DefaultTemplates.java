package ConquerSpace.game.templates;

import ConquerSpace.util.ExceptionHandling;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Zyun
 */
public class DefaultTemplates {

    public static Template[] createDefaultTemplates() {
        try {
            Template[] templates;
            File file = new File(System.getProperty("user.dir") + "/assets/templates/default.json");
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();
            String text = new String(data);
            JSONObject root = new JSONObject(text);
            JSONArray templateList = root.getJSONArray("templates");
            templates = new Template[templateList.length()];
            for (int i = 0; i < templateList.length(); i++) {
                try {
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
                } catch (ClassNotFoundException ex) {
                    ExceptionHandling.ExceptionMessageBox(ex.getMessage(), ex);
                }
            }
            return templates;
        } catch (IOException ex) {
            ExceptionHandling.ExceptionMessageBox(ex.getMessage(), ex);
        } catch (JSONException jsone) {
            ExceptionHandling.ExceptionMessageBox(jsone.getMessage(), jsone);
        }
        return null;
    }
}
