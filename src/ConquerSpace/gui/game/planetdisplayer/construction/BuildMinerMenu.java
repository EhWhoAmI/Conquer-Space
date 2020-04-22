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

import ConquerSpace.game.GameController;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.civilization.Civilization;
import ConquerSpace.game.universe.resources.Good;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.resources.Stratum;
import ConquerSpace.util.Utilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author EhWhoAmI
 */
public class BuildMinerMenu extends JPanel {

    //JComboBox<Good> resourceToMine;
    JLabel resourceToMineLabel;
    JLabel miningSpeed;
    JSpinner miningSpeedSpinner;

    JComboBox<Stratum> strataComboBox;

    ArrayList<Stratum> strata;

    StockpileStorageModel stockpileStorageModel;
    JTable stratumResourceTable;

    Planet p;

    public BuildMinerMenu(Planet p, Civilization c, GeographicPoint where) {
        this.p = p;
        resourceToMineLabel = new JLabel("Mining stratum: ");

        miningSpeed = new JLabel("Mining speed, units per month");

        SpinnerNumberModel miningSpeedSpinnerNumberModel = new SpinnerNumberModel(10f, 0f, 50000f, 0.5f);
        miningSpeedSpinner = new JSpinner(miningSpeedSpinnerNumberModel);

        strata = new ArrayList<>();

        //Fill it out
        for (Stratum stratum : p.strata) {
            if (inCircle(stratum.getX(), stratum.getY(), stratum.getRadius(), where.getX(), where.getY())) {
                strata.add(stratum);
            }
        }

        DefaultComboBoxModel<Stratum> strataModel = new DefaultComboBoxModel<>(new Vector<>(strata));
        strataComboBox = new JComboBox<>(strataModel);

        stockpileStorageModel = new StockpileStorageModel();
        stratumResourceTable = new JTable(stockpileStorageModel);
        stratumResourceTable.setRowSelectionInterval(0, 0);
        stratumResourceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(resourceToMineLabel, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(strataComboBox, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.gridheight = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(stratumResourceTable, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(miningSpeed, constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(miningSpeedSpinner, constraints);
    }

    private boolean inCircle(int xC, int yC, int r, int x, int y) {
        return (Utilities.distanceBetweenPoints(xC, yC, x, y) <= r);
    }

    private class StockpileStorageModel extends AbstractTableModel {

        String[] colunmNames = {"Good", "Count"};

        @Override
        public int getRowCount() {
            if (strataComboBox.getSelectedItem() == null) {
                return 0;
            } else {
                Stratum stratum = (Stratum) strataComboBox.getSelectedItem();

                return stratum.minerals.size();
            }
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            if (strataComboBox.getSelectedItem() != null) {
                Stratum stratum = (Stratum) strataComboBox.getSelectedItem();

                switch (columnIndex) {
                    case 0:
                        return new ArrayList<>(stratum.minerals.keySet()).get(rowIndex);
                    case 1:
                        return stratum.minerals.get(new ArrayList<>(stratum.minerals.keySet()).get(rowIndex));
                }
            }
            return 0;
        }

        @Override
        public String getColumnName(int column) {
            return colunmNames[column];
        }
    }
}
