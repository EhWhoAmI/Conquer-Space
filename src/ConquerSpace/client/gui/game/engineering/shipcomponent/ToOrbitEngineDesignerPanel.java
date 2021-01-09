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

package ConquerSpace.client.gui.game.engineering.shipcomponent;

import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.ships.LaunchSystem;
import ConquerSpace.common.game.ships.components.ShipComponent;
import ConquerSpace.common.game.ships.components.ToOrbitEngineComponent;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

/**
 *
 * @author EhWhoAmI
 */
public class ToOrbitEngineDesignerPanel extends ShipComponentDesignerPanel {

    DefaultComboBoxModel<LaunchSystem> comboBoxModel;
    JComboBox<LaunchSystem> launchSystemTypeComboBox;
    JSpinner thrustSpinner;
    Civilization civ;
    GameState gameState;

    public ToOrbitEngineDesignerPanel(GameState gameState, Civilization civ) {
        setLayout(new VerticalFlowLayout());
        this.civ = civ;
        this.gameState = gameState;
        comboBoxModel = new DefaultComboBoxModel<>();
        for (ObjectReference or : civ.launchSystems) {
            comboBoxModel.addElement(gameState.getObject(or, LaunchSystem.class));
        }
        launchSystemTypeComboBox = new JComboBox<>(comboBoxModel);
        thrustSpinner = new JSpinner();

        JPanel panel = new JPanel(new HorizontalFlowLayout());
        panel.add(new JLabel("Launch System type: "));
        panel.add(launchSystemTypeComboBox);
        add(panel);

        panel = new JPanel(new HorizontalFlowLayout());
        panel.add(new JLabel("Thrust: "));
        panel.add(thrustSpinner);
        panel.add(new JLabel("kn"));
        add(panel);
    }

    @Override
    public void clearUI() {
        //launchSystemTypes.setD
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            comboBoxModel.removeAllElements();
            //Re-add all elements
            for (ObjectReference or : civ.launchSystems) {
                comboBoxModel.addElement(gameState.getObject(or, LaunchSystem.class));
            }
        }
    }

    @Override
    ShipComponent generateComponent() {
        ToOrbitEngineComponent component = new ToOrbitEngineComponent(gameState);
        component.setThrust((Integer) thrustSpinner.getValue());
        component.setLaunchSystemType(comboBoxModel
                .getElementAt(launchSystemTypeComboBox.getSelectedIndex()).getReference());
        return component;
    }
}
