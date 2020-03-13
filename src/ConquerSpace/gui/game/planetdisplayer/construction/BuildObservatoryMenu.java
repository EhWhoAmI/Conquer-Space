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

import ConquerSpace.game.Calculators;
import ConquerSpace.game.GameUpdater;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author EhWhoAmI
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
            telescopeRangeValueLabel.setText(Calculators.Optics.getRange(1, (int) lensSizeSpinner.getValue()) + " light years");

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
