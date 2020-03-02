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

import java.awt.Color;
import java.util.HashMap;

public class Resource {

    private String name;
    private int value;
    private int id;
    private float rarity;
    private int difficulty;
    private boolean mineable;
    private boolean raw;
    private float density;
    private int distributionLow;
    private int distributionHigh;
    private int r;
    private int g;
    private int b;
    private String[] tags;
    private HashMap<String, Integer> attributes;

    public Resource(String name, int value, float rarity, int id) {
        this.name = name;
        this.value = value;
        this.rarity = rarity;
        this.id = id;
        attributes = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public float getRarity() {
        return rarity;
    }

    public int getValue() {
        return value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRarity(float rarity) {
        this.rarity = rarity;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public float getDensity() {
        return density;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setMineable(boolean mineable) {
        this.mineable = mineable;
    }

    public void setColor(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Color getColor() {
        return new Color(r, g, b);
    }

    public boolean isMineable() {
        return mineable;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Resource) {
            Resource res = (Resource) obj;
            return (res.name.equals(name) && res.id == id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public void setRaw(boolean raw) {
        this.raw = raw;
    }

    public boolean isRaw() {
        return raw;
    }    

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String[] getTags() {
        return tags;
    }

    public HashMap<String, Integer> getAttributes() {
        return attributes;
    }

    public int getDistributionLow() {
        return distributionLow;
    }

    public int getDistributionHigh() {
        return distributionHigh;
    }

    public void setDistributionHigh(int distributionHigh) {
        this.distributionHigh = distributionHigh;
    }

    public void setDistributionLow(int distributionLow) {
        this.distributionLow = distributionLow;
    }
}
