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
import ConquerSpace.common.GameState;
import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.common.game.ships.ShipClass;
import ConquerSpace.common.game.ships.components.EngineTechnology;
import ConquerSpace.common.game.ships.components.EngineTemplate;
import ConquerSpace.common.game.ships.components.ShipComponentTemplate;
import ConquerSpace.common.game.ships.hull.Hull;
import ConquerSpace.common.game.ships.hull.HullMaterial;
import ConquerSpace.common.util.names.NameGenerator;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author EhWhoAmI
 */
public class BuildSpaceShipAutomationMenu extends JPanel {

    private Civilization civ;

    private JList<ShipClass> shipList;
    private DefaultListModel<ShipClass> shipListModel;

    JPanel shipDetailsPanel;
    private JLabel shipNameLabel;
    private JTextField shipNameField;
    private JButton randomShipNameButton;

    private JLabel massLabel;
    private JLabel massText;
    private JLabel massUnit;

    private JLabel hullLabel;
    private JCheckBox toDesignHullOrNotToDesign;
    private JButton chooseHullButton;
    private HullChooserDialog hullChooserDialog;

    private JLabel hullNameLabel;
    private JLabel hullSpaceLabel;
    private JLabel hullMaterialLabel;

    private JLabel shipTypeLabel;
    private JComboBox<String> shipTypeComboBox;

    private JLabel shipArmorLabel;

    private JLabel shipShieldLabel;

    //Will be automatically decided
    private JLabel shipSpaceLabel;

    private JLabel shipCargoSpaceLabel;

    private JLabel shipSpeedLabel;
    private JLabel shipSpeedUnit;
    private JSpinner shipSpeedSpinner;

    private JPanel engineStuffPanel;
    private JLabel engineTypeLabel;
    private JButton setEngineButton;
    private JLabel engineTypeNotificationLabel;

    private JLabel fuelCapacityLabel;
    private JLabel fuelCapacityValue;
    private JLabel fuelCapacityUnit;
    private JButton fuelCapicityConfig;

    private JTable shipComponentsTable;

    private JLabel shipSensorsLabel;
    private JButton shipSensorButton;
    private JLabel shipWeaponLabel;
    private JButton shipWeaponButton;
    private JLabel shipScienceLabel;
    private JButton shipScienceButton;

    private JTabbedPane mainTabs;
    private JPanel componentRoughDesignPanel;

    private EngineConfigWindow engineConfigWindow = null;

    //Hull is null means it is self designed
    private Hull selectedHull = null;

    private NameGenerator nameGenerator;

    private EngineTemplate selectedEngine;

    private GameState gameState;

