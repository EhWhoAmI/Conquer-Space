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
package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.resources.Good;
import ConquerSpace.game.universe.resources.ResourceStockpile;
import ConquerSpace.game.universe.bodies.Planet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractListModel;
import javax.swing.Timer;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author EhWhoAmI
 */
public class PlanetResources extends javax.swing.JPanel {

    private Planet p;
    private HashMap<Good, Integer> planetResource;
    private ArrayList<ResourceStockpile> stockpiles;

    private StockpileStorageModel storageModel;

    private PlanetResourceTableModel planetModel;
    
    private ResourceStorageListModel storageList;

    private ResourceStockpile selectedStockpile = null;

    /**
     * Creates new form PlanetResources
     */
    public PlanetResources(Planet p) {
        this.p = p;
        planetResource = new HashMap<>();
        stockpiles = new ArrayList<>();
        compileResources();
        planetModel = new PlanetResourceTableModel();
        storageModel = new StockpileStorageModel();
        storageList = new ResourceStorageListModel();
        
        initComponents();

        Timer t = new Timer(1000, l -> {
            compileResources();
        });
        t.setRepeats(true);
        t.start();

        storageJList.addListSelectionListener(l -> {
            selectedStockpile = stockpiles.get(storageJList.getSelectedIndex());
            //Refresh table
            storageModel.fireTableDataChanged();
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        planetResourceTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        storageJList = new javax.swing.JList<>();
        jScrollPane6 = new javax.swing.JScrollPane();
        storageResources = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));

        planetResourceTable.setModel(planetModel);
        jScrollPane1.setViewportView(planetResourceTable);

        jTabbedPane1.addTab("Planet Resources", jScrollPane1);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        storageJList.setModel(storageList);
        jScrollPane5.setViewportView(storageJList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jScrollPane5, gridBagConstraints);

        storageResources.setModel(storageModel);
        jScrollPane6.setViewportView(storageResources);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jScrollPane6, gridBagConstraints);

        jTabbedPane1.addTab("Individual Storages", jPanel3);

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private class StockpileStorageModel extends AbstractTableModel {

        String[] colunmNames = {"Good", "Count"};

        @Override
        public int getRowCount() {
            if (selectedStockpile == null) {
                return 0;
            } else {
                return selectedStockpile.storedTypes().length;
            }
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (selectedStockpile != null) {
                switch (columnIndex) {
                    case 0:
                        return selectedStockpile.storedTypes()[rowIndex];
                    case 1:
                        return selectedStockpile.getResourceAmount(selectedStockpile.storedTypes()[rowIndex]);
                }
            }
            return 0;
        }

        @Override
        public String getColumnName(int column) {
            return colunmNames[column];
        }

    }

    private class PlanetResourceTableModel extends AbstractTableModel {

        String[] colunmNames = {"Good", "Count"};

        @Override
        public int getRowCount() {
            return planetResource.keySet().size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return planetResource.keySet().toArray()[rowIndex];
                case 1:
                    return planetResource.get(planetResource.keySet().toArray()[rowIndex]);
            }
            return 0;
        }

        @Override
        public String getColumnName(int column) {
            return colunmNames[column];
        }

    }

    private class ResourceStorageListModel extends AbstractListModel<String> {

        @Override
        public int getSize() {
            return stockpiles.size();
        }

        @Override
        public String getElementAt(int index) {
            return "Storage";
        }

    }

    private void compileResources() {
        planetResource.clear();
        stockpiles.clear();
        for (Map.Entry<GeographicPoint, Building> entry : p.buildings.entrySet()) {
            GeographicPoint key = entry.getKey();
            Building val = entry.getValue();

            if (val instanceof ResourceStockpile) {
                Good[] goods = ((ResourceStockpile) val).storedTypes();
                //Sort through stuff
                for (Good g : goods) {
                    if (!planetResource.containsKey(g)) {
                        //Add key
                        planetResource.put(g, 0);
                    }
                    int amount = planetResource.get(g);
                    int toAdd = ((ResourceStockpile) val).getResourceAmount(g) + amount;

                    planetResource.put(g, toAdd);
                }
                stockpiles.add(((ResourceStockpile) val));
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable planetResourceTable;
    private javax.swing.JList<String> storageJList;
    private javax.swing.JTable storageResources;
    // End of variables declaration//GEN-END:variables
}
