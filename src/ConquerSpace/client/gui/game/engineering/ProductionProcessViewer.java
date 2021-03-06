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

package ConquerSpace.client.gui.game.engineering;

import ConquerSpace.client.gui.ObjectListModel;
import ConquerSpace.common.GameState;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.resources.ProductionProcess;
import ConquerSpace.common.game.resources.StoreableReference;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * So that you can see what each production process uses, and what processes lead up to this
 * process.
 *
 * @author EhWhoAmI
 */
public class ProductionProcessViewer extends JPanel {

    private GameState gameState;

    private JList<String> productionProcessList;
    private ObjectListModel<ProductionProcess> productionProcessListModel;
    private JPanel productionProcessInformationPanel;

    private JTable inputTable;
    private JTable outputTable;

    private ObjectListModel<ProductionProcess> inputProcessListModel;
    private ObjectListModel<ProductionProcess> outputProcessListModel;
    private JList<String> inputProcessesList;
    private JList<String> outputProcessesList;

    public ProductionProcessViewer(GameState gameState, Civilization civ) {
        this.gameState = gameState;

        setLayout(new BorderLayout());

        productionProcessListModel = new ObjectListModel<>();
        productionProcessListModel.setElements(civ.getProductionProcesses());
        productionProcessList = new JList<>(productionProcessListModel);
        productionProcessList.addListSelectionListener(l -> {
            //productionProcessInformationPanel.removeAll();
            ProductionProcess process = productionProcessListModel.getObject(productionProcessList.getSelectedIndex());
            inputTable.setModel(new ResourceTableModel(process.getInput()));
            //productionProcessInformationPanel.add(new JScrollPane(inputTable));
            //productionProcessInformationPanel.add(new JLabel("Becomes ->"));
            outputTable.setModel(new ResourceTableModel(process.getOutput()));
            //productionProcessInformationPanel.add(new JScrollPane(inputTable));
            //productionProcessInformationPanel.add(new JLabel("Difficulty: " + process.getDifficulty()));

            //Get production processes that lead up to this
            ArrayList<ProductionProcess> input = new ArrayList<>();
            ArrayList<ProductionProcess> output = new ArrayList<>();

            for (ProductionProcess processSelection : civ.getProductionProcesses()) {
                //Look for processes that lead to this
                for (Map.Entry<StoreableReference, Double> entry : processSelection.getOutput().entrySet()) {
                    StoreableReference key = entry.getKey();
                    if (process.getInput().containsKey(key)) {
                        input.add(processSelection);
                        break;
                    }
                }

                //Look for things that this can lead into 
                for (Map.Entry<StoreableReference, Double> entry : processSelection.getInput().entrySet()) {
                    StoreableReference key = entry.getKey();
                    if (process.getOutput().containsKey(key)) {
                        output.add(processSelection);
                        break;
                    }
                }
            }

            inputProcessListModel.setElements(input);
            inputProcessesList.updateUI();
            outputProcessListModel.setElements(output);
            outputProcessesList.updateUI();
        });

        productionProcessInformationPanel = new JPanel(new HorizontalFlowLayout());
        //All information about the production process
        inputTable = new JTable();
        outputTable = new JTable();

        JPanel inputPanel = new JPanel(new VerticalFlowLayout());
        inputPanel.add(new JLabel("Input"));
        inputPanel.add(new JScrollPane(inputTable));

        productionProcessInformationPanel.add(inputPanel);

        JPanel becomesPanel = new JPanel();
        becomesPanel.add(new JLabel("Becomes \u2192"));
        productionProcessInformationPanel.add(becomesPanel);

        JPanel outputPanel = new JPanel(new VerticalFlowLayout());
        outputPanel.add(new JLabel("Output"));
        outputPanel.add(new JScrollPane(outputTable));
        productionProcessInformationPanel.add(outputPanel);

        //List the processes that lead up to this process.
        inputProcessListModel = new ObjectListModel<>();
        inputProcessesList = new JList<>(inputProcessListModel);
        inputProcessesList.setVisibleRowCount(10);

        outputProcessListModel = new ObjectListModel<>();
        outputProcessesList = new JList<>(outputProcessListModel);
        outputProcessesList.setVisibleRowCount(10);

        JPanel intoProcessPanel = new JPanel(new VerticalFlowLayout());
        intoProcessPanel.add(new JLabel("Processes which products are input to this process"));
        intoProcessPanel.add(new JScrollPane(inputProcessesList));

        JPanel outputProcessPanel = new JPanel(new VerticalFlowLayout());
        outputProcessPanel.add(new JLabel("Processes which inputs are products of this process"));
        outputProcessPanel.add(new JScrollPane(outputProcessesList));

        productionProcessInformationPanel.add(intoProcessPanel);
        productionProcessInformationPanel.add(outputProcessPanel);

        add(new JScrollPane(productionProcessList), BorderLayout.WEST);
        add(productionProcessInformationPanel, BorderLayout.CENTER);
    }

    private class ResourceTableModel extends AbstractTableModel {

        private String[] colunms = {
            "Resource",
            "Amount"
        };

        private HashMap<StoreableReference, Double> map;

        public ResourceTableModel(HashMap<StoreableReference, Double> map) {
            this.map = map;
        }

        @Override
        public Object getValueAt(int row, int column) {
            switch (column) {
                case 0:
                    return gameState.getGood((StoreableReference) map.keySet().toArray()[row]);
                case 1:
                    return map.values().toArray()[row];
                default:
                    return "";
            }
        }

        @Override
        public String getColumnName(int column) {
            return colunms[column];
        }

        @Override
        public int getColumnCount() {
            return colunms.length;
        }

        @Override
        public int getRowCount() {
            return map.size();
        }
    }
}
