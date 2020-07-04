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

import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.game.GameController;
import ConquerSpace.game.city.City;
import ConquerSpace.game.resources.Good;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.resources.ResourceStockpile;
import ConquerSpace.util.ResourceLoader;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author EhWhoAmI
 */
public class PlanetResources extends javax.swing.JPanel {

    private Planet p;
    private HashMap<Integer, Double> planetResource;
    private HashMap<Integer, HashMap<String, Double>> planetLedger;

    private ArrayList<ResourceStockpile> stockpiles;

    private StockpileStorageModel storageModel;

    private PlanetResourceTableModel planetModel;

    private ResourceStorageListModel storageList;

    private ResourceStockpile selectedStockpile = null;

    private PlanetInfoSheet parent;

    /**
     * Creates new form PlanetResources
     */
    public PlanetResources(Planet p, PlanetInfoSheet parent) {
        this.p = p;
        this.parent = parent;
        planetResource = new HashMap<>();
        planetLedger = new HashMap<>();
        stockpiles = new ArrayList<>();
        compileResources();
        planetModel = new PlanetResourceTableModel();
        storageModel = new StockpileStorageModel();
        storageList = new ResourceStorageListModel();

        initComponents();

        Timer t = new Timer(1000, l -> {
            if (this.isVisible()) {
                compileResources();
            }
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
        jPanel1 = new javax.swing.JPanel();
        gotoCityButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        jTabbedPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        planetResourceTable.setModel(planetModel);
        planetResourceTable.getColumnModel().getColumn(0).setCellRenderer(new GoodCellRenderer());
        planetResourceTable.getColumnModel().getColumn(2).setCellRenderer(new PlanetResourceCellRenderer());
        planetResourceTable.getTableHeader().setReorderingAllowed(false);
        planetResourceTable.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        planetResourceTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(planetResourceTable);

        jTabbedPane1.addTab(LOCALE_MESSAGES.getMessage("game.planet.resources.tabs.planetresources"), jScrollPane1);

        jPanel3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        storageJList.setModel(storageList);
        storageJList.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jScrollPane5.setViewportView(storageJList);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel3.add(jScrollPane5, gridBagConstraints);

        storageResources.setModel(storageModel);
        storageResources.getColumnModel().getColumn(0).setCellRenderer(new GoodCellRenderer());
        storageResources.getColumnModel().getColumn(2).setCellRenderer(new StockpileResourceDeltaCellRenderer());
        storageResources.getTableHeader().setReorderingAllowed(false);
        storageResources.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        storageResources.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane6.setViewportView(storageResources);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jScrollPane6, gridBagConstraints);

        jPanel1.setLayout(new java.awt.BorderLayout());

        gotoCityButton.setText("Goto City Info");
        gotoCityButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gotoCityButtonActionPerformed(evt);
            }
        });
        jPanel1.add(gotoCityButton, java.awt.BorderLayout.CENTER);
        gotoCityButton.getAccessibleContext().setAccessibleName(LOCALE_MESSAGES.getMessage("game.planet.resources.tabs.cityinfo"));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel3.add(jPanel1, gridBagConstraints);

        jTabbedPane1.addTab(LOCALE_MESSAGES.getMessage("game.planet.resources.tabs.individual"), jPanel3);

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void gotoCityButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gotoCityButtonActionPerformed
        //Select city
        if (selectedStockpile instanceof City) {
            parent.population.showCity(((City) selectedStockpile));
            parent.setSelectedTab(3);
        }
    }//GEN-LAST:event_gotoCityButtonActionPerformed

    private class StockpileStorageModel extends AbstractTableModel {

        String[] colunmNames = {
            LOCALE_MESSAGES.getMessage("game.planet.resources.table.good"),
            LOCALE_MESSAGES.getMessage("game.planet.resources.table.count"),
            LOCALE_MESSAGES.getMessage("game.planet.resources.delta", GameController.GameRefreshRate)};

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
            return colunmNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (selectedStockpile != null) {
                switch (columnIndex) {
                    case 0:
                        return GameController.goodHashMap.get(selectedStockpile.storedTypes()[rowIndex]);
                    case 1:
                        return selectedStockpile.getResourceAmount(selectedStockpile.storedTypes()[rowIndex]) + ", or " + (selectedStockpile.getResourceAmount(selectedStockpile.storedTypes()[rowIndex]) * GameController.goodHashMap.get(selectedStockpile.storedTypes()[rowIndex]).getMass()) + " kg";
                    case 2:
                        if (selectedStockpile instanceof City) {
                            City c = (City) selectedStockpile;
                            HashMap<String, Double> ledger = c.resourceLedger.get(selectedStockpile.storedTypes()[rowIndex]);
                            double change = 0;
                            for (Double d : ledger.values()) {
                                change += d;
                            }
                            return change;
                        }
                        return "N/A";
                }
            }
            return 0;
        }

        @Override
        public String getColumnName(int column) {
            return colunmNames[column];
        }
    }

    class StockpileResourceDeltaCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            //Cells are by default rendered as a JLabel.
            JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            //Get the status for the current row.
            if (col == 2) {
                String stringValue = String.valueOf(value);
                try {
                    double doubleValue = Double.parseDouble(stringValue);
                    //Set bold...
                    if (isSelected) {
                        if (doubleValue > 0) {
                            //Stonks
                            l.setForeground(Color.green);
                            l.setFont(l.getFont().deriveFont(Font.BOLD));
                            l.setText("+" + l.getText() + "\u21e7"); //Stonks!
                        } else if (doubleValue < 0) {
                            //Not stonks
                            l.setForeground(Color.red);
                            l.setFont(l.getFont().deriveFont(Font.BOLD));
                            l.setText(l.getText() + "\u21e9"); //Add downwards arrow :(
                        }
                    } else {
                        if (doubleValue > 0) {
                            //Stonks
                            l.setForeground(new Color(15, 157, 88));
                            l.setFont(l.getFont().deriveFont(Font.BOLD));
                            l.setText("+" + l.getText() + "\u21e7"); //Stonks!
                        } else if (doubleValue < 0) {
                            //Not stonks
                            l.setForeground(new Color(230, 74, 25));
                            l.setFont(l.getFont().deriveFont(Font.BOLD));
                            l.setText(l.getText() + "\u21e9"); //Add downwards arrow :(
                        }
                    }
                    if (selectedStockpile instanceof City) {
                        City c = (City) selectedStockpile;
                        l.setToolTipText(c.resourceLedger.get(selectedStockpile.storedTypes()[row]).toString());
                    }
                } catch (NumberFormatException nfe) {

                }
            }
            //Return the JLabel which renders the cell.
            return l;
        }
    }

    private class GoodCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            //Cells are by default rendered as a JLabel.
            JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            //Get the status for the current row.
            if (value instanceof Good) {
                l.setToolTipText("Density: " + (((Good) value).getMass() / ((Good) value).getVolume()));
            }
            //Return the JLabel which renders the cell.
            return l;
        }
    }

    private class PlanetResourceTableModel extends AbstractTableModel {

        String[] colunmNames = {
            LOCALE_MESSAGES.getMessage("game.planet.resources.table.good"),
            LOCALE_MESSAGES.getMessage("game.planet.resources.table.count"),
            LOCALE_MESSAGES.getMessage("game.planet.resources.delta", GameController.GameRefreshRate)};

        @Override
        public int getRowCount() {
            return planetResource.keySet().size();
        }

        @Override
        public int getColumnCount() {
            return colunmNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return GameController.goodHashMap.get(planetResource.keySet().toArray()[rowIndex]);
                case 1:
                    return planetResource.get(planetResource.keySet().toArray()[rowIndex]) + ", or " + (planetResource.get(planetResource.keySet().toArray()[rowIndex]) * GameController.goodHashMap.get(planetResource.keySet().toArray()[rowIndex]).getMass()) + " kg";
                case 2:
                    //Return the stuff
                    HashMap<String, Double> ledger = planetLedger.get(planetResource.keySet().toArray()[rowIndex]);
                    double change = 0;
                    for (Double d : ledger.values()) {
                        change += d;
                    }

                    return change;
                //+ " " + ledger.toString()
                }
            return 0;
        }

        @Override
        public String getColumnName(int column) {
            return colunmNames[column];
        }
    }

    class PlanetResourceCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            //Cells are by default rendered as a JLabel.
            JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

            //Get the status for the current row.
            if (col == 2) {

                String stringValue = String.valueOf(value);
                try {
                    double doubleValue = Double.parseDouble(stringValue);
                    if (isSelected) {
                        if (doubleValue > 0) {
                            //Stonks
                            l.setForeground(Color.green);
                            l.setFont(l.getFont().deriveFont(Font.BOLD));
                            l.setText("+" + l.getText() + "\u21e7"); //Stonks!
                        } else if (doubleValue < 0) {
                            //Not stonks
                            l.setForeground(Color.red);
                            l.setFont(l.getFont().deriveFont(Font.BOLD));
                            l.setText(l.getText() + "\u21e9"); //Add downwards arrow :(
                        }
                    } else {
                        if (doubleValue > 0) {
                            //Stonks
                            l.setForeground(new Color(15, 157, 88));
                            l.setFont(l.getFont().deriveFont(Font.BOLD));
                            l.setText("+" + l.getText() + "\u21e7"); //Stonks!
                        } else if (doubleValue < 0) {
                            //Not stonks
                            l.setForeground(new Color(230, 74, 25));
                            l.setFont(l.getFont().deriveFont(Font.BOLD));
                            l.setText(l.getText() + "\u21e9"); //Add downwards arrow :(
                        }
                    }

                    l.setToolTipText(planetLedger.get(planetResource.keySet().toArray()[row]).toString());
                } catch (NumberFormatException nfe) {
                }
            }
            //Return the JLabel which renders the cell.
            return l;
        }
    }

    private class ResourceStorageListModel extends AbstractListModel<String> {

        @Override
        public int getSize() {
            return stockpiles.size();
        }

        @Override
        public String getElementAt(int index) {
            ResourceStockpile storage = stockpiles.get(index);
            if (storage instanceof City) {
                return (LOCALE_MESSAGES.getMessage("game.planet.resources.table.storagedesc", ((City) storage).getName()));
            }
            return LOCALE_MESSAGES.getMessage("game.planet.resources.table.storage");
        }

    }

    private void compileResources() {
        planetResource.clear();
        stockpiles.clear();
        planetLedger.clear();
        int planetResourceRow = 0;
        int planetResourceColunm = 0;
        if (planetResourceTable != null) {
            planetResourceRow = planetResourceTable.getSelectedRow();
            planetResourceColunm = planetResourceTable.getSelectedColumn();
        }

        int stockpileRow = 0;
        int stockpileColunm = 0;
        if (storageResources != null) {
            stockpileRow = storageResources.getSelectedRow();
            stockpileColunm = storageResources.getSelectedColumn();
        }
        for (City city : p.cities) {
            Integer[] goods = city.storedTypes();
            //Sort through stuff
            for (Integer g : goods) {
                if (!planetResource.containsKey(g)) {
                    //Add key
                    planetResource.put(g, 0d);
                }
                Double amount = planetResource.get(g);
                Double toAdd = (city.getResourceAmount(g) + amount);

                //Add the ledger
                if (!planetLedger.containsKey(g)) {
                    planetLedger.put(g, new HashMap<>());
                }

                HashMap<String, Double> ledger = planetLedger.get(g);
                if (city.resourceLedger.get(g) != null) {
                    for (Map.Entry<String, Double> entry : city.resourceLedger.get(g).entrySet()) {
                        String key = entry.getKey();
                        Double val = entry.getValue();

                        if (ledger.containsKey(key)) {
                            Double amt = ledger.get(key);
                            ledger.put(key, val + amt);
                        } else {
                            ledger.put(key, val);
                        }
                    }
                }
                planetLedger.put(g, ledger);
                planetResource.put(g, toAdd);
            }
            stockpiles.add(city);
        }

        if (planetModel != null) {
            planetModel.fireTableDataChanged();
        }
        if (storageModel != null) {
            storageModel.fireTableDataChanged();
        }

        if (planetResourceTable != null) {
            if (planetResourceRow > -1 && planetResourceTable.getRowCount() > planetResourceRow) {
                planetResourceTable.setRowSelectionInterval(planetResourceRow, planetResourceRow);
            }
        }

        if (storageResources != null) {
            if (stockpileRow > -1 && storageResources.getRowCount() > stockpileRow) {
                storageResources.setRowSelectionInterval(stockpileRow, stockpileRow);
            }
        }
    }

    void selectStockpile(ResourceStockpile c) {
        selectedStockpile = c;
        storageJList.setSelectedIndex(stockpiles.indexOf(c));
        //Refresh table
        storageModel.fireTableDataChanged();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton gotoCityButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable planetResourceTable;
    javax.swing.JList<String> storageJList;
    private javax.swing.JTable storageResources;
    // End of variables declaration//GEN-END:variables
}
