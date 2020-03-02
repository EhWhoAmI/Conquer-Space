/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
