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

package ConquerSpace.common.util.profiler;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author EhWhoAmI
 */
public class Profiler {

    private final HashMap<String, Long> times;
    private long previousTime;
    private String currentSegment;
    private boolean running;

    public Profiler() {
        this.times = new HashMap<>();
    }

    public void push(String segmentName) {
        if(running) {
            return;
        }
        previousTime = System.nanoTime();
        currentSegment = segmentName;
        running = true;
    }

    public void pop() {
        long length = System.nanoTime() - previousTime;
        if(times.containsKey(currentSegment)) {
            length += times.get(currentSegment);
        }
        times.put(currentSegment, length);
        running = false;
    }

    public void reset() {
        System.out.println("--- Profiler ---");
        for (Map.Entry<String, Long> entry : times.entrySet()) {
            Object key = entry.getKey();
            double val = (double) entry.getValue();
            System.out.println(key + " - " + val/100000 + "ms");
            
        }
        System.out.println("---------");
        times.clear();
    }

    public HashMap<String, Long> getTimes() {
        return times;
    }
}
