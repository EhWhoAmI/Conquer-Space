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
import ConquerSpace.game.organizations.civilization.Civilization;
import ConquerSpace.game.city.City;
import ConquerSpace.game.city.area.Area;
import ConquerSpace.game.city.area.ManufacturerArea;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.resources.ProductionProcess;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author EhWhoAmI
 */
public class IndustrialFactoryConstructionPanel extends AreaDesignPanel {

    DefaultListModel<ProductionProcess> productionProcessListModel;
    JList<ProductionProcess> list;

    @SuppressWarnings("unchecked")
    public IndustrialFactoryConstructionPanel(Planet p, City c, Civilization civ) {
        super(p, c);
        setLayout(new GridBagLayout());
        productionProcessListModel = new DefaultListModel<>();
        for (int i = 0; i < civ.productionProcesses.size(); i++) {
            productionProcessListModel.addElement(civ.productionProcesses.get(i));
        }

        list = new JList<>(productionProcessListModel);

        JLabel processName = new JLabel("");
        JLabel input = new JLabel();
        JLabel output = new JLabel();
        list.addListSelectionListener(l -> {
            processName.setText(((ProductionProcess) list.getSelectedValue()).name);
            String inputString = "Input: ";

            for (Map.Entry<Integer, Double> entry : ((ProductionProcess) list.getSelectedValue()).input.entrySet()) {
                Integer key = entry.getKey();
                Double val = entry.getValue();
                inputString = inputString + GameController.goodHashMap.get(key).getName();
                inputString = inputString + " amount " + val;
                inputString = inputString + ", ";
            }
            input.setText(inputString);

            String outputString = "Output: ";
            for (Map.Entry<Integer, Double> entry : ((ProductionProcess) list.getSelectedValue()).output.entrySet()) {
                Integer key = entry.getKey();
                Double val = entry.getValue();
                outputString = outputString + GameController.goodHashMap.get(key).getName();
                outputString = outputString + " amount " + val;
                outputString = outputString + ", ";
            }
            output.setText(outputString);

        });
        JPanel processInfoPanel = new JPanel();
        processInfoPanel.setLayout(new VerticalFlowLayout());
        //Add industrial processes
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        constraints.weighty = 0.1;
        constraints.anchor = GridBagConstraints.NORTH;
        constraints.fill = GridBagConstraints.BOTH;
        add(new JScrollPane(list), constraints);
        processInfoPanel.add(processName);
        processInfoPanel.add(input);
        processInfoPanel.add(output);

        constraints.gridx = 1;
        constraints.gridy = 0;
        add(processInfoPanel, constraints);

    }

    @Override
    public Area getAreaToConstruct() {
        if (list.getSelectedIndex() > -1) {
            return new ManufacturerArea(list.getSelectedValue(), 1);
        }
        return null;
    }
}
