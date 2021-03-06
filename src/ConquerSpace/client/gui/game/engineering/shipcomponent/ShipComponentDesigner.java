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

import ConquerSpace.client.gui.ObjectListModel;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.ships.components.ShipComponent;
import ConquerSpace.common.game.ships.components.ShipComponentType;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;

/**
 *
 * @author EhWhoAmI
 */
public class ShipComponentDesigner extends JPanel {

    private GameState gameState;
    private JButton saveComponentButton;
    private JButton newComponentButton;

    private ObjectListModel<ObjectReference> shipComponentListModel;
    private JList<String> shipComponentList;

    private JPanel componentInformationPanel;
    private JPanel componentCustomizationPanel;

    private DefaultComboBoxModel<ShipComponentType> shipComponentTypeComboBoxModel;
    private JComboBox<ShipComponentType> shipComponentTypes;

    private JTextField shipComponentName;
    CardLayout layout;

    ArrayList<ShipComponentDesignerPanel> designerPanels;

    @SuppressWarnings("unchecked")
    public ShipComponentDesigner(GameState gameState, Civilization c) {
        this.gameState = gameState;

        designerPanels = new ArrayList<>();
        setLayout(new BorderLayout());

        JToolBar toolBar = new JToolBar();
        saveComponentButton = new JButton("Save Component");
        saveComponentButton.addActionListener(l -> {
            for (Component comp : componentCustomizationPanel.getComponents()) {
                if (comp.isVisible() && comp instanceof ShipComponentDesignerPanel) {
                    //It's what we want
                    ShipComponent ref = ((ShipComponentDesignerPanel) comp).generateComponent();
                    if (ref != null && !shipComponentName.getText().isEmpty()) {
                        ref.setName(shipComponentName.getText());
                        //Add ship component
                        c.getShipComponentList().add(ref.getReference());
                        shipComponentList.updateUI();

                        clearUI();
                    }
                    break;
                }
            }
        });
        toolBar.add(saveComponentButton);
        newComponentButton = new JButton("New Component");
        newComponentButton.addActionListener(l -> {
            clearUI();
        });
        toolBar.add(newComponentButton);

        shipComponentListModel = new ObjectListModel<>();
        shipComponentListModel.setElements(c.getShipComponentList());
        shipComponentListModel.setHandler(l -> {
            return gameState.getObject(l, ShipComponent.class).getName();
        });

        shipComponentList = new JList<>(shipComponentListModel);
        shipComponentList.addListSelectionListener(l -> {
            ObjectReference shipComponentId = shipComponentListModel.getObject(shipComponentList.getSelectedIndex());
            ShipComponent component = gameState.getObject(shipComponentId, ShipComponent.class);
            //Set name
            shipComponentName.setText(component.getName());
            //Set the type
            layout.show(componentCustomizationPanel, component.getShipComponentType().toString());
            shipComponentTypeComboBoxModel.setSelectedItem(component.getShipComponentType());
            //Load UI
            for (ShipComponentDesignerPanel panel : designerPanels) {
                panel.loadComponent(component);
            }
        });
        shipComponentList.setFixedCellWidth(250);

        componentInformationPanel = new JPanel();
        componentInformationPanel.setLayout(new VerticalFlowLayout());

        shipComponentTypeComboBoxModel = new DefaultComboBoxModel<>(ShipComponentType.values());
        shipComponentTypes = new JComboBox<>(shipComponentTypeComboBoxModel);
        shipComponentTypes.addActionListener(l -> {
            //Set selected panel
            layout.show(componentCustomizationPanel, shipComponentTypes.getSelectedItem().toString());
        });

        JPanel typeContainerPanel = new JPanel(new HorizontalFlowLayout());
        typeContainerPanel.add(new JLabel("Ship Component Type: "));
        typeContainerPanel.add(shipComponentTypes);

        componentInformationPanel.add(typeContainerPanel);

        JPanel namecontainerPanel = new JPanel(new HorizontalFlowLayout());
        namecontainerPanel.add(new JLabel("Component Name: "));
        shipComponentName = new JTextField(32);
        namecontainerPanel.add(shipComponentName);
        componentInformationPanel.add(namecontainerPanel);

        componentCustomizationPanel = new JPanel();

        layout = new CardLayout();
        componentCustomizationPanel.setLayout(layout);

        //Add the various things
        EngineDesignerPanel engineDesignerPanel = new EngineDesignerPanel(gameState, c);
        designerPanels.add(engineDesignerPanel);
        componentCustomizationPanel.add(engineDesignerPanel, ShipComponentType.Engine.toString());
        PowerSupplyDesignerPanel psdp = new PowerSupplyDesignerPanel();
        designerPanels.add(psdp);
        componentCustomizationPanel.add(psdp, ShipComponentType.PowerSupply.toString());
        CrewComponentDesignerPanel ccdp = new CrewComponentDesignerPanel(gameState);
        designerPanels.add(ccdp);
        componentCustomizationPanel.add(ccdp, ShipComponentType.Crew.toString());
        ToOrbitEngineDesignerPanel toedp = new ToOrbitEngineDesignerPanel(gameState, c);
        designerPanels.add(toedp);
        componentCustomizationPanel.add(toedp, ShipComponentType.ToOrbit.toString());

        componentInformationPanel.add(componentCustomizationPanel);
        JPanel container = new JPanel(new VerticalFlowLayout());
        container.add(componentInformationPanel);
        add(toolBar, BorderLayout.NORTH);
        add(new JScrollPane(shipComponentList), BorderLayout.WEST);
        add(container, BorderLayout.CENTER);
    }

    private void clearUI() {
        //Clear UI
        shipComponentName.setText("");
        for (ShipComponentDesignerPanel panel : designerPanels) {
            panel.clearUI();
        }
    }
}
