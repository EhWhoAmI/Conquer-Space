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

import ConquerSpace.common.GameState;
import ConquerSpace.common.game.city.area.PowerPlantArea;
import javax.swing.JLabel;

/**
 *
 * @author EhWhoAmI
 */
public class PowerPlantAreaInformationPanel extends AreaInformationPanel<PowerPlantArea>{

    public PowerPlantAreaInformationPanel(PowerPlantArea area, GameState gameState) {
        super(area, gameState);
        add(new JLabel("Power Plant"));
        add(new JLabel("Production: " + area.getProduction()));
        add(new JLabel("Used Resource: " + area.getUsedResource().getName()));
        genericInformation();
    }
    
}
