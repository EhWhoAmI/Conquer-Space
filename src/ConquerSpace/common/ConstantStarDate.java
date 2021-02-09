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

package ConquerSpace.common;

import java.io.Serializable;

/**
 * Star date that does not change, used for science
 *
 * @author EhWhoAmI
 */
public class ConstantStarDate implements Serializable{

    private final long date;

    public ConstantStarDate(long date) {
        this.date = date;
    }

    public ConstantStarDate(StarDate date) {
        this.date = date.getDate();
    }

    public long getDate() {
        return date;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConstantStarDate) {
            return ((ConstantStarDate) obj).getDate() == date;
        } else if (obj instanceof StarDate) {
            return ((StarDate) obj).getDate() == date;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int) date;
    }

}
