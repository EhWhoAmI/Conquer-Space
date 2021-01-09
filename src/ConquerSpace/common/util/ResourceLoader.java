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
package ConquerSpace.common.util;

import ConquerSpace.ConquerSpace;
import ConquerSpace.common.util.logging.CQSPLogger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.swing.ImageIcon;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author EhWhoAmI
 */
public class ResourceLoader {

    private static final Logger LOGGER = CQSPLogger.getLogger(ResourceLoader.class.getName());

    static final Properties prop = new Properties();

    static {
        try {
            String resource = "/assets/assets.properties";
            InputStream in = ResourceLoader.class.getResourceAsStream(resource);
            if (in == null) {
                in = ResourceLoader.class.getClassLoader().getResourceAsStream(resource);
            }
            prop.load(in);
        } catch (FileNotFoundException ex) {
            //Fatal, can't live without it...
            LOGGER.error(ex);
            ExceptionHandling.fatalExceptionMessageBox("Failed to load asset properties!", ex);
        } catch (IOException ex) {
            LOGGER.error(ex);
            ExceptionHandling.fatalExceptionMessageBox("Failed to load asset properties!", ex);
        }
    }

    public static String loadResourceString(String key) {
        return (ConquerSpace.USER_DIR + "/assets/" + prop.getProperty(key));
    }

    public static FileReader silentLoadResourceReader(String key) {
        try {
            return (new FileReader(loadResourceString(key)));
        } catch (Exception e) {
        }
        return null;
    }

    public static ImageIcon getIcon(String key) {
        return new ImageIcon(loadResourceString(key));
    }

    public static File getResourceByFile(String key) {
        return (new File(loadResourceString(key)));
    }
}
