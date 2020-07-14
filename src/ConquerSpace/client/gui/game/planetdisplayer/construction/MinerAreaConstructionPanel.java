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
package ConquerSpace.client.gui.game.planetdisplayer.construction;

import ConquerSpace.server.GameController;
import ConquerSpace.common.GameState;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.city.area.Area;
import ConquerSpace.common.game.city.area.MineArea;
import ConquerSpace.common.game.universe.GeographicPoint;
import ConquerSpace.common.game.universe.bodies.Planet;
import ConquerSpace.common.game.resources.Good;
import ConquerSpace.common.game.resources.Stratum;
import ConquerSpace.common.util.Utilities;
import com.alee.extended.layout.HorizontalFlowLayout;
import java.awt.GridBagConstraints;
import java.util.Iterator;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author EhWhoAmI
 */
public class MinerAreaConstructionPanel extends AreaDesignPanel {

    private DefaultTableModel resourceListTableModel;
    private JTable resourceListTable;

    private Good miningGood = null;
    private Stratum miningStratum = null;
    private float amountMining = 10;
    
    private GameState gameState;

    public MinerAreaConstructionPanel(GameState gameState, Planet planet, City c) {
        super(planet, c);
        this.gameState = gameState;
        
        setLayout(new HorizontalFlowLayout());

        Iterator<GeographicPoint> cityDist = planet.cityDistributions.keySet().iterator();
        DefaultListModel<Stratum> strataListModel = new DefaultListModel<>();
        while (cityDist.hasNext()) {
            GeographicPoint nextElement = cityDist.next();
            City city = planet.getCity(nextElement);
            if (city != null && city.equals(c)) {
                //Get point, search area
                for (int k = 0; k < planet.strata.size(); k++) {
                    
                    Stratum stratum = gameState.getObject(planet.strata.get(k), Stratum.class);
                    if (inCircle(stratum.getX(), stratum.getY(), stratum.getRadius(), nextElement.getX(), nextElement.getY())) {
                        //Is inside
                        if (!strataListModel.contains(stratum)) {
                            strataListModel.addElement(stratum);
                        }
                    }
                }
            }
        }

        JList stratumList = new JList<>(strataListModel);
        stratumList.addListSelectionListener(l -> {
            resourceListTableModel.setRowCount(0);
            Stratum strat = strataListModel.elementAt(stratumList.getSelectedIndex());
            for (Map.Entry<Integer, Integer> en : strat.minerals.entrySet()) {
                Integer key = en.getKey();
                Integer val = en.getValue();

                resourceListTableModel.addRow(new Object[]{gameState.getGood(key).getName(), val});
            }

            if (resourceListTableModel.getRowCount() > 0) {
                resourceListTable.addRowSelectionInterval(0, 0);
            }

            miningStratum = strat;
        });

        String[] rows = new String[]{"Resource", "Amount"};
        resourceListTableModel = new DefaultTableModel(rows, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        resourceListTable = new JTable(resourceListTableModel);
        resourceListTable.getSelectionModel().addListSelectionListener(l -> {
            int selected = resourceListTable.getSelectedRow();
            if (selected > 0) {
                Object selectedGoodObject = resourceListTableModel.getValueAt(selected, 0);
                if (selectedGoodObject instanceof Good) {
                    miningGood = (Good) selectedGoodObject;
                }
            }
        });
        resourceListTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        add(new JScrollPane(stratumList));

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        add(resourceListTable);

        if (strataListModel.size() > 0) {
            stratumList.setSelectedIndex(0);
        }
    }

    private boolean inCircle(int xC, int yC, int r, int x, int y) {
        return (Utilities.distanceBetweenPoints(xC, yC, x, y) <= r);
    }

    @Override
    public Area getAreaToConstruct() {
        if (miningStratum != null && miningGood != null) {
            return new MineArea(gameState, miningStratum.getId(), miningGood.getId(), amountMining);
        }
        return null;
    }

}
