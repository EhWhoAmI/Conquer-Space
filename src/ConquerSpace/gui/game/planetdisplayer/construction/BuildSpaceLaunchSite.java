package ConquerSpace.gui.game.planetdisplayer.construction;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.launch.LaunchSystem;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author zyunl
 */
public class BuildSpaceLaunchSite extends JPanel implements ActionListener {

    JLabel amount;

    JSpinner maxPopulation;
    JLabel launchTypes;
    JComboBox<LaunchSystem> launchTypesValue;

    public BuildSpaceLaunchSite(Civilization c) {
        setLayout(new GridLayout(2, 2));
        amount = new JLabel("Amount of launch ports");

        launchTypes = new JLabel("Launch types");

        SpinnerNumberModel model = new SpinnerNumberModel(3, 0, 5000, 1);

        maxPopulation = new JSpinner(model);
        ((JSpinner.DefaultEditor) maxPopulation.getEditor()).getTextField().setEditable(false);

        launchTypesValue = new JComboBox<LaunchSystem>();

        for (LaunchSystem t : c.launchSystems) {
            launchTypesValue.addItem(t);
        }
        add(amount);
        add(maxPopulation);
        add(launchTypes);
        add(launchTypesValue);
    }

    //Determine price
    //Add this 
    @Override
    public void actionPerformed(ActionEvent e) {
        //Calculate the cost...
        long pop = 0;
        try {
            pop = (Long) maxPopulation.getValue();

        } catch (NumberFormatException | ArithmeticException nfe) {
            //Because who cares!
        }
    }
}
