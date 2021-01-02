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

import ConquerSpace.client.gui.ObjectListModel;
import ConquerSpace.common.GameState;
import ConquerSpace.common.Nameable;
import ConquerSpace.common.game.economy.GoodOrder;
import ConquerSpace.common.game.economy.Market;
import ConquerSpace.common.game.resources.StoreableReference;
import ConquerSpace.common.game.universe.bodies.Planet;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author EhWhoAmI
 */
public class PlanetMarket extends JPanel {

    private GameState gameState;
    private JList<String> tradedGoods;
    private ObjectListModel<StoreableReference> tradedGoodsModel;

    private JTable goodOrderSellingTable;
    private DefaultTableModel goodOrderSellingTableModel;
    private JPanel sellOrdersContainer;

    private JTable goodOrderBuyingTable;
    private DefaultTableModel goodOrderBuyingTableModel;
    private JPanel buyOrdersContainer;

    private JPanel chartContainer;

    private JTabbedPane tabs;
    private Market planetMarket;

    private JLabel demandLabel;
    private JLabel supplyLabel;
    private JLabel combinedLabel;

    public PlanetMarket(GameState gameState, Planet planet) {
        this.gameState = gameState;

        setLayout(new BorderLayout());

        planetMarket = gameState.getObject(planet.getPlanetaryMarket(), Market.class);

        tradedGoodsModel = new ObjectListModel<>();
        for (Map.Entry<StoreableReference, ArrayList<GoodOrder>> en : planetMarket.sellOrders.entrySet()) {
            StoreableReference key = en.getKey();
            tradedGoodsModel.addElement(key);
        }

        for (Map.Entry<StoreableReference, ArrayList<GoodOrder>> en : planetMarket.buyOrders.entrySet()) {
            StoreableReference key = en.getKey();

            if (!tradedGoodsModel.contains(key)) {
                tradedGoodsModel.addElement(key);
            }
        }

        tradedGoodsModel.setHandler(r -> {
            return gameState.getGood(r).getName();
        });

        tradedGoods = new JList<>(tradedGoodsModel);
        tradedGoods.addListSelectionListener(l -> {
            update();
        });

        goodOrderSellingTableModel = new DefaultTableModel(new String[]{"Owner", "Amount", "Cost"}, 0);
        goodOrderBuyingTable = new JTable(goodOrderSellingTableModel);
        //Add data
        //Add charts
        chartContainer = new JPanel();

        sellOrdersContainer = new JPanel(new BorderLayout());

        JPanel pan = new JPanel(new BorderLayout());
        pan.add(new JScrollPane(goodOrderBuyingTable), BorderLayout.CENTER);

        sellOrdersContainer.add(pan, BorderLayout.CENTER);

        goodOrderBuyingTableModel = new DefaultTableModel(new String[]{"Owner", "Amount", "Cost"}, 0);
        goodOrderBuyingTable = new JTable(goodOrderBuyingTableModel);

        buyOrdersContainer = new JPanel();
        buyOrdersContainer.add(new JScrollPane(goodOrderBuyingTable));

        //Other stats
        demandLabel = new JLabel();
        supplyLabel = new JLabel();
        combinedLabel = new JLabel();

        JPanel ordersStatsPanel = new JPanel(new VerticalFlowLayout());
        ordersStatsPanel.add(demandLabel);
        ordersStatsPanel.add(supplyLabel);
        ordersStatsPanel.add(combinedLabel);

        //tabs = new JTabbedPane();
        JPanel ordersContainer = new JPanel(new BorderLayout());
        ordersContainer.add(ordersStatsPanel, BorderLayout.NORTH);
        ordersContainer.add(sellOrdersContainer, BorderLayout.WEST);
        ordersContainer.add(buyOrdersContainer, BorderLayout.EAST);
        ordersContainer.add(chartContainer, BorderLayout.SOUTH);

        add(new JScrollPane(tradedGoods), BorderLayout.WEST);
        add(ordersContainer, BorderLayout.CENTER);

    }

    private void update() {
        goodOrderSellingTableModel.setRowCount(0);
        StoreableReference ref = tradedGoodsModel.getObject(tradedGoods.getSelectedIndex());

        demandLabel.setText("Demand: 0");
        supplyLabel.setText("Supply: 0");
        double ratio = 0;
        boolean isZeroSDRatio = false;

        if (planetMarket.sellOrders.containsKey(ref)) {
            for (GoodOrder oir : planetMarket.sellOrders.get(ref)) {
                Object obj = gameState.getObject(oir.getOwner());
                String name = oir.getOwner().toString();
                if (obj instanceof Nameable) {
                    name = ((Nameable) obj).getName();
                }
                goodOrderSellingTableModel.addRow(new Object[]{name, oir.getAmount(), oir.getCost()});
            }
            supplyLabel.setText("Supply: " + planetMarket.supplyMap.get(ref));
            ratio = (double) planetMarket.supplyMap.get(ref);
            isZeroSDRatio = true;
        }

        goodOrderBuyingTableModel.setRowCount(0);

        isZeroSDRatio = false;
        if (planetMarket.buyOrders.containsKey(ref)) {
            for (GoodOrder oir : planetMarket.buyOrders.get(ref)) {
                Object obj = gameState.getObject(oir.getOwner());
                String name = oir.getOwner().toString();
                if (obj instanceof Nameable) {
                    name = ((Nameable) obj).getName();
                }
                goodOrderBuyingTableModel.addRow(new Object[]{name, oir.getAmount(), oir.getCost()});
            }
            demandLabel.setText("Demand: " + planetMarket.demandMap.get(ref));
            if (planetMarket.demandMap.get(ref) > 0) {
                ratio /= (double) planetMarket.demandMap.get(ref);
            }
            isZeroSDRatio = true;
        }

        if (isZeroSDRatio) {
            combinedLabel.setText("S/D ratio: " + ratio);
        }

        chartContainer.removeAll();
        if (planetMarket.historicSDRatio.containsKey(ref)) {

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            int index = 0;

            for (Double sdRatio : planetMarket.historicSDRatio.get(ref)) {
                dataset.addValue((Number) sdRatio, 0, index);
                index++;
            }

            JFreeChart chart = ChartFactory.createLineChart("S/D Ratio", "Time", "Ratio", dataset);

            ChartPanel panel = new ChartPanel(chart);
            chartContainer.add(panel);
        }
    }
}
