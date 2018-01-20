package ConquerSpace;

import ConquerSpace.util.CQSPLogger;
import ConquerSpace.util.Version;
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
        try {
            //Set look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //Init logger
        } catch (ClassNotFoundException ex) {
            LOGGER.error("", ex);
        } catch (InstantiationException ex) {
            LOGGER.error("", ex);
        } catch (IllegalAccessException ex) {
            LOGGER.error("", ex);        
        } catch (UnsupportedLookAndFeelException ex) {
            LOGGER.error("", ex);
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
