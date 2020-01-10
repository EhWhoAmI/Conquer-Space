package ConquerSpace.gui.game.planetdisplayer.construction;

import ConquerSpace.game.GameUpdater;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author zyunl
 */
public class BuildObservatoryMenu extends JPanel {

    JLabel lensSizeLabel;
    JSpinner lensSizeSpinner;
    JLabel telescopeRangeLabel;
    JLabel telescopeRangeValueLabel;
    
    public BuildObservatoryMenu() {
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();

        lensSizeLabel = new JLabel("Lens Size(cm)");

        SpinnerNumberModel model = new SpinnerNumberModel(50, 0, 5000, 1);

        lensSizeSpinner = new JSpinner(model);
        ((JSpinner.DefaultEditor) lensSizeSpinner.getEditor()).getTextField().setEditable(false);

        lensSizeSpinner.addChangeListener(a -> {
            //Calculate
            telescopeRangeValueLabel.setText(GameUpdater.Calculators.Optics.getRange(1, (int) lensSizeSpinner.getValue()) + " light years");

        });

        telescopeRangeLabel = new JLabel("Range: ");
        telescopeRangeValueLabel = new JLabel("0 light years");

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipady = 5;
        add(lensSizeLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.ipady = 5;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(lensSizeSpinner, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1;
        constraints.weighty = 1;
        add(telescopeRangeLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(telescopeRangeValueLabel, constraints);
    }
}
