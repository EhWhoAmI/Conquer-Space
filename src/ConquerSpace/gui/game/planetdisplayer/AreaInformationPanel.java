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
package ConquerSpace.gui.game.planetdisplayer;

import ConquerSpace.game.buildings.area.Area;
import ConquerSpace.game.buildings.area.CapitolArea;
import ConquerSpace.game.buildings.area.FinancialArea;
import ConquerSpace.game.buildings.area.InfrastructureArea;
import ConquerSpace.game.buildings.area.ResearchArea;
import ConquerSpace.game.buildings.area.industrial.Factory;
import ConquerSpace.game.universe.resources.Good;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.Dimension;
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
public class AreaInformationPanel extends JPanel {

    public AreaInformationPanel(Area a) {
        setLayout(new VerticalFlowLayout());
        if (a instanceof ResearchArea) {
            JLabel title = new JLabel("Research Area");
            add(title);

            ResearchArea research = (ResearchArea) a;

            DefaultTableModel model = new DefaultTableModel(new String[]{"Field", "Value"}, 0);
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
        } else if (a instanceof CapitolArea) {
            JLabel title = new JLabel("Capitol Area");
            add(title);
        } else if (a instanceof FinancialArea) {
            JLabel title = new JLabel("Finance Area");
            add(title);
        } else if (a instanceof InfrastructureArea) {
            JLabel title = new JLabel("Infrastructure Area");
            add(title);
        } else if (a instanceof Factory) {
            JLabel title = new JLabel("Factory Area");
            add(title);

            Factory factory = (Factory) a;

            JLabel processName = new JLabel(factory.getProcess().name);
            String inputString = "Input: ";

            for (Map.Entry<Good, Integer> entry : factory.getProcess().input.entrySet()) {
                Good key = entry.getKey();
                Integer val = entry.getValue();
                inputString = inputString + key.getName();
                inputString = inputString + " amount " + val;
                inputString = inputString + ", ";
            }

            JLabel input = new JLabel(inputString);

            String outputString = "Output: ";
            for (Map.Entry<Good, Integer> entry : factory.getProcess().output.entrySet()) {
                Good key = entry.getKey();
                Integer val = entry.getValue();
                outputString = outputString + key.getName();
                outputString = outputString + " amount " + val;
                outputString = outputString + ", ";
            }

            JLabel output = new JLabel(outputString);

            add(processName);
            add(input);
            add(output);
        }
    }
}
