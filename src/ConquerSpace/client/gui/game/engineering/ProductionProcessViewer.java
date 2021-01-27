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
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.swingx.JXBusyLabel;

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

    public ProductionProcessViewer(GameState gameState, Civilization civ) {
        this.gameState = gameState;

        setLayout(new BorderLayout());

        productionProcessListModel = new ObjectListModel<>();
        productionProcessListModel.setElements(civ.productionProcesses);
        productionProcessList = new JList<>(productionProcessListModel);
        productionProcessList.addListSelectionListener(l -> {
            productionProcessInformationPanel.removeAll();
            ProductionProcess process = productionProcessListModel.getObject(productionProcessList.getSelectedIndex());
            inputTable = new JTable(new ResourceTableModel(process.input));
            productionProcessInformationPanel.add(new JScrollPane(inputTable));
            productionProcessInformationPanel.add(new JLabel("Becomes ->"));
            inputTable = new JTable(new ResourceTableModel(process.output));
            productionProcessInformationPanel.add(new JScrollPane(inputTable));
            productionProcessInformationPanel.add(new JLabel("Difficulty: " + process.getDifficulty()));

            //Get production processes that lead up to this
            ArrayList<ProductionProcess> input = new ArrayList<>();
            ArrayList<ProductionProcess> output = new ArrayList<>();

            for (ProductionProcess processSelection : civ.productionProcesses) {
                //Look for processes that lead to this
                for (Map.Entry<StoreableReference, Double> entry : processSelection.output.entrySet()) {
                    StoreableReference key = entry.getKey();
                    if (process.input.containsKey(key)) {
                        input.add(processSelection);
                        break;
                    }
                }
                
                //Look for things that this can lead into 
                for (Map.Entry<StoreableReference, Double> entry : processSelection.input.entrySet()) {
                    StoreableReference key = entry.getKey();
                    if (process.output.containsKey(key)) {
                        output.add(processSelection);
                        break;
                    }
                }
            }

            ObjectListModel<ProductionProcess> productionProcessInputListModel = new ObjectListModel<>();
            productionProcessInputListModel.setElements(input);
            productionProcessInformationPanel.add(new JScrollPane(new JList(productionProcessInputListModel)));
            ObjectListModel<ProductionProcess> productionProcessOutputListModel = new ObjectListModel<>();
            productionProcessOutputListModel.setElements(output);
            productionProcessInformationPanel.add(new JScrollPane(new JList(productionProcessOutputListModel)));
        });

        productionProcessInformationPanel = new JPanel(new HorizontalFlowLayout());
        //All information about the production process

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
