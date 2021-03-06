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
package ConquerSpace.common.game.city.modifier;

import ConquerSpace.common.ConstantStarDate;
import ConquerSpace.common.StarDate;
import java.io.Serializable;

/**
 *
 * @author EhWhoAmI
 */
public class CityModifier implements Serializable {

    public static enum CityModifierEnum {
        RIOT_MODIFIER(1),
        STARVATION_MODIFIER(2),
        UNEMPLOYED_MODIFIER(3);

        int id;

        private CityModifierEnum(int id) {
            this.id = id;
        }
    };

    private String name;
    private ConstantStarDate date;

    public CityModifier(StarDate currentDate) {
        this("", currentDate);
    }

    public CityModifier(String name, StarDate currentDate) {
        this.name = name;
        date = currentDate.getConstantDate();
    }

    public String getName() {
        return name;
    }

    public ConstantStarDate getStartDate() {
        return date;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof CityModifier && obj.hashCode() == hashCode()) || obj instanceof CityModifierEnum && hashCode() == ((CityModifierEnum) obj).id;
    }

    @Override
    public int hashCode() {
        return -1;
    }
}
