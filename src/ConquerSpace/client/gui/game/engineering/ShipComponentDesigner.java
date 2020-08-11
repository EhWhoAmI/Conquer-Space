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

import ConquerSpace.common.Calculators;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.common.game.ships.components.EngineTechnology;
import ConquerSpace.common.game.ships.components.EngineTemplate;
import ConquerSpace.common.game.ships.components.ShipComponentTemplate;
import ConquerSpace.common.game.ships.components.ShipComponentTypes;
import ConquerSpace.common.util.names.NameGenerator;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author EhWhoAmI
 */
public class ShipComponentDesigner extends JPanel {

    private JMenuBar menuBar;

    private DefaultListModel<ShipComponentContainer> componentsListModel;
    private JList<ShipComponentContainer> componentsList;

    private DefaultListModel<ShipComponentTypes> componentTypeListModel;
    private JList<ShipComponentTypes> componentTypeList;

    private JPanel componentPanel;
    private JLabel nameLabel;
    private JTextField nameTextField;
    private JButton selectRandomNameButton;
    private JLabel massLabel;
    private JLabel massText;
    private JLabel massUnit;

    private JPanel upperPanel;
    private JPanel lowerPanel;

    private CardLayout cardLayout;

    private JPanel testComponent;
    private JLabel testComponentLabel;
    private final String TEST_COMPONENT = "test";

    private JPanel scienceComponent;
    private JLabel scienceComponentLabel;
    private final String SCIENCE_COMPONENT = "science";

    private JPanel bridgeComponent;
    private final String BRIDGE_COMPONENT = "bridge";

    private JPanel probeComponent;
    private final String PROBE_COMPONENT = "probe";

    private JPanel engineComponent;
    private JLabel engineTechnologyLabel;
    private DefaultComboBoxModel<EngineTechnology> engineTechBoxModel;
    private JComboBox<EngineTechnology> engineTechBox;
    private JLabel thrustRatingLabel;
    private JSpinner thrustRatingSpinner;
    private final String ENGINE_COMPONENT = "engine";

    private int mass = 0;

    private NameGenerator componentGenerator;

    private GameState gameState;

