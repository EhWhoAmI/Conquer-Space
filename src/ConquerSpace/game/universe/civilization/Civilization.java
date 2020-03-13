/*
 * Conquer Space - Conquer Space!
 * Copyright (C) 2019 EhWhoAmI
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ConquerSpace.game.universe.civilization;

import ConquerSpace.game.buildings.City;
import ConquerSpace.game.economy.Currency;
import ConquerSpace.game.events.Event;
import ConquerSpace.game.population.Race;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.people.Person;
import ConquerSpace.game.people.Scientist;
import ConquerSpace.game.jobs.Employer;
import ConquerSpace.game.people.Administrator;
import ConquerSpace.game.population.PopulationUnit;
import ConquerSpace.game.science.Field;
import ConquerSpace.game.tech.Technologies;
import ConquerSpace.game.tech.Technology;
import ConquerSpace.game.universe.civilization.controllers.AIController.AIController;
import ConquerSpace.game.universe.civilization.controllers.CivilizationController;
import ConquerSpace.game.universe.civilization.government.Government;
import ConquerSpace.game.universe.civilization.stats.Economy;
import ConquerSpace.game.universe.civilization.stats.Population;
import ConquerSpace.game.universe.civilization.vision.VisionPoint;
import ConquerSpace.game.universe.resources.Good;
import ConquerSpace.game.universe.resources.ProductionProcess;
import ConquerSpace.game.universe.resources.ResourceStockpile;
import ConquerSpace.game.universe.ships.Ship;
import ConquerSpace.game.universe.ships.ShipClass;
import ConquerSpace.game.universe.ships.components.engine.EngineTechnology;
import ConquerSpace.game.universe.ships.hull.Hull;
import ConquerSpace.game.universe.ships.hull.HullMaterial;
import ConquerSpace.game.universe.ships.launch.LaunchSystem;
import ConquerSpace.game.universe.ships.launch.LaunchVehicle;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.Universe;
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
public class Civilization implements Employer{

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

    public HashMap<String, Double> multipliers;
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
    public Field fields;
    public ArrayList<JSONObject> shipComponentList;
    public ArrayList<EngineTechnology> engineTechs;
    public ArrayList<LaunchVehicle> launchVehicles;

    public HashMap<Good, Integer> resourceList;

    public ArrayList<Planet> habitatedPlanets;

    public ArrayList<PopulationUnit> population;

    public ArrayList<Event> events;
    
    public ArrayList<ProductionProcess> productionProcesses;

    private Race foundingSpecies;

    private City capitalCity;

    private Planet capitalPlanet;

    private int techPoints = 0; //Research months or whatever

    private Currency nationalCurrency;
    
    //Amount of money in millions of isk of their national currency ^
    private long moneyReserves = 0;
    
    public ArrayList<Civilization> contacts;
    
    public Government government;

    public Civilization(int ID, Universe u) {
        this.ID = ID;

        //Set a temp starting point as in 0:0:0
        vision = new HashMap<>();

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
        
        launchVehicles = new ArrayList<>();

        multipliers = new HashMap<>();
        values = new HashMap<>();

        habitatedPlanets = new ArrayList<>();

        resourceList = new HashMap<>();

        population = new ArrayList<>();

        events = new ArrayList<>();
        
        contacts = new ArrayList<>();
        
        productionProcesses = new ArrayList<>();
        
        government = new Government();
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

    public void putMultiplier(String key, Double value) {
        multipliers.put(key, value);
    }

    public void putMultiplier(String key, double value) {
        multipliers.put(key, value);
    }

    public void setFoundingSpecies(Race foundingSpecies) {
        this.foundingSpecies = foundingSpecies;
    }

    public Race getFoundingSpecies() {
        return foundingSpecies;
    }

    public City getCapitalCity() {
        return capitalCity;
    }

    public void setCapitalCity(City capitalCity) {
        this.capitalCity = capitalCity;
    }

    public Planet getCapitalPlanet() {
        return capitalPlanet;
    }

    public void setCapitalPlanet(Planet capitalPlanet) {
        this.capitalPlanet = capitalPlanet;
    }

    public int getTechPoints() {
        return techPoints;
    }

    public void setTechPoints(int techPoints) {
        this.techPoints = techPoints;
    }

    //Field stuff
    public void upgradeField(String name, int amount) {
        //Search for field...
        if (fields != null) {
            Field f = fields.getNode(name);
            if (f != null) {
                if (f.getLevel() < 0) {
                    f.setLevel(0);
                }
                f.incrementLevel(amount);
            }
        }
    }

    public void setField(Field start) {
        fields = start;
    }

    public void setNationalCurrency(Currency nationalCurrency) {
        this.nationalCurrency = nationalCurrency;
    }

    public Currency getNationalCurrency() {
        return nationalCurrency;
    }

    public void setMoneyReserves(long moneyReserves) {
        this.moneyReserves = moneyReserves;
    }

    public long getMoneyReserves() {
        return moneyReserves;
    }

    @Override
    public Currency getCurrency() {
        return nationalCurrency;
    }

    @Override
    public long getMoney() {
        return moneyReserves;
    }

    @Override
    public void changeMoney(long amount) {
        moneyReserves += amount;
    }
    
    public void passEvent(Event e) {
        controller.passEvent(e);
    }
    
    public void employ(Person p) {
        people.add(p);
        if(p instanceof Administrator) {
            ((Administrator) p).employer = this;
        }
    }
}
