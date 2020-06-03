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

import ConquerSpace.game.GameController;
import ConquerSpace.game.city.area.Area;
import ConquerSpace.game.city.area.CapitolArea;
import ConquerSpace.game.city.area.CommercialArea;
import ConquerSpace.game.city.area.FarmFieldArea;
import ConquerSpace.game.city.area.FinancialArea;
import ConquerSpace.game.city.area.InfrastructureArea;
import ConquerSpace.game.city.area.ManufacturerArea;
import ConquerSpace.game.city.area.MineArea;
import ConquerSpace.game.city.area.ResearchArea;
import ConquerSpace.game.city.area.SpacePortArea;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.Color;
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
        if (a != null) {
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
            } else if (a instanceof ManufacturerArea) {
                JLabel title = new JLabel("Factory Area");
                add(title);

                ManufacturerArea factory = (ManufacturerArea) a;

                JLabel processName = new JLabel(factory.getProcess().name);
                String inputString = "Input: ";

                for (Map.Entry<Integer, Double> entry : factory.getProcess().input.entrySet()) {
                    Integer key = entry.getKey();
                    Double val = entry.getValue();
                    inputString = inputString + GameController.goodHashMap.get(key).getName();
                    inputString = inputString + " amount " + val;
                    inputString = inputString + ", ";
                }

                JLabel input = new JLabel(inputString);

                String outputString = "Output: ";
                for (Map.Entry<Integer, Double> entry : factory.getProcess().output.entrySet()) {
                    Integer key = entry.getKey();
                    Double val = entry.getValue();
                    outputString = outputString + GameController.goodHashMap.get(key).getName();
                    outputString = outputString + " amount " + val;
                    outputString = outputString + ", ";
                }

                JLabel output = new JLabel(outputString);

                add(processName);
                add(input);
                add(output);
            } else if (a instanceof FarmFieldArea) {
                JLabel title = new JLabel("Field Area");
                add(title);
                FarmFieldArea field = (FarmFieldArea) a;
                JLabel fieldType = new JLabel("Growing: " + field.getGrown());
                add(fieldType);
                if (field.getQueue().size() == 1) {
                    JLabel timeLeft = new JLabel("Time Left: " + field.getQueue().get(0).getTimeLeft());
                    add(timeLeft);
                }
            } else if (a instanceof MineArea) {
                JLabel title = new JLabel("Mine Area");
                add(title);
                MineArea area = (MineArea) a;
                JLabel resourceMined = new JLabel("Resource Mine: " + area.getResourceMined());
                add(resourceMined);
            } else if (a instanceof CommercialArea) {
                JLabel title = new JLabel("Commercial Area");
                add(title);
                CommercialArea area = (CommercialArea) a;
                JLabel tradeValue = new JLabel("Trade Value: " + area.getTradeValue());
                add(tradeValue);
            } else if (a instanceof SpacePortArea) {
                JLabel title = new JLabel("Space Port");
                add(title);
                SpacePortArea area = (SpacePortArea) a;
                JLabel launchSystemLabel = new JLabel("Launch System: " + area.getLaunchSystem().getName());
                add(launchSystemLabel);
                JLabel launchPadLabel = new JLabel("Launch Pads: " + area.launchPads.size());
                add(launchPadLabel);
            }

            JLabel currentJobs = new JLabel("Current Manpower: " + a.getCurrentlyManningJobs());
            JLabel minimumJobs = new JLabel("Minimum Jobs Needed: " + a.operatingJobsNeeded());
            JLabel maximumJobs = new JLabel("Maximum Jobs supportable: " + a.getMaxJobsProvided());
            if (a.getCurrentlyManningJobs() < a.operatingJobsNeeded()) {
                currentJobs.setForeground(Color.red);
                currentJobs.setToolTipText("Insufficient manpower to operate this area!");
            }
            add(currentJobs);
            add(minimumJobs);
            add(maximumJobs);
        }
    }
}
