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
package ConquerSpace.game.buildings;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author EhWhoAmI
 */
public class InfrastructureBuilding extends Building{
    public ArrayList<Building> connectedTo;
    //private int 

    public InfrastructureBuilding() {
        connectedTo = new ArrayList<>();
    }

    @Override
    public Color getColor() {
        return Color.orange;
    }

    @Override
    public String getType() {
        return "Infrastructure Hub";
    }
    
    public void addBuilding(Building b) {
        b.infrastructure.add(this);
        connectedTo.add(b);
    }
}
