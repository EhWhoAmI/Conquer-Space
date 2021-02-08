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
package ConquerSpace.common.game.organizations;

import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.characters.Person;
import ConquerSpace.common.game.characters.Scientist;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.economy.Currency;
import ConquerSpace.common.game.events.Event;
import ConquerSpace.common.game.organizations.civilization.government.Government;
import ConquerSpace.common.game.population.Race;
import ConquerSpace.common.game.population.RacePreferredClimateType;
import ConquerSpace.common.game.population.jobs.Employer;
import ConquerSpace.common.game.resources.ProductionProcess;
import ConquerSpace.common.game.resources.ResourceStockpile;
import ConquerSpace.common.game.resources.StoreableReference;
import ConquerSpace.common.game.science.Field;
import ConquerSpace.common.game.science.Technologies;
import ConquerSpace.common.game.science.Technology;
import ConquerSpace.common.game.universe.UniversePath;
import ConquerSpace.common.game.universe.bodies.Planet;
import ConquerSpace.common.save.Serialize;
import ConquerSpace.common.save.SerializeClassName;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Civilization
 *
 * @author EhWhoAmI
 */
@SerializeClassName("civilization")
public class Civilization extends Organization implements Employer {

    public static final int CIV_TECH_RESEARCH_CHANCE = 0;
    public static final int CIV_TECH_RESEARCH_AMOUNT = 1;

    private Color color;
    private RacePreferredClimateType civilizationPreferedClimate;
    private String civilizationSymbol;

    @Serialize("speciesName")
    private String speciesName;

    private String homePlanetName;

    private HashMap<UniversePath, Integer> vision;

    private UniversePath startingPlanet;

    private HashMap<Technology, Integer> civTechs;
    private HashMap<Technology, Integer> civResearch;
    private HashMap<Technology, Scientist> currentlyResearchingTechonologys;

    private HashMap<String, Double> multipliers;
    private HashMap<String, Integer> values;

    private int techLevel = 0;

    private ArrayList<ObjectReference> people;
    private ArrayList<ObjectReference> unrecruitedPeople;

    private ArrayList<ObjectReference> launchSystems;

    private ArrayList<ObjectReference> visionPoints;
    private ArrayList<ObjectReference> resourceStorages;

    private ArrayList<ObjectReference> spaceships;
    private ArrayList<ObjectReference> shipClasses;
    private ArrayList<ObjectReference> hullMaterials;
    private ArrayList<ObjectReference> hulls;
    public Field fields;
    private ArrayList<ObjectReference> shipComponentList;
    private ArrayList<ObjectReference> engineTechs;
    private ArrayList<ObjectReference> launchVehicles;

    /**
     * Resources that they possess.
     */
    private HashMap<StoreableReference, Double> resourceList;

    private HashMap<String, StoreableReference> taggedGoods;

    private ArrayList<ObjectReference> habitatedPlanets;

    private ArrayList<Event> events;

    private ArrayList<ProductionProcess> productionProcesses;

    private ArrayList<StoreableReference> mineableGoods;

    private ArrayList<ObjectReference> scienceLabs;

    private ArrayList<ObjectReference> cities;

    private ObjectReference foundingSpecies;

    private ObjectReference capitalCity;

    private ObjectReference capitalPlanet;

    private int techPoints = 0; //Research months or whatever

    private ObjectReference nationalCurrency;

    //Amount of money in millions of isk of their national currency ^
    private long moneyReserves = 0;

    private ArrayList<Integer> contacts;

    private Government government;

    public Civilization(GameState gameState) {
        super(gameState);

        //Set a temp starting point as in 0:0:0
        vision = new HashMap<>();

        civTechs = new HashMap<>();
        civResearch = new HashMap<>();

        currentlyResearchingTechonologys = new HashMap<>();

        people = new ArrayList<>();
        unrecruitedPeople = new ArrayList<>();

        launchSystems = new ArrayList<>();

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
        taggedGoods = new HashMap<>();

        events = new ArrayList<>();

        contacts = new ArrayList<>();

        productionProcesses = new ArrayList<>();

        mineableGoods = new ArrayList<>();

        government = new Government();

        scienceLabs = new ArrayList<>();

        cities = new ArrayList<>();
    }

