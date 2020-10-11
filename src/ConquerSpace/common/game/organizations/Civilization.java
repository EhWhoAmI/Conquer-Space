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
import ConquerSpace.common.game.population.RacePreferredClimateTpe;
import ConquerSpace.common.game.population.jobs.Employer;
import ConquerSpace.common.game.resources.StoreableReference;
import ConquerSpace.common.game.resources.ProductionProcess;
import ConquerSpace.common.game.resources.ResourceStockpile;
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
    private RacePreferredClimateTpe civilizationPreferedClimate;
    private String civilizationSymbol;

    @Serialize("speciesName")
    private String speciesName;

    private String homePlanetName;

    public HashMap<UniversePath, Integer> vision;

    private UniversePath startingPlanet;

    public HashMap<Technology, Integer> civTechs;
    public HashMap<Technology, Integer> civResearch;
    public HashMap<Technology, Scientist> currentlyResearchingTechonologys;

    public HashMap<String, Double> multipliers;
    public HashMap<String, Integer> values;

    private int techLevel = 0;

    public ArrayList<ObjectReference> people;
    public ArrayList<ObjectReference> unrecruitedPeople;

    public ArrayList<ObjectReference> launchSystems;

    public ArrayList<ObjectReference> visionPoints;
    public ArrayList<ObjectReference> resourceStorages;

    public ArrayList<ObjectReference> spaceships;
    public ArrayList<ObjectReference> shipClasses;
    public ArrayList<ObjectReference> hullMaterials;
    public ArrayList<ObjectReference> hulls;
    public Field fields;
    public ArrayList<ObjectReference> shipComponentList;
    public ArrayList<ObjectReference> engineTechs;
    public ArrayList<ObjectReference> launchVehicles;

    /**
     * Resources that they possess.
     */
    public HashMap<StoreableReference, Double> resourceList;
    
    public HashMap<String, StoreableReference> taggedGoods;

    public ArrayList<ObjectReference> habitatedPlanets;

    public ArrayList<Event> events;

    public ArrayList<ProductionProcess> productionProcesses;

    public ArrayList<StoreableReference> mineableGoods;

    public ArrayList<ObjectReference> scienceLabs;

    private ArrayList<ObjectReference> cities;

    private ObjectReference foundingSpecies;

    private ObjectReference capitalCity;

    private ObjectReference capitalPlanet;

    private int techPoints = 0; //Research months or whatever

    private ObjectReference nationalCurrency;

    //Amount of money in millions of isk of their national currency ^
    private long moneyReserves = 0;

    public ArrayList<Integer> contacts;

    public Government government;

    public Civilization(GameState gameState, String name) {
        super(gameState, name);

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

    public void setCivilizationPreferredClimate(RacePreferredClimateTpe civilizationPreferedClimate) {
        this.civilizationPreferedClimate = civilizationPreferedClimate;
    }

    public RacePreferredClimateTpe getCivilizationPreferredClimate() {
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

    public void researchTech(GameState gameState, Technology t) {
        //Parse actions.
        for (String act : t.getActions()) {
            Technologies.parseAction(act, gameState, this);
        }
        civTechs.put(t, Technologies.RESEARCHED);
        //Delete the tech because it has been researhed
        civResearch.remove(t);
    }

    public void assignResearch(Technology t, Person p) {
        if (people.contains(p.getReference()) && p instanceof Scientist) {
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

    public void addShipComponent(ObjectReference s) {
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
        people.add(p);
        gameState.getObject(p, Person.class).employer = getReference();
    }

    public ArrayList<ResourceStockpile> getResourceStorages() {
        ArrayList<ResourceStockpile> resourceStockpiles = new ArrayList<>();
        
        for (int i = 0; i < resourceStorages.size(); i++) {
            ResourceStockpile pile = gameState.getObject(resourceStorages.get(i), ResourceStockpile.class);
            if(pile != null) {
                resourceStockpiles.add(pile);
            }
        }
        return resourceStockpiles;
    }
}
