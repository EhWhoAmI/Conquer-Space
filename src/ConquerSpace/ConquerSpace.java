package ConquerSpace;

import ConquerSpace.gui.start.MainMenu;
import ConquerSpace.i18n.Messages;
import ConquerSpace.util.CQSPLogger;
import ConquerSpace.util.Version;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.logging.log4j.Logger;

/*
  ______   ______   .__   __.   ______      __    __   _______ .______              _______..______      ___       ______  _______ 
 /      | /  __  \  |  \ |  |  /  __  \    |  |  |  | |   ____||   _  \            /       ||   _  \    /   \     /      ||   ____|
|  ,----'|  |  |  | |   \|  | |  |  |  |   |  |  |  | |  |__   |  |_)  |          |   (----`|  |_)  |  /  ^  \   |  ,----'|  |__   
|  |     |  |  |  | |  . `  | |  |  |  |   |  |  |  | |   __|  |      /            \   \    |   ___/  /  /_\  \  |  |     |   __|  
|  `----.|  `--'  | |  |\   | |  `--'  '--.|  `--'  | |  |____ |  |\  \----.   .----)   |   |  |     /  _____  \ |  `----.|  |____ 
 \______| \______/  |__| \__|  \_____\_____\\______/  |_______|| _| `._____|   |_______/    | _|    /__/     \__\ \______||_______|
                                                                                                                                   
*/
/**
 * Conquer Space main class. Where everything starts.
 *
 * @author Zyun
 */
public class ConquerSpace {

    /**
     * Logger for the class.
     */
    private static final Logger LOGGER = CQSPLogger.getLogger(ConquerSpace.class.getName());

    /**
     * Build number for debugging.
     */
    public static int BUILD_NUMBER = 0;

    static {
        Properties buildno = new Properties();
        try {
            buildno.load(new FileInputStream(System.getProperty("user.dir") + "/assets/BUILDNO"));
            BUILD_NUMBER = Integer.parseInt(buildno.getProperty("build.number"));
        } catch (FileNotFoundException ex) {
            LOGGER.info("Asset file not found. No Problem.", ex);
        } catch (IOException ex) {
            LOGGER.info("Io exception. No Problem.", ex);
        }

    }

    /**
     * The version of the game.
     */
    public static final Version VERSION = new Version(0, 0, 0, "dev-b" + BUILD_NUMBER);

    /**
     * Localization.
     */
    public static Messages localeMessages;

    /**
     * Main class.
     *
     * @param args Command line arguments. Does nothing so far.
     */
    public static void main(String[] args) {
        CQSPLogger.initLoggers();
        LOGGER.info("Run started: " + new Date().toString());
        LOGGER.info("Version " + VERSION.toString());

        //Init settings, and read from file if possible
        Globals.settings = new Properties();
        //Check for the existance of the settings file
        File settingsFile = new File(System.getProperty("user.home") + "/.conquerspace/settings.properties");
        if (settingsFile.exists()) {
            try {
                //Read from file.
                FileInputStream fis = new FileInputStream(settingsFile);
                Globals.settings.load(fis);

                //Get settings
                String locale = Globals.settings.getProperty("locale");
                String[] locales = locale.split("-");
                localeMessages = new Messages(new Locale(locales[0], locales[1]));

                //get version
                String version = Globals.settings.getProperty("version");
                if (!(((version.split("-"))[0]).equals(VERSION.toString().split("-")[0]))) {
                    //Then different version, update. How, idk.
                }
                
            } catch (IOException ex) {
                LOGGER.warn("Cannot load settings. Using default", ex);
            }
        } else {
            try {
                if (!settingsFile.getParentFile().exists()) {
                    settingsFile.getParentFile().mkdir();
                }
                settingsFile.createNewFile();
                //Add default settings

                //Default settings
                Globals.settings.setProperty("locale", "en-US");
                localeMessages = new Messages(new Locale("en", "US"));

                //Version
                Globals.settings.setProperty("version", VERSION.toString());
                Globals.settings.setProperty("debug", "no");

                Globals.settings.store(new FileOutputStream(settingsFile), "Created by Conquer Space version " + VERSION.toString());
            } catch (IOException ex) {
                LOGGER.warn("Unable to create settings file!", ex);
            }
        }

        try {
            //Set look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            LOGGER.warn("", ex);
        } catch (InstantiationException ex) {
            LOGGER.warn("", ex);
        } catch (IllegalAccessException ex) {
            LOGGER.warn("", ex);
        } catch (UnsupportedLookAndFeelException ex) {
            LOGGER.warn("", ex);
        }

        // Load all the files.
        InitialLoading loading = new InitialLoading();
        loading.setVisible(true);
        loading.run();
        loading.dispose();

        //New Game Menu
        MainMenu menu = new MainMenu();
        menu.setVisible(true);
    }
}
