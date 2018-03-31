package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.Globals;
import ConquerSpace.start.gui.Manual;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Zyun
 */
public class TurnSaveWindow{

    private JLabel turnLabel;
    private JButton nextTurnButton;
    private JButton saveGameButton;
    private JButton exitGameButton;
    private JButton manualButton;

    public TurnSaveWindow() {
        JPanel pan = new JPanel();

        //Init components
        turnLabel = new JLabel();
        nextTurnButton = new JButton("Next turn");
        manualButton = new JButton("Manual");
        saveGameButton = new JButton("Save Game");
        exitGameButton = new JButton("Exit Game");

        turnLabel.setText("Turn " + Globals.turn);

        exitGameButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        manualButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Manual.getInstance().setVisible(true);
            }
        });

        pan.add(turnLabel);
        pan.add(nextTurnButton);
        pan.add(manualButton);
        pan.add(saveGameButton);
        pan.add(exitGameButton);
        pan.setLayout(new GridLayout(5, 1, 5, 5));
        JOptionPane.showMessageDialog(null, pan);

    }

}
