package ConquerSpace.gui.game;

import ConquerSpace.game.StarDate;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.util.CQSPLogger;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.AbstractAction;
import javax.swing.JButton;
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
 * @author Zyun
 */
public class TurnSaveWindow extends JInternalFrame implements ActionListener {
    private static final Logger LOGGER = CQSPLogger.getLogger(TurnSaveWindow.class.getName());
    
    private JLabel turnLabel;
    //Public for the action listeners later on
    public JButton pausePlayButton;
    private JButton alertsButton;
    private JButton saveGameButton;
    private JButton exitGameButton;
    private JButton manualButton;
    private JButton runningstatsButton;
    private JProgressBar statusProgressBar;

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
        
        alertsButton.setFocusable(false);
        alertsButton.addActionListener((e) -> {
            AlertDisplayer disp = AlertDisplayer.getInstance();
            disp.toFront();
        });
        
        exitGameButton.setFocusable(false);
        exitGameButton.addActionListener((e) -> {
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException se) {
                if (( (se.getErrorCode() == 50000)
                            && ("XJ015".equals(se.getSQLState()) ))) {
                        // we got the expected exception
                        LOGGER.info("Derby shut down normally");
                        // Note that for single database shutdown, the expected
                        // SQL state is "08006", and the error code is 45000.
                    } else {
                        // if the error code or SQLState is different, we have
                        // an unexpected exception (shutdown failed)
                        LOGGER.error("Derby did not shut down normally", se);
                    }

            }
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

        pan.setLayout(new GridLayout(8, 1, 5, 5));
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
}
