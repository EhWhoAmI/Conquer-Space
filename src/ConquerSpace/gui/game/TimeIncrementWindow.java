package ConquerSpace.gui.game;

import com.alee.extended.layout.HorizontalFlowLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;

/**
 *
 * @author Zyun
 */
public class TimeIncrementWindow extends JInternalFrame{

    //Remember: incrementation is in hours
    private JButton[] incrementButtons;
    private String[] list = {
        "1 Hour",
        "5 Hours",
        "10 Hours",
        "24 Hours",
        "120 Hours",
        "240 Hours",
        "720 Hours",
    };
    public TimeIncrementWindow() {
        setLayout(new HorizontalFlowLayout());
        incrementButtons = new JButton[list.length];
        for(int i = 0; i < incrementButtons.length; i++) {
            incrementButtons[i] = new JButton(list[i]);
            incrementButtons[i].setFocusable(false);
            add(incrementButtons[i]);
        }
        pack();
        setVisible(true);
    }
}
