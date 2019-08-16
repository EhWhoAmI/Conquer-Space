package ConquerSpace.gui.game;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.ShipClass;
import ConquerSpace.game.universe.ships.components.EngineComponent;
import ConquerSpace.game.universe.ships.components.ShipComponent;
import ConquerSpace.game.universe.ships.hull.Hull;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
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
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
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

    private JLabel estimatedAccLabel;
    private JLabel estimatedAcc;
    private JLabel estimatedAccUnit;

    private JPanel installedShipComponentsPanel;
    private JScrollPane shipInstalledComponentsScrollPane;
    private String[] installedShipComponentColunms = {"Name", "Mass", "Space", "Rating Type", "Rating", "Cost", "Type", "Quantity"};
    private JTable installedShipComponents;
    private AddedComponentTableModel installedShipComponentsTableModel;
    private JButton removeInstalledComponent;

    private JTabbedPane componentTableTabs;

    private JTable hullTable;
    private HullTableModel hullTableModel;
    private String[] hullTableColunms = {"Name", "Mass", "Space", "Type", "Rated Thrust", "Material"};
    private JScrollPane hullTableScrollPane;
    private JPanel hullTableContainer;
    private JButton hullSelectorButton;

    private JTable componentTable;
    private String[] componentTableColunms = {"Name", "Mass", "Space", "Rating Type", "Rating", "Cost", "Type"};
    private ComponentTableModel componentTableModel;
    private JScrollPane componentTableScrollPane;
    private JPanel componentTableContainer;
    private JButton selectComponentButton;

    private JTabbedPane shipStuffPanel;
    private Civilization c;

    long hullRatedThrust = 0;
    long preparedThrust = 0;

    long massValue = 0;

    Hull selectedHull = null;

    public ShipDesigner(Civilization c) {
        this.c = c;
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
            if (selectedHull == null) {
                JOptionPane.showMessageDialog(this, "You need to have a hull in order for the ship to work!");
            } else {
                Hull hull = selectedHull;
                if (hull != null) {
                    ShipClass shipClass = new ShipClass(className.getText(), hull);
                    //Add components

                    if (installedShipComponents.getRowCount() > 0) {
                        for (int i = 0; i < installedShipComponents.getRowCount(); i++) {
                            JSONObject v = installedShipComponentsTableModel.get(i);
                            //get component, etc...
                            switch ((String) v.get("type")) {
                                case "engine":
                                    EngineComponent comp;
                                    break;
                            }
                        }
                        //Check for reqired components.... Later
                        shipClass.setEstimatedThrust(preparedThrust);
                        c.shipClasses.add(shipClass);
                        shipClassListModel.addElement(shipClass);
                    } else {
                        //Show alert
                        JOptionPane.showMessageDialog(this, "You need to have a hull in order for the ship to work!");
                    }
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

        shipClassesList.setVisibleRowCount(16);

        shipClassesList.addListSelectionListener(l -> {
            ShipClass s = shipClassesList.getSelectedValue();

            //Set all the values
            mass.setText("" + s.getMass());
            massValue = s.getMass();

            className.setText(s.getName());

            //Thrust
            hullRatedThrust = s.getHull().getThrust();
            preparedThrust = s.getEstimatedThrust();
            thrust.setText("" + s.getEstimatedThrust());

            
            //Set UI
            thrust.setText(preparedThrust + "/" + hullRatedThrust);
            if (preparedThrust > hullRatedThrust) {
                thrust.setForeground(Color.red);
                thrust.setToolTipText("Too much thrust!");
            } else {
                thrust.setForeground(Color.black);
                thrust.setToolTipText("");
            }
            //Set acceleration
            if (massValue > 0) {
                long mn = (preparedThrust) / massValue; //No need to multiply because mass is also in kg
                estimatedAcc.setText("" + NumberFormat.getInstance().format(mn));
            }

            //Set hull
            Hull hull = s.getHull();
            if (hull != null) {
                mass.setText("" + hull.getMass());
                massValue = hull.getMass();
                volume.setText("0/" + hull.getSpace());
                thrust.setText(preparedThrust + "/" + hull.getThrust());
                hullRatedThrust = hull.getThrust();
                if (preparedThrust > hullRatedThrust) {
                    thrust.setForeground(Color.red);
                    thrust.setToolTipText("Too much thrust!");
                } else {
                    thrust.setForeground(Color.black);
                    thrust.setToolTipText("");
                }
                if (massValue > 0) {
                    long mn = (preparedThrust * 1000l) / massValue;
                    estimatedAcc.setText("" + NumberFormat.getInstance().format(mn));
                }
            }

            //Fill tables
            //Oof skip...
            for(ShipComponent sc : s.components) {
                //To json
                JSONObject obj = new JSONObject();
                sc.getMass();
                sc.getCost();
                sc.getName();
                
                sc.getRatingType();
                sc.getRating();
                //installedShipComponentsTableModel.add();
                
            }
        });

        shipClassesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
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
        shipDataPanel.setLayout(new GridLayout(6, 3));

        className = new JTextField();
        className.setColumns(16);
        classText = new JLabel("-class");
        shipTypeNameText = new JLabel("Ship");

        hullTypeTextLabel = new JLabel("Hull: ");
        hullTypeName = new JLabel("");

        massLabel = new JLabel("Mass:");
        mass = new JLabel("" + massValue);
        massUnit = new JLabel("kg");

        volumeLabel = new JLabel("Volume");
        volume = new JLabel("0/0");
        volumeUnit = new JLabel("<html>m<sup>3</sup></html");

        thrustLabel = new JLabel("Thrust");
        thrust = new JLabel("0/0");
        thrustUnit = new JLabel("kn");

        estimatedAccLabel = new JLabel("Estimated Max. Acceleration");
        estimatedAcc = new JLabel("0");
        estimatedAccUnit = new JLabel("<html>m/s<sup>2</sup></html");

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
        shipDataPanel.add(estimatedAccLabel);
        shipDataPanel.add(estimatedAcc);
        shipDataPanel.add(estimatedAccUnit);

        shipClassViewer.add(shipDataPanel);

        installedShipComponentsPanel = new JPanel();
        installedShipComponentsPanel.setLayout(new VerticalFlowLayout());
        installedShipComponentsTableModel = new AddedComponentTableModel();
        installedShipComponents = new JTable(installedShipComponentsTableModel);
        shipInstalledComponentsScrollPane = new JScrollPane(installedShipComponents);
        removeInstalledComponent = new JButton("Remove Component");
        removeInstalledComponent.addActionListener(a -> {
            if (installedShipComponents.getSelectedRow() != -1) {
                //installedShipComponentsTableModel.removeRow(installedShipComponents.getSelectedRow());
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

        componentTableModel = new ComponentTableModel();

        componentTable = new JTable(componentTableModel);
        componentTableScrollPane = new JScrollPane(componentTable);

        hullTableModel = new HullTableModel();
        //Fill the table
        for (Hull h : c.hulls) {
            hullTableModel.add(h);
        }

        hullTable = new JTable(hullTableModel) {
            //Disable cell editing
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        hullTable.getTableHeader().setReorderingAllowed(false);

        selectComponentButton = new JButton("Add selected component");
        selectComponentButton.addActionListener(a -> {
            int row = componentTable.getSelectedRow();
            JSONObject v = componentTableModel.get(row);
            installedShipComponentsTableModel.add(v);
            //Add mass

            massValue += v.getInt("mass");
            mass.setText("" + massValue);
            //Get information
            if (v.get("type").equals("engine")) {
                //Get type
                //Get rating in mn and set value
                preparedThrust = +v.getInt("rating");
                thrust.setText(preparedThrust + "/" + hullRatedThrust);
                if (preparedThrust > hullRatedThrust) {
                    thrust.setForeground(Color.red);
                    thrust.setToolTipText("Too much thrust!");
                } else {
                    thrust.setForeground(Color.black);
                    thrust.setToolTipText("");
                }
                //Set acceleration
                if (massValue > 0) {
                    long mn = (preparedThrust) / massValue; //No need to multiply because mass is also in kg
                    estimatedAcc.setText("" + NumberFormat.getInstance().format(mn));
                }
            }
        });
        componentTableContainer = new JPanel(new VerticalFlowLayout());
        componentTableContainer.add(componentTableScrollPane);
        componentTableContainer.add(selectComponentButton);

        hullTable.getTableHeader().setReorderingAllowed(false);

        hullTableScrollPane = new JScrollPane(hullTable);
        hullSelectorButton = new JButton("Set selected hull");
        hullSelectorButton.addActionListener(a -> {
            int row = hullTable.getSelectedRow();
            //hullTableModel.getValueAt(row, 0);
            hullTypeName.setText(hullTable.getValueAt(row, 0).toString());

            //Calculate all the values needed
            //Get the hull
            Hull hull = hullTableModel.get(row);
            selectedHull = hull;
            if (hull != null) {
                mass.setText("" + hull.getMass());
                massValue = hull.getMass();
                volume.setText("0/" + hull.getSpace());
                thrust.setText(preparedThrust + "/" + hull.getThrust());
                hullRatedThrust = hull.getThrust();
                if (preparedThrust > hullRatedThrust) {
                    thrust.setForeground(Color.red);
                    thrust.setToolTipText("Too much thrust!");
                } else {
                    thrust.setForeground(Color.black);
                    thrust.setToolTipText("");
                }
                if (massValue > 0) {
                    long mn = (preparedThrust * 1000l) / massValue;
                    estimatedAcc.setText("" + NumberFormat.getInstance().format(mn));
                }
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

        shipStuffPanel.addChangeListener(l -> {
            update();
        });
        shipStuffPanel.add("Components", shipContainerThings);

        rootContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        update();
        add(menubar, BorderLayout.NORTH);
        add(shipStuffPanel, BorderLayout.CENTER);

        //const
        //setClosable(true);
        setVisible(true);
        //setResizable(true);
        //setSize(100, 100);
        //pack();
    }

    public void update() {
        for (Hull h : c.hulls) {
            if (!hullTableModel.objects.contains(h)) {
                hullTableModel.add(h);
            }
        }
        hullTableModel.fireTableDataChanged();

        for (JSONObject obj : c.shipComponentList) {
            if (!componentTableModel.objects.contains(obj)) {
                componentTableModel.add(obj);
            }
        }
        componentTableModel.fireTableDataChanged();
        
        //Update ship class list
        for (ShipClass sc : c.shipClasses) {
            shipClassListModel.addElement(sc);
        }
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

    private class ComponentTableModel extends AbstractTableModel {

        private String[] colunms = {"Name", "Mass", "Space", "Rating Type", "Rating", "Cost"};
        private ArrayList<JSONObject> objects;

        public ComponentTableModel() {
            objects = new ArrayList<>();
        }

        @Override
        public int getRowCount() {
            return objects.size();
        }

        @Override
        public int getColumnCount() {
            return colunms.length;
        }

        @Override
        public Object getValueAt(int arg0, int arg1) {
            String key = "";
            switch (arg1) {
                case 0:
                    key = "name";
                    break;
                case 1:
                    key = "mass";
                    break;
                case 2:
                    key = "volume";
                    break;
                case 3:
                    switch (objects.get(arg0).getString("type")) {
                        case "test":
                            return "Testing value";
                        case "engine":
                            return "Thrust(kn)";
                    }
                    break;
                case 4:
                    key = "rating";
                    break;
                case 5:
                    key = "cost";
                    return 0;
                //break;
            }
            return objects.get(arg0).get(key);
        }

        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public JSONObject get(int id) {
            return objects.get(id);
        }

        public void add(JSONObject id) {
            objects.add(id);
        }

        public void empty() {
            objects.clear();
        }

        @Override
        public String getColumnName(int column) {
            return colunms[column];
        }

        @Override
        public Class getColumnClass(int column) {
            return String.class;
        }

        @Override
        public void fireTableDataChanged() {
            super.fireTableDataChanged(); //To change body of generated methods, choose Tools | Templates.
        }
    }

    private class AddedComponentTableModel extends AbstractTableModel {

        private String[] colunms = {"Name", "Mass", "Space", "Rating Type", "Rating", "Cost", "Quantity"};
        private ArrayList<JSONObject> objects;
        private ArrayList<Integer> quantities;

        public AddedComponentTableModel() {
            objects = new ArrayList<>();
            quantities = new ArrayList<>();
        }

        @Override
        public int getRowCount() {
            return objects.size();
        }

        @Override
        public int getColumnCount() {
            return colunms.length;
        }

        @Override
        public Object getValueAt(int arg0, int arg1) {
            String key = "";
            switch (arg1) {
                case 0:
                    key = "name";
                    break;
                case 1:
                    key = "mass";
                    break;
                case 2:
                    key = "volume";
                    break;
                case 3:
                    switch (objects.get(arg0).getString("type")) {
                        case "test":
                            return "Testing value";
                        case "engine":
                            return "Thrust(kn)";
                    }
                    break;
                case 4:
                    key = "rating";
                    break;
                case 5:
                    key = "cost";
                    return 0;
                //break;
                case 6:
                    return quantities.get(arg0);
            }
            return objects.get(arg0).get(key);
        }

        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public JSONObject get(int id) {
            return objects.get(id);
        }

        public int getQuantity(int num) {
            return quantities.get(num);
        }

        public void add(JSONObject id) {
            //System.out.println("");
            if (objects.contains(id)) {
                int index = objects.indexOf(id);
                quantities.set(index, (quantities.get(index) + 1));
            } else {
                objects.add(id);
                quantities.add(1);
            }
            fireTableDataChanged();
        }

        public void empty() {
            objects.clear();
        }

        @Override
        public String getColumnName(int column) {
            return colunms[column];
        }

        @Override
        public Class getColumnClass(int column) {
            return String.class;
        }
    }

    private class HullTableModel extends AbstractTableModel {

        private String[] colunms = {"Name", "Mass", "Space", "Type", "Rated Thrust", "Material"};
        private ArrayList<Hull> objects;

        public HullTableModel() {
            objects = new ArrayList<>();
        }

        @Override
        public int getRowCount() {
            return objects.size();
        }

        @Override
        public int getColumnCount() {
            return colunms.length;
        }

        // {"Name", "Mass", "Space", "Type", "Rated Thrust", "Material"};
        @Override
        public Object getValueAt(int arg0, int arg1) {
            Hull h = objects.get(arg0);
            switch (arg1) {
                case 0:
                    return h.getName();
                case 1:
                    return h.getMass();
                case 2:
                    return h.getSpace();
                case 3:
                    return h.getShipType();
                case 4:
                    return h.getThrust();
                case 5:
                    return h.getMaterial().getName();
                //break;
            }
            return "";
        }

        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public Hull get(int id) {
            return objects.get(id);
        }

        public void add(Hull id) {
            objects.add(id);
        }

        public void empty() {
            objects.clear();
        }

        @Override
        public String getColumnName(int column) {
            return colunms[column];
        }

        @Override
        public Class getColumnClass(int column) {
            return String.class;
        }

        @Override
        public void fireTableDataChanged() {
            super.fireTableDataChanged(); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
