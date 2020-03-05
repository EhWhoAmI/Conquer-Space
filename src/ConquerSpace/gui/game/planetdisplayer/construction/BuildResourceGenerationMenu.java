/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
 * @author EhWhoAmI
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
