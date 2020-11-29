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
import ConquerSpace.common.game.resources.GoodReference;
import ConquerSpace.common.game.resources.StoreableReference;
import ConquerSpace.common.game.universe.bodies.Planet;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.BoxAndWhiskerCalculator;
import org.jfree.data.statistics.BoxAndWhiskerItem;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerXYDataset;

/**
 *
 * @author EhWhoAmI
 */
public class PlanetMarket extends JPanel {

    GameState gameState;
    JList<String> tradedGoods;
    ObjectListModel<StoreableReference> tradedGoodsModel;

    JTable goodOrderSellingTable;
    DefaultTableModel goodOrderSellingTableModel;

    JPanel chartContainer;

    JPanel sellOrdersContainer;
    JTabbedPane tabs;

    public PlanetMarket(GameState gameState, Planet planet) {
        this.gameState = gameState;
        
        setLayout(new BorderLayout());
        
        Market planetMarket = gameState.getObject(planet.getPlanetaryMarket(), Market.class);

        tradedGoodsModel = new ObjectListModel<>();
        for (Map.Entry<StoreableReference, ArrayList<GoodOrder>> en : planetMarket.sellOrders.entrySet()) {
            StoreableReference key = en.getKey();
            Object val = en.getValue();
            tradedGoodsModel.addElement(key);
        }

        tradedGoodsModel.setHandler(r -> {
            return gameState.getGood(r).getName();
        });
        tradedGoods = new JList<>(tradedGoodsModel);
        tradedGoods.addListSelectionListener(l -> {
            goodOrderSellingTableModel.setRowCount(0);
            StoreableReference ref = tradedGoodsModel.getObject(tradedGoods.getSelectedIndex());

            for (GoodOrder oir : planetMarket.sellOrders.get(ref)) {
                Object obj = gameState.getObject(oir.getOwner());
                String name = oir.getOwner().toString();
                if (obj instanceof Nameable) {
                    name = ((Nameable) obj).getName();
                }
                goodOrderSellingTableModel.addRow(new Object[]{name, oir.getAmount(), oir.getCost()});
            }
        });

        goodOrderSellingTableModel = new DefaultTableModel(new String[]{"Owner", "Amount", "Cost"}, 0);
        //Add data
        //Add charts
        chartContainer = new JPanel();

        sellOrdersContainer = new JPanel(new BorderLayout());

        sellOrdersContainer.add(new JScrollPane(tradedGoods), BorderLayout.WEST);

        JPanel pan = new JPanel(new BorderLayout());
        pan.add(new JScrollPane(new JTable(goodOrderSellingTableModel)), BorderLayout.CENTER);
        pan.add(chartContainer, BorderLayout.SOUTH);
        
        sellOrdersContainer.add(pan, BorderLayout.CENTER);

        tabs = new JTabbedPane();
        tabs.add("Sell Orders", sellOrdersContainer);

        add(tabs, BorderLayout.CENTER);
    }
}
