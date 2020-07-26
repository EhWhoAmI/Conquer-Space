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
package ConquerSpace.client.gui.game.planetdisplayer;

import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.common.GameState;
import ConquerSpace.common.game.city.area.Area;
import ConquerSpace.common.game.city.area.CapitolArea;
import ConquerSpace.common.game.city.area.CommercialArea;
import ConquerSpace.common.game.city.area.ConstructingArea;
import ConquerSpace.common.game.city.area.FarmFieldArea;
import ConquerSpace.common.game.city.area.InfrastructureArea;
import ConquerSpace.common.game.city.area.ManufacturerArea;
import ConquerSpace.common.game.city.area.MineArea;
import ConquerSpace.common.game.city.area.ResearchArea;
import ConquerSpace.common.game.city.area.SpacePortArea;
import ConquerSpace.common.game.organizations.Organization;
import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.common.game.resources.Stratum;
import ConquerSpace.common.game.ships.launch.LaunchSystem;
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

    public AreaInformationPanel(GameState gameState, Area a) {
        if (a != null) {
            setLayout(new VerticalFlowLayout());
            if (a instanceof ResearchArea) {
                JLabel title = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.research.title"));
                add(title);

                ResearchArea research = (ResearchArea) a;

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
            } else if (a instanceof CapitolArea) {
                JLabel title = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.capitol.title"));
                add(title);
            } else if (a instanceof InfrastructureArea) {
                JLabel title = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.infrastructure.title"));
                add(title);
            } else if (a instanceof ManufacturerArea) {
                JLabel title = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.factory.title"));
                add(title);

                ManufacturerArea factory = (ManufacturerArea) a;

                if(!factory.producedLastTick()) {
                    JLabel notProductive = new JLabel("Not functioning");
                    notProductive.setForeground(Color.red);
                    add(notProductive);
                }
                
                JLabel processName = new JLabel(factory.getProcess().name);
                StringBuilder inputString = new StringBuilder(LOCALE_MESSAGES.getMessage("game.planet.areas.factory.input"));

                for (Map.Entry<Integer, Double> entry : factory.getProcess().input.entrySet()) {
                    Integer key = entry.getKey();
                    Double val = entry.getValue();
                    inputString.append(gameState.getGood(key).getName());
                    inputString.append(" "
                            + LOCALE_MESSAGES.getMessage("game.planet.areas.factory.amount") + " ");
                    inputString.append(val * factory.getProductivity());
                    inputString.append(LOCALE_MESSAGES.getMessage("game.planet.areas.factory.separator"));
                }

                JLabel input = new JLabel(inputString.toString());

                StringBuilder outputString = new StringBuilder(LOCALE_MESSAGES.getMessage("game.planet.areas.factory.output"));
                for (Map.Entry<Integer, Double> entry : factory.getProcess().output.entrySet()) {
                    Integer key = entry.getKey();
                    Double val = entry.getValue();
                    outputString.append(gameState.getGood(key).getName());
                    outputString.append(" " + LOCALE_MESSAGES.getMessage("game.planet.areas.factory.amount") + "");
                    outputString.append(val * factory.getProductivity());
                    outputString.append(LOCALE_MESSAGES.getMessage("game.planet.areas.factory.separator"));
                }

                JLabel output = new JLabel(outputString.toString());

                add(processName);
                add(input);
                add(output);
            } else if (a instanceof FarmFieldArea) {
                JLabel title = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.farmfield.title"));
                add(title);
                FarmFieldArea field = (FarmFieldArea) a;
                JLabel fieldType = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.farmfield.growing", field.getGrown()));
                add(fieldType);
                if (field.getQueue().size() == 1) {
                    JLabel timeLeft = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.farmfield.timeleft", field.getQueue().get(0).getTimeLeft()));
                    add(timeLeft);
                }
            } else if (a instanceof MineArea) {
                JLabel title = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.mine.title"));
                add(title);
                MineArea area = (MineArea) a;
                JLabel resourceMined = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.mine.mined", gameState.getGood(area.getResourceMinedId())));
                JLabel miningVein = new JLabel("Stratum: " + gameState.getObject(area.getStratumMining(), Stratum.class));
                add(resourceMined);
                add(miningVein);
            } else if (a instanceof CommercialArea) {
                JLabel title = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.commercial.title"));
                add(title);
                CommercialArea area = (CommercialArea) a;
                JLabel tradeValue = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.commercial.value", area.getTradeValue()));
                add(tradeValue);
            } else if (a instanceof SpacePortArea) {
                JLabel title = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.spaceport.title"));
                add(title);
                SpacePortArea area = (SpacePortArea) a;
                JLabel launchSystemLabel = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.spaceport.launchsystem", gameState.getObject(area.getLaunchSystem(), LaunchSystem.class).getName()));
                add(launchSystemLabel);
                JLabel launchPadLabel = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.spaceport.pads", area.launchPads.size()));
                add(launchPadLabel);
            } else if (a instanceof ConstructingArea) {
                JLabel title = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.construction.title"));
                add(title);
                ConstructingArea area = (ConstructingArea) a;
                JLabel constructingArea = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.construction.what", area.getToBuild().toString()));
                add(constructingArea);
                JLabel timeLeft = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.construction.left", area.getTicksLeft()));
                add(timeLeft);
            }
            JLabel owner = new JLabel("Owner: None");

            Organization org = gameState.getOrganizationObjectByIndex(a.getOwner());
            if (org != null) {
                if (org instanceof Civilization) {
                    owner.setText("Owner: State owned by " + org.getName());
                } else {
                    owner.setText("Owner: " + org.getName());
                }
            }

            JLabel currentJobs = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.manpower.current", a.getCurrentlyManningJobs()));
            JLabel minimumJobs = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.manpower.minimum", a.operatingJobsNeeded()));
            JLabel maximumJobs = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.manpower.maximum", a.getMaxJobsProvided()));
            if (a.getCurrentlyManningJobs() < a.operatingJobsNeeded()) {
                currentJobs.setForeground(Color.red);
                currentJobs.setToolTipText(LOCALE_MESSAGES.getMessage("game.planet.areas.manpower.tooltip"));
            }
            add(owner);
            add(currentJobs);
            add(minimumJobs);
            add(maximumJobs);
        }
    }
}
