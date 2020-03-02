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
package ConquerSpace.game.people;

import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.government.GovernmentPosition;

/**
 *
 * @author
 */
public class Administrator extends Person {
    public GovernmentPosition position;
    public Civilization employer;
    
    public Administrator(String name, int age) {
        super(name, age);
    }

    @Override
    public String getJobName() {
        return "Administrator";
    }
}
