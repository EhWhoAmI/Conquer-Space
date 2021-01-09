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

import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.common.GameState;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.util.Utilities;
import ConquerSpace.common.util.logging.CQSPLogger;
import ConquerSpace.server.GameController;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import org.apache.logging.log4j.Logger;
import swinglogger.SwingMessageAppender;

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

    private JLabel tickTimeLabel;

    private JLabel objectCountLabel;

    private JButton runTrashCompactor;

    private JButton openConsole;

    private JButton deviceInfo;

    private JButton logger;

    private JButton throwException;

    Runtime runtime;

    GameState gameState;

    /**
     * Creates the window, and adds all things.
     */
    private DebugStatsWindow(GameState state) {
        gameState = state;
        runtime = Runtime.getRuntime();

        setTitle(LOCALE_MESSAGES.getMessage("game.debug.title"));
        setLayout(new VerticalFlowLayout(5, 4));

        memoryusedLabel = new JLabel(getMemoryString());
        threadCountLabel = new JLabel(getThreadString());
        tickTimeLabel = new JLabel(getTickTimeString());
        objectCountLabel = new JLabel(getGameObjectString());
        deviceInfo = new JButton(LOCALE_MESSAGES.getMessage("game.debug.runtimestats"));
        logger = new JButton(LOCALE_MESSAGES.getMessage("game.debug.logs.show"));

        runTrashCompactor = new JButton(LOCALE_MESSAGES.getMessage("game.debug.gc"));
        runTrashCompactor.setFocusable(false);
        runTrashCompactor.addActionListener((e) -> {
            System.gc();
        });

        openConsole = new JButton(LOCALE_MESSAGES.getMessage("game.debug.console"));
        openConsole.setFocusable(false);
        openConsole.addActionListener((e) -> {
            CQSPConsole con
                    = new CQSPConsole(state, state.getObject(state.playerCiv, Civilization.class));
            getDesktopPane().add(con);
        });

        deviceInfo.addActionListener((e) -> {
            //Writes the debug stats to a file...
            FileWriter writer = null;
            try {
                //Write the universe to file
                writer = new FileWriter("./runstats.txt");
                PrintWriter pw = new PrintWriter(writer);
                pw.println("This file contains data about your computer.");
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
                JOptionPane.showMessageDialog(this, LOCALE_MESSAGES.getMessage("game.debug.logs.error.title"),
                        LOCALE_MESSAGES.getMessage("game.debug.logs.error.body"), JOptionPane.ERROR_MESSAGE);
            }
        });
        logger.setEnabled(false);

        throwException = new JButton(LOCALE_MESSAGES.getMessage("game.debug.fun"));
        throwException.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent l) {
                throw new ConquerSpaceExceptionRouletteExceptionThatDoesntReallyDoAnything("oof");
            }
        });
        add(memoryusedLabel);
        add(threadCountLabel);
        add(tickTimeLabel);
        add(objectCountLabel);
        add(runTrashCompactor);
        add(openConsole);
        add(logger);
        add(throwException);

        pack();

        //Ticker to tick and update the text.
        Timer ticker = new Timer(0, (e) -> {
            memoryusedLabel.setText(getMemoryString());
            threadCountLabel.setText(getThreadString());
            tickTimeLabel.setText(getTickTimeString());
            objectCountLabel.setText(getGameObjectString());
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

    private String getMemoryString() {
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        return LOCALE_MESSAGES.getMessage("game.debug.memoryused",
                Utilities.byteCountToDisplaySize(totalMemory - freeMemory),
                Utilities.byteCountToDisplaySize(totalMemory),
                calculatePercentage((double) totalMemory, (double) freeMemory));
    }

    public double calculatePercentage(double total, double part) {
        return (double) Math.round((total - part) / total * 10000) / 100d;
    }

    private String getThreadString() {
        return LOCALE_MESSAGES.getMessage("game.debug.threads", Thread.getAllStackTraces().size());
    }

    private String getTickTimeString() {
        return LOCALE_MESSAGES.getMessage("game.debug.ticktime", GameController.updateTime);
    }

    private String getGameObjectString() {
        return LOCALE_MESSAGES.getMessage("game.debug.objectCount", gameState.getObjectCount());
    }

    /**
     * Get an instance of this class.
     *
     * @param state game state
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
    //Runtime exception so that it doesn't have to be caught
    public static class ConquerSpaceExceptionRouletteExceptionThatDoesntReallyDoAnything
            extends RuntimeException {

        public ConquerSpaceExceptionRouletteExceptionThatDoesntReallyDoAnything() {
            super();
        }

        public ConquerSpaceExceptionRouletteExceptionThatDoesntReallyDoAnything(String text) {
            super(text);
        }
    }
}
