/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ConquerSpace.client.gui.game;

import ConquerSpace.common.GameState;
import ConquerSpace.common.game.universe.bodies.Galaxy;
import ConquerSpace.common.util.logging.CQSPLogger;
import ConquerSpace.client.gui.logging.SwingMessageAppender;
import ConquerSpace.common.game.organizations.civilization.Civilization;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import org.apache.logging.log4j.Logger;

/**
 * Show debug stats, like memory used.
 *
 * @author EhWhoAmI
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

    private JButton runTrashCompactor;

    private JButton openConsole;

    private JButton deviceInfo;

    private JButton logger;

    private JButton throwException;

//    private TimeSeriesCollection memoryInfoStats;
//    private TimeSeries usedMemorySeries;
//    private TimeSeries availableMemorySeries;
//    private JFreeChart chart;
    /**
     * Universe
     */
    private Galaxy universe;

    /**
     * Creates the window, and adds all things.
     */
    private DebugStatsWindow(GameState state) {
        universe = state.universe;

        setTitle("Stats for Nerds");
        setLayout(new VerticalFlowLayout(5, 4));
        Runtime runtime = Runtime.getRuntime();
        memoryusedLabel = new JLabel("Memory used: " + byteCountToDisplaySize(runtime.totalMemory() - runtime.freeMemory()) + "/" + byteCountToDisplaySize(runtime.totalMemory()) + ". Something like " + (((((float) runtime.totalMemory()) - ((float) runtime.freeMemory()))) / ((float) runtime.totalMemory()) * 100) + "%");
        threadCountLabel = new JLabel("Threads currently running: " + Thread.getAllStackTraces().size());
        deviceInfo = new JButton("Current runtime stats");
        logger = new JButton("Show Logs");

        

        runTrashCompactor = new JButton("Force Garbage Collection");
        runTrashCompactor.setFocusable(false);
        runTrashCompactor.addActionListener((e) -> {
            System.gc();
        });

        openConsole = new JButton("Open Console");
        openConsole.setFocusable(false);
        openConsole.addActionListener((e) -> {
            CQSPConsole con = new CQSPConsole(state, state.getObject(state.playerCiv, Civilization.class));
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
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException ex) {
                    LOGGER.error("Cannot close file!", ex);
                }
            }
        });

        logger.addActionListener(l -> {
            if (SwingMessageAppender.panel != null) {
                JInternalFrame frame = new JInternalFrame();
                frame.add(SwingMessageAppender.panel);
                frame.pack();
                frame.setResizable(true);
                frame.setClosable(true);
                frame.setVisible(true);

                getDesktopPane().add(frame);
            } else {
                JOptionPane.showMessageDialog(this, "We had an issue opening the log panel", "Could not open log panel", JOptionPane.ERROR_MESSAGE);
            }
        });

        throwException = new JButton("Exception Roulette");
        throwException.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent l) {
                throw new ConquerSpaceExceptionRouletteExceptionThatDoesntReallyDoAnything("No u. Seriously, though, you can just press don\'t exit, and nothing will happen.");
            }
        });
//        memoryInfoStats = new TimeSeriesCollection();
//
//        usedMemorySeries = new TimeSeries("Used Memory");
//        availableMemorySeries = new TimeSeries("Available Memory");
//        usedMemorySeries.setMaximumItemAge(1000 * 120);
//        availableMemorySeries.setMaximumItemAge(1000 * 120);
//
//        memoryInfoStats.addSeries(usedMemorySeries);
//        memoryInfoStats.addSeries(availableMemorySeries);
//
//        chart = ChartFactory.createTimeSeriesChart("Memory Usage over time", "", "MB", memoryInfoStats, true, true, false);
//        ChartPanel panel = new ChartPanel(chart);
//        panel.setDomainZoomable(false);
//        panel.setPopupMenu(null);
//        panel.setRangeZoomable(false);
        add(memoryusedLabel);
        add(threadCountLabel);
        add(runTrashCompactor);
        add(openConsole);
        add(logger);
        add(throwException);

        pack();

        //Ticker to tick and update the text.
        Timer ticker = new Timer(0, (e) -> {
            Runtime r = Runtime.getRuntime();
            memoryusedLabel.setText("Memory used: " + byteCountToDisplaySize(r.totalMemory() - r.freeMemory()) + "/" + byteCountToDisplaySize(r.totalMemory()) + ". Something like " + (((((float) r.totalMemory()) - ((float) r.freeMemory()))) / ((float) r.totalMemory()) * 100) + "%");
//            Millisecond time = new Millisecond();
//            usedMemorySeries.add(time, byteCountToDisplaySizeNumber(BigInteger.valueOf(r.totalMemory() - r.freeMemory())));
//            availableMemorySeries.add(time, byteCountToDisplaySizeNumber(BigInteger.valueOf(r.totalMemory())));
//
//            usedMemorySeries.removeAgedItems(true);
//            availableMemorySeries.removeAgedItems(true);
//            chart.fireChartChanged();
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
    public static DebugStatsWindow getInstance(GameState state) {
        if (instance == null) {
            instance = new DebugStatsWindow(state);
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

    public static double byteCountToDisplaySizeNumber(final BigInteger size) {
        double displaySize;

        if (size.divide(ONE_EB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = size.divide(ONE_EB_BI).doubleValue();
        } else if (size.divide(ONE_PB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = size.divide(ONE_PB_BI).doubleValue();
        } else if (size.divide(ONE_TB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = size.divide(ONE_TB_BI).doubleValue();
        } else if (size.divide(ONE_GB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = size.divide(ONE_GB_BI).doubleValue();
        } else if (size.divide(ONE_MB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = size.divide(ONE_MB_BI).doubleValue();
        } else if (size.divide(ONE_KB_BI).compareTo(BigInteger.ZERO) > 0) {
            displaySize = size.divide(ONE_KB_BI).doubleValue();
        } else {
            displaySize = size.doubleValue();
        }
        return displaySize;
    }

    public static String byteCountToDisplaySize(final long size) {
        return byteCountToDisplaySize(BigInteger.valueOf(size));
    }

    //Runtime exception so that it doesn't have to be caught
    public static class ConquerSpaceExceptionRouletteExceptionThatDoesntReallyDoAnything extends RuntimeException {

        public ConquerSpaceExceptionRouletteExceptionThatDoesntReallyDoAnything() {
            super();
        }

        public ConquerSpaceExceptionRouletteExceptionThatDoesntReallyDoAnything(String text) {
            super(text);
        }
    }
}
