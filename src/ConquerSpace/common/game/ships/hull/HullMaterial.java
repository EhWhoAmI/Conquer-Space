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
package ConquerSpace.common.game.ships.hull;

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameState;
import ConquerSpace.common.save.SerializeClassName;

/**
 *
 * @author EhWhoAmI
 */
@SerializeClassName("hullmaterial")
public class HullMaterial extends ConquerSpaceGameObject{
    private String name;
    private int strength;
    //Kg per m3
    private float density;
    
    //cost per kg
    private int cost;
    
    public int getCost() {
        return cost;
    }

    public float getDensity() {
        return density;
    }

    public String getName() {
        return name;
    }

    public int getStrength() {
        return strength;
    }

    @Override
    public String toString() {
        return name;
    }

    public HullMaterial(GameState gameState, String name, int strength, float density, int cost) {
        super(gameState);
        this.name = name;
        this.strength = strength;
        this.density = density;
        this.cost = cost;
    }
}