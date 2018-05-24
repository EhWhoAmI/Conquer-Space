package ConquerSpace.game.universe.civilizations;

import ConquerSpace.game.universe.civilization.controllers.AIController.AIController;
import ConquerSpace.game.universe.civilization.controllers.CivilizationController;
import ConquerSpace.game.GameObject;
import ConquerSpace.game.UniversePath;
import ConquerSpace.game.universe.civilizations.stats.Economy;
import ConquerSpace.game.universe.civilizations.stats.Population;
import java.awt.Color;
import java.util.ArrayList;

/**
 * Civilization
 *
 * @author Zyun
 */
public class Civilization extends GameObject {

    private int ID;

    private Color color;
    private String name;
    private int civilizationPreferedClimate;
    private String civilizationSymbol;
    private String speciesName;

    private String homePlanetName;

    public ArrayList<UniversePath> control = new ArrayList<>();
    /**
     * The controller of this civ.
     */
    public CivilizationController controller;

    public Population pop;
    public Economy economy;

    public ArrayList<UniversePath> vision = new ArrayList<>();

    public Civilization(int ID) {
        this.ID = ID;

        //Set a temp starting point as in 0:0:0
        this.control.add(new UniversePath(0,0,0));
        
        pop = new Population();
        economy = new Economy();
    }

    public void setCivilizationPrefferedClimate(int civilizationPrefferedClimate) {
        this.civilizationPreferedClimate = civilizationPrefferedClimate;
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

    public void setHomePlanetName(String homePlanetName) {
        this.homePlanetName = homePlanetName;
    }

    public void setHomeplanetPath(int homeSectorID, int homeSystemID, int homePlanetID) {
        addControl(new UniversePath(homeSectorID, homeSystemID, homePlanetID));
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public void setCivilizationPreferredClimate(int civilizationPreferedClimate) {
        this.civilizationPreferedClimate = civilizationPreferedClimate;
    }

    public int getCivilizationPreferredClimate() {
        return civilizationPreferedClimate;
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
        return control.get(0).getPlanetID();
    }

    public String getHomePlanetName() {
        return homePlanetName;
    }

    public int getHomeSystemID() {
        return control.get(0).getSystemID();
    }

    public int getHomesectorID() {
        return control.get(0).getSectorID();
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

    public String toReadableString() {
        //Return values...
        StringBuilder builder = new StringBuilder();
        builder.append("<Civ " + ID + ", Name=" + name + ", Home Planet Name=" + homePlanetName);
        builder.append(", Species Name=" + speciesName + ", Civ Symbol=" + civilizationSymbol + ", Civ Prefferred Climate=");

        //Get the species preferred climate in name
        switch (civilizationPreferedClimate) {
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
        } else {
            builder.append("Player");
        }
        builder.append(", Home system=Sector " + getHomesectorID() + " System " + getHomeSystemID() + " Planet " + getHomePlanetID());
        builder.append(">\n");
        return (builder.toString());
    }

    public void addControl(UniversePath p) {
        if (!control.contains(p)) {
            // we only add the planet
            // Also do vision.
            control.add(p);
            vision.add(p);
        }
    }
    
    public void processTurn(int turn) {
            //Stat everything
    }
}
