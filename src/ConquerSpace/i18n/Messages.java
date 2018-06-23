package ConquerSpace.i18n;

import ConquerSpace.util.CQSPLogger;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Zyun
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
        boolean found = false;
        for (Locale loc : SUPPORTED_LOCALES) {
            if (loc.equals(SUPPORTED_LOCALES)) {
                try {
                    File resourcesFolder = new File(System.getProperty("user.dir") + "/assets/i18n/");
                    URL[] urls = {resourcesFolder.toURI().toURL()};
                    ClassLoader loader = new URLClassLoader(urls);
                    bundle = ResourceBundle.getBundle(("ApplicationMessages").replace("/", "."), l, loader);
                } catch (MalformedURLException ex) {
                    LOGGER.error(ex);
                }
                found = true;
                break;
            }
        }
        if(!found) {
            try {
                    File resourcesFolder = new File(System.getProperty("user.dir") + "/assets/i18n/");
                    URL[] urls = {resourcesFolder.toURI().toURL()};
                    ClassLoader loader = new URLClassLoader(urls);
                    bundle = ResourceBundle.getBundle(("ApplicationMessages").replace("/", "."), new Locale("en", "us"), loader);
                } catch (MalformedURLException ex) {
                    LOGGER.error(ex);
                }
        }
    }

    public String getMessage(String s) {
        return (bundle.getString(s));
    }
}
