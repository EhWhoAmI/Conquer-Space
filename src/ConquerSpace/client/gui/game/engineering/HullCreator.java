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

import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.ships.Hull;
import ConquerSpace.common.game.ships.ShipType;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
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
 *
 * @author EhWhoAmI
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
    private JComboBox<ShipType> shipTypecomboBox;
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
    private JComboBox<ObjectReference> hullMaterialComboBox;

    private JLabel emptyLabel;

    private Civilization c;

    private GameState gameState;

    @SuppressWarnings("unchecked")
    public HullCreator(GameState gameState, Civilization c) {
        this.c = c;
        this.gameState = gameState;
        //setTitle("Create Hull");
        setLayout(new BorderLayout());
        //Set menubar
        menubar = new JMenuBar();
        //Add all the things
        JMenu newStuff = new JMenu("Hulls");
        JMenuItem newHull = new JMenuItem("New Hull");
        JMenuItem saveHull = new JMenuItem("Save Hull");
        saveHull.addActionListener(a -> {
            saveHull();
        });
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

        for (ObjectReference sc : c.hulls) {
            Hull shipClass = gameState.getObject(sc, Hull.class);

            hullListModel.addElement(shipClass);
        }
        shipHullList = new JList<>(hullListModel);
        shipHullList.setVisibleRowCount(16);

        shipHullList.addListSelectionListener(l -> {
            //Selected hull
            Hull h = shipHullList.getSelectedValue();
            //Set the various values
            massTextField.setText("" + h.getMass());
            spaceBox.setText("" + h.getSpace());
            estThrustField.setText("" + h.getThrust());
            shipTypecomboBox.setSelectedItem(h.getShipType());
            
            className.setText(h.getName());
        });

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
        //Vector v = new Vector((gameState.shipTypes.keySet()));
        //v.sort(Comparator.naturalOrder());
        shipTypecomboBox = new JComboBox<>(Arrays.copyOf(gameState.shipTypes.toArray(), gameState.shipTypes.size(), ShipType[].class));
        shipTypecomboBox.setFocusable(false);
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
        meganewtonsText = new JLabel("kn");

        hullViewer.add(className);
        hullViewer.add(classText);
        hullViewer.add(selectRandomNameButton);
        hullViewer.add(hullComboBoxLabel);
        hullViewer.add(shipTypecomboBox);
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

    private void saveHull() {
        //Do the things
        long mass = Long.parseLong(massTextField.getText().replaceAll(",", ""));
        long space = Long.parseLong(spaceBox.getText().replaceAll(",", ""));
        long thrust = Long.parseLong(estThrustField.getText().replaceAll(",", ""));
        ShipType shipType = (ShipType) shipTypecomboBox.getSelectedItem();
        ObjectReference material = (ObjectReference) hullMaterialComboBox.getSelectedItem();

        Hull hull = new Hull(gameState, mass, space, material, shipType, thrust, className.getText());
        hullListModel.addElement(hull);
        c.hulls.add(hull.getReference());
    }
}
