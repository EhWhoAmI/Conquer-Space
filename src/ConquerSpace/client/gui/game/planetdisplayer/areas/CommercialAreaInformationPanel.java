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
import ConquerSpace.common.game.city.area.CommercialArea;
import javax.swing.JLabel;

/**
 *
 * @author EhWhoAmI
 */
public class CommercialAreaInformationPanel extends AreaInformationPanel<CommercialArea> {

    public CommercialAreaInformationPanel(CommercialArea area, GameState gameState) {
        super(area, gameState);
        JLabel title = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.commercial.title"));
        add(title);

        JLabel tradeValue = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.commercial.value", area.getTradeValue()));
        add(tradeValue);
        genericInformation();
    }
}