    @SuppressWarnings("unchecked")
    public ShipComponentDesigner(GameState gameState, Civilization c) {
        this.gameState = gameState;
        setLayout(new BorderLayout());

        try {
            componentGenerator = NameGenerator.getNameGenerator("component.names");
        } catch (IOException ex) {
        }

        menuBar = new JMenuBar();
        JMenu menu = new JMenu("Ship Components");

        JMenuItem saveShipComponents = new JMenuItem("Save Component");
        saveShipComponents.addActionListener(a -> {
            //Create object
            if (!nameTextField.getText().equals("")) {
                ShipComponentTemplate obj = new ShipComponentTemplate(gameState);

                String type = "";
                int rating = 0;
                int mass = 0;
                switch (componentTypeList.getSelectedValue()) {
                    case Test:
                        //
                        mass = 18000;
                        break;
                    case Science:
                        type = "science";
                        break;
                    case Engine:
                        obj = new EngineTemplate(gameState);
                        EngineTechnology tech = (EngineTechnology) engineTechBox.getSelectedItem();
                        ((EngineTemplate) obj).setEngineTechnology(tech);
                        rating = ((int) thrustRatingSpinner.getValue());

                        ((EngineTemplate) obj).setThrust(mass);
                        mass = Calculators.Engine.getEngineMass((int) thrustRatingSpinner.getValue(), tech);

                        break;
                }
                obj.setName(nameTextField.getText());
                //obj.put("id", c.shipComponentList.size());

                obj.setMass(mass);
                c.shipComponentList.add(obj.getReference());
                componentsListModel.addElement(new ShipComponentContainer(obj));
            }
        });

        menu.add(saveShipComponents);
        menuBar.add(menu);
        add(menuBar, BorderLayout.NORTH);

        componentsListModel = new DefaultListModel<>();
        for (ObjectReference obj : c.shipComponentList) {
            ShipComponentTemplate template = gameState.getObject(obj, ShipComponentTemplate.class);
            componentsListModel.addElement(new ShipComponentContainer(template));
        }
        componentsList = new JList(componentsListModel);
        componentsList.isSelectedIndex(0);

        Dimension dim = componentsList.getPreferredSize();
        dim.width = 100;
        componentsList.setPreferredSize(dim);

        componentsList.addListSelectionListener(l -> {
            //Set the component name
            ShipComponentTemplate object = componentsList.getSelectedValue().object;
            nameTextField.setText(object.getName());
            //Set seleceted
            //Get type
            switch (object.getType()) {
                case Test:
                    //Set values
                    cardLayout.show(lowerPanel, TEST_COMPONENT);
                    break;
                case Science:
                    cardLayout.show(lowerPanel, SCIENCE_COMPONENT);
                    break;
                case Engine:
                    EngineTechnology engt = ((EngineTemplate) object).getEngineTechnology();
                    //Set the stuff
                    //Set selectdd thing
                    componentTypeList.setSelectedIndex(4);
                    engineTechBox.setSelectedItem(engt);

                    engineTechBoxModel.removeAllElements();
                    for (ObjectReference techs : c.engineTechs) {
                        EngineTechnology tech = gameState.getObject(techs, EngineTechnology.class);
                        engineTechBoxModel.addElement(tech);
                    }

                    if (engineTechBox.getSelectedItem() != null) {
                        EngineTechnology tech = (EngineTechnology) engineTechBox.getSelectedItem();
                        //Set mass
                        int i = Calculators.Engine.getEngineMass((int) thrustRatingSpinner.getValue(), tech);
                        massText.setText("" + i);
                    }
                    cardLayout.show(lowerPanel, ENGINE_COMPONENT);
                    break;
            }
            //Set mass
            int mass = object.getMass();
            massText.setText("" + mass);
        });

        for (ObjectReference obj : c.shipComponentList) {
            ShipComponentTemplate template = gameState.getObject(obj, ShipComponentTemplate.class);
            componentsListModel.addElement(new ShipComponentContainer(template));
        }

        componentPanel = new JPanel();
        componentPanel.setLayout(new VerticalFlowLayout());

        upperPanel = new JPanel();
        upperPanel.setLayout(new GridLayout(2, 3));

        nameLabel = new JLabel("Name: ");
        nameTextField = new JTextField();
        selectRandomNameButton = new JButton("Get Random Name");

        selectRandomNameButton.addActionListener(l -> {
            nameTextField.setText(componentGenerator.getName(0));
        });

        upperPanel.add(nameLabel);
        upperPanel.add(nameTextField);
        upperPanel.add(selectRandomNameButton);

        //Add other stats
        massLabel = new JLabel("Mass");
        massText = new JLabel("0");
        massUnit = new JLabel("kg");

        upperPanel.add(massLabel);
        upperPanel.add(massText);
        upperPanel.add(massUnit);

        componentPanel.add(upperPanel);

        lowerPanel = new JPanel();
        cardLayout = new CardLayout();
        lowerPanel.setLayout(cardLayout);

        {
            testComponent = new JPanel();
            testComponentLabel = new JLabel("This is a testing component. There is nothing here!");
            testComponent.add(testComponentLabel);
            lowerPanel.add(testComponent, TEST_COMPONENT);
        }

        {
            scienceComponent = new JPanel();
            scienceComponentLabel = new JLabel("Science Component");
            scienceComponent.add(scienceComponentLabel);
            lowerPanel.add(scienceComponent, SCIENCE_COMPONENT);
        }

        {
            engineComponent = new JPanel(new GridLayout(2, 2));
            engineTechnologyLabel = new JLabel("Engine Tech:");
            engineTechBoxModel = new DefaultComboBoxModel<>();
            //Add the civ info
            for (ObjectReference techs : c.engineTechs) {
                EngineTechnology tech = gameState.getObject(techs, EngineTechnology.class);
                engineTechBoxModel.addElement(tech);
            }
            engineTechBox = new JComboBox<>(engineTechBoxModel);
            engineComponent.add(engineTechnologyLabel);
            engineComponent.add(engineTechBox);

            thrustRatingLabel = new JLabel("Thrust Rating (kn)");
            thrustRatingSpinner = new JSpinner(new SpinnerNumberModel(100, 0, Integer.MAX_VALUE, 1));

            thrustRatingSpinner.addChangeListener(a -> {
                //Calculate the mass...
                if (engineTechBox.getSelectedItem() != null) {
                    EngineTechnology tech = (EngineTechnology) engineTechBox.getSelectedItem();
                    //Set mass
                    int i = Calculators.Engine.getEngineMass((int) thrustRatingSpinner.getValue(), tech);
                    massText.setText("" + i);
                }
            });

            engineComponent.add(thrustRatingLabel);
            engineComponent.add(thrustRatingSpinner);
            lowerPanel.add(engineComponent, ENGINE_COMPONENT);
        }
        massText.setText("" + 18000);

        componentPanel.add(lowerPanel);
        componentTypeListModel = new DefaultListModel<>();
        componentTypeList = new JList<>(componentTypeListModel);

        Dimension d = componentTypeList.getPreferredSize();
        d.width = 100;
        componentTypeList.setPreferredSize(d);

        for (ShipComponentTypes s : ShipComponentTypes.values()) {
            componentTypeListModel.addElement(s);
        }
        componentTypeList.setSelectedIndex(0);

        componentTypeList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                //This is the important one
                //Get selected one
                ShipComponentTypes selected = componentTypeList.getSelectedValue();
                //Because the strings in ShipComponentTypes.SHIP_COMPONENT_TYPE_NAMES is 
                //coordinated with the index, so we do not need for much
                //coordination
                switch (selected) {
                    case Test:
                        cardLayout.show(lowerPanel, TEST_COMPONENT);
                        massText.setText("" + 18000);
                        break;
                    case Science:
                        cardLayout.show(lowerPanel, SCIENCE_COMPONENT);
                        break;
                    case Engine:
                        //Update components
                        engineTechBoxModel.removeAllElements();
                        for (ObjectReference techs : c.engineTechs) {
                            EngineTechnology tech = gameState.getObject(techs, EngineTechnology.class);
                            engineTechBoxModel.addElement(tech);
                        }

                        if (engineTechBox.getSelectedItem() != null) {
                            EngineTechnology tech = (EngineTechnology) engineTechBox.getSelectedItem();
                            //Set mass
                            int i = Calculators.Engine.getEngineMass((int) thrustRatingSpinner.getValue(), tech);
                            massText.setText("" + i);
                        }
                        cardLayout.show(lowerPanel, ENGINE_COMPONENT);
                        break;
                }
            }
        });
        add(componentsList, BorderLayout.WEST);
        add(componentPanel, BorderLayout.CENTER);
        add(componentTypeList, BorderLayout.EAST);
        setVisible(true);
        //setSize(100, 100);
    }

    private static class ShipComponentContainer {

        private ShipComponentTemplate object;

        public ShipComponentContainer(ShipComponentTemplate object) {
            this.object = object;
        }

        @Override
        public String toString() {
            return object.getName();
        }
    }
}
