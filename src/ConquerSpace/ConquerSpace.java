package ConquerSpace;

import ConquerSpace.start.gui.MainMenu;
import ConquerSpace.util.CQSPLogger;
import ConquerSpace.util.Version;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.logging.log4j.Logger;

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
     * Main class.
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
            } catch (IOException ex) {
                LOGGER.warn("Cannot load settings. Using default", ex);
            }
        }
        else {
            try {
                if (!settingsFile.getParentFile().exists()) {
                    settingsFile.getParentFile().mkdir();
                }
                settingsFile.createNewFile();
                //Add default settings
                
            } catch (IOException ex) {
                LOGGER.warn("Unable to create settings file!", ex);
            }
        }

        try {
            //Set look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //Init logger
        } catch (ClassNotFoundException ex) {
            LOGGER.warn("", ex);
        } catch (InstantiationException ex) {
            LOGGER.warn("", ex);
        } catch (IllegalAccessException ex) {
            LOGGER.warn("", ex);        
        } catch (UnsupportedLookAndFeelException ex) {
            LOGGER.warn("", ex);
        }
        
        InitialLoading loading = new InitialLoading();
        loading.setVisible(true);
        loading.run();
        loading.dispose();

        //New Game Menu
        MainMenu menu = new MainMenu();
        menu.setVisible(true);
    }
}
