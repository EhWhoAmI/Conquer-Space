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

/**
 *
 * @author EhWhoAmI
 */
public class ResourceDistribution {
    /**
     * How maximum size of the vein.
     */
    public int distributionHigh;
    
    /**
     * Minimum size of the vein.
     */
    public int distributionLow;
    
    /**
     * How deep the vein is, how the lower limit to depth. I know, it's confusing, because low means high on the crust.
     */
    public int depthLow;
    
    /**
     * How deep the vein is, maximum depth.
     */
    public int depthHigh;
    
    /**
     * How many planets out of a percentage.
     */
    public double rarity;
    
    /**
     * How much resources in a reserve.
     */
    public int abundance;
    
    /**
     * How the resource is stuffed with other resources. 
     */
    public double density;
}
