package ConquerSpace;

import java.util.logging.Level;
import java.util.logging.Logger;

import ConquerSpace.util.CQSPLogger;
import ConquerSpace.util.Version;
/**
 *
 * @author Zyun
 */
public class ConquerSpace {
	private static final Logger LOGGER = CQSPLogger.getLogger(ConquerSpace.class.getName());
        public static final Version VERSION = new Version(0, 0, 0, "dev");
    public static void main(String[] args) {
        InitialLoading loading = new InitialLoading(() -> {
            try {
                //Load
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ConquerSpace.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        loading.setVisible(true);
        loading.run();
        loading.dispose();
        
        //New Game Menu
        MainMenu menu = new MainMenu();
        menu.setVisible(true);
    }
}
