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
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 *
 * @author EhWhoAmI
 */
public class IncompleteResourceBundle extends ResourceBundle {

    private HashMap<String, String> prop;
    private ResourceBundle defaultProperties;

    @SuppressWarnings("unchecked")
    public IncompleteResourceBundle(InputStream is) throws IOException {
        Properties properties = new Properties();
        properties.load(is);
        prop = new HashMap(properties);
        defaultProperties = ResourceBundle.getBundle("assets.i18n.ApplicationMessages", ConquerSpace.DEFAULT_LOCALE);
    }

    @Override
    protected Object handleGetObject(String key) {
        if (prop.containsKey(key)) {
            String content = (prop.get(key));
            if (ConquerSpace.TRANSLATE_TEST) {
                return "Translate OK:(" + content + ")";
            }
            return content;
        } else if (defaultProperties.containsKey(key)) {
            String content = defaultProperties.getString(key);
            if (ConquerSpace.TRANSLATE_TEST) {
                return "Translate Missing:(" + content + ")";
            }
            return content;
        } else {
            String content = key;
            if (ConquerSpace.TRANSLATE_TEST) {
                return "Translate Non-existent:(" + content + ")";
            }
            return content;
        }
    }

    @Override
    public Enumeration<String> getKeys() {
        return Collections.enumeration(prop.keySet());
    }
}
