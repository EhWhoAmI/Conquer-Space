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
package ConquerSpace.client.gui.game;

import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.ships.Ship;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * Listing the ships
 *
 * @author EhWhoAmI
 */
public class ShipManager extends JPanel {

    private ShipTableModel model;
    private JScrollPane scrollPane;
    private JTable table;

    private Civilization c;

    private GameState gameState;

    public ShipManager(GameState gameState, Civilization c, SpaceShipOverview shipController) {
        this.c = c;
        this.gameState = gameState;

        setLayout(new BorderLayout());
        model = new ShipTableModel();

        table = new JTable(model) {
            //Disable cell editing
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.getSelectionModel().addListSelectionListener(l -> {
            if (l.getValueIsAdjusting() && table.getSelectedRow() > -1) {
                Ship s = model.get(table.getSelectedRow());
                //Window
                //Just show on a separate window
                shipController.showShip(s);
            }
        });
        table.getTableHeader().setReorderingAllowed(false);

        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        update();
    }

    public void update() {
        //Populate table
        int selected = table.getSelectedRow();
        //model.empty();
        for (ObjectReference shipId : c.getSpaceships()) {
            Ship s = gameState.getObject(shipId, Ship.class);
            //process
            if (!model.objects.contains(s)) {
                model.add(s);
            }
        }
        if (selected > -1) {
            //table.setRowSelectionInterval(selected, 0);
        }
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
