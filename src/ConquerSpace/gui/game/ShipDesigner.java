package ConquerSpace.gui.game;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.ShipClass;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

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
    
    private JTable componentTable;

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

        shipClassViewer = new JPanel();
        
        className = new JTextField();
        className.setColumns(16);
        classText = new JLabel("-class");

        shipClassViewer.add(className);
        shipClassViewer.add(classText);
        shipClassViewer.setBorder(new TitledBorder(new LineBorder(Color.gray), "Class Designer"));
        
        rootContainer.add(shipClassViewer);
        
        shipComponentsContainer = new JPanel();
        componentTable = new JTable();
        shipComponentsContainer.setBorder(new TitledBorder(new LineBorder(Color.gray), "Components"));
        
        shipComponentsContainer.add(componentTable);
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
