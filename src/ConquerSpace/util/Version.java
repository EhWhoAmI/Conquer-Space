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
package ConquerSpace.util;

/**
 * Version for easy parts.
 * @author Zyun
 */
public class Version {

    private int major;
    private int minor;
    private int patch;
    private String extras;

    /**
     *
     * @param major major number.
     * @param minor Minor number.
     * @param patch Patch number.
     * @param extras Extras, e.g.:"-dev", "-alpha"
     */
    public Version(int major, int minor, int patch, String extras) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.extras = extras;
    }
    
    @Override
    public String toString() {
        return ("" + major + "." + minor + "." + patch + "-" + extras);
    }
    
    static boolean isGreater(Version v1, Version v2) {
        if (v1.major > v2.major && v1.minor > v2.minor && v1.patch > v2.patch)
            return true;
        return false;
    }
    
    //Ultra lazy with this one
    static boolean isLess(Version v1, Version v2) {
        return (!isGreater(v1, v2));
    }

    public String getExtras() {
        return extras;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }
}
