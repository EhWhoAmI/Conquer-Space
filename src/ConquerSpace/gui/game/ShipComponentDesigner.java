package ConquerSpace.gui.game;

import ConquerSpace.game.GameUpdater;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.components.ShipComponentTypes;
import ConquerSpace.game.universe.ships.components.engine.EngineTechnology;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import org.json.JSONObject;

/**
 *
 * @author Zyun
 */
public class ShipComponentDesigner extends JPanel {

    private JMenuBar menuBar;

    private DefaultListModel<ShipComponentContainer> componentsListModel;
    private JList<ShipComponentContainer> componentsList;

    private DefaultListModel<String> componentTypeListModel;
    private JList<String> componentTypeList;

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

    public ShipComponentDesigner(Civilization c) {
        setLayout(new BorderLayout());

        menuBar = new JMenuBar();
        JMenu menu = new JMenu("Ship Components");

        JMenuItem saveShipComponents = new JMenuItem("Save Component");
        saveShipComponents.addActionListener(a -> {
            //Create object
            if (!nameTextField.getText().equals("")) {
                JSONObject obj = new JSONObject();
                obj.put("name", nameTextField.getText());
                obj.put("id", c.shipComponentList.size());
                obj.put("volume", 0);
                String type = "";
                int rating = 0;
                int mass = 0;
                switch (componentTypeList.getSelectedIndex()) {
                    case ShipComponentTypes.TEST_COMPONENT:
                        type = "test";
                        mass = 18000;
                        break;
                    case ShipComponentTypes.SCIENCE_COMPONENT:
                        type = "science";
                        break;
                    case ShipComponentTypes.ENGINE_COMPONENT:
                        type = "engine";
                        obj.put("eng-tech", ((EngineTechnology) engineTechBox.getSelectedItem()).getId());
                        rating = ((int) thrustRatingSpinner.getValue());
                        EngineTechnology tech = (EngineTechnology) engineTechBox.getSelectedItem();

                        mass = GameUpdater.Calculators.Engine.getEngineMass((int) thrustRatingSpinner.getValue(), tech);
                        break;
                }
                obj.put("type", type);

                //What ever, who cares
                obj.put("rating", rating);
                obj.put("cost", 0);
                obj.put("mass", mass);
                c.shipComponentList.add(obj);
                componentsListModel.addElement(new ShipComponentContainer(obj));
            }
        });

        menu.add(saveShipComponents);
        menuBar.add(menu);
        add(menuBar, BorderLayout.NORTH);

        componentsListModel = new DefaultListModel<>();
        componentsList = new JList(componentsListModel);
        componentsList.isSelectedIndex(0);

        Dimension dim = componentsList.getPreferredSize();
        dim.width = 100;
        componentsList.setPreferredSize(dim);

        for (JSONObject obj : c.shipComponentList) {
            componentsListModel.addElement(new ShipComponentContainer(obj));
        }

        componentPanel = new JPanel();
        componentPanel.setLayout(new VerticalFlowLayout());

        upperPanel = new JPanel();
        upperPanel.setLayout(new GridLayout(2, 3));

        nameLabel = new JLabel("Name: ");
        nameTextField = new JTextField();
        selectRandomNameButton = new JButton("Get Random Name");

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
            for (EngineTechnology t : c.engineTechs) {
                engineTechBoxModel.addElement(t);
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
                    int i = GameUpdater.Calculators.Engine.getEngineMass((int) thrustRatingSpinner.getValue(), tech);
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

        for (String s : ShipComponentTypes.SHIP_COMPONENT_TYPE_NAMES) {
            componentTypeListModel.addElement(s);
        }
        componentTypeList.setSelectedIndex(0);

        componentTypeList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                //This is the important one
                //Get selected one
                int selected = componentTypeList.getSelectedIndex();
                //Because the strings in ShipComponentTypes.SHIP_COMPONENT_TYPE_NAMES is 
                //coordinated with the index, so we do not need for much
                //coordination
                switch (selected) {
                    case ShipComponentTypes.TEST_COMPONENT:
                        cardLayout.show(lowerPanel, TEST_COMPONENT);
                        massText.setText("" + 18000);
                        break;
                    case ShipComponentTypes.SCIENCE_COMPONENT:
                        cardLayout.show(lowerPanel, SCIENCE_COMPONENT);
                        break;
                    case ShipComponentTypes.ENGINE_COMPONENT:
                        //Update components
                        engineTechBoxModel.removeAllElements();
                        for (EngineTechnology t : c.engineTechs) {
                            engineTechBoxModel.addElement(t);
                        }

                        if (engineTechBox.getSelectedItem() != null) {
                            EngineTechnology tech = (EngineTechnology) engineTechBox.getSelectedItem();
                            //Set mass
                            int i = GameUpdater.Calculators.Engine.getEngineMass((int) thrustRatingSpinner.getValue(), tech);
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

        private JSONObject object;

        public ShipComponentContainer(JSONObject object) {
            this.object = object;
        }

        @Override
        public String toString() {
            return object.getString("name");
        }
    }
}
