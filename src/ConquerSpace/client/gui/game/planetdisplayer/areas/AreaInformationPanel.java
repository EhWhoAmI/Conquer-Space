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
import ConquerSpace.common.game.city.area.Area;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.organizations.Organization;
import ConquerSpace.common.util.logging.CQSPLogger;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author EhWhoAmI
 */
public class AreaInformationPanel<T extends Area> extends JPanel {

    private static final Logger LOGGER = CQSPLogger.getLogger(AreaInformationPanel.class.getName());
    protected T area;
    protected GameState gameState;

    public AreaInformationPanel(T area, GameState gameState) {
        this.area = area;
        this.gameState = gameState;

        setLayout(new VerticalFlowLayout());
    }

    protected void genericInformation() {
        JLabel owner = new JLabel("Owner: None");

        Organization org = gameState.getOrganizationObjectByReference(area.getOwner());
        if (org != null) {
            if (org instanceof Civilization) {
                owner.setText("Owner: State owned by " + org.getName());
            } else {
                owner.setText("Owner: " + org.getName());
            }
        }

        JLabel currentJobs = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.manpower.current", area.getCurrentlyManningJobs()));
        JLabel minimumJobs = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.manpower.minimum", area.operatingJobsNeeded()));
        JLabel maximumJobs = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.manpower.maximum", area.getMaxJobsProvided()));
        if (area.getCurrentlyManningJobs() < area.operatingJobsNeeded()) {
            currentJobs.setForeground(Color.red);
            currentJobs.setToolTipText(LOCALE_MESSAGES.getMessage("game.planet.areas.manpower.tooltip"));
        }
        add(owner);
        add(currentJobs);
        add(minimumJobs);
        add(maximumJobs);
    }

    public void update() {

    }
}
