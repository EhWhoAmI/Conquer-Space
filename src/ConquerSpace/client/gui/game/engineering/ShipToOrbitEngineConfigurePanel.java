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

package ConquerSpace.client.gui.game.engineering;

import ConquerSpace.client.gui.ObjectListModel;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.ships.components.ShipComponent;
import ConquerSpace.common.game.ships.components.ToOrbitEngineComponent;
import ConquerSpace.common.game.ships.launch.LaunchSystem;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;

/**
 *
 * @author EhWhoAmI
 */
public class ShipToOrbitEngineConfigurePanel extends JPanel {

    JTabbedPane tabs;

    GameState gameState;
    Civilization civ;

    ObjectReference selectedEngine = null;
    ChooseEngineConfigurePanel chooseEngineConfigurePanel;
    CreateEngineConfigurePanel createEngineConfigurePanel;

    public ShipToOrbitEngineConfigurePanel(GameState gameState, Civilization civ) {
        this.gameState = gameState;
        this.civ = civ;
        setLayout(new BorderLayout());

        chooseEngineConfigurePanel = new ChooseEngineConfigurePanel();
        createEngineConfigurePanel = new CreateEngineConfigurePanel();

        //Get the engine types and etc.
        tabs = new JTabbedPane();
        tabs.add("Choose Engine", chooseEngineConfigurePanel);
        tabs.add("Create Engine", createEngineConfigurePanel);
        add(tabs, BorderLayout.CENTER);
    }

    class ChooseEngineConfigurePanel extends JPanel {

        ObjectListModel<ToOrbitEngineComponent> engineListModel;

        JPanel engineInfoPanel;
        JLabel engineType;
        JLabel thrustLabel;

        public ChooseEngineConfigurePanel() {
            setLayout(new BorderLayout());
            engineListModel = new ObjectListModel<>();
            //Fill list	
            for (ObjectReference obj : civ.shipComponentList) {
                ShipComponent template = gameState.getObject(obj, ShipComponent.class);
                if (template instanceof ToOrbitEngineComponent) {
                    engineListModel.addElement((ToOrbitEngineComponent) template);
                }
            }

            engineListModel.setHandler(l -> {
                return l.getName();
            });
            JList<String> list = new JList<>(engineListModel);

            list.addListSelectionListener(l -> {
                ToOrbitEngineComponent component = engineListModel.getObject(list.getSelectedIndex());
                selectedEngine = component.getReference();

                //Set displaying UI
                engineType.setText("Type: " + gameState.getObject(component.getLaunchSystemType(), LaunchSystem.class).getName());
                thrustLabel.setText("Thrust: " + component.getThrust());
            });

            engineInfoPanel = new JPanel(new VerticalFlowLayout());
            engineType = new JLabel("Type: ");
            thrustLabel = new JLabel("Thrust: ");

            engineInfoPanel.add(engineType);
            engineInfoPanel.add(thrustLabel);

            list.setFixedCellWidth(64);
            add(new JScrollPane(list), BorderLayout.WEST);
            add(engineInfoPanel, BorderLayout.CENTER);
        }
    }

    class CreateEngineConfigurePanel extends JPanel {

        DefaultComboBoxModel<LaunchSystem> comboBoxModel;
        JComboBox<LaunchSystem> launchSystemTypeComboBox;
        JSpinner thrustSpinner;

        public CreateEngineConfigurePanel() {
            setLayout(new VerticalFlowLayout());
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
            panel.add(new JLabel("Amount to orbit: "));
            panel.add(thrustSpinner);
            panel.add(new JLabel("kg"));
            add(panel);
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

        ShipComponent generateComponent() {
            ToOrbitEngineComponent component = new ToOrbitEngineComponent(gameState);
            component.setThrust((Integer) thrustSpinner.getValue());
            component.setLaunchSystemType(comboBoxModel
                    .getElementAt(launchSystemTypeComboBox.getSelectedIndex()).getReference());
            return component;
        }
    }

    public int getAmountToOrbit() {
        return (Integer) createEngineConfigurePanel.thrustSpinner.getValue();
    }

    public ObjectReference getEngineTech() {
        return createEngineConfigurePanel.comboBoxModel
                .getElementAt(createEngineConfigurePanel.launchSystemTypeComboBox.getSelectedIndex()).getReference();
    }

    public boolean getToDesign() {
        return tabs.getSelectedIndex() == 1;
    }

    ObjectReference getShipComponent() {
        return selectedEngine;
    }
}
