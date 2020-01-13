package ConquerSpace.util;

import java.io.File;

/**
 *
 * @author zyunl
 */
public class Utilities {

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            //some JVMs return null for empty dirs
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
    }
    
}
