package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.universe.spaceObjects.Universe;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

/**
 * This shows the user interface and things...
 *
 * @author Zyun
 */
public class UserInterface extends JInternalFrame {

    //Components
    private JButton industryButton;
    private JButton militaryButton;
    private JButton economyButton;
    private JButton diplomacyButton;
    private JButton researchButton;

    private JButton universeBreakdownButton;

    private Universe universe;
    
    public UserInterface(Universe u) {
        this.universe = u;
        
        JPanel pan = new JPanel();
        industryButton = new JButton("Industry");
        militaryButton = new JButton("Military");
        economyButton = new JButton("Economy");
        diplomacyButton = new JButton("Diplomacy");
        researchButton = new JButton("Research");
        
        universeBreakdownButton = new JButton("Universe Breakdown");

        universeBreakdownButton.addActionListener((e) -> {
            UniverseBreakdown.getInstance(universe);
        });
        pan.add(industryButton);
        pan.add(militaryButton);
        pan.add(economyButton);
        pan.add(universeBreakdownButton);
        pan.add(diplomacyButton);
        pan.add(researchButton);

        add(pan);
        setResizable(true);
        setClosable(true);
        pack();
        setVisible(true);
    }

}
