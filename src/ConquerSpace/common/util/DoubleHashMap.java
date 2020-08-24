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
package ConquerSpace.common.util;

import java.util.HashMap;

/**
 * Hashmap that stores values, and adds them if they exist and stuff. No need for fancy code anymore
 * @author EhWhoAmI
 */
public class DoubleHashMap<K> extends HashMap<K, Double>{
    public void addValue(K w, Double i) {
        if(containsKey(w)) {
            Double k = get(w);
            put(w, k + i);
        } else {
            put(w, i);
        }
    }

    @Override
    public Object clone() {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
}

