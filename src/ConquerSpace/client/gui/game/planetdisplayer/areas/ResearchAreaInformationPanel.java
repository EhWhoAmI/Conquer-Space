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
package ConquerSpace.client.gui.game.planetdisplayer.areas;

import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.common.GameState;
import ConquerSpace.common.game.city.area.ResearchArea;
import java.awt.Dimension;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author EhWhoAmI
 */
public class ResearchAreaInformationPanel extends AreaInformationPanel<ResearchArea> {

    public ResearchAreaInformationPanel(ResearchArea research, GameState gameState) {
        super(research, gameState);
        JLabel title = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.research.title"));
        add(title);
        DefaultTableModel model = new DefaultTableModel(
                new String[]{
                    LOCALE_MESSAGES.getMessage("game.planet.areas.research.table.field"),
                    LOCALE_MESSAGES.getMessage("game.planet.areas.research.table.value")}, 0);
        for (Map.Entry<String, Integer> en : research.focusFields.entrySet()) {
            String key = en.getKey();
            Integer val = en.getValue();

            model.addRow(new Object[]{key, val});
        }
        JTable table = new JTable(model) {
            @Override
            public Dimension getPreferredScrollableViewportSize() {
                //So that you don't see too much
                return new Dimension(super.getPreferredSize().width,
                        getRowHeight() * getRowCount());
            }
        };
        JScrollPane scroll = new JScrollPane(table);
        add(scroll);
        genericInformation();
    }
}
