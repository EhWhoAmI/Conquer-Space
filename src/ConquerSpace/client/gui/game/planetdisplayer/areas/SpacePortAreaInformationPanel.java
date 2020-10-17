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
import ConquerSpace.client.gui.ObjectListModel;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.city.area.SpacePortArea;
import ConquerSpace.common.game.ships.Ship;
import ConquerSpace.common.game.ships.launch.LaunchSystem;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

/**
 *
 * @author EhWhoAmI
 */
public class SpacePortAreaInformationPanel extends AreaInformationPanel<SpacePortArea> {

    public SpacePortAreaInformationPanel(SpacePortArea area, GameState gameState) {
        super(area, gameState);
        JLabel title = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.spaceport.title"));
        add(title);

        JLabel launchSystemLabel = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.spaceport.launchsystem", gameState.getObject(area.getLaunchSystem(), LaunchSystem.class).getName()));
        add(launchSystemLabel);
        JLabel launchPadLabel = new JLabel(LOCALE_MESSAGES.getMessage("game.planet.areas.spaceport.pads", area.getLaunchPadCount()));
        add(launchPadLabel);

        //List all the space ships
        ObjectListModel<ObjectReference> spaceShipList = new ObjectListModel<>();
        spaceShipList.setElements(area.landedShips);
        spaceShipList.setHandler(l -> {
            return gameState.getObject(l, Ship.class).getName();
        });
        JList<String> list = new JList<>(spaceShipList);
        //Set action listener
        add(new JScrollPane(list));
        
        genericInformation();
    }

}
