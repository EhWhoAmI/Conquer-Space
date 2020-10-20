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
import ConquerSpace.common.game.city.area.MineArea;
import ConquerSpace.common.game.resources.Stratum;
import javax.swing.JLabel;

/**
 *
 * @author EhWhoAmI
 */
public class MineAreaInformationPanel extends AreaInformationPanel<MineArea> {

    public MineAreaInformationPanel(MineArea area, GameState gameState) {
        super(area, gameState);
        JLabel title = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.mine.title"));
        add(title);
        JLabel resourceMined = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.mine.mined", gameState.getGood(area.getResourceMinedId())));
        JLabel miningVein = new JLabel("Stratum: " + gameState.getObject(area.getStratumMining(), Stratum.class));
        JLabel amountMined = new JLabel("Productivity: " + area.getProductivity() * gameState.getGood(area.getResourceMinedId()).getMass() * 24 + " kg per day");
        add(resourceMined);
        add(miningVein);
        add(amountMined);
        genericInformation();
    }

}
