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
import ConquerSpace.server.GameController;
import ConquerSpace.common.GameSpeeds;
import ConquerSpace.common.GameState;
import ConquerSpace.common.StarDate;
import ConquerSpace.common.save.SaveGame;
import ConquerSpace.common.game.universe.bodies.Galaxy;
import ConquerSpace.common.util.ExceptionHandling;
import ConquerSpace.common.util.logging.CQSPLogger;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author EhWhoAmI
 */
public class TurnSaveWindow extends JInternalFrame implements ActionListener {

    private static final Logger LOGGER = CQSPLogger.getLogger(TurnSaveWindow.class.getName());
    private JLabel turnLabel;
    //Public for the action listeners later on
    public JButton pausePlayButton;
    private JComboBox<String> speedComboBox;
    private JButton alertsButton;
    private JButton saveGameButton;
    private JButton exitGameButton;
    private JButton manualButton;
    private JButton runningstatsButton;
    private JProgressBar statusProgressBar;
    private JButton optionsWindowButton;

    private String[] speeds = {
        LOCALE_MESSAGES.getMessage("game.speed.slowest"),
        LOCALE_MESSAGES.getMessage("game.speed.slower"),
        LOCALE_MESSAGES.getMessage("game.speed.slow"),
        LOCALE_MESSAGES.getMessage("game.speed.normal"),
        LOCALE_MESSAGES.getMessage("game.speed.fast"),
        LOCALE_MESSAGES.getMessage("game.speed.faster"),
        LOCALE_MESSAGES.getMessage("game.speed.fastest")
    };
    private boolean isPaused = true;
    private StarDate date;
    private Galaxy universe;

    public TurnSaveWindow(GameState state) {
        universe = state.universe;
        this.date = state.date;
        JPanel pan = new JPanel();

        //Init components
        turnLabel = new JLabel();
        statusProgressBar = new JProgressBar();
        pausePlayButton = new JButton(LOCALE_MESSAGES.getMessage("game.already.paused"));
        speedComboBox = new JComboBox<>(speeds);
        alertsButton = new JButton(LOCALE_MESSAGES.getMessage("game.alerts"));
        manualButton = new JButton(LOCALE_MESSAGES.getMessage("manual.title"));
        saveGameButton = new JButton(LOCALE_MESSAGES.getMessage("game.save"));
        exitGameButton = new JButton(LOCALE_MESSAGES.getMessage("game.exit"));
        //Copy youtube's
        runningstatsButton = new JButton(LOCALE_MESSAGES.getMessage("game.debug"));
        optionsWindowButton = new JButton(LOCALE_MESSAGES.getMessage("options.title"));

        turnLabel.setText(String.format(LOCALE_MESSAGES.getMessage("game.date.display"), date.getYears(), date.getMonthNumber(), date.getDays()));

        statusProgressBar.setIndeterminate(false);

        pausePlayButton.addActionListener(this);
        pausePlayButton.setForeground(Color.red);
        pausePlayButton.setFocusable(false);
        pausePlayButton.getActionMap().put("doPause", new AbstractAction("Pause") {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
        pausePlayButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "doPause");

        speedComboBox.setSelectedItem(LOCALE_MESSAGES.getMessage("game.speed.normal"));
        speedComboBox.setFocusable(false);

        alertsButton.setFocusable(false);
        alertsButton.addActionListener((e) -> {
            AlertDisplayer disp = AlertDisplayer.getInstance();
            disp.toFront();
        });

        exitGameButton.setFocusable(false);
        exitGameButton.addActionListener((e) -> {
            if (GameController.musicPlayer != null) {
                GameController.musicPlayer.clean();
            }
            System.exit(0);
        });

        manualButton.setFocusable(false);
        manualButton.addActionListener((e) -> {
            InternalManual manual = new InternalManual();
            getDesktopPane().add(manual);
        });

        saveGameButton.setFocusable(false);
        saveGameButton.addActionListener(e -> {
            this.getTopLevelAncestor().setCursor(new Cursor(Cursor.WAIT_CURSOR));
            SaveGame game = new SaveGame(SaveGame.getSaveFolder());
            long before = System.currentTimeMillis();
            try {
                game.save(state);
            } catch (IOException ex) {
                ExceptionHandling.ExceptionMessageBox("IO exception while saving!", ex);
            } catch (IllegalArgumentException ex) {
                ExceptionHandling.ExceptionMessageBox("Illegal Argument exception while saving!", ex);
            } catch (IllegalAccessException ex) {
                ExceptionHandling.ExceptionMessageBox("Illegal Access exception while saving!", ex);
            }
            LOGGER.info("Time to save " + (System.currentTimeMillis() - before));
            this.getTopLevelAncestor().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        });

        runningstatsButton.setFocusable(false);
        runningstatsButton.addActionListener((e) -> {
            DebugStatsWindow win = DebugStatsWindow.getInstance(state);
            if (!win.isShowing()) {
                getDesktopPane().add(win);
            }
        });

        optionsWindowButton.setFocusable(false);
        optionsWindowButton.addActionListener((e) -> {
            InternalOptionsWindow win = InternalOptionsWindow.getInstance();
            if (!win.isShowing()) {
                getDesktopPane().add(win);
            }
        });

        pan.add(turnLabel);
        pan.add(statusProgressBar);
        pan.add(pausePlayButton);
        pan.add(speedComboBox);
        pan.add(alertsButton);
        pan.add(manualButton);
        pan.add(saveGameButton);
        pan.add(optionsWindowButton);
        pan.add(runningstatsButton);
        pan.add(exitGameButton);

        //Set the keybindings
        getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("SPACE"), "pauseplay");
        getActionMap().put("pauseplay", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pausePlayButton.doClick();
            }
        });
        Timer updater = new Timer(100, (e) -> {
            turnLabel.setText(String.format(LOCALE_MESSAGES.getMessage("game.date.display"), date.getYears(), date.getMonthNumber(), date.getDays()));
        });
        updater.start();

        //Pls increment the first parameter when you add more stuff.
        pan.setLayout(new VerticalFlowLayout());
        add(pan);
        pack();
        setResizable(false);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("pauseplay") || e.getSource() == pausePlayButton) {
            if (isPaused) {
                (pausePlayButton).setText(LOCALE_MESSAGES.getMessage("game.pause"));
                (pausePlayButton).setForeground(Color.green);
                statusProgressBar.setIndeterminate(true);
            } else {
                (pausePlayButton).setText(LOCALE_MESSAGES.getMessage("game.already.paused"));
                (pausePlayButton).setForeground(Color.red);
                statusProgressBar.setIndeterminate(false);
            }
            isPaused = !isPaused;
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    public int getTickCount() {
        switch (speedComboBox.getSelectedIndex()) {
            case 0: // Slowest
                return GameSpeeds.SLOWEST;
            case 1: // Slower
                return GameSpeeds.SLOWER;
            case 2: // Slow
                return GameSpeeds.SLOW;
            case 3: // Normal
                return GameSpeeds.NORMAL;
            case 4: // Fast
                return GameSpeeds.FAST;
            case 5: // Faster
                return GameSpeeds.FASTER;
            case 6: // Fastest
                return GameSpeeds.FASTEST;
        }
        //Default to a normal speed otherwise.
        return GameSpeeds.NORMAL;
    }
}
