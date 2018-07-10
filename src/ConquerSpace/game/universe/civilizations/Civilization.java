package ConquerSpace.game.universe.civilizations;

import ConquerSpace.game.UniversePath;
import ConquerSpace.game.tech.FieldNode;
import ConquerSpace.game.tech.Techonologies;
import ConquerSpace.game.tech.Techonology;
import ConquerSpace.game.templates.Template;
import ConquerSpace.game.universe.civilization.controllers.AIController.AIController;
import ConquerSpace.game.universe.civilization.controllers.CivilizationController;
import ConquerSpace.game.universe.civilizations.stats.Economy;
import ConquerSpace.game.universe.civilizations.stats.Population;
import ConquerSpace.game.universe.spaceObjects.Sector;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.Universe;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Civilization
 *
 * @author Zyun
 */
public class Civilization {

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

    public HashMap<UniversePath, Integer> vision;

    private UniversePath startingPlanet;

    private HashMap<String, Template> templatesList;

    public HashMap<Techonology, Integer> civTechs;

    public FieldNode fields;

    private int techLevel = 0;
    public Civilization(int ID, Universe u) {
        this.ID = ID;

        //Set a temp starting point as in 0:0:0
        this.control.add(new UniversePath(0, 0, 0));

        vision = new HashMap<>();
        //Add all the vision.
        for (int i = 0; i < u.getSectorCount(); i++) {
            Sector s = u.getSector(i);
            for (int n = 0; n < s.getStarSystemCount(); n++) {
                this.vision.put(new UniversePath(i, n), VisionTypes.UNDISCOVERED);
                StarSystem sys = s.getStarSystem(n);
                for (int h = 0; h < sys.getPlanetCount(); h++) {
                    //Add planets
                    this.vision.put(new UniversePath(i, n, h), VisionTypes.UNDISCOVERED);
                }
                for (int h2 = 0; h2 < sys.getStarCount(); h2++) {
                    this.vision.put(new UniversePath(i, n, h2, true), VisionTypes.UNDISCOVERED);
                }
            }
        }
        pop = new Population();
        economy = new Economy();

        templatesList = new HashMap<>();
        civTechs = new HashMap<>();
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
        builder.append(", Home system=Sector " + startingPlanet.toString());
        builder.append(">\n");
        return (builder.toString());
    }

    public void addControl(UniversePath p) {
        if (!control.contains(p)) {
            // we only add the planet
            control.add(p);
            vision.put(p, VisionTypes.KNOWS_ALL);
        }
    }

    public void processTurn(int turn) {
        //Stat everything
    }

    public void setStartingPlanet(UniversePath startingPlanet) {
        this.startingPlanet = startingPlanet;
    }

    public UniversePath getStartingPlanet() {
        return startingPlanet;
    }

    public void addTemplate(Template b, String name) {
        templatesList.put(name, b);
    }

    public Template getTemplate(String name) {
        return (templatesList.get(name));
    }

    public void updateTemplate(String name, Template b) {
        templatesList.put(name, b);
    }

    public String[] getTemplateNameList() {
        return ((String[]) templatesList.keySet().toArray());
    }

    public void addTech(Techonology t) {
        civTechs.put(t, 0);
    }

    public Techonology getTechByName(String s) {
        return (civTechs.keySet().stream().filter(e -> e.getName().toLowerCase().equals(s.toLowerCase()))).findFirst().get();
    }

    public Techonology[] getTechsByTag(String tag) {
        return ((Techonology[]) (civTechs.keySet().stream().filter(e -> Arrays.asList(e.getTags()).contains(tag)).toArray()));
    }

    public void researchTech(Techonology t) {
        civTechs.put(t, Techonologies.RESEARCHED);
    }

    public int getTechLevel() {
        return techLevel;
    }
    
    public void calculateTechLevel() {
        techLevel = 0;
        civTechs.keySet().stream().filter((t) -> (civTechs.get(t) == Techonologies.RESEARCHED)).forEachOrdered((t) -> {
            techLevel += t.getLevel();
        });
    }
}
