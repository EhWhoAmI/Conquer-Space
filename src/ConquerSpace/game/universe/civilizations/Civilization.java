package ConquerSpace.game.universe.civilizations;

import ConquerSpace.game.universe.civControllers.AIController;
import ConquerSpace.game.universe.civControllers.CivilizationController;
import java.awt.Color;

/**
 *
 * @author Zyun
 */
public class Civilization {
    private int ID;
    
    private Color color;
    private String name;
    private int civilizationPrefferedClimate;
    private String civilizationSymbol;
    private String speciesName;
    
    private String homePlanetName;
    private int homesectorID;
    private int homeSystemID;
    private int homePlanetID;
    
    public CivilizationController controller;

    public Civilization(int ID) {
        this.ID = ID;
    }
    
    public void setCivilizationPrefferedClimate(int civilizationPrefferedClimate) {
        this.civilizationPrefferedClimate = civilizationPrefferedClimate;
    }

    public void setCivilizationSymbol(String civilizationSymbol) {
        this.civilizationSymbol = civilizationSymbol;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setController(CivilizationController controller) {
        this.controller = controller;
    }

    public void setHomePlanetID(int homePlanetID) {
        this.homePlanetID = homePlanetID;
    }

    public void setHomePlanetName(String homePlanetName) {
        this.homePlanetName = homePlanetName;
    }

    public void setHomeSystemID(int homeSystemID) {
        this.homeSystemID = homeSystemID;
    }

    public void setHomesectorID(int homesectorID) {
        this.homesectorID = homesectorID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public int getCivilizationPrefferedClimate() {
        return civilizationPrefferedClimate;
    }

    public String getCivilizationSymbol() {
        return civilizationSymbol;
    }

    public Color getColor() {
        return color;
    }

    public CivilizationController getController() {
        return controller;
    }

    public int getHomePlanetID() {
        return homePlanetID;
    }

    public String getHomePlanetName() {
        return homePlanetName;
    }

    public int getHomeSystemID() {
        return homeSystemID;
    }

    public int getHomesectorID() {
        return homesectorID;
    }

    public String getName() {
        return name;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public int getID() {
        return ID;
    }
    
    public String toReadableString(){
        //Return values...
        StringBuilder builder = new StringBuilder();
        builder.append("<Civ " + ID + ", Name=" + name + ", Home Planet Name=" + homePlanetName);
        builder.append(", Species Name=" + speciesName + ", Civ Prefferred Climate");
        
        //Get the species preferred climate in name
        
        switch(civilizationPrefferedClimate) {
            case CivilizationPreferredClimateTypes.VARIED:
                builder.append("Varied");
                break;
            case CivilizationPreferredClimateTypes.COLD:
                builder.append("Cold");
                break;
            case CivilizationPreferredClimateTypes.HOT:
                builder.append("Hot");
                break;
        }
        builder.append(", Civ Controller=");
        if (controller instanceof AIController) {
            builder.append("AI");
        }
        else {
            builder.append("Player");
        }
        builder.append(">");
        return (builder.toString());
    }
}