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
    //-1 is none
    private int sectorID = -1;
    private int systemID = -1;
    private int planetID = -1;
    private int orbitID = -1;
    private int onGID = -1;
    
    @Deprecated
    public UniversePath(String path) {
        parse(path);
    }

    public UniversePath() {
    }

    public UniversePath(int sectorID, int systemID, int planetID) {
        this.sectorID = sectorID;
        this.systemID = systemID;
        this.planetID = planetID;
    }
    
    public UniversePath(int sectorID, int systemID, int planetID, int orbitID) {
        this.sectorID = sectorID;
        this.systemID = systemID;
        this.planetID = planetID;
        this.orbitID = orbitID;
    }
    
   public UniversePath(int sectorID) {
        this.sectorID = sectorID;
    }
   
   public UniversePath(int sectorID, int systemID) {
        this.sectorID = sectorID;
        this.systemID = systemID;
    }
   
   /**
    * Parses the universe path from a string
    * @param s the string
    * @deprecated 
    */
    public void parse(String s) {
        int sectorID = -1;
        int systemID = -1;
        int planetID = -1;
        String[] text = s.split(":");
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
    
    /**
     * Get the id of the planet selected
     * @return 
     */
    public int getPlanetID() {
        return planetID;
    }

    public int getSectorID() {
        return sectorID;
    }

    public int getSystemID() {
        return systemID;
    }
    
    /**
     * To string
     * @return this object in a string form, so that it can be parsed.
     */
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
        return b.toString();
    }

    public int getOnGID() {
        return onGID;
    }

    public int getOrbitID() {
        return orbitID;
    }

    public void setOnGID(int onGID) {
        this.onGID = onGID;
    }

    public void setOrbitID(int orbitID) {
        this.orbitID = orbitID;
    }
}
