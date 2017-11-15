package ConquerSpace;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Zyun
 */
public class ConquerSpace {
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
