package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author Zyun
 */
public class DebugStats extends JFrame {
    private static DebugStats instance;
    private JLabel memoryusedLabel;

    private DebugStats() {
        setTitle("Stats for Nerds");
        Runtime runtime = Runtime.getRuntime();
        memoryusedLabel = new JLabel("Memory used: " + ((runtime.maxMemory() - runtime.freeMemory()) / 1048576) + " MB/" + (runtime.maxMemory() / 1048576) + " MB. Something like " + (((((float) runtime.maxMemory()) - ((float) runtime.freeMemory()))) / ((float) runtime.maxMemory()) * 100) + "%");
        add(memoryusedLabel);
        pack();
        Timer ticker = new Timer(0, (e) -> {
            Runtime r = Runtime.getRuntime();
            memoryusedLabel.setText("Memory used: " + ((r.maxMemory() - r.freeMemory()) / 1048576) + " MB/" + (r.maxMemory() / 1048576) + " MB. Something like " + (((((float) r.maxMemory()) - ((float) r.freeMemory()))) / ((float) r.maxMemory()) * 100) + "%");
            repaint();
        });
        //Every 1 second... make it update ever so often, but not so that it
        //will interrupt the game performance.
        //Turn it up on releases
        ticker.setDelay(1000);
        ticker.setRepeats(true);
        ticker.start();
        
        setVisible(true);
    }
    public static DebugStats getInstance() {
        if (instance == null)
            instance = new DebugStats();
        instance.setVisible(true);
        return instance;
    }
}
