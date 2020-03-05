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
package ConquerSpace.gui.game.planetdisplayer.buildings;

import ConquerSpace.game.buildings.BuildingBuilding;
import com.alee.extended.layout.VerticalFlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author EhWhoAmI
 */
public class BuildingBuildingViewMenu extends JPanel{
    private JLabel timeLeft;
    private JLabel peopleWorking;
    public BuildingBuildingViewMenu(BuildingBuilding building) {
        setLayout(new VerticalFlowLayout());
        //Get the building
        add(new JLabel("Constructing a " + building.getToBuild().toString()));
        timeLeft = new JLabel("Construction progress: " + building.getLength() + ", estimated to be x months");
        peopleWorking = new JLabel("Constructors: " + 0);
        add(timeLeft);
        add(peopleWorking);
    }
}
