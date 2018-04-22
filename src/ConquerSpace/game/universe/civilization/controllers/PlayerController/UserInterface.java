package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This shows the user interface and things...
 *
 * @author Zyun
 */
public class UserInterface extends JFrame {

    //Components
    private JButton industryButton;
    private JButton militaryButton;
    private JButton economyButton;
    private JButton diplomacyButton;
    private JButton researchButton;

    private JButton universeBreakdownButton;

    public UserInterface() {
        JPanel pan = new JPanel();
        industryButton = new JButton("Industry");
        militaryButton = new JButton("Military");
        economyButton = new JButton("Economy");
        diplomacyButton = new JButton("Diplomacy");
        researchButton = new JButton("Research");
        
        universeBreakdownButton = new JButton("Universe Breakdown");

        universeBreakdownButton.addActionListener((e) -> {
            UniverseBreakdown.getInstance();
        });
        pan.add(industryButton);
        pan.add(militaryButton);
        pan.add(economyButton);
        pan.add(universeBreakdownButton);
        pan.add(diplomacyButton);
        pan.add(researchButton);

        add(pan);
        pack();
        setVisible(true);
    }

}
