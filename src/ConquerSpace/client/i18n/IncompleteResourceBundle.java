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
package ConquerSpace.client.i18n;

import ConquerSpace.ConquerSpace;
import ConquerSpace.common.util.logging.CQSPLogger;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.ResourceBundle;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author EhWhoAmI
 */
public class IncompleteResourceBundle extends ResourceBundle {

    private static final Logger LOGGER = CQSPLogger.getLogger(IncompleteResourceBundle.class.getName());

    private HashMap<String, String> prop;
    private ResourceBundle defaultProperties;

    @SuppressWarnings("unchecked")
    public IncompleteResourceBundle(InputStream is) throws IOException {
        Properties properties = new Properties();
        properties.load(is);
        prop = new HashMap(properties);

        defaultProperties = ResourceBundle.getBundle("assets.i18n." + ConquerSpace.DEFAULT_LOCALE);
    }

    @Override
    protected Object handleGetObject(String key) {
        String content = "";
        if (prop.containsKey(key)) {
            content = (prop.get(key));
            if (ConquerSpace.TRANSLATE_TEST) {
                content = "Translate OK:(" + content + ")";
            }
        } else if (defaultProperties.containsKey(key)) {
            content = defaultProperties.getString(key);
            if (ConquerSpace.TRANSLATE_TEST) {
                LOGGER.warn("TRANSLATE MISSING: " + content);
                content = "Translate Missing:(" + content + ")";
            }
        } else {
            content = key;
            if (ConquerSpace.TRANSLATE_TEST) {
                LOGGER.warn("NO TRANSLATE: " + content);
                content = "Translate Non-existent:(" + content + ")";
            }
        }
        return content;
    }

    public TranslateStatus getTranslateStatus(String key) {
        if (prop.containsKey(key)) {
            return TranslateStatus.EXISTS;
        } else if (defaultProperties.containsKey(key)) {
            return (TranslateStatus.MISSING);
        }
        return TranslateStatus.NON_EXISTENT;
    }

    @Override
    public Enumeration<String> getKeys() {
        return Collections.enumeration(prop.keySet());
    }

    public static enum TranslateStatus {
        EXISTS, //Exists
        MISSING, //Missing from current properties
        NON_EXISTENT; //Does not exist anywhere
    }
}
