package ConquerSpace.gui.game;

import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.util.CQSPLogger;
import com.alee.extended.layout.VerticalFlowLayout;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
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

    private JLabel threadCountLabel;

    private JButton dumpUniverseButton;

    private JButton runTrashCompactor;

    private JButton openConsole;

    private JButton deviceInfo;
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
        memoryusedLabel = new JLabel("Memory used: " + byteCountToDisplaySize(runtime.totalMemory() - runtime.freeMemory()) + "/" + byteCountToDisplaySize(runtime.totalMemory()) + ". Something like " + (((((float) runtime.totalMemory()) - ((float) runtime.freeMemory()))) / ((float) runtime.totalMemory()) * 100) + "%");
        threadCountLabel = new JLabel("Threads currently running: " + Thread.getAllStackTraces().size());
        deviceInfo = new JButton("Current runtime stats");

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

        deviceInfo.addActionListener((e) -> {
            //Writes the debug stats to a file...
            FileWriter writer = null;
            try {
                //Write the universe to file
                writer = new FileWriter("./runstats.txt");
                PrintWriter pw = new PrintWriter(writer);
                pw.println("This file contains data about your computer. You do not need to ");
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
        add(threadCountLabel);
        add(dumpUniverseButton);
        add(runTrashCompactor);
        add(openConsole);

        pack();

        //Ticker to tick and update the text.
        Timer ticker = new Timer(0, (e) -> {
            Runtime r = Runtime.getRuntime();
            memoryusedLabel.setText("Memory used: " + byteCountToDisplaySize(r.totalMemory() - r.freeMemory()) + "/" + byteCountToDisplaySize(r.totalMemory()) + ". Something like " + (((((float) r.totalMemory()) - ((float) r.freeMemory()))) / ((float) r.totalMemory()) * 100) + "%");
            threadCountLabel.setText("Threads currently running: " + Thread.getAllStackTraces().size());
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

    //The following code is taken from the apache commons io library.
    /**
     * The number of bytes in a kilobyte.
     */
    public static final long ONE_KB = 1024;

    /**
     * The number of bytes in a kilobyte.
     *
     * @since 2.4
     */
    public static final BigInteger ONE_KB_BI = BigInteger.valueOf(ONE_KB);

    /**
     * The number of bytes in a megabyte.
     */
    public static final long ONE_MB = ONE_KB * ONE_KB;

    /**
     * The number of bytes in a megabyte.
     *
     * @since 2.4
     */
    public static final BigInteger ONE_MB_BI = ONE_KB_BI.multiply(ONE_KB_BI);

    /**
     * The number of bytes in a gigabyte.
     */
    public static final long ONE_GB = ONE_KB * ONE_MB;

    /**
     * The number of bytes in a gigabyte.
     *
     * @since 2.4
     */
    public static final BigInteger ONE_GB_BI = ONE_KB_BI.multiply(ONE_MB_BI);

    /**
     * The number of bytes in a terabyte.
     */
    public static final long ONE_TB = ONE_KB * ONE_GB;

    /**
     * The number of bytes in a terabyte.
     *
     * @since 2.4
     */
    public static final BigInteger ONE_TB_BI = ONE_KB_BI.multiply(ONE_GB_BI);

    /**
     * The number of bytes in a petabyte.
     */
    public static final long ONE_PB = ONE_KB * ONE_TB;

    /**
     * The number of bytes in a petabyte.
     *
     * @since 2.4
     */
    public static final BigInteger ONE_PB_BI = ONE_KB_BI.multiply(ONE_TB_BI);

    /**
     * The number of bytes in an exabyte.
     */
    public static final long ONE_EB = ONE_KB * ONE_PB;

    /**
     * The number of bytes in an exabyte.
     *
     * @since 2.4
     */
    public static final BigInteger ONE_EB_BI = ONE_KB_BI.multiply(ONE_PB_BI);

    /**
     * The number of bytes in a zettabyte.
     */
    public static final BigInteger ONE_ZB = BigInteger.valueOf(ONE_KB).multiply(BigInteger.valueOf(ONE_EB));

    /**
     * The number of bytes in a yottabyte.
     */
    public static final BigInteger ONE_YB = ONE_KB_BI.multiply(ONE_ZB);

    public static String byteCountToDisplaySize(final BigInteger size) {
        String displaySize;

        if (size.divide(ONE_EB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_EB_BI)) + " EB";
        } else if (size.divide(ONE_PB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_PB_BI)) + " PB";
        } else if (size.divide(ONE_TB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_TB_BI)) + " TB";
        } else if (size.divide(ONE_GB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_GB_BI)) + " GB";
        } else if (size.divide(ONE_MB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_MB_BI)) + " MB";
        } else if (size.divide(ONE_KB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = String.valueOf(size.divide(ONE_KB_BI)) + " KB";
        } else {
            displaySize = String.valueOf(size) + " bytes";
        }
        return displaySize;
    }

    public static String byteCountToDisplaySize(final long size) {
        return byteCountToDisplaySize(BigInteger.valueOf(size));
    }
}
