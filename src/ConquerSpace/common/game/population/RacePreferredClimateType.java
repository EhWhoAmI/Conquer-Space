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
package ConquerSpace.common.game.population;

import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;

/**
 * Class of the civ preferred climate. Completely arbitrary, needs to change to
 * better stats
 *
 * @author EhWhoAmI
 */
public enum RacePreferredClimateType {
    Varied("civ.climate.varied"),
    Hot("civ.climate.hot"),
    Cold("civ.climate.cold");

    String text;

    private RacePreferredClimateType(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return LOCALE_MESSAGES.getMessage(text);
    }
}
