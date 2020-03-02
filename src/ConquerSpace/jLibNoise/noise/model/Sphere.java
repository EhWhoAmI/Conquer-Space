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
package ConquerSpace.jLibNoise.noise.model;

import ConquerSpace.jLibNoise.noise.LatLon;
import ConquerSpace.jLibNoise.noise.module.Module;

/**
 * Model that defines the surface of a sphere.
 *
 * @image html modelsphere.png
 * <p/>
 * This model returns an output value from a noise module given the
 * coordinates of an input value located on the surface of a sphere.
 * <p/>
 * To generate an output value, pass the (latitude, longitude)
 * coordinates of an input value to the GetValue() method.
 * <p/>
 * This model is useful for creating:
 * - seamless textures that can be mapped onto a sphere
 * - terrain height maps for entire planets
 * <p/>
 * This sphere has a radius of 1.0 unit and its center is located at
 * the origin.
 * @source 'models/sphere.h/cpp'
 */
public class Sphere {

    // A pointer to the noise module used to generate the output values.
    private Module module;

    public Sphere() {
    }

    /**
     * Constructor
     *
     * @param module The noise module that is used to generate the output values.
     */
    public Sphere(Module module) {
        this.module = module;
    }

    /**
     * Returns the noise module that is used to generate the output values.
     *
     * @return A reference to the noise module.
     * @pre A noise module was passed to the SetModule() method.
     */
    public Module getModule() {
        assert (module != null);
        return module;
    }
    
    /**
     * Sets the noise module that is used to generate the output values.
     * <p/>
     * This noise module must exist for the lifetime of this object,
     * until you pass a new noise module to this method.
     *
     * @param module The noise module that is used to generate the output values.
     */
    public void setModule(Module module) {
        this.module = module;
    }

    /**
     * Returns the output value from the noise module given the
     * (latitude, longitude) coordinates of the specified input value
     * located on the surface of the sphere.
     * <p/>
     * This output value is generated by the noise module passed to the
     * SetModule() method.
     * <p/>
     * Use a negative latitude if the input value is located on the
     * southern hemisphere.
     * <p/>
     * Use a negative longitude if the input value is located on the
     * western hemisphere.
     *
     * @param lat The latitude of the input value, in degrees.
     * @param lon The longitude of the input value, in degrees.
     * @return The output value from the noise module.
     * @pre A noise module was passed to the SetModule() method.
     */
    public double getValue(double lat, double lon) {
        assert (module != null);
        
        double[] xyz = LatLon.latLonToXYZ(lat, lon);
        return module.getValue(xyz[0], xyz[1], xyz[2]);
    }
}
