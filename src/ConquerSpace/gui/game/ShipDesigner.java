package ConquerSpace.gui.game;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.ShipClass;
import ConquerSpace.game.universe.ships.hull.Hull;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import org.json.JSONObject;

/**
 *
 * @author Zyun
 */
public class ShipDesigner extends JPanel {

    private JMenuBar menubar;

    private JList<ShipClass> shipClassesList;
    private DefaultListModel<ShipClass> shipClassListModel;
    private JScrollPane shipClassesScrollPane;

    private JPanel shipClassViewer;
    private JPanel shipClassesListContainer;
    private JPanel shipComponentsContainer;
    private JPanel rootContainer;
    private JTabbedPane shipDataTabs;

    private JPanel shipDataPanel;
    private JTextField className;
    private JLabel classText;
    private JLabel hullTypeTextLabel;
    private JLabel hullTypeName;
    private JLabel shipTypeNameText;
    private JLabel massLabel;
    private JLabel mass;
    private JLabel massUnit;
    //In the format ( x / total )
    private JLabel volumeLabel;
    private JLabel volume;
    private JLabel volumeUnit;
    private JLabel thrustLabel;
    private JLabel thrust;
    private JLabel thrustUnit;

    private JPanel installedShipComponentsPanel;
    private JScrollPane shipInstalledComponentsScrollPane;
    private String[] installedShipComponentColunms = {"Name", "Mass", "Space", "Rating Type", "Rating", "Cost", "Quantity"};
    private JTable installedShipComponents;
    private DefaultTableModel installedShipComponentsTableModel;
    private JButton removeInstalledComponent;

    private JTabbedPane componentTableTabs;

    private JTable hullTable;
    private DefaultTableModel hullTableModel;
    private String[] hullTableColunms = {"Name", "Mass", "Space", "Type", "Rated Thrust", "Material"};
    private JScrollPane hullTableScrollPane;
    private JPanel hullTableContainer;
    private JButton hullSelectorButton;

    private JTable componentTable;
    private String[] componentTableColunms = {"Name", "Mass", "Space", "Rating Type", "Rating", "Cost"};
    private DefaultTableModel componentTableModel;
    private JScrollPane componentTableScrollPane;
    private JPanel componentTableContainer;
    private JButton selectComponentButton;

    private JTabbedPane shipStuffPanel;