    public BuildSpaceShipAutomationMenu(GameState gameState, Civilization c) {
        this.gameState = gameState;
        this.civ = c;
        setLayout(new BorderLayout());

        try {
            nameGenerator = NameGenerator.getNameGenerator("component.names");
        } catch (IOException ex) {
        }

        JMenuBar menuBar = new JMenuBar();
        JMenu newStuff = new JMenu(LOCALE_MESSAGES.getMessage("game.engineering.ship.classes"));
        JMenuItem newShipClass = new JMenuItem(LOCALE_MESSAGES.getMessage("game.engineering.ship.newclass"));
        JMenuItem saveShipClass = new JMenuItem(LOCALE_MESSAGES.getMessage("game.engineering.ship.design"));

        newShipClass.addActionListener(l -> {
            //Empty all components
            shipSpeedSpinner.setValue(0);
            shipNameField.setText("");
            selectedHull = null;
        });

        saveShipClass.addActionListener(l -> {
            //Create ship class...
            if (selectedHull == null) {
                //Autogenerate...
                //Need to make hull material...
                selectedHull = new Hull(gameState, 100, 100,
                        new HullMaterial(gameState, "Test Hull Material", 10, 10, 10),
                        shipTypeComboBox.getSelectedIndex(), 100, "name");
            }
            //Add hull
            civ.hulls.add(selectedHull.getId());
            ShipClass sc = new ShipClass(gameState, shipNameField.getText(), selectedHull);
            //Add components
            //Autogenerate engine

            civ.shipClasses.add(sc.getId());
        });

        newStuff.add(newShipClass);
        newStuff.add(saveShipClass);
        menuBar.add(newStuff);
        add(menuBar, BorderLayout.NORTH);

        JPanel shipInformationPanel = new JPanel();

        shipInformationPanel.setLayout(new HorizontalFlowLayout());

        //The panel that you cant really change the stuff inside
        shipListModel = new DefaultListModel<>();
        for (Integer sc : c.shipClasses) {
            ShipClass shipClass = gameState.getObject(sc, ShipClass.class);
            shipListModel.addElement(shipClass);
        }
        shipList = new JList<>(shipListModel);
        JScrollPane shipListScrollPane = new JScrollPane(shipList);
        shipInformationPanel.add(shipListScrollPane);

        shipDetailsPanel = new JPanel();
        JPanel shipDetailsContainer = new JPanel(new VerticalFlowLayout());
        {
            shipDetailsPanel.setLayout(new GridBagLayout());
            shipNameLabel = new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.shipname"));
            shipNameField = new JTextField();
            shipNameField.setColumns(16);
            randomShipNameButton = new JButton(LOCALE_MESSAGES.getMessage("game.engineering.ship.randomname"));
            randomShipNameButton.addActionListener(l -> {
                shipNameField.setText(nameGenerator.getName(0));
            });

            massLabel = new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.mass"));
            massText = new JLabel("0");
            massUnit = new JLabel("kg");

            shipTypeLabel = new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.shiptype"));
            Vector<String> v = new Vector<>((gameState.shipTypes.keySet()));
            shipTypeComboBox = new JComboBox<String>(v);
            shipTypeComboBox.addActionListener(l -> {
                //Get selected thing
                String text = (String) shipTypeComboBox.getSelectedItem();
                int id = gameState.shipTypes.get(text);
                id = ((id / 100));
                shipScienceButton.setEnabled(false);

                for (String s : gameState.shipTypeClasses.keySet()) {
                    if (gameState.shipTypeClasses.get(s) == id) {
                        //It is something!
                        if (s.equals("Science")) {
                            shipScienceButton.setEnabled(true);
                        }
                    }
                }
            });

            shipSpeedLabel = new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.speed"));
            shipSpeedUnit = new JLabel("<html>m/s<sup>2</sup></html");
            shipSpeedSpinner = new JSpinner(new SpinnerNumberModel(10, 0, 1000000, 10));

            hullLabel = new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.hull.hull"));
            toDesignHullOrNotToDesign = new JCheckBox(LOCALE_MESSAGES.getMessage("game.engineering.ship.hull.autodesign"));
            toDesignHullOrNotToDesign.setSelected(true);
            toDesignHullOrNotToDesign.addActionListener(l -> {
                if (toDesignHullOrNotToDesign.isSelected()) {
                    chooseHullButton.setEnabled(false);

                } else {
                    chooseHullButton.setEnabled(true);
                }
            });
            chooseHullButton = new JButton(LOCALE_MESSAGES.getMessage("game.engineering.ship.hull.choose"));
            chooseHullButton.addActionListener(l -> {
                //Close and recreate
                if (hullChooserDialog != null) {
                    hullChooserDialog.dispose();
                    hullChooserDialog = null;
                }

                hullChooserDialog = new HullChooserDialog();

                //window.setContentPane(this);
                JComponent comp;
                for (comp = this; comp != null; comp = (JComponent) comp.getParent()) {
                    if (comp instanceof JDesktopPane) {
                        break;
                    }
                }
                JDesktopPane desktop = (JDesktopPane) comp;
                desktop.add(hullChooserDialog);

                hullChooserDialog.setVisible(true);
            });
            chooseHullButton.setEnabled(false);

            hullNameLabel = new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.hull.autodesign.name"));
            hullSpaceLabel = new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.hull.autodesign.volume"));
            hullMaterialLabel = new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.hull.autodesign.material"));

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 0;
            //constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(shipNameLabel, constraints);
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 0;
            //constraints.weighty = 1;
            shipDetailsPanel.add(shipNameField, constraints);
            constraints.gridx = 2;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 0;
            //constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(randomShipNameButton, constraints);

            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 0;
            //constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(massLabel, constraints);
            constraints.gridx = 1;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 0;
            //constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(massText, constraints);
            constraints.gridx = 2;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 0;
            //constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(massUnit, constraints);

            constraints.gridx = 0;
            constraints.gridy = 2;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 0;
            //constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(shipTypeLabel, constraints);
            constraints.gridx = 1;
            constraints.gridy = 2;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 0;
            //constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(shipTypeComboBox, constraints);

            constraints.gridx = 0;
            constraints.gridy = 3;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 0;
            //constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(shipSpeedLabel, constraints);

            constraints.gridx = 1;
            constraints.gridy = 3;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 0;
            //constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(shipSpeedSpinner, constraints);
            constraints.gridx = 2;
            constraints.gridy = 3;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 0;
            //constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(shipSpeedUnit, constraints);
            constraints.gridx = 0;
            constraints.gridy = 4;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 0;
            //constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(hullLabel, constraints);
            constraints.gridx = 1;
            constraints.gridy = 4;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 0;
            //constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(toDesignHullOrNotToDesign, constraints);
            constraints.gridx = 2;
            constraints.gridy = 4;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 0;
            //constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(chooseHullButton, constraints);
            constraints.gridx = 0;
            constraints.gridy = 5;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 0;
            //constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.gridwidth = GridBagConstraints.REMAINDER;
            shipDetailsPanel.add(hullNameLabel, constraints);

            constraints.gridx = 0;
            constraints.gridy = 6;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 0;
            //constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.gridwidth = GridBagConstraints.REMAINDER;
            shipDetailsPanel.add(hullSpaceLabel, constraints);

            constraints.gridx = 0;
            constraints.gridy = 7;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 0;
            //constraints.weighty = 1;
            constraints.gridwidth = GridBagConstraints.REMAINDER;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(hullMaterialLabel, constraints);
            shipDetailsContainer.add(shipDetailsPanel);
        }

        JPanel shipChangablePanel = new JPanel(new GridBagLayout());
        JPanel shipChangablePanelContainer = new JPanel(new GridBagLayout());
        {
            GridBagConstraints constraints = new GridBagConstraints();

            shipArmorLabel = new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.armor.label"));

            shipShieldLabel = new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.shields.label"));

            engineTypeLabel = new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.engines.label"));
            setEngineButton = new JButton(LOCALE_MESSAGES.getMessage("game.engineering.ship.engines.button"));
            setEngineButton.addActionListener(l -> {
                //Close and recreate
                if (engineConfigWindow != null) {
                    engineConfigWindow.dispose();
                    engineConfigWindow = null;
                }

                engineConfigWindow = new EngineConfigWindow();

                //window.setContentPane(this);
                JComponent comp;
                for (comp = this; comp != null; comp = (JComponent) comp.getParent()) {
                    if (comp instanceof JDesktopPane) {
                        break;
                    }
                }
                JDesktopPane desktop = (JDesktopPane) comp;
                desktop.add(engineConfigWindow);
                engineConfigWindow.setVisible(true);
                //Add the thing...
            });
            engineTypeNotificationLabel = new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.engines.none"));

            fuelCapacityLabel = new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.fuel.label"));
            fuelCapacityValue = new JLabel("0");
            fuelCapacityUnit = new JLabel("<html>m<sup>3</sup></html");
            fuelCapicityConfig = new JButton(LOCALE_MESSAGES.getMessage("game.engineering.ship.fuel.configure"));

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 0;
            //constraints.weighty = 1;
            shipChangablePanel.add(shipArmorLabel, constraints);
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 0;
            //constraints.weighty = 1;
            shipChangablePanel.add(new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.nocombat")), constraints);

            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 0;
            //constraints.weighty = 1;
            shipChangablePanel.add(shipShieldLabel, constraints);

            constraints.gridx = 1;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 0;
            //constraints.weighty = 1;
            shipChangablePanel.add(new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.nocombat")), constraints);

            constraints.gridx = 0;
            constraints.gridy = 2;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 0;
            //constraints.weighty = 1;
            shipChangablePanel.add(engineTypeLabel, constraints);

            constraints.gridx = 1;
            constraints.gridy = 2;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 0;
            //constraints.weighty = 1;
            shipChangablePanel.add(setEngineButton, constraints);

            constraints.gridx = 2;
            constraints.gridy = 2;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 1;
            //constraints.weighty = 1;
            shipChangablePanel.add(engineTypeNotificationLabel, constraints);

            constraints.gridx = 0;
            constraints.gridy = 3;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 1;
            //constraints.weighty = 1;
            shipChangablePanel.add(fuelCapacityLabel, constraints);

            constraints.gridx = 1;
            constraints.gridy = 3;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 1;
            //constraints.weighty = 1;
            shipChangablePanel.add(fuelCapacityValue, constraints);

            constraints.gridx = 2;
            constraints.gridy = 3;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 1;
            //constraints.weighty = 1;
            shipChangablePanel.add(fuelCapacityUnit, constraints);

            constraints.gridx = 3;
            constraints.gridy = 3;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 1;
            //constraints.weighty = 1;
            shipChangablePanel.add(fuelCapicityConfig, constraints);
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            shipChangablePanelContainer.add(shipChangablePanel, constraints);
        }
        componentRoughDesignPanel = new JPanel();
        //Format stuff
        {
            componentRoughDesignPanel.setLayout(new GridBagLayout());
            JPanel grandContainer = new JPanel(new GridBagLayout());

            shipSensorsLabel = new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.sensors"));
            shipSensorButton = new JButton(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.sensors.configure"));

            shipScienceLabel = new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.science"));
            shipScienceButton = new JButton(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.science.configure"));
            shipScienceButton.setEnabled(false);

            GridBagConstraints constraints = new GridBagConstraints();

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            grandContainer.add(shipSensorsLabel, constraints);

            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 1;
            //constraints.weighty = 1;
            grandContainer.add(shipSensorButton, constraints);

            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 1;
            //constraints.weighty = 1;
            grandContainer.add(shipScienceLabel, constraints);

            constraints.gridx = 1;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            //constraints.weightx = 1;
            //constraints.weighty = 1;
            grandContainer.add(shipScienceButton, constraints);

            //So that it goes to the top left corner...            
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            componentRoughDesignPanel.add(grandContainer, constraints);
        }

        //Init all final stuff to init. 
        {
            String text = (String) shipTypeComboBox.getSelectedItem();
            int id = gameState.shipTypes.get(text);
            id = ((id / 100));
            shipScienceButton.setEnabled(false);

            for (String s : gameState.shipTypeClasses.keySet()) {
                if (gameState.shipTypeClasses.get(s) == id) {
                    //It is something!
                    if (s.equals("Science")) {
                        shipScienceButton.setEnabled(true);
                    }
                }
            }
        }
        mainTabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        shipInformationPanel.add(shipDetailsContainer);
        shipInformationPanel.add(shipChangablePanelContainer);

        mainTabs.add(LOCALE_MESSAGES.getMessage("game.engineering.ship.info"), shipInformationPanel);
        mainTabs.add(LOCALE_MESSAGES.getMessage("game.engineering.ship.components"), componentRoughDesignPanel);
        add(mainTabs, BorderLayout.CENTER);
    }

    private class EngineConfigWindow extends JInternalFrame {

        private JCheckBox toDesignOrNotToDesign;
        private JButton closeButton;

        private JPanel mainEngineEventPanel;

        private JPanel createEnginePanel;
        private JLabel engineNameLabel;
        private JTextField engineNameField;
        private JComboBox<EngineTechnology> engineTechComboBox;

        private JPanel chooseEnginePanel;
        private JList<EngineComponentWrapper> engineList;
        private DefaultListModel<EngineComponentWrapper> engineListModel;
        private JButton selectEngineButton;

        private JPanel engineInfoPanel;

        private CardLayout cardLayout;

        public EngineConfigWindow() {
            setLayout(new VerticalFlowLayout());
            setTitle(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.engine"));
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            add(new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.engine")));
            toDesignOrNotToDesign = new JCheckBox(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.engine.autodesign"));
            toDesignOrNotToDesign.setSelected(true);

            toDesignOrNotToDesign.addItemListener(l -> {
                //l.getStateChange() == 1 means checked
                if (l.getStateChange() == 1) {
                    cardLayout.show(mainEngineEventPanel, "autodesign");
                    toDesignOrNotToDesign.setText(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.engine.autodesign.yes"));
                } else {
                    cardLayout.show(mainEngineEventPanel, "no");
                    toDesignOrNotToDesign.setText(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.engine.autodesign.no"));
                }
            });
            add(toDesignOrNotToDesign);
            cardLayout = new CardLayout();

            mainEngineEventPanel = new JPanel();
            mainEngineEventPanel.setLayout(cardLayout);
            add(mainEngineEventPanel);

            createEnginePanel = new JPanel(new VerticalFlowLayout());
            createEnginePanel.add(new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.engine.designer")));
            mainEngineEventPanel.add("autodesign", createEnginePanel);

            //Just type of engine, and thrust power i guess
            DefaultComboBoxModel<EngineTechnology> engineTechBoxModel = new DefaultComboBoxModel<>();

            //Add the civ info
            for (Integer t : civ.engineTechs) {
                EngineTechnology technology = gameState.getObject(t, EngineTechnology.class);

                engineTechBoxModel.addElement(technology);
            }
            engineTechComboBox = new JComboBox<>(engineTechBoxModel);
            createEnginePanel.add(new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.engine.tech")));
            createEnginePanel.add(engineTechComboBox);

            //Select engine....
            chooseEnginePanel = new JPanel();
            mainEngineEventPanel.add("no", chooseEnginePanel);
            chooseEnginePanel.setLayout(new VerticalFlowLayout());
            chooseEnginePanel.add(new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.engine.dialog.choose")));

            JPanel engineSelectionPanel = new JPanel();
            engineListModel = new DefaultListModel<>();
            //Fill list
            {
                for (Integer obj : civ.shipComponentList) {
                    ShipComponentTemplate template = gameState.getObject(obj, ShipComponentTemplate.class);
                    if (template instanceof EngineTemplate) {
                        engineListModel.addElement(new EngineComponentWrapper(template));
                    }
                }
            }
            engineList = new JList<>(engineListModel);
            engineList.addListSelectionListener(l -> {
                ShipComponentTemplate componentTemplate = engineList.getSelectedValue().object;
                if (componentTemplate instanceof EngineTemplate) {
                    EngineTemplate engineTemplate = (EngineTemplate) componentTemplate;
                    engineInfoPanel.removeAll();
                    //Add info
                    engineInfoPanel.setLayout(new VerticalFlowLayout());
                    engineInfoPanel.add(new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.engine.dialog.name", engineTemplate.getName())));
                    engineInfoPanel.add(new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.engine.dialog.tech", engineTemplate.getEngineTechnology().getName())));
                    engineInfoPanel.add(new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.engine.dialog.power", engineTemplate.getThrust())));
                    engineInfoPanel.add(new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.engine.dialog.mass", engineTemplate.getMass())));

                    engineInfoPanel.validate();
                    engineInfoPanel.repaint();
                }
            });
            JScrollPane engineListScrollPane = new JScrollPane(engineList);
            //chooseEnginePanel.add(engineListScrollPane);

            engineInfoPanel = new JPanel();
            //chooseEnginePanel.add(engineInfoPanel);

            engineSelectionPanel.setLayout(new GridLayout(1, 2));
            engineSelectionPanel.add(engineListScrollPane);
            engineSelectionPanel.add(engineInfoPanel);
            chooseEnginePanel.add(engineSelectionPanel);
            //End Self design

            //Other panel...
            closeButton = new JButton(LOCALE_MESSAGES.getMessage("gui.close"));
            closeButton.addActionListener(l -> {
                if (toDesignOrNotToDesign.isSelected()) {
                    engineTypeNotificationLabel.setText(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.engine.dialog.seldesign"));
                    engineTypeNotificationLabel.repaint();
                } else {
                    if (engineList.getSelectedValue() != null) {
                        engineTypeNotificationLabel.setText(engineList.getSelectedValue().object.getName());
                        engineTypeNotificationLabel.repaint();
                    }
                }
                dispose();
            });
            add(closeButton);
            setClosable(true);
            setResizable(true);
            pack();
        }

        private class EngineComponentWrapper {

            ShipComponentTemplate object;

            public EngineComponentWrapper(ShipComponentTemplate object) {
                this.object = object;
            }

            @Override
            public String toString() {
                return object.getName();
            }
        }
    }

    private class HullChooserDialog extends JInternalFrame {

        private DefaultListModel<Hull> hullListModel;
        private JList<Hull> hullList;

        private JPanel hullInfoPanel;

        private HullChooserDialog() {
            setTitle(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.hull.dialog.choose"));
            setLayout(new GridLayout(1, 2));
            hullListModel = new DefaultListModel<>();
            for (Integer obj : civ.hulls) {
                Hull hull = gameState.getObject(obj, Hull.class);
                hullListModel.addElement(hull);
            }
            hullList = new JList<>(hullListModel);
            hullList.addListSelectionListener(l -> {
                hullInfoPanel.removeAll();
                //Add components
                hullInfoPanel.setLayout(new VerticalFlowLayout());

                Hull h = hullList.getSelectedValue();
                hullInfoPanel.add(new JLabel(h.getName()));
                hullInfoPanel.add(new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.hull.dialog.material", h.getMaterial().getName())));
                hullInfoPanel.add(new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.hull.dialog.mass", h.getMass())));
                hullInfoPanel.add(new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.hull.dialog.rated", h.getThrust())));
                //hullInfoPanel.add(new JLabel(h()));
                hullInfoPanel.validate();
                hullInfoPanel.repaint();
            });
            JScrollPane scrollPane = new JScrollPane(hullList);
            hullInfoPanel = new JPanel();
            add(scrollPane);
            add(hullInfoPanel);
            setClosable(true);
            setResizable(true);
            pack();
        }
    }
}
