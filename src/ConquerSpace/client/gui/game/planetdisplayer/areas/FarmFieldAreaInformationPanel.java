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
import ConquerSpace.common.game.city.area.FarmFieldArea;
import javax.swing.JLabel;

/**
 *
 * @author EhWhoAmI
 */
public class FarmFieldAreaInformationPanel extends AreaInformationPanel<FarmFieldArea> {

    public FarmFieldAreaInformationPanel(FarmFieldArea area, GameState gameState) {
        super(area, gameState);
        JLabel title = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.farmfield.title"));
        add(title);

        JLabel fieldType = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.farmfield.growing", area.getGrown()));
        add(fieldType);
        if (area.getQueue().size() == 1) {
            JLabel timeLeft = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.farmfield.timeleft", area.getQueue().get(0).getTimeLeft()));
            add(timeLeft);
        }
        genericInformation();
    }

}
