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
package ConquerSpace.i18n;

import ConquerSpace.util.logging.CQSPLogger;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author EhWhoAmI
 */
public class Messages {

    private static final Logger LOGGER = CQSPLogger.getLogger(Messages.class.getName());
    private ResourceBundle bundle;

    private static final Locale[] SUPPORTED_LOCALES = {
        new Locale("en", "us")
    };

    public Messages() {
        try {
            File resourcesFolder = new File(System.getProperty("user.dir") + "/assets/i18n/");
            URL[] urls = {resourcesFolder.toURI().toURL()};
            ClassLoader loader = new URLClassLoader(urls);
            bundle = ResourceBundle.getBundle(("ApplicationMessages").replace("/", "."), Locale.getDefault(), loader);
        } catch (MalformedURLException ex) {
            LOGGER.error(ex);
        }
    }

    public Messages(Locale l) {
        for (Locale loc : SUPPORTED_LOCALES) {
            if (loc.equals(l)) {
                try {
                    File resourcesFolder = new File(System.getProperty("user.dir") + "/assets/i18n/");
                    URL[] urls = {resourcesFolder.toURI().toURL()};
                    ClassLoader loader = new URLClassLoader(urls);
                    bundle = ResourceBundle.getBundle(("ApplicationMessages").replace("/", "."), l, loader);
                } catch (MalformedURLException ex) {
                    LOGGER.error(ex);
                }
                return;
            }
        }

        LOGGER.warn("Can't find locale!");
        try {
            File resourcesFolder = new File(System.getProperty("user.dir") + "/assets/i18n/");
            URL[] urls = {resourcesFolder.toURI().toURL()};
            ClassLoader loader = new URLClassLoader(urls);
            bundle = ResourceBundle.getBundle(("ApplicationMessages").replace("/", "."), new Locale("en", "us"), loader);
        } catch (MalformedURLException ex) {
            LOGGER.error(ex);
        }

    }

    public String getMessage(String s) {
        return (bundle.getString(s));
    }
}
