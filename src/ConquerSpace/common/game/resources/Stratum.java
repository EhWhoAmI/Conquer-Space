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

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameState;
import ConquerSpace.common.save.SaveStuff;
import ConquerSpace.common.save.Serialize;
import java.util.HashMap;

/**
 *
 * @author EhWhoAmI
 */
public class Stratum extends ConquerSpaceGameObject{
    @Serialize(key = "depth")
    //Depth in kilometers
    private int depth = 0;
    @Serialize(key = "x")
    private int x;
    @Serialize(key = "y")
    private int y;
    @Serialize(key = "radius")
    private int radius;
    @Serialize(key = "name")
    private String name = "layer";
    
    @Serialize(key = "minerals", special = SaveStuff.Good)
    public HashMap<Integer, Integer> minerals;

    public Stratum(GameState gameState) {
        super(gameState);
        minerals = new HashMap<>();
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
