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
package ConquerSpace.common.game.resources;

import ConquerSpace.common.save.Serialize;

/**
 *
 * @author EhWhoAmI
 */
public class Element extends Good{
    
    @Serialize(key = "element-no")
    private int elementNumber;
    public Element(String name, double volume, double mass) {
        super(name, "e_" + name.toLowerCase(), volume, mass);
    }

    public void setElementNumber(int elementNumber) {
        this.elementNumber = elementNumber;
    }

    public int getElementNumber() {
        return elementNumber;
    }
}
