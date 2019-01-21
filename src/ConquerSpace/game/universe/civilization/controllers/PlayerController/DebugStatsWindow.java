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
import org.apache.commons.io.FileUtils;
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

    private JButton runTrashCompactor;
    
    private JButton openConsole;
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
        setLayout(new VerticalFlowLayout(5, 4));
        Runtime runtime = Runtime.getRuntime();
        memoryusedLabel = new JLabel("Memory used: " + FileUtils.byteCountToDisplaySize(runtime.totalMemory() - runtime.freeMemory()) + "/" + FileUtils.byteCountToDisplaySize(runtime.totalMemory()) + ". Something like " + (((((float) runtime.totalMemory()) - ((float) runtime.freeMemory()))) / ((float) runtime.totalMemory()) * 100) + "%");

        dumpUniverseButton = new JButton("Dump universe");
        dumpUniverseButton.setFocusable(false);
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

        runTrashCompactor = new JButton("Force Garbage Collection");
        runTrashCompactor.setFocusable(false);
        runTrashCompactor.addActionListener((e) -> {
            System.gc();
        });
        
        openConsole = new JButton("Open Console");
        openConsole.setFocusable(false);
        openConsole.addActionListener((e) -> {
            CQSPConsole con = new CQSPConsole(u, u.getCivilization(0));
            getDesktopPane().add(con);
        });
        add(memoryusedLabel);
        add(dumpUniverseButton);
        add(runTrashCompactor);
        add(openConsole);
        
        pack();

        //Ticker to tick and update the text.
        Timer ticker = new Timer(0, (e) -> {
            Runtime r = Runtime.getRuntime();
            memoryusedLabel.setText("Memory used: " + FileUtils.byteCountToDisplaySize(r.totalMemory() - r.freeMemory()) + "/" + FileUtils.byteCountToDisplaySize(r.totalMemory()) + ". Something like " + (((((float) r.totalMemory()) - ((float) r.freeMemory()))) / ((float) r.totalMemory()) * 100) + "%");
            repaint();
        });
        //Every 1 second, update. make it update ever so often, but not so that it
        //will interrupt the game performance.
        //Turn it up on releases
        ticker.setDelay(1000);
        ticker.setRepeats(true);
        ticker.start();
        setResizable(true);
        setClosable(true);
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
