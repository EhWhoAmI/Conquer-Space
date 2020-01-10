package ConquerSpace.gui.game.planetdisplayer.construction;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author zyunl
 */
public class BuildPopulationStorage extends JPanel implements ActionListener {

    long maxPopulation;
    private JLabel amount;
    private JLabel amountEnding;
    JSpinner maxPopulationTextField;

    public BuildPopulationStorage() {
        GridBagLayout gridbag = new GridBagLayout();

        setLayout(gridbag);
        GridBagConstraints constraints = new GridBagConstraints();
        amount = new JLabel("Max Population");

        SpinnerNumberModel model = new SpinnerNumberModel(200, 10, Integer.MAX_VALUE, 10);
        maxPopulationTextField = new JSpinner(model);
        ((JSpinner.DefaultEditor) maxPopulationTextField.getEditor()).getTextField().setEditable(false);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1;
        constraints.weighty = 1;
        add(amount, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(maxPopulationTextField, constraints);

        amountEnding = new JLabel("million people");
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.insets = new Insets(2, 5, 0, 0);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(amountEnding, constraints);
    }

    //Determine price
    //Add this 
    @Override
    public void actionPerformed(ActionEvent e) {
        //Calculate the cost...
        try {
            maxPopulation = (Integer) maxPopulationTextField.getValue();

        } catch (NumberFormatException | ArithmeticException nfe) {
            //Because who cares!

        }
    }
}
