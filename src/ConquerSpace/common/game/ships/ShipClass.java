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
package ConquerSpace.common.game.ships;

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameState;
import ConquerSpace.common.game.ships.hull.Hull;
import ConquerSpace.common.save.SerializeClassName;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author EhWhoAmI
 */
@SerializeClassName("ship-class")
public class ShipClass extends ConquerSpaceGameObject implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private Hull hull;
    public ArrayList<Integer> components;
    private int mass = 1;
    private long estimatedThrust = 0;

    public ShipClass(GameState gameState, String name, Hull h) {
        super(gameState);
        this.name = name;
        this.hull = h;
        components = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public Hull getHull() {
        return hull;
    }

    public void setHull(Hull hull) {
        this.hull = hull;
    }

    public int getMass() {
        return mass;
    }

    public long getEstimatedThrust() {
        return estimatedThrust;
    }

    public void setEstimatedThrust(long estimatedThrust) {
        this.estimatedThrust = estimatedThrust;
    }
}
