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

import ConquerSpace.common.GameState;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.city.area.Area;
import ConquerSpace.common.game.city.area.AreaFactory;
import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.common.game.resources.Good;
import ConquerSpace.common.game.universe.bodies.Planet;
import com.alee.extended.layout.VerticalFlowLayout;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author EhWhoAmI
 */
public abstract class AreaDesignPanel extends JPanel {

    protected Area toConstruct = null;
    protected Civilization civ;
    protected GameState gameState;


    public AreaDesignPanel(GameState gameState, Planet p, City c, Civilization constructor) {
        civ = constructor;
        this.gameState = gameState;
    }

    public abstract AreaFactory getAreaToConstruct();

    public JPanel getCostPanel() {
        JPanel costPanel = new JPanel(new VerticalFlowLayout());
        DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Good", "Amount"}, 0);
        if (getAreaToConstruct() != null) {
            for (Map.Entry<Integer, Double> entry : getAreaToConstruct().getCost().entrySet()) {
                Integer key = entry.getKey();
                Double val = entry.getValue();
                Good good = gameState.getGood(key);
                tableModel.addRow(new Object[]{good.getName(), val});
            }
        }
        JTable costTable = new JTable(tableModel);
        costPanel.add(new JLabel("Cost"));
        costPanel.add(new JScrollPane(costTable));
        return costPanel;
    }
}
