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
package ConquerSpace.game.universe;

/**
 * Galactic location. In a nutshell, this is a polar coordinate.
 * @author Zyun
 */
public class GalacticLocation {
    private float degrees;
    private long distance;

    /**
     * Constructor of the polar coordinate.
     * @param degrees degrees from north in a anticlockwise direction.
     * @param distance distance from the center.
     */
    public GalacticLocation(float degrees, long distance) {
        this.degrees = (degrees%360);
        this.distance = distance;
    }

    /**
     * To string. Example output: (Degrees: &lt;degrees&gt; Distance &lt;distance&gt;)
     * @return String value of this class
     */
    @Override
    public String toString() {
        return("(Degrees: " + degrees + " Distance: " + distance + ")");
    }
    
    /**
     * Get the degrees.
     * @return Degrees.
     */
    public float getDegrees() {
        return (degrees);
    }
    
    /**
     * Get the distance.
     * @return Distance.
     */
    public long getDistance() {
        return (distance);
    }
    
    /**
     * Set the degrees
     * @param degrees degrees
     */
    public void setDegrees(float degrees) {
        this.degrees = (degrees%360);
    }
    
    /**
     * Set the distance
     * @param distance Distance
     */
    public void setDistance(long distance) {
        this.distance = distance;
    }
}
