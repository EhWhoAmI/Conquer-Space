package ConquerSpace;

import ConquerSpace.util.CQSPLogger;
import ConquerSpace.util.Version;
import com.alee.laf.WebLookAndFeel;
import java.util.logging.Logger;

/**
 *
 * @author Zyun
 */
public class ConquerSpace {

    private static final Logger LOGGER = CQSPLogger.getLogger(ConquerSpace.class.getName());
    public static final Version VERSION = new Version(0, 0, 0, "dev");

    public static void main(String[] args) {
        //Set look and feel
        WebLookAndFeel.install();
        InitialLoading loading = new InitialLoading();
        loading.setVisible(true);
        loading.run();
        loading.dispose();

        //New Game Menu
        MainMenu menu = new MainMenu();
        menu.setVisible(true);
    }
}
