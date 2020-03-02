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

import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.BuildingBuilding;
import ConquerSpace.game.buildings.CityDistrict;
import ConquerSpace.gui.game.planetdisplayer.buildings.BuildingBuildingViewMenu;
import ConquerSpace.gui.game.planetdisplayer.buildings.PopulationStorageViewMenu;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author zyunl
 */
public class BuildingInfoContainer extends JPanel {

    public BuildingInfoContainer(Building building) {
        //Get the building and set the thingy
        if (building instanceof BuildingBuilding) {
            BuildingBuildingViewMenu menu = new BuildingBuildingViewMenu((BuildingBuilding) building);
            add(menu);
        } else if (building instanceof CityDistrict) {
            PopulationStorageViewMenu menu = new PopulationStorageViewMenu((CityDistrict) building);
            add(menu);
        } else {
            add(new JLabel(building.toString()));
        }
    }
}
