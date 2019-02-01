package ConquerSpace.gui.game;

import ConquerSpace.game.GameSpeeds;
import ConquerSpace.gui.game.DebugStatsWindow;
import ConquerSpace.gui.game.AlertDisplayer;
import ConquerSpace.gui.game.InternalManual;
import ConquerSpace.game.StarDate;
import ConquerSpace.game.universe.spaceObjects.Universe;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
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

/**
 *
 * @author Zyun
 */
public class TurnSaveWindow extends JInternalFrame implements ActionListener {

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

    private String[] speeds = {
        "Slowest",
        "Slower",
        "Slow",
        "Normal",
        "Fast",
        "Faster",
        "Fastest"
    };
    private boolean isPaused = true;
    private StarDate date;
    private Universe universe;

    public TurnSaveWindow(StarDate date, Universe u) {
        universe = u;
        this.date = date;
        JPanel pan = new JPanel();

        //Init components
        turnLabel = new JLabel();
        statusProgressBar = new JProgressBar();
        pausePlayButton = new JButton("Paused");
        speedComboBox = new JComboBox<>(speeds);
        alertsButton = new JButton("Alerts");
        manualButton = new JButton("Manual");
        saveGameButton = new JButton("Save Game");
        exitGameButton = new JButton("Exit Game");
        //Copy youtube's
        runningstatsButton = new JButton("Stats for Nerds");

        turnLabel.setText("Year " + date.getYears() + " Month " + date.getMonthNumber() + " Day " + date.getDays());

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

        speedComboBox.setSelectedItem("Normal");
        speedComboBox.setFocusable(false);
        
        alertsButton.setFocusable(false);
        alertsButton.addActionListener((e) -> {
            AlertDisplayer disp = AlertDisplayer.getInstance();
            disp.toFront();
        });

        exitGameButton.setFocusable(false);
        exitGameButton.addActionListener((e) -> {
            System.exit(0);
        });

        manualButton.setFocusable(false);
        manualButton.addActionListener((e) -> {
            InternalManual manual = new InternalManual();
            getDesktopPane().add(manual);
        });

        saveGameButton.setFocusable(false);

        runningstatsButton.setFocusable(false);
        runningstatsButton.addActionListener((e) -> {
            DebugStatsWindow win = DebugStatsWindow.getInstance(universe);
            getDesktopPane().add(win);
        });

        pan.add(turnLabel);
        pan.add(statusProgressBar);
        pan.add(pausePlayButton);
        pan.add(speedComboBox);
        pan.add(alertsButton);
        pan.add(manualButton);
        pan.add(saveGameButton);
        pan.add(exitGameButton);
        pan.add(runningstatsButton);

        //Set the keybindings
        getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("SPACE"), "pauseplay");
        getActionMap().put("pauseplay", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pausePlayButton.doClick();
            }
        });
        Timer updater = new Timer(100, (e) -> {
            turnLabel.setText("Year " + date.getYears() + " Month " + date.getMonthNumber() + " Day " + date.getDays());
        });
        updater.start();

        //Pls increment the first parameter when you add more stuff.
        pan.setLayout(new GridLayout(9, 1, 5, 5));
        add(pan);
        pack();
        setResizable(true);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("pauseplay") || e.getSource() == pausePlayButton) {
            if (isPaused) {
                (pausePlayButton).setText("Pause");
                (pausePlayButton).setForeground(Color.green);
                statusProgressBar.setIndeterminate(true);
            } else {
                (pausePlayButton).setText("Paused");
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