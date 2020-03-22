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
package ConquerSpace.game.universe.resources;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author EhWhoAmI
 */
public class ProductionProcess {

    public String name;
    public HashMap<Good, Integer> input;
    public HashMap<Good, Integer> output;
    public ArrayList<Good> catalyst;
    //How difficult it is to extract. Will replace with the parts for the factory in the futute.
    public int diff;

    public ProductionProcess() {
        input = new HashMap<>();
        output = new HashMap<>();
        catalyst = new ArrayList<>();
    }
}
