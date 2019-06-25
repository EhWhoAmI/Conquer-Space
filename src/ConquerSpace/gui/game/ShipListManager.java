package ConquerSpace.gui.game;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.Ship;
import ConquerSpace.game.universe.spaceObjects.Universe;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Zyun
 */
public class ShipListManager extends JPanel {

    private String[] colunms = {"Name", "Class", "Location", "Speed", "Status"};
    private ShipTableModel model;
    private JScrollPane scrollPane;
    private JTable table;

    public ShipListManager(Universe u, Civilization c) {
        //setTitle("All Ships");
        setLayout(new BorderLayout());
        model = new ShipTableModel();

        table = new JTable(model) {
            //Disable cell editing
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };
        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        //pack();
        setVisible(true);
        //setResizable(true);
    }

    //Table model
    private class ShipTableModel extends AbstractTableModel {

        private String[] colunms = {"Name", "Class", "Location", "Speed", "Status"};
        private ArrayList<Ship> objects;

        public ShipTableModel() {
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
            //{"Name", "Class", "Location", "Speed", "Status"};
            Ship ship = objects.get(arg0);

            switch (arg1) {
                case 0:
                    return (ship.getName());
                case 1:
                    return (ship.getShipClass());
                case 2:
                    return (ship.getX() + ", " + ship.getY() + ": " + ship.getLocation());
                case 3:
                    return (ship.getEstimatedThrust());
                case 4:
                    return "Nothing!";
                default:
                    return null;
            }
        }

        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public Ship get(int id) {
            return objects.get(id);
        }

        public void add(Ship id) {
            objects.add(id);
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

        @Override
        public void fireTableDataChanged() {
            super.fireTableDataChanged(); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
