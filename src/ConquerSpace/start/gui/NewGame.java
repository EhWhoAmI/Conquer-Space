package ConquerSpace.start.gui;

import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Zyun
 */
public class NewGame extends JFrame {
    private JLabel universeSizeLabel;
    private JComboBox<String> universeSizeBox;
    public NewGame() {
        setSize(500, 400);
        setLayout(new GridLayout(3, 4));
        //Add components
        universeSizeLabel = new JLabel("Universe Size");
        universeSizeBox = new JComboBox<>();
        universeSizeBox.addItem("Small");
        universeSizeBox.addItem("Medium");
        
        add(universeSizeLabel);
        add(universeSizeBox);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
