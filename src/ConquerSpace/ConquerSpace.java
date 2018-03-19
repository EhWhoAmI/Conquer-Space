package ConquerSpace;

import ConquerSpace.util.CQSPLogger;
import ConquerSpace.util.Version;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
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
     * The version of the game.
     */
    public static final Version VERSION = new Version(0, 0, 0, "dev");

    /**
     * Main class.
     * @param args Command line arguments. Does nothing so far.
    */
    public static void main(String[] args) {
        CQSPLogger.initLoggers();
        
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
