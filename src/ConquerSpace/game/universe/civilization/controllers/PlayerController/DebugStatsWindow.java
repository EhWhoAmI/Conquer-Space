package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * Show debug stats, like memory used.
 * @author Zyun
 */
public class DebugStatsWindow extends JFrame {
    /**
     * Instance of this class.
     */
    private static DebugStatsWindow instance;
    
    /**
     * Label to show how much memory is used.
     */
    private JLabel memoryusedLabel;

    /**
     * Creates the window, and adds all things.
     */
    private DebugStatsWindow() {
        setTitle("Stats for Nerds");
        Runtime runtime = Runtime.getRuntime();
        memoryusedLabel = new JLabel("Memory used: " + ((runtime.maxMemory() - runtime.freeMemory()) / 1048576) + " MB/" + (runtime.maxMemory() / 1048576) + " MB. Something like " + (((((float) runtime.maxMemory()) - ((float) runtime.freeMemory()))) / ((float) runtime.maxMemory()) * 100) + "%");
        add(memoryusedLabel);
        pack();
        
        //Ticker to tick and update the text.
        Timer ticker = new Timer(0, (e) -> {
            Runtime r = Runtime.getRuntime();
            memoryusedLabel.setText("Memory used: " + ((r.maxMemory() - r.freeMemory()) / 1048576) + " MB/" + (r.maxMemory() / 1048576) + " MB. Something like " + (((((float) r.maxMemory()) - ((float) r.freeMemory()))) / ((float) r.maxMemory()) * 100) + "%");
            repaint();
        });
        //Every 1 second, update. make it update ever so often, but not so that it
        //will interrupt the game performance.
        //Turn it up on releases
        ticker.setDelay(1000);
        ticker.setRepeats(true);
        ticker.start();
        
        setVisible(true);
    }
    
    /**
     * Get an instance of this class.
     * @return An instance of a debug stats window.
     */
    public static DebugStatsWindow getInstance() {
        if (instance == null)
            instance = new DebugStatsWindow();
        instance.setVisible(true);
        return instance;
    }
}
