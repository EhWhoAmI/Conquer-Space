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
package ConquerSpace.gui.game;

import ConquerSpace.game.GameController;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.resources.Resource;
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
        resourceTableModel = new DefaultTableModel(colunmNames, 0);
        resourceTable = new JTable(resourceTableModel);

        //Initalize with default values
        for (Resource s : GameController.resources) {
            resourceTableModel.addRow(new String[]{s.toString(), "0"});
        }
        JScrollPane pane = new JScrollPane(resourceTable);
        add(pane);
    }

    public void update() {
        //fill table
        
        int x = 0;
        for (Resource s : GameController.resources) {
            resourceTableModel.setValueAt(c.resourceList.get(s), x, 1);
                x++;
        }
    }
}