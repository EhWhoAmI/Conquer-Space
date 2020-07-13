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
package ConquerSpace.client.gui.music.renderers;

import ConquerSpace.common.game.universe.PolarCoordinate;

/**
 * Does all the math for rendering.
 *
 * @author EhWhoAmI
 */
public class RendererMath {

    /**
     * Converts a GalaticLocation(AKA polar coordinate) to a point on a
     * Cartesian coordinate pane(or a swing panel pane), so that you can plot it
     * on a swing jpanel.
     *
     * @see PolarCoordinate
     * @param center center of the plot of polar coordinate.
     * @param unitSize the size of the individual unit(as in the distance of
     * <code>GalaticLocation</code>.
     * @return Point of the converted polar coordinate
     */
    public static Point polarCoordToCartesianCoord(double distance, double degrees, Point center, int unitSize) {
        double xpos;
        double ypos;

        double opp = (Math.sin(Math.toRadians(degrees)) * distance);
        double adj = (Math.cos(Math.toRadians(degrees)) * distance);

        //Multipy units. May have to change this for accuracy.
        opp *= unitSize;
        adj *= unitSize;

        xpos = (long) (center.x + adj);
        ypos = (long) (center.y - opp);
        
        return (new RendererMath.Point(xpos, ypos));
    }

    public static class Point {

        public double x;
        public double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }

    /**
     * Hide constructor.
     */
    private RendererMath() {
    }
}