    public ShipDesigner(Civilization c) {
        setLayout(new BorderLayout());
        //Set menubar
        menubar = new JMenuBar();
        //Add all the things
        JMenu newStuff = new JMenu("Classes");
        JMenuItem newShipClass = new JMenuItem("New Ship Class");
        JMenuItem saveShipClass = new JMenuItem("Save Ship Class");
        saveShipClass.addActionListener(a -> {
            //Save the ship class
            ///Get the hull
            int row = hullTable.getSelectedRow();
            if (c.hulls.isEmpty()) {
                JOptionPane.showMessageDialog(this, "You need to have a hull in order for the ship to work!");

            } else {
                Hull hull = c.hulls.stream().findFirst().filter(h -> (h.getName().toLowerCase().
                        equals(hullTable.getValueAt(row, 0).toString().toLowerCase()))).orElseGet(null);
                if (hull != null) {
                    ShipClass shipClass = new ShipClass(className.getText(), hull);
                    //Add components

                    if (installedShipComponents.getRowCount() > 0) {
                        for (int i = 0; i < installedShipComponents.getRowCount(); i++) {
                            installedShipComponents.getValueAt(row, 0);
                            //get component, etc...
                        }
                    }
                    //Check for reqired components.... Later
                    c.shipClasses.add(shipClass);
                    shipClassListModel.addElement(shipClass);
                } else {
                    //Show alert
                    JOptionPane.showMessageDialog(this, "You need to have a hull in order for the ship to work!");
                }
            }
        });

        newStuff.add(newShipClass);
        newStuff.add(saveShipClass);
        menubar.add(newStuff);
        //setJMenuBar(menubar);
        shipStuffPanel = new JTabbedPane();

        rootContainer = new JPanel();
        rootContainer.setLayout(new HorizontalFlowLayout(10));

        //Set components
        //Ship class list
        shipClassesListContainer = new JPanel();
        shipClassesListContainer.setLayout(new BorderLayout());
        shipClassListModel = new DefaultListModel<>();

        for (ShipClass sc : c.shipClasses) {
            shipClassListModel.addElement(sc);
        }
        shipClassesList = new JList<>(shipClassListModel);
        //shipClassesList.setVisibleRowCount(16);

        shipClassesScrollPane = new JScrollPane(shipClassesList);
        shipClassesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        //shipClassesScrollPane.setSize(30, 310);
        shipClassesListContainer.setLayout(new VerticalFlowLayout());
        shipClassesListContainer.add(shipClassesScrollPane, BorderLayout.CENTER);

        shipClassesListContainer.setPreferredSize(new Dimension(100, shipClassesScrollPane.getHeight() + 10));
        shipClassesListContainer.setBorder(new TitledBorder(new LineBorder(Color.gray), "Classes"));
        rootContainer.add(shipClassesListContainer);

        //View the ship class
        shipDataTabs = new JTabbedPane();
        shipClassViewer = new JPanel();

        shipDataPanel = new JPanel();
        shipDataPanel.setLayout(new GridLayout(5, 3));

        className = new JTextField();
        className.setColumns(16);
        classText = new JLabel("-class");
        shipTypeNameText = new JLabel("Ship");

        hullTypeTextLabel = new JLabel("Hull: ");
        hullTypeName = new JLabel("");

        massLabel = new JLabel("Mass:");
        mass = new JLabel("0");
        massUnit = new JLabel("kg");

        volumeLabel = new JLabel("Volume");
        volume = new JLabel("0/0");
        volumeUnit = new JLabel("<html>m<sup>3</sup></html");

        thrustLabel = new JLabel("Thrust");
        thrust = new JLabel("0/0");
        thrustUnit = new JLabel("mn");
        //Ship components

        shipDataPanel.add(className);
        shipDataPanel.add(classText);
        shipDataPanel.add(shipTypeNameText);
        shipDataPanel.add(hullTypeTextLabel);
        shipDataPanel.add(hullTypeName);
        shipDataPanel.add(new JLabel()); // Empty
        shipDataPanel.add(massLabel);
        shipDataPanel.add(mass);
        shipDataPanel.add(massUnit);
        shipDataPanel.add(volumeLabel);
        shipDataPanel.add(volume);
        shipDataPanel.add(volumeUnit);
        shipDataPanel.add(thrustLabel);
        shipDataPanel.add(thrust);
        shipDataPanel.add(thrustUnit);

        shipClassViewer.add(shipDataPanel);

        installedShipComponentsPanel = new JPanel();
        installedShipComponentsPanel.setLayout(new VerticalFlowLayout());
        installedShipComponentsTableModel = new DefaultTableModel(installedShipComponentColunms, 0);
        installedShipComponents = new JTable(installedShipComponentsTableModel);
        shipInstalledComponentsScrollPane = new JScrollPane(installedShipComponents);
        removeInstalledComponent = new JButton("Remove Component");
        removeInstalledComponent.addActionListener(a -> {
            if (installedShipComponents.getSelectedRow() != -1) {
                installedShipComponentsTableModel.removeRow(installedShipComponents.getSelectedRow());
            }
        });
        installedShipComponentsPanel.add(removeInstalledComponent);

        installedShipComponentsPanel.add(shipInstalledComponentsScrollPane);
        installedShipComponentsPanel.setBorder(new TitledBorder(new LineBorder(Color.gray), "Installed Components"));
        //shipDataTabs.add("Ship Components", installedShipComponentsPanel);

        //shipClassViewer.add(shipDataTabs);
        shipClassViewer.setBorder(new TitledBorder(new LineBorder(Color.gray), "Class Designer"));

        rootContainer.add(shipClassViewer);
        shipStuffPanel.add("Ship information", rootContainer);

        //Tables and stuff
        JPanel shipContainerThings = new JPanel();
        shipComponentsContainer = new JPanel();
        componentTableTabs = new JTabbedPane();

        componentTableModel = new DefaultTableModel(componentTableColunms, 0);

        for (JSONObject obj : c.shipComponentList) {
            String[] data = new String[6];
//          private String[] componentTableColunms = {"Name", "Mass", "Space", "Rating Type", "Rating"};

            data[0] = obj.getString("name");
            data[1] = "" + obj.getInt("mass");
            data[2] = "" + obj.getInt("volume");
            data[3] = "" + obj.getInt("rating");
            //Get Rating type
            String s = "";
            switch (obj.getString("type")) {
                case "test":
                    s = "Testing value";
                    break;
            }
            data[4] = s;

            data[5] = "" + obj.getInt("cost");
            componentTableModel.addRow(data);
        }
        componentTable = new JTable(componentTableModel) {
            //Disable cell editing
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        componentTableScrollPane = new JScrollPane(componentTable);

        hullTableModel = new DefaultTableModel(hullTableColunms, 0);
        //Fill the table
        for (Hull h : c.hulls) {
            String[] data = new String[6];
            //Just for notes
            //hullTableColunms = {"Name", "Mass", "Space", "Type", "Rated Thrust", "Material"};
            data[0] = h.getName();
            data[1] = "" + h.getMass();
            data[2] = "" + h.getSpace();
            data[3] = "" + h.getShipType();
            data[4] = "" + h.getThrust();
            data[5] = h.getMaterial().getName();
            hullTableModel.addRow(data);
        }

        hullTable = new JTable(hullTableModel) {
            //Disable cell editing
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        selectComponentButton = new JButton("Add selected component");
        selectComponentButton.addActionListener(a -> {
            int row = componentTable.getSelectedRow();
            Vector v = ((Vector) componentTableModel.getDataVector().elementAt(componentTable.getSelectedRow()));
            installedShipComponentsTableModel.addRow(v.toArray());
        });
        componentTableContainer = new JPanel(new VerticalFlowLayout());
        componentTableContainer.add(componentTableScrollPane);
        componentTableContainer.add(selectComponentButton);

        hullTableScrollPane = new JScrollPane(hullTable);
        hullSelectorButton = new JButton("Set selected hull");
        hullSelectorButton.addActionListener(a -> {
            int row = hullTable.getSelectedRow();
            //hullTableModel.getValueAt(row, 0);
            hullTypeName.setText(hullTable.getValueAt(row, 0).toString());

            //Calculate all the values needed
            //Get the hull
            Hull hull = c.hulls.stream().findFirst().filter(h -> (h.getName().toLowerCase().
                    equals(hullTable.getValueAt(row, 0).toString().toLowerCase()))).orElseGet(null);
            if (hull != null) {
                mass.setText("" + hull.getMass());
                volume.setText("0/" + hull.getSpace());
                thrust.setText("0/" + hull.getThrust());
            }

        });

        hullTableContainer = new JPanel(new VerticalFlowLayout());
        hullTableContainer.add(hullTableScrollPane);
        hullTableContainer.add(hullSelectorButton);
        shipComponentsContainer.setBorder(new TitledBorder(new LineBorder(Color.gray), "Components and hulls"));

        componentTableTabs.addTab("Components", componentTableContainer);
        componentTableTabs.addTab("Hulls", hullTableContainer);

        shipComponentsContainer.add(componentTableTabs);

        shipContainerThings.add(installedShipComponentsPanel);
        shipContainerThings.add(shipComponentsContainer);
        
        shipStuffPanel.add("Components", shipContainerThings);
        rootContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        add(menubar, BorderLayout.NORTH);
        add(shipStuffPanel, BorderLayout.CENTER);

        //const
        //setClosable(true);
        setVisible(true);
        //setResizable(true);
        //setSize(100, 100);
        //pack();
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
