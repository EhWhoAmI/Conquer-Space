package ConquerSpace.gui.game;

import ConquerSpace.game.GameController;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.Ship;
import ConquerSpace.game.universe.ships.ShipClass;
import ConquerSpace.game.universe.ships.components.EngineComponent;
import ConquerSpace.game.universe.ships.components.engine.EngineTechnology;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import org.json.JSONObject;

/**
 *
 * @author zyunl
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

    private JLabel shipTypeLabel;
    private JComboBox<String> shipTypeComboBox;

    private JLabel shipArmorLabel;

    private JLabel shipShieldLabel;

    //Will be automatically decided
    private JLabel shipSpaceLabel;

    private JLabel shipCargoSpaceLabel;

    private JLabel shipSpeedLabel;
    private JLabel shipSpeedText;
    private JLabel shipSpeedUnit;
    private JSlider shipSpeedSlider;

    private JPanel engineStuffPanel;
    private JLabel engineTypeLabel;
    private JButton setEngineButton;

    private JLabel fuelCapacityLabel;
    private JLabel fuelCapacityValue;
    private JLabel fuelCapacityUnit;
    private JButton fuelCapicityConfig;

    private JTable shipComponentsTable;

    private JTabbedPane mainTabs;
    private JTabbedPane componentRoughDesignTabs;

    EngineConfigWindow engineConfigWindow = null;

    public BuildSpaceShipAutomationMenu(Civilization c) {
        this.civ = c;
        setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu newStuff = new JMenu("Classes");
        JMenuItem newShipClass = new JMenuItem("New Ship Class");
        JMenuItem saveShipClass = new JMenuItem("Design components, make hull, create ship class");

        saveShipClass.addActionListener(l -> {
        });

        newStuff.add(newShipClass);
        newStuff.add(saveShipClass);
        menuBar.add(newStuff);
        add(menuBar, BorderLayout.NORTH);

        JPanel shipInformationPanel = new JPanel();

        shipInformationPanel.setLayout(new HorizontalFlowLayout());

        //The panel that you cant really change the stuff inside
        shipListModel = new DefaultListModel<>();
        for (ShipClass sc : c.shipClasses) {
            shipListModel.addElement(sc);
        }
        shipList = new JList<>(shipListModel);
        JScrollPane shipListScrollPane = new JScrollPane(shipList);
        shipInformationPanel.add(shipListScrollPane);

        shipDetailsPanel = new JPanel();
        {
            shipDetailsPanel.setLayout(new GridBagLayout());
            shipNameLabel = new JLabel("Ship Name");
            shipNameField = new JTextField();
            shipNameField.setColumns(16);
            randomShipNameButton = new JButton("Choose random name");

            massLabel = new JLabel("Mass");
            massText = new JLabel("0");
            massUnit = new JLabel("kg");

            shipTypeLabel = new JLabel("Ship Type");
            Vector v = new Vector((GameController.shipTypes.keySet()));
            shipTypeComboBox = new JComboBox<>(v);

            shipSpeedLabel = new JLabel("Speed");
            shipSpeedText = new JLabel("0");
            shipSpeedUnit = new JLabel("<html>m/s<sup>2</sup></html");
            shipSpeedSlider = new JSlider(0, 10000);
            shipSpeedSlider.setValue(0);
            shipSpeedSlider.addChangeListener(l -> {
                shipSpeedText.setText("" + shipSpeedSlider.getValue());
            });

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(shipNameLabel, constraints);
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            shipDetailsPanel.add(shipNameField, constraints);
            constraints.gridx = 2;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(randomShipNameButton, constraints);

            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(massLabel, constraints);
            constraints.gridx = 1;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(massText, constraints);
            constraints.gridx = 2;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(massUnit, constraints);

            constraints.gridx = 0;
            constraints.gridy = 2;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(shipTypeLabel, constraints);
            constraints.gridx = 1;
            constraints.gridy = 2;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(shipTypeComboBox, constraints);

            constraints.gridx = 0;
            constraints.gridy = 3;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(shipSpeedLabel, constraints);
            constraints.gridx = 1;
            constraints.gridy = 3;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(shipSpeedText, constraints);
            constraints.gridx = 2;
            constraints.gridy = 3;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(shipSpeedSlider, constraints);
            constraints.gridx = 3;
            constraints.gridy = 3;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shipDetailsPanel.add(shipSpeedUnit, constraints);
        }
        JPanel shipChangablePanel = new JPanel(new GridBagLayout());
        {
            GridBagConstraints constraints = new GridBagConstraints();

            shipArmorLabel = new JLabel("Armor");

            shipShieldLabel = new JLabel("Shields");

            engineTypeLabel = new JLabel("Engines:");
            setEngineButton = new JButton("Configure Engines");
            setEngineButton.addActionListener(l -> {
                if (engineConfigWindow == null) {
                    engineConfigWindow = new EngineConfigWindow();
                }
                //window.setContentPane(this);
                engineConfigWindow.setVisible(true);
            });

            fuelCapacityLabel = new JLabel("Fuel capacity:");
            fuelCapacityValue = new JLabel("0");
            fuelCapacityUnit = new JLabel("<html>m<sup>3</sup></html");
            fuelCapicityConfig = new JButton("Configure fuel tanks");

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            shipChangablePanel.add(shipArmorLabel, constraints);
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            shipChangablePanel.add(new JLabel("TODO, because no combat"), constraints);

            constraints.gridx = 0;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            shipChangablePanel.add(shipShieldLabel, constraints);

            constraints.gridx = 1;
            constraints.gridy = 1;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            shipChangablePanel.add(new JLabel("TODO, because no combat"), constraints);

            constraints.gridx = 0;
            constraints.gridy = 2;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            shipChangablePanel.add(engineTypeLabel, constraints);

            constraints.gridx = 1;
            constraints.gridy = 2;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            shipChangablePanel.add(setEngineButton, constraints);

            constraints.gridx = 0;
            constraints.gridy = 3;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            shipChangablePanel.add(fuelCapacityLabel, constraints);

            constraints.gridx = 1;
            constraints.gridy = 3;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            shipChangablePanel.add(fuelCapacityValue, constraints);

            constraints.gridx = 2;
            constraints.gridy = 3;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            shipChangablePanel.add(fuelCapacityUnit, constraints);

            constraints.gridx = 3;
            constraints.gridy = 3;
            constraints.anchor = GridBagConstraints.NORTHWEST;
            constraints.weightx = 1;
            constraints.weighty = 1;
            shipChangablePanel.add(fuelCapicityConfig, constraints);
        }
        componentRoughDesignTabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        mainTabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        shipInformationPanel.add(shipDetailsPanel);
        shipInformationPanel.add(shipChangablePanel);

        mainTabs.add("Ship info", shipInformationPanel);
        mainTabs.add("Components", componentRoughDesignTabs);
        add(mainTabs, BorderLayout.CENTER);
    }

    private class EngineConfigWindow extends JDialog {

        private JCheckBox toDesignOrNotToDesign;
        private JButton closeButton;

        private JPanel mainEngineEventPanel;

        private JPanel createEnginePanel;

        private JPanel chooseEnginePanel;
        private JList<EngineComponentWrapper> engineList;
        private DefaultListModel<EngineComponentWrapper> engineListModel;
        private JPanel engineInfoPanel;
        
        private JComboBox<EngineTechnology> engineTechComboBox;

        private CardLayout cardLayout;

        public EngineConfigWindow() {
            setLayout(new VerticalFlowLayout());
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            add(new JLabel("Engine Designer"));
            toDesignOrNotToDesign = new JCheckBox("Autodesign Engines/Use Preexisting engines");
            toDesignOrNotToDesign.setSelected(true);

            toDesignOrNotToDesign.addItemListener(l -> {
                //l.getStateChange() == 1 means checked
                if (l.getStateChange() == 1) {
                    cardLayout.show(mainEngineEventPanel, "autodesign");
                } else {
                    cardLayout.show(mainEngineEventPanel, "no");
                }
            });
            add(toDesignOrNotToDesign);

            mainEngineEventPanel = new JPanel();
            cardLayout = new CardLayout();
            mainEngineEventPanel.setLayout(cardLayout);
            add(mainEngineEventPanel);

            createEnginePanel = new JPanel();
            createEnginePanel.add(new JLabel("Design Engine"));
            mainEngineEventPanel.add("autodesign", createEnginePanel);

            chooseEnginePanel = new JPanel();
            mainEngineEventPanel.add("no", chooseEnginePanel);
            chooseEnginePanel.setLayout(new VerticalFlowLayout());
            chooseEnginePanel.add(new JLabel("Choose Engine"));

            engineListModel = new DefaultListModel<>();
            engineList = new JList<>(engineListModel);
            JScrollPane engineListScrollPane = new JScrollPane(engineList);
            chooseEnginePanel.add(engineListScrollPane);
            
            engineInfoPanel = new JPanel();
            engineInfoPanel.add(new JLabel("Engine"));
            chooseEnginePanel.add(engineInfoPanel);
            
            //Other panel...

            DefaultComboBoxModel<EngineTechnology> engineTechBoxModel = new DefaultComboBoxModel<>();

            //Add the civ info
            for (EngineTechnology t : civ.engineTechs) {
                engineTechBoxModel.addElement(t);
            }
            engineTechComboBox = new JComboBox<>(engineTechBoxModel);
            add(new JLabel("Engine Tech: "));
            //add(engineTechComboBox);

            closeButton = new JButton("Close");
            closeButton.addActionListener(l -> {
                dispose();
            });
            add(closeButton);
            pack();
        }

        private class EngineComponentWrapper {

            JSONObject object;

            @Override
            public String toString() {
                return object.getString("name");
            }
        }
    }

}
