package ConquerSpace.gui.game;

import ConquerSpace.game.GameController;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.hull.Hull;
import ConquerSpace.game.universe.ships.hull.HullMaterial;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Comparator;
import java.util.UUID;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.NumberFormatter;

/**
 * Design and create a file
 * @author Zyun
 */
public class HullCreator extends JPanel {

    private JMenuBar menubar;

    private JList<Hull> shipHullList;
    private DefaultListModel<Hull> hullListModel;
    private JScrollPane hullScrollPane;

    private JPanel hullViewer;
    private JPanel hullListContainer;
    private JPanel rootContainer;

    private JTextField className;
    private JLabel classText;
    private JLabel hullComboBoxLabel;
    private JButton selectRandomNameButton;
    private JComboBox<String> hullComboBox;
    private JLabel spaceLabel;
    private JFormattedTextField spaceBox;
    private JLabel meterscubedlabel;
    private JLabel estThrust;
    private JFormattedTextField estThrustField;
    private JLabel meganewtonsText;
    private JLabel massLabel;
    private JFormattedTextField massTextField;
    private JLabel massUnitText;
    private JLabel hullMaterialLabel;
    private JComboBox<HullMaterial> hullMaterialComboBox;
    
    
    private JLabel emptyLabel;
    
    private Civilization c;

    @SuppressWarnings("unchecked")
    public HullCreator(Civilization c) {
        this.c = c;
        //setTitle("Create Hull");
        setLayout(new BorderLayout());
        //Set menubar
        menubar = new JMenuBar();
        //Add all the things
        JMenu newStuff = new JMenu("Hulls");
        JMenuItem newHull = new JMenuItem("New Hull");
        JMenuItem saveHull = new JMenuItem("Save Hull");
        saveHull.addActionListener(a -> {saveHull();});
        newStuff.add(newHull);
        newStuff.add(saveHull);
        menubar.add(newStuff);
        add(menubar, BorderLayout.NORTH);
        //setJMenuBar(menubar);

        rootContainer = new JPanel();
        rootContainer.setLayout(new HorizontalFlowLayout(10));

        NumberFormat longFormat = NumberFormat.getIntegerInstance();

        NumberFormatter formatter = new NumberFormatter(longFormat) {
            @Override
            public Object stringToValue(String text) throws ParseException {
                if (text.equals("")) {
                    return null;
                }
                return super.stringToValue(text);
            }
        };
        formatter.setAllowsInvalid(false);
        formatter.setValueClass(Long.class);
        
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
        className.setText(UUID.randomUUID().toString());
        classText = new JLabel("-type hull");
        emptyLabel = new JLabel("");
        selectRandomNameButton = new JButton("Select random name");
        selectRandomNameButton.setFocusable(false);

        hullMaterialLabel = new JLabel("Material: ");
        Vector hullMatVector = new Vector(c.hullMaterials);
        hullMatVector.sort(Comparator.naturalOrder());
        hullMaterialComboBox = new JComboBox<>(hullMatVector);
        hullMaterialComboBox.setFocusable(false);
        
        massLabel = new JLabel("Mass: ");
        massTextField = new JFormattedTextField(formatter);
        massTextField.setText("1");
        massUnitText = new JLabel("kg");
        
        
        hullComboBoxLabel = new JLabel("Hull for type: ");
        Vector v = new Vector((GameController.shipTypes.keySet()));
        v.sort(Comparator.naturalOrder());
        hullComboBox = new JComboBox<>(v);
        hullComboBox.setFocusable(false);
        JLabel emptyLabel2 = new JLabel("");

        spaceLabel = new JLabel("Hull space");
        spaceBox = new JFormattedTextField(formatter);
        spaceBox.setText("0");
        spaceBox.setColumns(16);
        meterscubedlabel = new JLabel("<html>m<sup>3</sup></html");
        
        estThrust = new JLabel("Rated thrust: ");
        estThrustField = new JFormattedTextField(formatter);
        estThrustField.setText("1");
        estThrustField.setColumns(16);
        meganewtonsText = new JLabel("mn");
        
        
        hullViewer.add(className);
        hullViewer.add(classText);
        hullViewer.add(selectRandomNameButton);
        hullViewer.add(hullComboBoxLabel);
        hullViewer.add(hullComboBox);
        hullViewer.add(emptyLabel2);
        hullViewer.add(hullMaterialLabel);
        hullViewer.add(hullMaterialComboBox);
        hullViewer.add(emptyLabel);
        hullViewer.add(spaceLabel);
        hullViewer.add(spaceBox);
        hullViewer.add(meterscubedlabel);
        hullViewer.add(massLabel);
        hullViewer.add(massTextField);
        hullViewer.add(massUnitText);
        hullViewer.add(estThrust);
        hullViewer.add(estThrustField);
        hullViewer.add(meganewtonsText);
        
        hullViewer.setLayout(new GridLayout(6, 3));
        hullViewer.setBorder(new TitledBorder(new LineBorder(Color.gray), "Hull Designer"));
        rootContainer.add(hullViewer);

        rootContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(rootContainer, BorderLayout.CENTER);
        //const
        //setClosable(true);
        setVisible(true);
        //setResizable(true);
        //setSize(100, 100);
        //pack();
    }

    private void saveHull(){
        //Do the things
        long mass = Long.parseLong(massTextField.getText());
        long space = Long.parseLong(spaceBox.getText());
        long thrust = Long.parseLong(estThrustField.getText());
        int shipType = GameController.shipTypes.get((String)hullComboBox.getSelectedItem());
        HullMaterial material = (HullMaterial)hullMaterialComboBox.getSelectedItem();
        
        Hull hull = new Hull(mass, space, material, shipType, thrust, className.getText());
        hullListModel.addElement(hull);
        c.hulls.add(hull);
    }
}
