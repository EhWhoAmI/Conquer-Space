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
 *
 * @author EhWhoAmI
 */
public class Version implements Comparable<Version> {

    private int major;
    private int minor;
    private int patch;
    private int build = -1;
    private String prerelease = "";

    /**
     *
     * @param major major number.
     * @param minor Minor number.
     * @param patch Patch number.
     * @param extras Extras, e.g.:"-dev", "-alpha"
     */
    public Version(int major, int minor, int patch, String extras, int build) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.prerelease = extras;
        this.build = build;
    }

    public Version(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public Version(String text) {
        String parts[] = text.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Version format is wrong");
        }
        //Parse the stuff
        major = Integer.parseInt(parts[0]);
        minor = Integer.parseInt(parts[1]);
        //Split the next thing
        String otherParts[] = parts[2].split("\\+|\\-");
        patch = Integer.parseInt(otherParts[0]);
        if (otherParts.length == 2 && parts[2].contains("-")) {
            prerelease = otherParts[1];
        }
        if (otherParts.length == 2 && parts[2].contains("+")) {
            build = Integer.parseInt(otherParts[1]);
        }
        if (otherParts.length == 3) {
            prerelease = otherParts[1];
            build = Integer.parseInt(otherParts[2]);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(major);
        builder.append(".");
        builder.append(minor);
        builder.append(".");
        builder.append(patch);
        if (!prerelease.isEmpty()) {
            builder.append("-");
            builder.append(prerelease);
        }
        if (build > 0) {
            builder.append("+");
            builder.append(build);
        }
        return builder.toString();
    }

    static boolean isGreater(Version v1, Version v2) {
        if (v1.major > v2.major) {
            return true;
        } else if (v1.major == v2.major) {
            if(v1.minor > v2.minor) {
                return true;
            } else if (v1.minor == v2.minor) {
                if(v1.patch > v2.patch) {
                    return true;
                }
            }
            return true;
        }
        return false;
    }

    //Ultra lazy with this one
    static boolean isLess(Version v1, Version v2) {
        return (!isGreater(v1, v2));
    }

    public String getExtras() {
        return prerelease;
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

    @Override
    public int compareTo(Version o) {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Version) {
            Version v = (Version) obj;
            return (v.major == this.major && v.minor == this.minor && v.patch == v.patch);
        }
        return false;
    }
    
    
}
