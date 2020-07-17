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
package ConquerSpace.common.game.ships.satellites.templates;

import java.io.Serializable;

/**
 *
 * @author EhWhoAmI
 */
public class SatelliteTemplate implements Serializable {

    protected static int idCounter = 0;
    protected int mass;
    protected String name = "";
    protected int id;

    public SatelliteTemplate() {
        id = idCounter++;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMass(int mass) {
        this.mass = mass;
    }

    public int getId() {
        return id;
    }

    public int getMass() {
        return mass;
    }

    public String getName() {
        return name;
    }
}
