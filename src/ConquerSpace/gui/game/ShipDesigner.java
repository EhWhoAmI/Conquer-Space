package ConquerSpace.gui.game;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.ShipClass;
import ConquerSpace.game.universe.ships.hull.Hull;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
public class ShipDesigner extends JInternalFrame {

    private JMenuBar menubar;

    private JList<ShipClass> shipClassesList;
    private DefaultListModel<ShipClass> shipClassListModel;
    private JScrollPane shipClassesScrollPane;

    private JPanel shipClassViewer;
    private JPanel shipClassesListContainer;
    private JPanel shipComponentsContainer;
    private JPanel rootContainer;

    private JTextField className;
    private JLabel classText;
    private JLabel hullTypeTextLabel;
    private JLabel hullTypeName;
    private JLabel shipTypeNameText;
    private JLabel mass;

    private JTabbedPane componentTableTabs;

    private JTable hullTable;
    private DefaultTableModel hullTableModel;
    private String[] hullTableColunms = {"Name", "Mass", "Space", "Type", "Rated Thrust", "Material"};
    private JScrollPane hullTableScrollPane;
    private JPanel hullTableContainer;
    private JButton hullSelectorButton;

    private JTable componentTable;
    private String[] componentTableColunms = {"Name"};
    private DefaultTableModel componentTableModel;
    private JScrollPane componentTableScrollPane;

    public ShipDesigner(Civilization c) {
        setTitle("Ship Designer");

        //Set menubar
        menubar = new JMenuBar();
        //Add all the things
        JMenu newStuff = new JMenu("Classes");
        JMenuItem newShipClass = new JMenuItem("New Ship Class");
        JMenuItem saveShipClass = new JMenuItem("Save Ship Class");
        newStuff.add(newShipClass);
        newStuff.add(saveShipClass);
        menubar.add(newStuff);
        setJMenuBar(menubar);

        rootContainer = new JPanel();
        rootContainer.setLayout(new HorizontalFlowLayout(10));

        //Set components
        //Ship class list
        shipClassesListContainer = new JPanel();
        shipClassesListContainer.setLayout(new VerticalFlowLayout());
        shipClassListModel = new DefaultListModel<>();

        for (ShipClass sc : c.shipClasses) {
            shipClassListModel.addElement(sc);
        }
        shipClassesList = new JList<>(shipClassListModel);
        shipClassesList.setVisibleRowCount(16);

        shipClassesScrollPane = new JScrollPane(shipClassesList);
        shipClassesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        shipClassesScrollPane.setSize(30, 310);
        shipClassesListContainer.setLayout(new VerticalFlowLayout());
        shipClassesListContainer.add(shipClassesScrollPane);

        shipClassesListContainer.setPreferredSize(new Dimension(100, shipClassesScrollPane.getHeight() + 10));
        shipClassesListContainer.setBorder(new TitledBorder(new LineBorder(Color.gray), "Classes"));
        rootContainer.add(shipClassesListContainer);

        //View the ship class
        shipClassViewer = new JPanel();
        shipClassViewer.setLayout(new GridLayout(2, 3));

        className = new JTextField();
        className.setColumns(16);
        classText = new JLabel("-class");
        shipTypeNameText = new JLabel("Ship");

        hullTypeTextLabel = new JLabel("Hull: ");
        hullTypeName = new JLabel("");

        shipClassViewer.add(className);
        shipClassViewer.add(classText);
        shipClassViewer.add(shipTypeNameText);
        shipClassViewer.add(hullTypeTextLabel);
        shipClassViewer.add(hullTypeName);

        shipClassViewer.setBorder(new TitledBorder(new LineBorder(Color.gray), "Class Designer"));

        rootContainer.add(shipClassViewer);

        //Tables and stuff
        shipComponentsContainer = new JPanel();
        componentTableTabs = new JTabbedPane();

        componentTableModel = new DefaultTableModel(componentTableColunms, 0);
        
        for(JSONObject obj : c.shipComponentList) {
            String[] data = new String[1];
            data[0] = obj.getString("name");
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

        hullTableScrollPane = new JScrollPane(hullTable);
        hullSelectorButton = new JButton("Set selected hull");
        hullSelectorButton.addActionListener(a -> {
            int row = hullTable.getSelectedRow();
            //hullTableModel.getValueAt(row, 0);
            hullTypeName.setText(hullTable.getValueAt(row, 0).toString());
        });

        hullTableContainer = new JPanel(new VerticalFlowLayout());
        hullTableContainer.add(hullTableScrollPane);
        hullTableContainer.add(hullSelectorButton);
        shipComponentsContainer.setBorder(new TitledBorder(new LineBorder(Color.gray), "Components and hulls"));

        componentTableTabs.addTab("Components", componentTableScrollPane);
        componentTableTabs.addTab("Hulls", hullTableContainer);

        shipComponentsContainer.add(componentTableTabs);

        rootContainer.add(shipComponentsContainer);

        rootContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(rootContainer);
        //const
        setClosable(true);
        setVisible(true);
        setResizable(true);
        //setSize(100, 100);
        pack();
    }
}
