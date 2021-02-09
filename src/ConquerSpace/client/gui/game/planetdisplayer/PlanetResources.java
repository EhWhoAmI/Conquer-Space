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
package ConquerSpace.client.gui.game.planetdisplayer;

import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.actions.Actions;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.resources.Good;
import ConquerSpace.common.game.resources.GoodReference;
import ConquerSpace.common.game.resources.ResourceStockpile;
import ConquerSpace.common.game.resources.ResourceTransfer;
import ConquerSpace.common.game.resources.StoreableReference;
import ConquerSpace.common.game.universe.bodies.Planet;
import ConquerSpace.common.util.DoubleHashMap;
import ConquerSpace.common.util.Utilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * @author EhWhoAmI
 */
public class PlanetResources extends javax.swing.JPanel {

    private Planet p;
    private Civilization c;

    private HashMap<StoreableReference, Double> planetResource;
    private HashMap<StoreableReference, HashMap<String, Double>> planetLedger;

    private ArrayList<ResourceStockpile> stockpiles;

    private StockpileStorageModel storageModel;

    private PlanetResourceTableModel planetModel;

    private ResourceStorageListModel storageList;

    private ResourceStockpile selectedStockpile = null;

    private PlanetInfoSheet parent;

    private GameState gameState;

    private javax.swing.JButton gotoCityButton;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private JTable planetResourceTable;
    private JLabel resourceInputLabel;
    private javax.swing.JComboBox<String> resourceSendCityFromComboBox;
    private javax.swing.JComboBox<String> resourceSendCityToComboBox;
    private javax.swing.JComboBox<String> resourceToTakeComboBox;
    private javax.swing.JSpinner resourcesToTransferSpinner;
    private javax.swing.JList<String> storageJList;
    private JTable storageResources;
    private JLabel to;
    private javax.swing.JButton transferResourceButton;

