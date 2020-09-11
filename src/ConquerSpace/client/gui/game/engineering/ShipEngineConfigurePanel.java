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

import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.client.gui.ObjectListModel;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.ships.EngineTechnology;
import ConquerSpace.common.game.ships.components.EngineComponent;
import ConquerSpace.common.game.ships.components.ShipComponent;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author EhWhoAmI
 */
public class ShipEngineConfigurePanel extends JPanel {

    private JTabbedPane pane;

    private ChooseEnginePanel chooseEnginePanel;
    private CustomizeEnginePanel customizeEnginePanel;

    Civilization civ;
    GameState gameState;

    ObjectReference selectedEngine = ObjectReference.INVALID_REFERENCE;

    public ShipEngineConfigurePanel(GameState gameState, Civilization civ) {
        this.gameState = gameState;
        this.civ = civ;
        setLayout(new BorderLayout());

        pane = new JTabbedPane();
        chooseEnginePanel = new ChooseEnginePanel();
        customizeEnginePanel = new CustomizeEnginePanel();

        pane.add("Choose Engine", chooseEnginePanel);
        pane.add("Customize Engine", customizeEnginePanel);
        add(pane, BorderLayout.CENTER);
    }

    public class ChooseEnginePanel extends JPanel {

        ObjectListModel<EngineComponent> engineListModel;

        JPanel engineInfoPanel;
        JLabel efficienyLabel;
        JLabel thrustLabel;
        JLabel engineTypeLabel;

        public ChooseEnginePanel() {
            setLayout(new BorderLayout());
            engineListModel = new ObjectListModel<>();
            //Fill list	
            for (ObjectReference obj : civ.shipComponentList) {
                ShipComponent template = gameState.getObject(obj, ShipComponent.class);
                if (template instanceof EngineComponent) {
                    engineListModel.addElement((EngineComponent) template);
                }
            }

            engineListModel.setHandler(l -> {
                return l.getName();
            });
            JList<String> list = new JList<>(engineListModel);

            list.addListSelectionListener(l -> {
                EngineComponent component = engineListModel.getObject(list.getSelectedIndex());
                selectedEngine = component.getReference();

                //Set displaying UI
                efficienyLabel.setText("Efficiency: " + component.getEfficiency());
                thrustLabel.setText("Thrust: " + component.getThrust());
                engineTypeLabel.setText("Engine type: " + component.getEngineType());
            });

            engineInfoPanel = new JPanel(new VerticalFlowLayout());
            efficienyLabel = new JLabel();
            thrustLabel = new JLabel();
            engineTypeLabel = new JLabel();

            engineInfoPanel.add(efficienyLabel);
            engineInfoPanel.add(thrustLabel);
            engineInfoPanel.add(engineTypeLabel);

            add(new JScrollPane(list), BorderLayout.WEST);
            add(engineInfoPanel, BorderLayout.CENTER);
        }

    }

    public class CustomizeEnginePanel extends JPanel {

        JComboBox<EngineTechnology> engineTechComboBox;
        JPanel createEnginePanel;

        JSpinner engineSpeedSpinner;

        public CustomizeEnginePanel() {
            setLayout(new VerticalFlowLayout());

            //Just type of engine, and thrust power i guess
            DefaultComboBoxModel<EngineTechnology> engineTechBoxModel = new DefaultComboBoxModel<>();

            //Add the civ info
            for (ObjectReference t : civ.engineTechs) {
                EngineTechnology technology = gameState.getObject(t, EngineTechnology.class);

                engineTechBoxModel.addElement(technology);
            }
            engineTechComboBox = new JComboBox<>(engineTechBoxModel);

            engineSpeedSpinner = new JSpinner(new SpinnerNumberModel(500, 0, Integer.MAX_VALUE, 1));
            //Set width
            Component engineSpeedSpinnerEditor = engineSpeedSpinner.getEditor();
            JFormattedTextField jftf = ((JSpinner.DefaultEditor) engineSpeedSpinnerEditor).getTextField();
            jftf.setColumns(16);

            JPanel engineTechContainer = new JPanel(new HorizontalFlowLayout());
            engineTechContainer.add(new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.engine.tech")));
            engineTechContainer.add(engineTechComboBox);
            
            JPanel engineSpeedContainer = new JPanel(new HorizontalFlowLayout());
            engineSpeedContainer.add(new JLabel("Expected Velocity"));
            engineSpeedContainer.add(engineSpeedSpinner);

            add(new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.engine.designer")));
            add(engineTechContainer);
            add(engineSpeedContainer);
        }
    }

    public ObjectReference getEngine() {
        if (pane.getSelectedIndex() == 0) {
            return selectedEngine;
        }
        return ObjectReference.INVALID_REFERENCE;
    }
}
