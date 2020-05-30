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
package ConquerSpace.gui.game.planetdisplayer.construction;

import ConquerSpace.game.districts.City;
import ConquerSpace.game.districts.area.Area;
import ConquerSpace.game.districts.area.MineArea;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.resources.Good;
import ConquerSpace.game.universe.resources.Stratum;
import ConquerSpace.util.Utilities;
import com.alee.extended.layout.HorizontalFlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
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

    public MinerAreaConstructionPanel(Planet p, City c) {
        super(p, c);
        setLayout(new HorizontalFlowLayout());

        Iterator<GeographicPoint> cityDist = p.cityDistributions.keySet().iterator();
        DefaultListModel<Stratum> strataListModel = new DefaultListModel<Stratum>();
        while (cityDist.hasNext()) {
            GeographicPoint nextElement = cityDist.next();
            if (p.cityDistributions.get(nextElement).equals(c)) {
                //Get point, search area
                for (int k = 0; k < p.strata.size(); k++) {
                    Stratum stratum = p.strata.get(k);
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
            for (Map.Entry<Good, Integer> en : strat.minerals.entrySet()) {
                Good key = en.getKey();
                Integer val = en.getValue();

                resourceListTableModel.addRow(new Object[]{key, val});
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
            return new MineArea(miningStratum, miningGood, amountMining);
        }
        return null;
    }

}
