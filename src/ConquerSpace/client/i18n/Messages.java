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
import ConquerSpace.client.i18n.IncompleteResourceBundle.TranslateStatus;
import ConquerSpace.common.util.ExceptionHandling;
import ConquerSpace.common.util.logging.CQSPLogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.IllegalFormatConversionException;
import java.util.Locale;
import java.util.UnknownFormatConversionException;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author EhWhoAmI
 */
public class Messages {

    private static final Logger LOGGER = CQSPLogger.getLogger(Messages.class.getName());
    private IncompleteResourceBundle bundle;

    private static final Locale[] SUPPORTED_LOCALES = {
        new Locale("en", "us")
    };

    public Messages(Locale l) {
        //Load resource file
        LOGGER.warn("Loading locale " + l.toString());

        File resourcesFolder = new File(ConquerSpace.USER_DIR + "/assets/i18n/" + l.toString() + ".properties");
        try {
            FileInputStream is = new FileInputStream(resourcesFolder);
            bundle = new IncompleteResourceBundle(is);
            return;
        } catch (FileNotFoundException ex) {
            LOGGER.error(ex);
        } catch (IOException ex) {
            LOGGER.error(ex);
        }

        LOGGER.warn("Can't find locale " + l.toString());

        loadDefaultbundle();
    }

    private void loadDefaultbundle() {
        try {
            LOGGER.info("Loading default bundle " + ConquerSpace.DEFAULT_LOCALE);
            //Default bundle is in assets, so we don't meet any issues, unless they mess with the jar itself
            InputStream in = getClass().getResourceAsStream("/assets/i18n/" + ConquerSpace.DEFAULT_LOCALE.toString() + ".properties");

            bundle = new IncompleteResourceBundle(in);
        } catch (Exception ex) {
            LOGGER.error(ex);
            ExceptionHandling.FatalExceptionMessageBox("Failed to load backup application messages!", ex);
        }
    }

    public String getMessage(String key) {
        if (ConquerSpace.TRANSLATE_TEST && bundle.getTranslateStatus(key) != TranslateStatus.EXISTS) {
            //REPORT
            LOGGER.warn("Translate " + key + " does not exist", new Exception("Stack trace"));
            
        }
        String content = bundle.getString(key);
        return (content);
    }

    public String getMessage(String key, Object... objs) {
        String content = getMessage(key);
        try {
            content = MessageFormat.format(content, objs);
        } catch (IllegalFormatConversionException ifce) {
            //Fail silently when there is an incorrect format
            LOGGER.warn("Problem with formatting " + key + " " + ifce.getMessage(), ifce);
        } catch (UnknownFormatConversionException ufce) {
            LOGGER.warn("Problem with formatting " + key + " " + ufce.getMessage() + " format " + ufce.getConversion(), ufce);
        }
        return (content);
    }
}
