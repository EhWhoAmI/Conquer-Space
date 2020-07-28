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
import ConquerSpace.common.game.city.area.ConstructingArea;
import javax.swing.JLabel;

/**
 *
 * @author EhWhoAmI
 */
public class ConstructionAreaInformationPanel extends AreaInformationPanel<ConstructingArea> {

    public ConstructionAreaInformationPanel(ConstructingArea area, GameState gameState) {
        super(area, gameState);
        JLabel title = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.construction.title"));
        add(title);

        JLabel constructingArea = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.construction.what", area.getToBuild().toString()));
        add(constructingArea);
        JLabel timeLeft = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.construction.left", area.getTicksLeft()));
        add(timeLeft);
        genericInformation();
    }

}