    /**
     * Creates new form PlanetResources
     */
    public PlanetResources(GameState gameState, Planet p, Civilization c, PlanetInfoSheet parent) {
        this.gameState = gameState;
        this.p = p;
        this.c = c;
        this.parent = parent;
        planetResource = new HashMap<>();
        planetLedger = new HashMap<>();
        stockpiles = new ArrayList<>();
        compileResources();
        planetModel = new PlanetResourceTableModel();
        storageModel = new StockpileStorageModel();
        storageList = new ResourceStorageListModel();

        initComponents();

        //Initialize stockpile selection so that it selects something
        loadSelectedResourceStockpile();

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
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        planetResourceTable = new JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        storageJList = new javax.swing.JList<>();
        jScrollPane6 = new javax.swing.JScrollPane();
        storageResources = new JTable();
        jPanel1 = new javax.swing.JPanel();
        gotoCityButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        resourceToTakeComboBox = new javax.swing.JComboBox<>();
        resourceSendCityFromComboBox = new javax.swing.JComboBox<>();
        resourceSendCityToComboBox = new javax.swing.JComboBox<>();
        jLabel3 = new JLabel();
        resourcesToTransferSpinner = new javax.swing.JSpinner();
        jLabel4 = new JLabel();
        to = new JLabel();
        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel5 = new JLabel();
        resourceInputLabel = new JLabel();
        transferResourceButton = new javax.swing.JButton();

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
                gotoCityButtonActionPerformed();
            }
        });
        jPanel1.add(gotoCityButton, java.awt.BorderLayout.CENTER);
        gotoCityButton.getAccessibleContext().setAccessibleName(LOCALE_MESSAGES.getMessage("game.planet.resources.tabs.cityinfo"));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel3.add(jPanel1, gridBagConstraints);

        //jTabbedPane1.addTab(LOCALE_MESSAGES.getMessage("game.planet.resources.tabs.individual"), jPanel3);
        jPanel2.setLayout(new java.awt.GridBagLayout());

        resourceToTakeComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Empty!"}));
        resourceToTakeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resourceToTakeComboBoxActionPerformed();
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel2.add(resourceToTakeComboBox, gridBagConstraints);

        resourceSendCityFromComboBox.setModel(new ResourceStorageComboBoxModel());
        if (resourceSendCityFromComboBox.getItemCount() > 1) {
            resourceSendCityFromComboBox.setSelectedIndex(0);
        }
        resourceSendCityFromComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resourceSendCityFromComboBoxActionPerformed();
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(resourceSendCityFromComboBox, gridBagConstraints);

        resourceSendCityToComboBox.setModel(new ResourceStorageComboBoxModel());
        if (resourceSendCityToComboBox.getItemCount() > 2) {
            resourceSendCityToComboBox.setSelectedIndex(1);
        }
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(resourceSendCityToComboBox, gridBagConstraints);

        jLabel3.setText("Amount");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanel2.add(jLabel3, gridBagConstraints);

        resourcesToTransferSpinner.setModel(new SpinnerNumberModel());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel2.add(resourcesToTransferSpinner, gridBagConstraints);

        jLabel4.setText("Resource");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanel2.add(jLabel4, gridBagConstraints);

        to.setText("to");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 11, 0, 12);
        jPanel2.add(to, gridBagConstraints);

        jLabel1.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanel2.add(jLabel1, gridBagConstraints);

        jLabel2.setText("Resource");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanel2.add(jLabel2, gridBagConstraints);

        jLabel5.setText("City");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanel2.add(jLabel5, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        jPanel2.add(resourceInputLabel, gridBagConstraints);

        transferResourceButton.setText("Transfer!");
        transferResourceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transferResourceButtonActionPerformed();
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel2.add(transferResourceButton, gridBagConstraints);

        jPanel5.add(jPanel2);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 292, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 397, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(LOCALE_MESSAGES.getMessage("game.planet.resources.tabs.transfer"), jPanel4);

        add(jTabbedPane1, java.awt.BorderLayout.CENTER);
    }

    private void gotoCityButtonActionPerformed() {
        //Select city
        if (selectedStockpile instanceof City) {
            parent.population.showCity(((City) selectedStockpile));
            parent.setSelectedTab(3);
        }
    }

    private void resourceToTakeComboBoxActionPerformed() {
        //Resource to...
        if (resourceSendCityToComboBox.getSelectedIndex() >= 0) {
            ResourceStockpile pileTo = stockpiles.get(resourceSendCityToComboBox.getSelectedIndex());
            ComboBoxModel<String> comboBoxModel = resourceToTakeComboBox.getModel();
            if (comboBoxModel instanceof ResourceValueComboBoxModel) {
                StoreableReference resourceId = ((ResourceValueComboBoxModel) comboBoxModel).list[resourceToTakeComboBox.getSelectedIndex()];
                if (ArrayUtils.contains(pileTo.storedTypes(), resourceId)) {
                    resourceInputLabel.setText(gameState.getGood(resourceId) + " " + pileTo.getResourceAmount(resourceId));
                } else {
                    resourceInputLabel.setText("Does not contain " + gameState.getGood(resourceId));
                }
                //Set the spinner
                ResourceStockpile pile = stockpiles.get(resourceSendCityFromComboBox.getSelectedIndex());
                double maxValue = pile.getResourceAmount(((ResourceValueComboBoxModel) comboBoxModel).list[resourceToTakeComboBox.getSelectedIndex()]);
                SpinnerNumberModel spinnerModel = new SpinnerNumberModel(0d, 0d, maxValue, 1d);

                resourcesToTransferSpinner.setModel(spinnerModel);
            }
        }
    }//GEN-LAST:event_resourceToTakeComboBoxActionPerformed

    private void resourceSendCityFromComboBoxActionPerformed() {
        loadSelectedResourceStockpile();
    }

    private void transferResourceButtonActionPerformed() {
        //Send resources
        ComboBoxModel<String> comboBoxModel = resourceToTakeComboBox.getModel();
        if (comboBoxModel instanceof ResourceValueComboBoxModel) {
            StoreableReference resourceId = ((ResourceValueComboBoxModel) comboBoxModel).list[resourceToTakeComboBox.getSelectedIndex()];
            //ResourceTransportAction act = new ResourceTransportAction(
            //stockpiles.get(resourceSendCityFromComboBox.getSelectedIndex()),
            //stockpiles.get(resourceSendCityToComboBox.getSelectedIndex()),
            //resourceId,
            //(Double) resourcesToTransferSpinner.getValue());
            //c.actionList.add(act);
            ResourceTransfer transferer = new ResourceTransfer(
                    stockpiles.get(resourceSendCityFromComboBox.getSelectedIndex()),
                    stockpiles.get(resourceSendCityToComboBox.getSelectedIndex()),
                    resourceId, (Double) resourcesToTransferSpinner.getValue());
            if (transferer.canTransferResources() == ResourceTransfer.ResourceTransferViability.TRANSFER_POSSIBLE) {
                transferer.doTransferResource();
            } else {
                JOptionPane.showMessageDialog(this, "Insufficient resources to transfer: " + transferer.canTransferResources().toString());
            }

            //Reload things
            loadSelectedResourceStockpile();
        }
    }

    private class ResourceValueComboBoxModel extends DefaultComboBoxModel<String> {

        ResourceStockpile pile;
        StoreableReference[] list;

        public ResourceValueComboBoxModel(StoreableReference[] list, ResourceStockpile pile) {
            this.list = list;
            this.pile = pile;
        }

        @Override
        public String getElementAt(int index) {
            return gameState.getGood(list[index]).getName() + " " + pile.getResourceAmount(list[index]);
        }

        @Override
        public int getSize() {
            return list.length;
        }
    }

    private class StockpileStorageModel extends AbstractTableModel {

        String[] colunmNames = {
            LOCALE_MESSAGES.getMessage("game.planet.resources.table.good"),
            LOCALE_MESSAGES.getMessage("game.planet.resources.table.count"),
            LOCALE_MESSAGES.getMessage("game.planet.resources.table.delta", gameState.GameRefreshRate)};

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
            StoreableReference storedValue = selectedStockpile.storedTypes()[rowIndex];

            if (selectedStockpile != null) {
                switch (columnIndex) {
                    case 0:
                        return gameState.getGood(storedValue);
                    case 1:
                        return selectedStockpile.getResourceAmount(storedValue)
                                + ", or "
                                //Get mass in kg...
                                + (selectedStockpile.getResourceAmount(storedValue) * gameState.getGood(storedValue).getMass())
                                + " kg";
                    case 2:
                        if (selectedStockpile instanceof City) {
                            City c = (City) selectedStockpile;
                            HashMap<String, Double> ledger = c.getResourceLedger().get(storedValue);
                            double change = 0;
                            if (ledger != null) {
                                for (Double d : ledger.values()) {
                                    change += d;
                                }
                            }
                            return change;
                        }
                        return "N/A";
                    default:
                        return "";
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
                        DoubleHashMap map = c.getResourceLedger().get(selectedStockpile.storedTypes()[row]);
                        if (map != null) {
                            l.setToolTipText(map.toString());
                        }
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
            LOCALE_MESSAGES.getMessage("game.planet.resources.table.delta", gameState.GameRefreshRate)};

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
            StoreableReference planetResourceId = GoodReference.INVALID_REFERENCE;
            if (planetResource.keySet().toArray()[rowIndex] instanceof StoreableReference) {
                planetResourceId = (StoreableReference) planetResource.keySet().toArray()[rowIndex];
            }
            if (!planetResourceId.equals(GoodReference.INVALID_REFERENCE)) {
                switch (columnIndex) {
                    case 0:
                        return gameState.getGood(planetResourceId);
                    case 1:
                        int mass = (int) (((planetResource.get(planetResourceId) * gameState.getGood(planetResourceId).getMass())) / 1000);
                        return Utilities.longToHumanString(planetResource.get(planetResourceId).intValue()) + " units, or "
                                + Utilities.longToHumanString(mass) + " tonnes";
                    case 2:
                        //Return the stuff
                        HashMap<String, Double> ledger = planetLedger.get(planetResourceId);
                        double change = 0;
                        for (Double d : ledger.values()) {
                            change += d;
                        }

                        return change;
                    //+ " " + ledger.toString()
                }
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

    private class ResourceStorageComboBoxModel extends DefaultComboBoxModel<String> {

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
        for (ObjectReference cityId : p.cities) {
            City city = gameState.getObject(cityId, City.class);
            StoreableReference[] goods = city.storedTypes();
            //Sort through stuff
            for (StoreableReference g : goods) {
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
                if (city.getResourceLedger().get(g) != null) {
                    for (Map.Entry<String, Double> entry : city.getResourceLedger().get(g).entrySet()) {
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

        if (planetResourceTable != null
                && planetResourceRow > -1
                && planetResourceTable.getRowCount() > planetResourceRow) {
            planetResourceTable.setRowSelectionInterval(planetResourceRow, planetResourceRow);
        }

        if (storageResources != null && stockpileRow > -1 && storageResources.getRowCount() > stockpileRow) {
            storageResources.setRowSelectionInterval(stockpileRow, stockpileRow);
        }
    }

    void selectStockpile(ResourceStockpile c) {
        selectedStockpile = c;
        storageJList.setSelectedIndex(stockpiles.indexOf(c));
        //Refresh table
        storageModel.fireTableDataChanged();
    }

    private void loadSelectedResourceStockpile() {
        ResourceStockpile pile = stockpiles.get(resourceSendCityFromComboBox.getSelectedIndex());
        ResourceValueComboBoxModel valueComboBoxModel = new ResourceValueComboBoxModel(pile.storedTypes(), pile);

        int preselected = resourceToTakeComboBox.getSelectedIndex();
        resourceToTakeComboBox.removeAllItems();
        resourceToTakeComboBox.setModel(valueComboBoxModel);
        if (pile.storedTypes().length > 0 && preselected != -1) {
            resourceToTakeComboBox.setSelectedIndex(0);
            resourceToTakeComboBox.setSelectedIndex(preselected);
        }
    }
}
