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
package ConquerSpace.client.gui.game.planetdisplayer.construction;

import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.city.area.AreaFactory;
import ConquerSpace.common.game.city.area.SpacePortAreaFactory;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.ships.LaunchSystem;
import ConquerSpace.common.game.universe.bodies.Planet;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
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

    private SpacePortAreaFactory factory;

    private final int defaultPortsCount = 3;

    public SpacePortConstructionPanel(GameState gameState, Planet p, City c, Civilization civ) {
        super(gameState, p, c, civ);
        factory = new SpacePortAreaFactory(civ);
        factory.setLaunchSystemCount(defaultPortsCount);

        setLayout(new GridBagLayout());
        amount = new JLabel("Amount of launch ports");

        launchTypes = new JLabel("Launch types");

        SpinnerNumberModel model = new SpinnerNumberModel(defaultPortsCount, 1, 5000, 1);

        maxLaunchTubes = new JSpinner(model);
        maxLaunchTubes.addChangeListener(l -> {
            factory.setLaunchSystemCount((Integer) maxLaunchTubes.getValue());
        });

        ((JSpinner.DefaultEditor) maxLaunchTubes.getEditor()).getTextField().setEditable(false);

        launchTypesValue = new JComboBox<>();

        for (ObjectReference id : civ.launchSystems) {
            launchTypesValue.addItem(gameState.getObject(id, LaunchSystem.class));
        }
        factory.setLaunchSystem(((LaunchSystem) launchTypesValue.getSelectedItem()).getReference());
        launchTypesValue.addActionListener(l -> {
            factory.setLaunchSystem(((LaunchSystem) launchTypesValue.getSelectedItem()).getReference());
        });

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
    public AreaFactory getAreaToConstruct() {
        return factory;
    }
}
