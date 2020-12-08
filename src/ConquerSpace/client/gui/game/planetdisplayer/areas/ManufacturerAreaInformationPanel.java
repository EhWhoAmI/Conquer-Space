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
import ConquerSpace.common.game.city.area.ManufacturerArea;
import ConquerSpace.common.game.resources.StorableReference;
import java.awt.Color;
import java.util.Map;
import javax.swing.JLabel;

/**
 *
 * @author EhWhoAmI
 */
public class ManufacturerAreaInformationPanel extends AreaInformationPanel<ManufacturerArea> {

    public ManufacturerAreaInformationPanel(ManufacturerArea area, GameState gameState) {
        super(area, gameState);
        JLabel title = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.factory.title"));
        add(title);
        
        JLabel productivityLabel = new JLabel("Productivity: " + area.getProductivity());
        add(productivityLabel);
        

        if (!area.producedLastTick()) {
            JLabel notProductive = new JLabel("Not functioning");
            notProductive.setForeground(Color.red);
            add(notProductive);
        }

        JLabel processName = new JLabel(area.getProcess().name);
        StringBuilder inputString = new StringBuilder(LOCALE_MESSAGES.getMessage("game.planet.areas.factory.input"));

        for (Map.Entry<StorableReference, Double> entry : area.getProcess().input.entrySet()) {
            StorableReference key = entry.getKey();
            Double val = entry.getValue();
            inputString.append(gameState.getGood(key).getName());
            inputString.append(" "
                    + LOCALE_MESSAGES.getMessage("game.planet.areas.factory.amount") + " ");
            inputString.append(val * area.getProductivity());
            inputString.append(LOCALE_MESSAGES.getMessage("game.planet.areas.factory.separator"));
        }

        JLabel input = new JLabel(inputString.toString());

        StringBuilder outputString = new StringBuilder(LOCALE_MESSAGES.getMessage("game.planet.areas.factory.output"));
        for (Map.Entry<StorableReference, Double> entry : area.getProcess().output.entrySet()) {
            StorableReference key = entry.getKey();
            Double val = entry.getValue();
            outputString.append(gameState.getGood(key).getName());
            outputString.append(" " + LOCALE_MESSAGES.getMessage("game.planet.areas.factory.amount") + "");
            outputString.append(val * area.getProductivity());
            outputString.append(LOCALE_MESSAGES.getMessage("game.planet.areas.factory.separator"));
        }

        JLabel output = new JLabel(outputString.toString());

        add(processName);
        add(input);
        add(output);
        genericInformation();
    }
}
