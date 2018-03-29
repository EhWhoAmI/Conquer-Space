package ConquerSpace.game;

import ConquerSpace.game.universe.GalaticLocation;

/**
 * Path of an object in a universe. To write path: "sectorid:star
 * systemid:planet id" for a planet "sectorid" for a sector "sectorid:star
 * systemid" for a star system "whateverpath:[r, d]" for an object in space, eg
 * a spaceship or satellite.
 *
 * @author Zyun
 */
public class UniversePath {

    private int sectorID = -1;
    private int systemID = -1;
    private int planetID = -1;
    private GalaticLocation loc = null;

    public UniversePath(String path) {
        parse(path);
    }

    public void parse(String s) {
        int sectorID = -1;
        int systemID = -1;
        int planetID = -1;
        loc = null;
        String[] text = s.split(":");
        // Split last one for galatic location
        if (text[text.length - 1].contains("[")) {
            //Then it has a galatic location
            String[] cont = text[text.length - 1].split("[");
            text[text.length - 1] = cont[0];
            //Parse the thingy
            cont[1] = cont[1].replace("]", "");
            String[] vals = cont[1].split(".");
            loc = new GalaticLocation(Float.parseFloat(vals[0]), Integer.parseInt(vals[1]));
        }
        if (text.length == 0) {
            //Incorrect format.
            return;
        } else {
            sectorID = Integer.parseInt(text[0]);
            if (text.length > 1) {
                systemID = Integer.parseInt(text[1]);
            }
            if (text.length > 2) {
                planetID = Integer.parseInt(text[2]);
            }
        }
    }

    public GalaticLocation getLoc() {
        return loc;
    }

    public int getPlanetID() {
        return planetID;
    }

    public int getSectorID() {
        return sectorID;
    }

    public int getSystemID() {
        return systemID;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        if (sectorID >= 0) {
            b.append(sectorID);
        }
        if (systemID >= 0) {
            b.append(":");
            b.append(systemID);
        }
        if (planetID >= 0) {
            b.append(":");
            b.append(planetID);
        }
        if (loc != null) {
            b.append("[").append(loc.getDistance()).append(",").append(loc.getDegrees()).append("]");
        }
        return b.toString();
    }
}
