package ConquerSpace.game;

/**
 * Path of an object in a universe. To write path: "sectorid:star
 * systemid:planet id" for a planet "sectorid" for a sector "sectorid:star
 * systemid" for a star system "whateverpath:[r, d]" for an object in space, eg
 * a spaceship or satellite.
 *
 * @author Zyun
 */
public class UniversePath {
    //-1 is none selected

    /**
     * Id of sector
     */
    private int sectorID = -1;
    /**
     * ID of system
     */
    private int systemID = -1;
    /**
     * ID of planet
     */
    private int planetID = -1;
    /**
     * ID of star
     */
    private int starID = -1;
    /**
     * ID of object in orbit
     */
    private int orbitID = -1;

    /**
     * Empty constructor.
     */
    public UniversePath() {
    }

    /**
     * Creates a new UniversePath object
     *
     * @param sectorID ID of sector
     * @param systemID ID of system
     * @param planetID ID of planet
     */
    public UniversePath(int sectorID, int systemID, int planetID) {
        this.sectorID = sectorID;
        this.systemID = systemID;
        this.planetID = planetID;
    }

    /**
     * Creates a new UniversePath object
     *
     * @param sectorID id of sector
     * @param systemID id of star system
     * @param starID id of star
     * @param isstar ignore this. Put anything that is a bool. Just for
     * reconition of this method.
     */
    public UniversePath(int sectorID, int systemID, int starID, boolean isstar) {
        this.sectorID = sectorID;
        this.systemID = systemID;
        this.starID = planetID;
    }

    /**
     * Creates a new UniversePath object
     *
     * @param sectorID id of sector
     * @param systemID id of star system
     * @param planetID id of planet
     * @param orbitID id of object in orbit
     */
    public UniversePath(int sectorID, int systemID, int planetID, int orbitID) {
        this.sectorID = sectorID;
        this.systemID = systemID;
        this.planetID = planetID;
        this.orbitID = orbitID;
    }

    /**
     * Creates a new UniversePath object
     *
     * @param sectorID ID of sector
     * @param systemID ID of system
     * @param starID ID of star
     * @param orbitID ID of object in oject
     * @param isstar ignore this. Put anything that is a bool. Just for
     * reconition of this Constructor.
     */
    public UniversePath(int sectorID, int systemID, int starID, int orbitID, boolean isstar) {
        this.sectorID = sectorID;
        this.systemID = systemID;
        this.starID = starID;
        this.orbitID = orbitID;
    }

    /**
     * Creates a new UniversePath object
     *
     * @param sectorID ID of sector
     */
    public UniversePath(int sectorID) {
        this.sectorID = sectorID;
    }

    /**
     * Creates a new UniversePath object
     *
     * @param sectorID ID of sector
     * @param systemID ID of system
     */
    public UniversePath(int sectorID, int systemID) {
        this.sectorID = sectorID;
        this.systemID = systemID;
    }

    /**
     * Get the id of the planet selected
     *
     * @return Planet ID
     */
    public int getPlanetID() {
        return planetID;
    }

    /**
     * Get the id of the sector selected
     *
     * @return Sector ID
     */
    public int getSectorID() {
        return sectorID;
    }

    /**
     * Get the id of the Star system
     *
     * @return Star system ID
     */
    public int getSystemID() {
        return systemID;
    }

    /**
     * To string
     *
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

    /**
     * Get id of the object in orbit
     *
     * @return Orbit ID
     */
    public int getOrbitID() {
        return orbitID;
    }

    /**
     * Get id of star
     *
     * @return Star ID
     */
    public int getStarID() {
        return starID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UniversePath) {
            UniversePath other = (UniversePath) obj;
            return ((this.sectorID == other.sectorID) && (this.starID == other.starID) && (this.planetID == other.planetID) && (this.orbitID == other.orbitID) && (this.systemID == other.systemID));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return(("" + sectorID + systemID + starID + planetID + orbitID).hashCode());
    }
}