    public void setCivilizationSymbol(String civilizationSymbol) {
        this.civilizationSymbol = civilizationSymbol;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setHomePlanetName(String homePlanetName) {
        this.homePlanetName = homePlanetName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public void setCivilizationPreferredClimate(RacePreferredClimateType civilizationPreferedClimate) {
        this.civilizationPreferedClimate = civilizationPreferedClimate;
    }

    public RacePreferredClimateType getCivilizationPreferredClimate() {
        return civilizationPreferedClimate;
    }

    public String getCivilizationSymbol() {
        return civilizationSymbol;
    }

    public Color getColor() {
        return color;
    }

    public String getHomePlanetName() {
        return homePlanetName;
    }

    public String getSpeciesName() {
        return speciesName;
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
        getCivTechs().put(t, 0);
        getCivResearch().put(t, 0);
    }

    public Technology getTechByName(String s) {
        return (getCivTechs().keySet().stream().filter(e -> e.getName().equalsIgnoreCase(s))).findFirst().get();
    }

    public Technology[] getTechsByTag(String tag) {
        Object[] techList = getCivTechs().keySet().stream().filter(e -> Arrays.asList(e.getTags()).contains(tag)).filter(e -> getCivTechs().get(e) == Technologies.RESEARCHED).toArray();
        return (Arrays.copyOf(techList, techList.length, Technology[].class));
    }

    public void researchTech(GameState gameState, Technology t) {
        //Parse actions.
        for (String act : t.getActions()) {
            Technologies.parseAction(act, gameState, this);
        }
        getCivTechs().put(t, Technologies.RESEARCHED);
        //Delete the tech because it has been researhed
        getCivResearch().remove(t);
    }

    public void assignResearch(Technology t, Person p) {
        if (getPeople().contains(p.getReference()) && p instanceof Scientist) {
            //Then do it...
            getCurrentlyResearchingTechonologys().put(t, (Scientist) p);
            getCivResearch().put(t, 0);
            //Hide because it is researching
            getCivTechs().put(t, -1);
        }
    }

    public int getTechLevel() {
        return techLevel;
    }

    public void calculateTechLevel() {
        techLevel = 0;
        getCivTechs().keySet().stream().filter((t) -> (getCivTechs().get(t) == Technologies.RESEARCHED)).forEachOrdered((t) -> {
            techLevel += t.getLevel();
        });
    }

    public void addShipComponent(ObjectReference s) {
        getShipComponentList().add(s);
    }

    public void putValue(String key, Integer value) {
        getValues().put(key, value);
    }

    public void putValue(String key, int value) {
        getValues().put(key, value);
    }

    public void putMultiplier(String key, Double value) {
        getMultipliers().put(key, value);
    }

    public void putMultiplier(String key, double value) {
        getMultipliers().put(key, value);
    }

    public void setFoundingSpecies(Race foundingSpecies) {
        this.foundingSpecies = foundingSpecies.getReference();
    }

    public Race getFoundingSpecies() {
        return gameState.getObject(foundingSpecies, Race.class);
    }

    public ObjectReference getCapitalCity() {
        return capitalCity;
    }

    public void setCapitalCity(City capitalCity) {
        this.capitalCity = capitalCity.getReference();
    }

    public Planet getCapitalPlanet() {
        return gameState.getObject(capitalPlanet, Planet.class);
    }

    public void setCapitalPlanet(Planet capitalPlanet) {
        this.capitalPlanet = capitalPlanet.getReference();
    }

    public int getTechPoints() {
        return techPoints;
    }

    public void setTechPoints(int techPoints) {
        this.techPoints = techPoints;
    }

    //Field stuff
    public void upgradeField(String name, Double amount) {
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
        this.nationalCurrency = nationalCurrency.getReference();
    }

    public Currency getNationalCurrency() {
        return gameState.getObject(nationalCurrency, Currency.class);
    }

    public void setMoneyReserves(long moneyReserves) {
        this.moneyReserves = moneyReserves;
    }

    public long getMoneyReserves() {
        return moneyReserves;
    }

    @Override
    public Currency getCurrency() {
        return gameState.getObject(nationalCurrency, Currency.class);
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

    }

    public void employ(ObjectReference p) {
        getPeople().add(p);
        gameState.getObject(p, Person.class).employer = getReference();
    }

    public ArrayList<ResourceStockpile> getResourceStorages() {
        ArrayList<ResourceStockpile> resourceStockpiles = new ArrayList<>();

        for (int i = 0; i < resourceStorages.size(); i++) {
            ResourceStockpile pile = gameState.getObject(resourceStorages.get(i), ResourceStockpile.class);
            if (pile != null) {
                resourceStockpiles.add(pile);
            }
        }
        return resourceStockpiles;
    }

    public boolean civValueIsGreaterThan(String key, Integer i) {
        if (getValues().containsKey(key)) {
            return getValues().get(key) > i;
        }
        return false;
    }

    /**
     * @return the unrecruitedPeople
     */
    public ArrayList<ObjectReference> getUnrecruitedPeople() {
        return unrecruitedPeople;
    }

    /**
     * @return the launchSystems
     */
    public ArrayList<ObjectReference> getLaunchSystems() {
        return launchSystems;
    }

    /**
     * @return the visionPoints
     */
    public ArrayList<ObjectReference> getVisionPoints() {
        return visionPoints;
    }

    /**
     * @return the spaceships
     */
    public ArrayList<ObjectReference> getSpaceships() {
        return spaceships;
    }

    /**
     * @return the shipClasses
     */
    public ArrayList<ObjectReference> getShipClasses() {
        return shipClasses;
    }

    /**
     * @return the hullMaterials
     */
    public ArrayList<ObjectReference> getHullMaterials() {
        return hullMaterials;
    }

    /**
     * @return the hulls
     */
    public ArrayList<ObjectReference> getHulls() {
        return hulls;
    }

    /**
     * @return the shipComponentList
     */
    public ArrayList<ObjectReference> getShipComponentList() {
        return shipComponentList;
    }

    /**
     * @return the engineTechs
     */
    public ArrayList<ObjectReference> getEngineTechs() {
        return engineTechs;
    }

    /**
     * @return the launchVehicles
     */
    public ArrayList<ObjectReference> getLaunchVehicles() {
        return launchVehicles;
    }

    /**
     * @return the resourceList
     */
    public HashMap<StoreableReference, Double> getResourceList() {
        return resourceList;
    }

    /**
     * @return the taggedGoods
     */
    public HashMap<String, StoreableReference> getTaggedGoods() {
        return taggedGoods;
    }

    /**
     * @return the habitatedPlanets
     */
    public ArrayList<ObjectReference> getHabitatedPlanets() {
        return habitatedPlanets;
    }

    /**
     * @return the events
     */
    public ArrayList<Event> getEvents() {
        return events;
    }

    /**
     * @return the productionProcesses
     */
    public ArrayList<ProductionProcess> getProductionProcesses() {
        return productionProcesses;
    }

    /**
     * @return the mineableGoods
     */
    public ArrayList<StoreableReference> getMineableGoods() {
        return mineableGoods;
    }

    /**
     * @return the scienceLabs
     */
    public ArrayList<ObjectReference> getScienceLabs() {
        return scienceLabs;
    }

    /**
     * @return the cities
     */
    public ArrayList<ObjectReference> getCities() {
        return cities;
    }

    /**
     * @return the contacts
     */
    public ArrayList<Integer> getContacts() {
        return contacts;
    }

    /**
     * @return the government
     */
    public Government getGovernment() {
        return government;
    }

    /**
     * Sets the government, AKA maybe a coup lol.
     * @param government
     */
    public void setGovernment(Government government) {
        this.government = government;
    }

    /**
     * @return the vision
     */
    public HashMap<UniversePath, Integer> getVision() {
        return vision;
    }

    /**
     * @return the civTechs
     */
    public HashMap<Technology, Integer> getCivTechs() {
        return civTechs;
    }

    /**
     * @return the civResearch
     */
    public HashMap<Technology, Integer> getCivResearch() {
        return civResearch;
    }

    /**
     * @return the currentlyResearchingTechonologys
     */
    public HashMap<Technology, Scientist> getCurrentlyResearchingTechonologys() {
        return currentlyResearchingTechonologys;
    }

    /**
     * @return the multipliers
     */
    public HashMap<String, Double> getMultipliers() {
        return multipliers;
    }

    /**
     * @return the values
     */
    public HashMap<String, Integer> getValues() {
        return values;
    }

    /**
     * @return the people
     */
    public ArrayList<ObjectReference> getPeople() {
        return people;
    }

    /**
     * @param people the people to set
     */
    public void setPeople(ArrayList<ObjectReference> people) {
        this.people = people;
    }
}
