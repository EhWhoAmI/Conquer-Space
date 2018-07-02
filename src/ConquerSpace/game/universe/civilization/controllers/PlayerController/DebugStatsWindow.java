package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.util.CQSPLogger;
import com.alee.extended.layout.VerticalFlowLayout;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import org.apache.logging.log4j.Logger;

/**
 * Show debug stats, like memory used.
 *
 * @author Zyun
 */
public class DebugStatsWindow extends JInternalFrame {

    private static final Logger LOGGER = CQSPLogger.getLogger(DebugStatsWindow.class.getName());

    /**
     * Instance of this class.
     */
    private static DebugStatsWindow instance;

    /**
     * Label to show how much memory is used.
     */
    private JLabel memoryusedLabel;

    private JButton dumpUniverseButton;

    /**
     * Universe
     */
    private Universe universe;

    /**
     * Creates the window, and adds all things.
     */
    private DebugStatsWindow(Universe u) {
        universe = u;

        setTitle("Stats for Nerds");
        setLayout(new VerticalFlowLayout());
        Runtime runtime = Runtime.getRuntime();
        memoryusedLabel = new JLabel("Memory used: " + ((runtime.maxMemory() - runtime.freeMemory()) / 1048576) + " MB/" + (runtime.maxMemory() / 1048576) + " MB. Something like " + (((((float) runtime.maxMemory()) - ((float) runtime.freeMemory()))) / ((float) runtime.maxMemory()) * 100) + "%");

        dumpUniverseButton = new JButton("Dump universe");

        dumpUniverseButton.addActionListener((e) -> {
            FileWriter writer = null;
            try {
                //Write the universe to file
                writer = new FileWriter("./universe.dump");
                PrintWriter pw = new PrintWriter(writer);
                pw.print(universe.toReadableString().replace("\n", System.getProperty("line.separator")));
            } catch (IOException ex) {
                LOGGER.error("Cannot write to file!", ex);
            } finally {
                try {
                    writer.close();
                } catch (IOException ex) {
                    LOGGER.error("Cannot close file!", ex);
                }
            }
        });

        add(memoryusedLabel);
        add(dumpUniverseButton);
        
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
        setResizable(true);

        setVisible(true);
    }

    /**
     * Get an instance of this class.
     *
     * @param u Universe
     * @return An instance of a debug stats window.
     */
    public static DebugStatsWindow getInstance(Universe u) {
        if (instance == null) {
            instance = new DebugStatsWindow(u);
        }
        instance.setVisible(true);
        return instance;
    }
}
