package ConquerSpace.game.universe.civilization;

import java.awt.Color;

/**
 * Configuration of the civilization.
 * @author Zyun
 */
public class CivilizationConfig {
    public Color civColor;
    public String civSymbol;
    public String homePlanetName;
    public String speciesName;
    public String civilizationName;
    public String civilizationPreferredClimate;

    public void setCivColor(Color civColor) {
        this.civColor = civColor;
    }

    public void setCivSymbol(String civSymbol) {
        this.civSymbol = civSymbol;
    }

    public void setCivilizationName(String civilizationName) {
        this.civilizationName = civilizationName;
    }

    public void setCivilizationPreferredClimate(String civilizationPreferredClimate) {
        this.civilizationPreferredClimate = civilizationPreferredClimate;
    }

    public void setHomePlanetName(String homePlanetName) {
        this.homePlanetName = homePlanetName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public Color getCivColor() {
        return civColor;
    }

    public String getCivSymbol() {
        return civSymbol;
    }

    public String getCivilizationName() {
        return civilizationName;
    }

    public String getCivilizationPreferredClimate() {
        return civilizationPreferredClimate;
    }

    public String getHomePlanetName() {
        return homePlanetName;
    }

    public String getSpeciesName() {
        return speciesName;
    }
}
