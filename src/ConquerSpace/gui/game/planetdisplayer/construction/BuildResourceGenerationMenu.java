package ConquerSpace.gui.game.planetdisplayer.construction;

import ConquerSpace.game.GameController;
import ConquerSpace.game.universe.resources.Resource;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author zyunl
 */
public class BuildResourceGenerationMenu extends JPanel {

        JComboBox<Resource> resourceToMine;
        JLabel resourceToMineLabel;
        JLabel miningSpeed;
        JSpinner miningSpeedSpinner;

        public BuildResourceGenerationMenu() {
            resourceToMineLabel = new JLabel("Mining resource: ");

            DefaultComboBoxModel<Resource> resourceComboBoxModel = new DefaultComboBoxModel<>();
            //Add the resources
            for (Resource res : GameController.resources) {
                resourceComboBoxModel.addElement(res);
            }
            resourceToMine = new JComboBox<>(resourceComboBoxModel);

            miningSpeed = new JLabel("Mining speed, units per month");
            SpinnerNumberModel miningSpeedSpinnerNumberModel = new SpinnerNumberModel(10f, 0f, 50000f, 0.5f);
            miningSpeedSpinner = new JSpinner(miningSpeedSpinnerNumberModel);

            setLayout(new GridBagLayout());

            GridBagConstraints constraints = new GridBagConstraints();

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            add(resourceToMineLabel, constraints);

            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            add(resourceToMine, constraints);

            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            add(miningSpeed, constraints);

            constraints.gridx = 1;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            add(miningSpeedSpinner, constraints);
        }
    }
