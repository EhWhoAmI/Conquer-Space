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
package ConquerSpace.common.game.ships;

import java.util.Objects;

/**
 *
 * @author EhWhoAmI
 */
public class ShipType {

    String identifier;
    boolean military;

    public ShipType() {
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public boolean isMilitary() {
        return military;
    }

    public void setMilitary(boolean military) {
        this.military = military;
    }

    @Override
    public int hashCode() {
        return identifier.hashCode(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ShipType other = (ShipType) obj;
        if (!Objects.equals(this.identifier, other.identifier)) {
            return false;
        }
        return true;
    }
}
