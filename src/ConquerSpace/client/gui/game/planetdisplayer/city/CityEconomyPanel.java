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

package ConquerSpace.client.gui.game.planetdisplayer.city;

import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.client.gui.ObjectListModel;
import ConquerSpace.client.gui.game.planetdisplayer.PlanetCities;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.city.modifier.CityModifier;
import ConquerSpace.common.game.logistics.SupplySegment;
import ConquerSpace.common.game.resources.ResourceStockpile;
import ConquerSpace.common.game.resources.StoreableReference;
import ConquerSpace.common.util.DoubleHashMap;
import ConquerSpace.common.util.Utilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author EhWhoAmI
 */
public class CityEconomyPanel extends JPanel {

    private City selectedCity;
    private GameState gameState;

    private JTabbedPane tabs;
    private JTable resourceTable;
    private JTable resourceDemandTable;
    private JTable resourceInputTable;
    private JTable resourceOutputTable;
    private JTable resourceGenerationTable;
    private JList<String> modifierList;
    private JList<String> connectedCityList;

    private static int selectedTab = 0;

    public CityEconomyPanel(PlanetCities parent, City selectedCity, GameState gameState) {
        this.selectedCity = selectedCity;
        this.gameState = gameState;
        setLayout(new BorderLayout());
        tabs = new JTabbedPane();
        tabs.setFocusable(false);

        ObjectListModel<CityModifier> modifierListModel = new ObjectListModel<>();
        modifierListModel.setElements(selectedCity.cityModifiers);
        modifierListModel.setHandler(l -> {
            return (l.toString());
        });

        modifierList = new JList<>(modifierListModel);

        ObjectListModel<ObjectReference> connectedCityListModel = new ObjectListModel<>();
        for (ObjectReference connection : selectedCity.getSupplyConnections()) {
            SupplySegment seg = gameState.getObject(connection, SupplySegment.class);
            if (!seg.getPoint1().equals(selectedCity.getReference())) {
                connectedCityListModel.addElement(seg.getPoint1());
            } else {
                connectedCityListModel.addElement(seg.getPoint2());
            }
        }
        connectedCityListModel.setHandler(l -> {
            return gameState.getObject(l, City.class).toString();
        });

        connectedCityList = new JList<>(connectedCityListModel);
        connectedCityList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    //Goto the city selected
                    City city
                            = gameState.getObject(connectedCityListModel.getObject(connectedCityList.getSelectedIndex()), City.class);
                    parent.showCity(city);
                }
            }

        });

        resourceTable = new JTable(new StockpileStorageModel());
        resourceTable.getColumnModel().getColumn(2).setCellRenderer(new StockpileResourceDeltaCellRenderer());

        resourceDemandTable = new JTable(new ResourceDemandModel());

        resourceInputTable = new JTable(new StockpileInModel());
        resourceOutputTable = new JTable(new StockpileOutModel());
        resourceGenerationTable = new JTable(new ResourceGenerationTable());

        tabs.add(LOCALE_MESSAGES.getMessage("game.planet.cities.modifiers"), new JScrollPane(modifierList));
        tabs.add(LOCALE_MESSAGES.getMessage("game.planet.cities.linked"), new JScrollPane(connectedCityList));
        tabs.add(LOCALE_MESSAGES.getMessage("game.planet.cities.ledger"), new JScrollPane(resourceTable));
        tabs.add("Resource Demands", new JScrollPane(resourceDemandTable));
        tabs.add("Resources Manufactured", new JScrollPane(resourceGenerationTable));
        tabs.add("Imports", new JScrollPane(resourceInputTable));
        tabs.add("Exports", new JScrollPane(resourceOutputTable));
        tabs.setSelectedIndex(selectedTab);

        tabs.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                selectedTab = tabs.getSelectedIndex();
            }
        });

        //Show table of resources
        add(tabs, BorderLayout.CENTER);
        setBorder(new TitledBorder(new LineBorder(Color.gray), LOCALE_MESSAGES.getMessage("game.planet.cities.economy")));
    }

    private class StockpileStorageModel extends AbstractTableModel {

        String[] colunmNames = {
            LOCALE_MESSAGES.getMessage("game.planet.resources.table.good"),
            LOCALE_MESSAGES.getMessage("game.planet.resources.table.count"),
            LOCALE_MESSAGES.getMessage("game.planet.resources.table.delta", gameState.GameRefreshRate)};

        @Override
        public int getRowCount() {
            if (selectedCity == null) {
                return 0;
            } else {
                return selectedCity.storedTypes().length;
            }
        }

        @Override
        public int getColumnCount() {
            return colunmNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            StoreableReference storedValue = selectedCity.storedTypes()[rowIndex];
            DecimalFormat df = new DecimalFormat("###.##");
            if (selectedCity != null) {
                switch (columnIndex) {
                    case 0:
                        return gameState.getGood(storedValue);
                    case 1:

                        String unitCount = df.format(selectedCity.getResourceAmount(storedValue));

                        if (selectedCity.getResourceAmount(storedValue) > 10000) {
                            unitCount = Utilities.longToHumanString(selectedCity.getResourceAmount(storedValue).longValue());
                        }

                        return unitCount
                                + " units/"
                                //Get mass in kg...
                                + Utilities.longToHumanString((long) ((selectedCity.getResourceAmount(storedValue) * gameState.getGood(storedValue).getMass()) / 1000))
                                + " tons";
                    case 2:
                        HashMap<String, Double> ledger = selectedCity.resourceLedger.get(storedValue);
                        double change = 0;
                        if (ledger != null) {
                            for (Double d : ledger.values()) {
                                change += d;
                            }
                        }
                        return change;
                }
            }
            return 0;
        }

        @Override
        public String getColumnName(int column) {
            return colunmNames[column];
        }
    }

    private class ResourceDemandModel extends AbstractTableModel {

        String[] colunmNames = {
            LOCALE_MESSAGES.getMessage("game.planet.resources.table.good"),
            LOCALE_MESSAGES.getMessage("game.planet.resources.table.count")
        };

        @Override
        public int getRowCount() {
            if (selectedCity == null) {
                return 0;
            } else {
                return selectedCity.resourceDemands.size();
            }
        }

        @Override
        public int getColumnCount() {
            return colunmNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            //Lol needs to be efficient
            StoreableReference storedValue = new ArrayList<StoreableReference>(selectedCity.resourceDemands.keySet()).get(rowIndex);

            if (selectedCity != null) {
                switch (columnIndex) {
                    case 0:
                        return gameState.getGood(storedValue);
                    case 1:
                        return selectedCity.resourceDemands.get(storedValue).toString()
                                + "u/"
                                //Get mass in kg...
                                + (selectedCity.resourceDemands.get(storedValue) * gameState.getGood(storedValue).getMass())
                                + " kg";
                }
            }
            return 0;
        }

        @Override
        public String getColumnName(int column) {
            return colunmNames[column];
        }
    }

    private class ResourceGenerationTable extends AbstractTableModel {

        String[] colunmNames = {
            LOCALE_MESSAGES.getMessage("game.planet.resources.table.good"),
            LOCALE_MESSAGES.getMessage("game.planet.resources.table.count")
        };

        @Override
        public int getRowCount() {
            if (selectedCity == null) {
                return 0;
            } else {
                return selectedCity.getPreviousQuarterProduction().size();
            }
        }

        @Override
        public int getColumnCount() {
            return colunmNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            //Lol needs to be efficient
            StoreableReference storedValue = new ArrayList<>(selectedCity.getPreviousQuarterProduction().keySet()).get(rowIndex);

            if (selectedCity != null) {
                switch (columnIndex) {
                    case 0:
                        return gameState.getGood(storedValue);
                    case 1:
                        return selectedCity.getPreviousQuarterProduction().get(storedValue).toString()
                                + "u/"
                                //Get mass in kg...
                                + (selectedCity.getPreviousQuarterProduction().get(storedValue) * gameState.getGood(storedValue).getMass())
                                + " kg";
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

                    DoubleHashMap<String> map = selectedCity.resourceLedger.get(selectedCity.storedTypes()[row]);
                    if (map != null) {
                        String text = "<html>";
                        for (Map.Entry<String, Double> object : map.entrySet()) {
                            Object key = object.getKey();
                            Double val = object.getValue();

                            text += key + ": ";
                            if (Math.signum(val) == -1) {
                                text += "-";
                            }
                            text += Utilities.longToHumanString(Math.abs(val.longValue()));
                            text += "<br/>";
                        }
                        text += "</html>";
                        l.setToolTipText(text);
                    }
                } catch (NumberFormatException nfe) {

                }
            }
            //Return the JLabel which renders the cell.
            return l;
        }
    }

    private class StockpileOutModel extends AbstractTableModel {

        String[] colunmNames = {"City", "Good", "Amount"};

        ArrayList<ResourcesChangeTuple> tuple;

        public StockpileOutModel() {
            tuple = new ArrayList<>();
            //Get the city
            for (Map.Entry<ResourceStockpile, DoubleHashMap<StoreableReference>> entry : selectedCity.getResourcesSentTo().entrySet()) {
                ResourceStockpile resourceStockpile = entry.getKey();
                DoubleHashMap<StoreableReference> val = entry.getValue();

                for (Map.Entry<StoreableReference, Double> entry1 : val.entrySet()) {
                    StoreableReference reference = entry1.getKey();
                    Double goodCount = entry1.getValue();
                    tuple.add(new ResourcesChangeTuple(resourceStockpile, reference, goodCount));
                }
            }
        }

        @Override
        public int getRowCount() {
            return tuple.size();
        }

        @Override
        public int getColumnCount() {
            return colunmNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            //get thingy
            switch (columnIndex) {
                case 0:
                    return tuple.get(rowIndex).stock;
                case 1:
                    return gameState.getGood(tuple.get(rowIndex).ref).getName();
                case 2:
                    return tuple.get(rowIndex).amount;
            }
            return 0;
        }

        @Override
        public String getColumnName(int column) {
            return colunmNames[column];
        }

    }

    private class StockpileInModel extends AbstractTableModel {

        String[] colunmNames = {"City", "Good", "Amount"};

        ArrayList<ResourcesChangeTuple> tuple;

        public StockpileInModel() {
            tuple = new ArrayList<>();
            //Get the city
            for (Map.Entry<ResourceStockpile, DoubleHashMap<StoreableReference>> entry : selectedCity.getResourcesGainedFrom().entrySet()) {
                ResourceStockpile resourceStockpile = entry.getKey();
                DoubleHashMap<StoreableReference> val = entry.getValue();

                for (Map.Entry<StoreableReference, Double> entry1 : val.entrySet()) {
                    StoreableReference reference = entry1.getKey();
                    Double goodCount = entry1.getValue();
                    tuple.add(new ResourcesChangeTuple(resourceStockpile, reference, goodCount));
                }
            }
        }

        @Override
        public int getRowCount() {
            return tuple.size();
        }

        @Override
        public int getColumnCount() {
            return colunmNames.length;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            //get thingy
            switch (columnIndex) {
                case 0:
                    return tuple.get(rowIndex).stock;
                case 1:
                    return gameState.getGood(tuple.get(rowIndex).ref).getName();
                case 2:
                    return tuple.get(rowIndex).amount;
            }
            return 0;
        }

        @Override
        public String getColumnName(int column) {
            return colunmNames[column];
        }

    }

    class ResourcesChangeTuple {

        ResourceStockpile stock;
        StoreableReference ref;
        Double amount;

        public ResourcesChangeTuple(ResourceStockpile stock, StoreableReference ref, Double amount) {
            this.stock = stock;
            this.ref = ref;
            this.amount = amount;
        }

        public ResourcesChangeTuple() {
        }

    }
}
