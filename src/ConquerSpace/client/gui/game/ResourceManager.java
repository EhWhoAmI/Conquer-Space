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

import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.resources.StoreableReference;
import java.awt.BorderLayout;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author
 */
public class ResourceManager extends JPanel {

    private DefaultTableModel resourceTableModel;
    private JTable resourceTable;
    private String[] colunmNames = {"Resource Type", "Amount"};
    private Civilization c;

    public ResourceManager(Civilization c) {
        this.c = c;
        init();
        update();
        //setClosable(true);
        setVisible(true);
        //setResizable(true);
        //setSize(100, 100);
        //pack();
    }

    public void init() {
        setLayout(new BorderLayout());
        resourceTableModel = new DefaultTableModel(colunmNames, 0);
        resourceTable = new JTable(resourceTableModel);

        //Initalize with default values
        for (Map.Entry<StoreableReference, Double> entry : c.resourceList.entrySet()) {
            StoreableReference key = entry.getKey();
            resourceTableModel.addRow(new String[]{key.toString(), "0"});
        }
        JScrollPane pane = new JScrollPane(resourceTable);
        add(pane, BorderLayout.CENTER);
    }

    public void update() {
        //fill table
        int x = 0;
        resourceTableModel.setRowCount(0);
        for (Map.Entry<StoreableReference, Double> entry : c.resourceList.entrySet()) {
            StoreableReference key = entry.getKey();
            Double val = entry.getValue();
            resourceTableModel.addRow(new String[]{key.toString(), Double.toString(val)});
            x++;
        }
    }
}
