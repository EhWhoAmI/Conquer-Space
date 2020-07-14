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
package ConquerSpace.common.game.ships.components;

/**
 * A component used for testing ships.
 * @author EhWhoAmI
 */
public class TestComponent extends ShipComponent{
    public TestComponent() {
        super(0, 0, "Test");
    }

    @Override
    public String getRatingType() {
        return "Testing Value";
    }

    @Override
    public int getRating() {
        return 0;
    }

    @Override
    public Object clone() {
        TestComponent ts =  new TestComponent();
        ts.mass = mass;
        ts.rating = rating;
        ts.cost = cost;
        ts.name = name;
        ts.id = id;
        return ts;
    }
}
