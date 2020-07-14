/*
 * jNoiseLib [https://github.com/andrewgp/jLibNoise]
 * Original code from libnoise [https://github.com/andrewgp/jLibNoise]
 *
 * Copyright (C) 2003, 2004 Jason Bevins
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License (COPYING.txt) for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * The developer's email is jlbezigvins@gmzigail.com (for great email, take
 * off every 'zig'.)
 */
package ConquerSpace.common.jLibNoise.noise.utils;

/**
 * Defines a point used to build a color gradient.
 * 
 * A color gradient is a list of gradually-changing colors.  A color
 * gradient is defined by a list of <i>gradient points</i>.  Each
 * gradient point has a position and a color.  In a color gradient, the
 * colors between two adjacent gradient points are linearly interpolated.
 * 
 * The ColorGradient class defines a color gradient by a list of these
 * objects.
 *
 * source 'noiseutils.h'
 */
public class GradientPoint {

    // The position of this gradient point.
    public double pos;

    // The color of this gradient point.
    public Color color;

    public GradientPoint() {
        color = new Color();
    }
}