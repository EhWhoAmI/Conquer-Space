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

import ConquerSpace.game.civilization.Civilization;
import ConquerSpace.game.districts.City;
import ConquerSpace.game.districts.area.Area;
import ConquerSpace.game.districts.area.SpacePortArea;
import ConquerSpace.game.ships.launch.LaunchSystem;
import ConquerSpace.game.universe.bodies.Planet;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author EhWhoAmI
 */
public class SpacePortConstructionPanel extends AreaDesignPanel {

    private JLabel amount;
    private JSpinner maxLaunchTubes;
    private JLabel launchTypes;
    private JComboBox<LaunchSystem> launchTypesValue;

    public SpacePortConstructionPanel(Planet p, City c, Civilization civ) {
        super(p, c);

        setLayout(new GridBagLayout());
        amount = new JLabel("Amount of launch ports");

        launchTypes = new JLabel("Launch types");

        SpinnerNumberModel model = new SpinnerNumberModel(3, 1, 5000, 1);

        maxLaunchTubes = new JSpinner(model);
        ((JSpinner.DefaultEditor) maxLaunchTubes.getEditor()).getTextField().setEditable(false);

        launchTypesValue = new JComboBox<LaunchSystem>();

        for (LaunchSystem t : civ.launchSystems) {
            launchTypesValue.addItem(t);
        }

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 0.1;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.fill = GridBagConstraints.BOTH;
        add(amount, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        add(maxLaunchTubes, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        add(launchTypes, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        add(launchTypesValue, constraints);
    }

    @Override
    public Area getAreaToConstruct() {
        if (launchTypesValue.getSelectedItem() != null && launchTypesValue.getSelectedItem() instanceof LaunchSystem) {
            LaunchSystem ls = (LaunchSystem) launchTypesValue.getSelectedItem();
            return new SpacePortArea(ls, (Integer) maxLaunchTubes.getValue());
        }
        return null;
    }
}
