package ConquerSpace.gui.game;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.ShipClass;
import ConquerSpace.game.universe.ships.hull.Hull;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.DefaultListModel;
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
public class HullCreator extends JInternalFrame {

    private JMenuBar menubar;

    private JList<Hull> shipHullList;
    private DefaultListModel<Hull> hullListModel;
    private JScrollPane hullScrollPane;

    private JPanel hullViewer;
    private JPanel hullListContainer;
    private JPanel shipComponentsContainer;
    private JPanel rootContainer;

    private JTextField className;
    private JLabel classText;

    private JTable componentTable;

    public HullCreator(Civilization c) {
        setTitle("Create Hull");

        //Set menubar
        menubar = new JMenuBar();
        //Add all the things
        JMenu newStuff = new JMenu("Hulls");
        JMenuItem newHull = new JMenuItem("New Hull");
        JMenuItem saveHull = new JMenuItem("Save Hull");
        newStuff.add(newHull);
        newStuff.add(saveHull);
        setJMenuBar(menubar);

        rootContainer = new JPanel();
        rootContainer.setLayout(new HorizontalFlowLayout(10));

        //Set components
        //Ship class list
        hullListContainer = new JPanel();
        hullListContainer.setLayout(new VerticalFlowLayout());
        hullListModel = new DefaultListModel<>();

        for (Hull sc : c.hulls) {
            hullListModel.addElement(sc);
        }
        shipHullList = new JList<>(hullListModel);
        shipHullList.setVisibleRowCount(16);

        hullScrollPane = new JScrollPane(shipHullList);
        hullScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        hullScrollPane.setSize(30, 310);
        hullListContainer.setLayout(new VerticalFlowLayout());
        hullListContainer.add(hullScrollPane);

        hullListContainer.setPreferredSize(new Dimension(100, hullScrollPane.getHeight() + 10));
        hullListContainer.setBorder(new TitledBorder(new LineBorder(Color.gray), "Classes"));
        rootContainer.add(hullListContainer);

        hullViewer = new JPanel();

        className = new JTextField();
        className.setColumns(16);
        classText = new JLabel("-class");

        hullViewer.add(className);
        hullViewer.add(classText);
        hullViewer.setBorder(new TitledBorder(new LineBorder(Color.gray), "Class Designer"));

        rootContainer.add(hullViewer);

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
