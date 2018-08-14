package ConquerSpace.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author Zyun
 */
public class ResourceLoader {
    static Properties prop = new Properties();
    static {
        try {
            prop.load(new FileInputStream(System.getProperty("user.dir") + "/assets/assets.properties"));
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }
    
    public static String loadResource(String key) {
        return (System.getProperty("user.dir") + "/assets/" + prop.getProperty(key));
    }
    
    public static FileReader silentLoadResourceReader(String key) {
        try {
            return (new FileReader(loadResource(key)));
        } catch (Exception e) {
        }
        return null;
    }
    
    public static File getResourceByFile(String key) {
        return (new File(loadResource(key)));
    }
}
