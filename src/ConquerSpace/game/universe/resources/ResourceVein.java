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

import ConquerSpace.game.universe.goods.Good;

/**
 *
 * @author EhWhoAmI
 */
public class ResourceVein {
    private int id;
    private Good resourceType;
    private int resourceAmount;
    private float difficulty;
    private int x;
    private int y;
    private int radius;

    public ResourceVein(Good resourceType, int resourceAmount) {
        this.resourceType = resourceType;
        this.resourceAmount = resourceAmount;
    }

    public Good getResourceType() {
        return resourceType;
    }

    public float getDifficulty() {
        return difficulty;
    }

    public int getRadius() {
        return radius;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getResourceAmount() {
        return resourceAmount;
    }

    public void setResourceAmount(int resourceAmount) {
        this.resourceAmount = resourceAmount;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setResourceType(Good resourceType) {
        this.resourceType = resourceType;
    }
    
    public void removeResources(int amount) {
        resourceAmount-=amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
