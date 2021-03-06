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
import ConquerSpace.common.game.ships.components.EngineComponent;
import ConquerSpace.common.game.ships.components.ShipComponent;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.Component;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author EhWhoAmI
 */
public class EngineDesignerPanel extends ShipComponentDesignerPanel {

    private JLabel engineTechonolgyLabel;
    private JComboBox<Object> engineTechnologyComboBox;
    private JLabel thrustLabel;
    private JSpinner thrustSpinner;
    private JLabel efficiencyLabel;
    private JSpinner efficiencySpinner;

    private GameState gameState;
    private Civilization civilization;

    public EngineDesignerPanel(GameState gameState, Civilization civilization) {
        this.gameState = gameState;
        this.civilization = civilization;
        setLayout(new VerticalFlowLayout());
        engineTechonolgyLabel = new JLabel("Propulsion Type");
        engineTechnologyComboBox = new JComboBox<>(civilization.getEngineTechs().toArray());
        thrustLabel = new JLabel("Max Thrust");
        thrustSpinner = new JSpinner(new SpinnerNumberModel(0d, 0d, Double.MAX_VALUE, 1d));

        //Set Width
        Component thrustSpinnerEditor = thrustSpinner.getEditor();
        JFormattedTextField jftf = ((JSpinner.DefaultEditor) thrustSpinnerEditor).getTextField();
        jftf.setColumns(16);

        efficiencyLabel = new JLabel("Fuel Efficiency");
        efficiencySpinner = new JSpinner(new SpinnerNumberModel(0d, 0d, Double.MAX_VALUE, 1d));

        //Set width
        Component efficiencySpinnerEditor = efficiencySpinner.getEditor();
        jftf = ((JSpinner.DefaultEditor) efficiencySpinnerEditor).getTextField();
        jftf.setColumns(16);

        JPanel container = new JPanel(new VerticalFlowLayout());

        JPanel anotherContainer = new JPanel(new HorizontalFlowLayout());
        anotherContainer.add(engineTechonolgyLabel);
        anotherContainer.add(engineTechnologyComboBox);
        container.add(anotherContainer);

        anotherContainer = new JPanel(new HorizontalFlowLayout());
        anotherContainer.add(thrustLabel);
        anotherContainer.add(thrustSpinner);
        container.add(anotherContainer);

        anotherContainer = new JPanel(new HorizontalFlowLayout());
        anotherContainer.add(efficiencyLabel);
        anotherContainer.add(efficiencySpinner);
        container.add(anotherContainer);

        add(container);

    }

    @Override
    ShipComponent generateComponent() {
        EngineComponent component = new EngineComponent(gameState);
        component.setThrust((double) thrustSpinner.getValue());
        component.setEfficiency((double) efficiencySpinner.getValue());
        component.setEngineType((ObjectReference) engineTechnologyComboBox.getSelectedItem());
        return component;
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag); //To change body of generated methods, choose Tools | Templates.
        if (aFlag) {
            refreshEngineTechs();
        }
    }

    @Override
    public void clearUI() {
        //Re-add engine techs
        refreshEngineTechs();
        thrustSpinner.setValue(0d);
        efficiencySpinner.setValue(0d);
    }

    private void refreshEngineTechs() {
        engineTechnologyComboBox.removeAllItems();
        for (ObjectReference or : civilization.getEngineTechs()) {
            engineTechnologyComboBox.addItem(or);
        }

        if (engineTechnologyComboBox.getItemCount() > 0) {
            engineTechnologyComboBox.setSelectedIndex(0);
        }
    }
}
