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

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author EhWhoAmI
 */
public class ProductionProcess {

    private static int idCounter = 0;
    
    private int id;
    public String name;
    public String identifier;
    public HashMap<Integer, Double> input;
    public HashMap<Integer, Double> output;
    public ArrayList<Integer> catalyst;
    //How difficult it is to extract. Will replace with the parts for the factory in the futute.
    public int diff;

    public ProductionProcess() {
        input = new HashMap<>();
        output = new HashMap<>();
        catalyst = new ArrayList<>();
        id = idCounter++;
    }
    
    
    public ProductionProcess(Integer outputGood) {
        input = new HashMap<>();
        output = new HashMap<>();
        output.put(outputGood, 1d);
        catalyst = new ArrayList<>();
        id = idCounter++;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }
}
