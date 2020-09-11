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
import ConquerSpace.common.game.city.area.AreaFactory;
import ConquerSpace.common.game.city.area.ManufacturerAreaFactory;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.resources.ProductionProcess;
import ConquerSpace.common.game.universe.bodies.Planet;
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
    ManufacturerAreaFactory factory;

    @SuppressWarnings("unchecked")
    public IndustrialFactoryConstructionPanel(GameState gameState, Planet p, City c, Civilization civ) {
        super(gameState, p, c, civ);
        factory = new ManufacturerAreaFactory(civ);
        factory.setMaxJobs(10000);
        factory.setOperatingJobs(5000);
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
            ProductionProcess process = ((ProductionProcess) list.getSelectedValue());
            processName.setText(process.name);
            String inputString = "Input: ";

            for (Map.Entry<Integer, Double> entry : process.input.entrySet()) {
                Integer key = entry.getKey();
                Double val = entry.getValue();
                inputString = inputString + gameState.getGood(key).getName();
                inputString = inputString + " amount " + val;
                inputString = inputString + ", ";
            }
            input.setText(inputString);

            String outputString = "Output: ";
            for (Map.Entry<Integer, Double> entry : process.output.entrySet()) {
                Integer key = entry.getKey();
                Double val = entry.getValue();
                outputString = outputString + gameState.getGood(key).getName();
                outputString = outputString + " amount " + val;
                outputString = outputString + ", ";
            }
            output.setText(outputString);
            factory.setProcess(process);
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
        
        add(getCostPanel());
    }

    @Override
    public AreaFactory getAreaToConstruct() {
        return factory;
    }
}
