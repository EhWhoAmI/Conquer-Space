package ConquerSpace.start.gui;

/**
 *
 * @author Zyun
 */
public class UniverseConfig {
    public String universeSize;
    public String universeShape;
    public String universeAge;
    public String planetCommonality;
    public String civilizationCount;

    public void setCivilizationCount(String civilizationCount) {
        this.civilizationCount = civilizationCount;
    }

    public void setPlanetCommonality(String planetCommonality) {
        this.planetCommonality = planetCommonality;
    }

    public void setUniverseAge(String universeAge) {
        this.universeAge = universeAge;
    }

    public void setUniverseShape(String universeShape) {
        this.universeShape = universeShape;
    }

    public void setUniverseSize(String universeSize) {
        this.universeSize = universeSize;
    }

    public String getCivilizationCount() {
        return civilizationCount;
    }

    public String getPlanetCommonality() {
        return planetCommonality;
    }

    public String getUniverseAge() {
        return universeAge;
    }

    public String getUniverseShape() {
        return universeShape;
    }

    public String getUniverseSize() {
        return universeSize;
    }
}
