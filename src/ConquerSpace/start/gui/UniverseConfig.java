package ConquerSpace.start.gui;

/**
 *
 * @author Zyun
 */
public class UniverseConfig {

    /**
     *
     */
    public String universeSize;

    /**
     *
     */
    public String universeShape;

    /**
     *
     */
    public String universeAge;

    /**
     *
     */
    public String planetCommonality;

    /**
     *
     */
    public String civilizationCount;
    
    public int seed;

    /**
     *
     * @param civilizationCount
     */
    public void setCivilizationCount(String civilizationCount) {
        this.civilizationCount = civilizationCount;
    }

    /**
     *
     * @param planetCommonality
     */
    public void setPlanetCommonality(String planetCommonality) {
        this.planetCommonality = planetCommonality;
    }

    /**
     *
     * @param universeAge
     */
    public void setUniverseAge(String universeAge) {
        this.universeAge = universeAge;
    }

    /**
     *
     * @param universeShape
     */
    public void setUniverseShape(String universeShape) {
        this.universeShape = universeShape;
    }

    /**
     *
     * @param universeSize
     */
    public void setUniverseSize(String universeSize) {
        this.universeSize = universeSize;
    }
    
    public void setSeed(int seed) {
        this.seed = seed;
    }

    /**
     *
     * @return
     */
    public String getCivilizationCount() {
        return civilizationCount;
    }

    /**
     *
     * @return
     */
    public String getPlanetCommonality() {
        return planetCommonality;
    }

    /**
     *
     * @return
     */
    public String getUniverseAge() {
        return universeAge;
    }

    /**
     *
     * @return
     */
    public String getUniverseShape() {
        return universeShape;
    }

    /**
     *
     * @return
     */
    public String getUniverseSize() {
        return universeSize;
    }
    
    public int getSeed() {
        return seed;
    }
}
