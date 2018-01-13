/*
 * The MIT License
 *
 * Copyright 2018 Zyun.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ConquerSpace.game.universe;

import java.util.ArrayList;

/**
 *
 * @author Zyun
 */
public class Sector {

    private ArrayList<StarSystem> starSystems;
    private GalaticLocation loc;
    int id;
    
    /**
     *
     * @param location
     * @param id
     */
    public Sector(GalaticLocation location, int id) {
        loc = location;
        this.id = id;
        starSystems = new ArrayList<>();
    }

    /**
     *
     * @param e
     */
    public void addStarSystem(StarSystem e) {
        starSystems.add(e);
    }

    /**
     *
     * @param i
     * @return
     */
    public StarSystem getStarSystem(int i) {
        return (starSystems.get(i));
    }

    /**
     *
     * @return
     */
    public int getStarSystemCount() {
        return (starSystems.size());
    }
    
    /**
     *
     * @return
     */
    public GalaticLocation getGalaticLocation(){
        return loc;
    }
    
    /**
     *
     * @return
     */
    public String toReadableString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Sector " + this.id + " Position-" + loc.toString() + ": {");
        for (StarSystem s : starSystems){
            builder.append(s.toReadableString());
        }
        
        builder.append("}\n");
        return(builder.toString());
    }
}
