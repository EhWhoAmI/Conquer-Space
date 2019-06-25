package ConquerSpace.game.universe.civilization;

import ConquerSpace.game.universe.civilization.vision.VisionTypes;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.people.Person;
import ConquerSpace.game.people.Scientist;
import ConquerSpace.game.science.Field;
import ConquerSpace.game.science.FieldNode;
import ConquerSpace.game.tech.Technologies;
import ConquerSpace.game.tech.Technology;
import ConquerSpace.game.universe.civilization.controllers.AIController.AIController;
import ConquerSpace.game.universe.civilization.controllers.CivilizationController;
import ConquerSpace.game.universe.civilization.stats.Economy;
import ConquerSpace.game.universe.civilization.stats.Population;
import ConquerSpace.game.universe.civilization.vision.VisionPoint;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.resources.ResourceStockpile;
import ConquerSpace.game.universe.ships.Ship;
import ConquerSpace.game.universe.ships.ShipClass;
import ConquerSpace.game.universe.ships.components.engine.EngineTechnology;
import ConquerSpace.game.universe.ships.hull.Hull;
import ConquerSpace.game.universe.ships.hull.HullMaterial;
import ConquerSpace.game.universe.ships.launch.LaunchSystem;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.Universe;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.json.JSONObject;

/**
 * Civilization
 *
 * @author Zyun
 */
public class Civilization {

    public static final int CIV_TECH_RESEARCH_CHANCE = 0;
    public static final int CIV_TECH_RESEARCH_AMOUNT = 1;
    private int ID;

    private Color color;
    private String name;
    private int civilizationPreferedClimate;
    private String civilizationSymbol;
    private String speciesName;

    private String homePlanetName;

    /**
     * The controller of this civ.
     */
    public CivilizationController controller;

    public Population pop;
    public Economy economy;

    public HashMap<UniversePath, Integer> vision;

    private UniversePath startingPlanet;

    public HashMap<Technology, Integer> civTechs;
    public HashMap<Technology, Integer> civResearch;
    public HashMap<Technology, Scientist> currentlyResearchingTechonologys;

    public HashMap<String, Integer> multipliers;
    public HashMap<String, Integer> values;

    private int techLevel = 0;

    public ArrayList<Person> people;
    public ArrayList<Person> unrecruitedPeople;

    public ArrayList<LaunchSystem> launchSystems;

    public ArrayList<JSONObject> satelliteTemplates;

    public ArrayList<VisionPoint> visionPoints;
    public ArrayList<ResourceStockpile> resourceStorages;

    public ArrayList<Ship> spaceships;
    public ArrayList<ShipClass> shipClasses;
    public ArrayList<HullMaterial> hullMaterials;
    public ArrayList<Hull> hulls;
    public ArrayList<Field> fields;
    public ArrayList<JSONObject> shipComponentList;
    public ArrayList<EngineTechnology> engineTechs;

    public HashMap<Resource, Integer> resourceList;

    public ArrayList<Planet> habitatedPlanets;
    
    public Civilization(int ID, Universe u) {
        this.ID = ID;

        //Set a temp starting point as in 0:0:0

        vision = new HashMap<>();

        //Add all the vision.
        for (int i = 0; i < u.getStarSystemCount(); i++) {
            StarSystem s = u.getStarSystem(i);
            this.vision.put(new UniversePath(i), VisionTypes.UNDISCOVERED);
            for (int h = 0; h < s.getPlanetCount(); h++) {
                //Add planets
                this.vision.put(new UniversePath(i, h), VisionTypes.UNDISCOVERED);
            }
            for (int h2 = 0; h2 < s.getStarCount(); h2++) {
                this.vision.put(new UniversePath(i, h2, true), VisionTypes.UNDISCOVERED);
            }
        }
        pop = new Population();
        economy = new Economy();

        civTechs = new HashMap<>();
        civResearch = new HashMap<>();

        currentlyResearchingTechonologys = new HashMap<>();
        
        people = new ArrayList<>();
        unrecruitedPeople = new ArrayList<>();
        
        launchSystems = new ArrayList<>();
        satelliteTemplates = new ArrayList<>();

        visionPoints = new ArrayList<>();
        resourceStorages = new ArrayList<>();

        spaceships = new ArrayList<>();
        shipClasses = new ArrayList<>();

        hullMaterials = new ArrayList<>();
        hulls = new ArrayList<>();
        shipComponentList = new ArrayList<>();
        
        engineTechs = new ArrayList<>();

        fields = new ArrayList<>();
        multipliers = new HashMap<>();
        values = new HashMap<>();

        habitatedPlanets = new ArrayList<>();
        
        resourceList = new HashMap<>();
        //Initialize resources
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

    public String getHomePlanetName() {
        return homePlanetName;
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

    public void processTurn(int turn) {
        //Stat everything
    }

    public void setStartingPlanet(UniversePath startingPlanet) {
        this.startingPlanet = startingPlanet;
    }

    public UniversePath getStartingPlanet() {
        return startingPlanet;
    }

    public void addTech(Technology t) {
        civTechs.put(t, 0);
        civResearch.put(t, 0);
    }

    public Technology getTechByName(String s) {
        return (civTechs.keySet().stream().filter(e -> e.getName().toLowerCase().equals(s.toLowerCase()))).findFirst().get();
    }

    public Technology[] getTechsByTag(String tag) {
        Object[] techList = civTechs.keySet().stream().filter(e -> Arrays.asList(e.getTags()).contains(tag)).filter(e -> civTechs.get(e) == Technologies.RESEARCHED).toArray();
        return (Arrays.copyOf(techList, techList.length, Technology[].class));
    }

    public void researchTech(Technology t) {
        //Parse actions.
        for (String act : t.getActions()) {
            Technologies.parseAction(act, this);
        }
        civTechs.put(t, Technologies.RESEARCHED);
        //Delete the tech because it has been researhed
        civResearch.remove(t);
    }

    public void assignResearch(Technology t, Person p) {
        if (people.contains(p) && p instanceof Scientist) {
            //Then do it...
            currentlyResearchingTechonologys.put(t, (Scientist) p);
            civResearch.put(t, 0);
            //Hide because it is researching
            civTechs.put(t, -1);
        }
    }

    public int getTechLevel() {
        return techLevel;
    }

    public void calculateTechLevel() {
        techLevel = 0;
        civTechs.keySet().stream().filter((t) -> (civTechs.get(t) == Technologies.RESEARCHED)).forEachOrdered((t) -> {
            techLevel += t.getLevel();
        });
    }

    public void addSatelliteTemplate(JSONObject s) {
        satelliteTemplates.add(s);
    }

    public void addShipComponent(JSONObject s) {
        shipComponentList.add(s);
    }

    public void putValue(String key, Integer value) {
        values.put(key, value);
    }

    public void putValue(String key, int value) {
        values.put(key, value);
    }

    public void putMultiplier(String key, Integer value) {
        multipliers.put(key, value);
    }

    public void putMultiplier(String key, int value) {
        multipliers.put(key, value);
    }

    public void upgradeField(FieldNode f) {
        //Loop through
        for (Field field : fields) {
            if (field.getNode().equals(f)) {
                //Increment field value
                field.incrementLevel(1);
                return;
            }
        }
        //Or else add it
        fields.add(new Field(f, 1));
    }

    public void upgradeField(FieldNode f, int amount) {
        //Loop through
        for (Field field : fields) {
            if (field.getNode().equals(f)) {
                //Increment field value
                field.incrementLevel(amount);
                return;
            }
        }
        //Or else add it
        fields.add(new Field(f, amount));
    }
}
