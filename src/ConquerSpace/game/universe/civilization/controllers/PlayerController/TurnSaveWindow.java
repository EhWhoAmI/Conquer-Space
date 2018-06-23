package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.StarDate;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.start.gui.Manual;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;

/**
 *
 * @author Zyun
 */
public class TurnSaveWindow extends JFrame implements ActionListener {

    private JLabel turnLabel;
    private JButton pausePlayButton;
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

        alertsButton.addActionListener((e) -> {
            AlertDisplayer disp = AlertDisplayer.getInstance();
            disp.toFront();
        });
        
        exitGameButton.addActionListener((e) -> {
            System.exit(0);
        });

        manualButton.addActionListener((e) -> {
            Manual.getInstance().setVisible(true);
        });

        runningstatsButton.addActionListener((e) -> {
            DebugStatsWindow.getInstance(universe);
        });

        pan.add(turnLabel);
        pan.add(statusProgressBar);
        pan.add(pausePlayButton);
        pan.add(alertsButton);
        pan.add(manualButton);
        pan.add(saveGameButton);
        pan.add(exitGameButton);
        pan.add(runningstatsButton);

        Timer updater = new Timer(100, (e) -> {
            turnLabel.setText("Year " + date.getYears() + " Month " + date.getMonthNumber() + " Day " + date.getDays());
        });
        updater.start();

        pan.setLayout(new GridLayout(8, 1, 5, 5));
        add(pan);
        pack();
        setVisible(true);
        setAlwaysOnTop(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == pausePlayButton) {
            if (isPaused) {
                ((JButton) e.getSource()).setText("Pause");
                ((JButton) e.getSource()).setForeground(Color.green);
                statusProgressBar.setIndeterminate(true);
            } else {
                ((JButton) e.getSource()).setText("Paused");
                ((JButton) e.getSource()).setForeground(Color.red);
                statusProgressBar.setIndeterminate(false);
            }
            isPaused = !isPaused;
        }
    }

    public boolean isPaused() {
        return isPaused;
    }
}
