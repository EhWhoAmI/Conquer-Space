package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.plaf.basic.BasicInternalFrameUI;

/**
 *
 * @author Zyun
 */
public class AlertNotification extends JInternalFrame implements Runnable {

    public static final int DEFAULT_WAIT = 5 * 1000;

    public AlertNotification(String title, String desc) {
        String message = desc;
        String header = title;
        setSize(300, 125);
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0f;
        constraints.weighty = 1.0f;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.BOTH;
        JLabel headingLabel = new JLabel(header);
        headingLabel.setOpaque(false);
        add(headingLabel, constraints);
        constraints.gridx++;
        constraints.weightx = 0f;
        constraints.weighty = 0f;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.NORTH;
        JButton cloesButton = new JButton("X");
        cloesButton.addActionListener((e) -> {
            dispose();
        });
        cloesButton.setMargin(new Insets(1, 4, 1, 4));
        cloesButton.setFocusable(false);
        add(cloesButton, constraints);
        constraints.gridx = 0;
        constraints.gridy++;
        constraints.weightx = 1.0f;
        constraints.weighty = 1.0f;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.fill = GridBagConstraints.BOTH;
        JLabel messageLabel = new JLabel("<html>" + message);
        add(messageLabel, constraints);
        setUndecorated(true);
    }

    //Default wait: 5 seconds
    @Override
    public void run() {
        try {
            Thread.sleep(DEFAULT_WAIT);
            dispose();
        } catch (InterruptedException ex) {
        }
    }

    public void setRootPaneCheckingEnabled(boolean enabled) {
        super.setRootPaneCheckingEnabled(enabled);
    }

    public void setUndecorated(boolean val) {
        setBorder(val ? null : getBorder());
        ((BasicInternalFrameUI) getUI()).setNorthPane(val ? null : ((BasicInternalFrameUI) getUI()).getNorthPane());
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        Thread t = new Thread(this);
        t.start();
    }
}
