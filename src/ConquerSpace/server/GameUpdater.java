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
package ConquerSpace.server;

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameIndexer;
import ConquerSpace.common.GameState;
import ConquerSpace.common.GameTicker;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.StarDate;
import ConquerSpace.common.actions.ActionStatus;
import static ConquerSpace.common.actions.Actions.removeResource;
import ConquerSpace.common.actions.Alert;
import ConquerSpace.common.actions.OrganizationAction;
import ConquerSpace.common.actions.ShipAction;
import ConquerSpace.common.game.characters.PersonalityTrait;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.city.CityType;
import ConquerSpace.common.game.city.area.Area;
import ConquerSpace.common.game.city.area.AreaClassification;
import ConquerSpace.common.game.city.area.ConstructingArea;
import ConquerSpace.common.game.city.modifier.CityModifier;
import ConquerSpace.common.game.city.modifier.RiotModifier;
import ConquerSpace.common.game.city.modifier.StarvationModifier;
import ConquerSpace.common.game.city.modifier.UnemployedModifier;
import ConquerSpace.common.game.life.LocalLife;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.organizations.Organization;
import ConquerSpace.common.game.organizations.behavior.Behavior;
import ConquerSpace.common.game.organizations.civilization.vision.VisionPoint;
import ConquerSpace.common.game.organizations.civilization.vision.VisionTypes;
import ConquerSpace.common.game.population.Population;
import ConquerSpace.common.game.population.PopulationSegment;
import ConquerSpace.common.game.population.Race;
import ConquerSpace.common.game.resources.ResourceStockpile;
import ConquerSpace.common.game.resources.StoreableReference;
import ConquerSpace.common.game.science.Technologies;
import ConquerSpace.common.game.science.Technology;
import ConquerSpace.common.game.ships.Ship;
import ConquerSpace.common.game.universe.SpacePoint;
import ConquerSpace.common.game.universe.UniversePath;
import ConquerSpace.common.game.universe.bodies.Body;
import ConquerSpace.common.game.universe.bodies.Galaxy;
import ConquerSpace.common.game.universe.bodies.Planet;
import ConquerSpace.common.game.universe.bodies.Star;
import ConquerSpace.common.game.universe.bodies.StarSystem;
import ConquerSpace.common.util.ExceptionHandling;
import ConquerSpace.common.util.logging.CQSPLogger;
import static ConquerSpace.server.generators.DefaultUniverseGenerator.AU_IN_LTYR;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.Logger;

/**
 * This controls the game.
 *
 * @author EhWhoAmI
 */
public class GameUpdater extends GameTicker {

    private static final Logger LOGGER = CQSPLogger.getLogger(GameUpdater.class.getName());

    private Galaxy universe;

    private StarDate starDate;

    private GameState gameState;

    private final int GameRefreshRate;

    private int ledgerClearInterval;

    private GameIndexer indexer;
    private PeopleProcessor peopleProcessor;

    public GameUpdater(GameState gameState) {
        universe = gameState.getUniverse();
        starDate = gameState.date;
        this.gameState = gameState;
        indexer = new GameIndexer(gameState.getUniverse());
        peopleProcessor = new PeopleProcessor(gameState);
        this.GameRefreshRate = gameState.GameRefreshRate;
        ledgerClearInterval = GameRefreshRate * 10;
    }

    //Process ingame tick.
    @Override
    public synchronized void tick(int tickIncrement) {
        //DO ticks
        starDate.increment(tickIncrement);

        //Execute org actions
        performActions();

        //Move ships
        moveShips();

        calculateControl();
        calculateVision();

        //Check for month increase            
        if (starDate.getDate() % GameRefreshRate == 1) {
            updateGame();
            updateObjectPositions();
        }
        //Process people and generate every 1000 ticks, which is about every 41 days
        if (starDate.getDate() % (GameRefreshRate * 2) == 1) {
            createPeople();
        }
    }

    public synchronized void updateGame() {
        updateUniverse();

        //Increment tech
        processResearch(GameRefreshRate);
        for (int i = 0; i < gameState.getCivilizationCount(); i++) {
            gameState.getCivilizationObject(i).calculateTechLevel();
        }

        //Increment resources
        processResources();

        peopleProcessor.processPeople(GameRefreshRate);
    }

    //Will have to check for actions that contradict each other or something in the future..
    public void performActions() {
        for (int i = 0; i < gameState.getOrganizationCount(); i++) {
            Organization org = gameState.getOrganizationObject(i);

            //Run code...
            Behavior be = org.getBehavior();

            if (be != null) {
                be.doBehavior();
            }
            for (int k = 0; k < org.actionList.size(); k++) {
                try {
                    OrganizationAction act = org.actionList.get(k);
                    ActionStatus status = act.doAction(gameState); //???
                } catch (Exception e) {
                    ExceptionHandling.ExceptionMessageBox("Exception while executing actions!", e);
                }
            }
            org.actionList.clear();
        }
    }

    public void calculateControl() {
        for (ObjectReference p : universe.control.keySet()) {
            StarSystem starsystem = gameState.getObject(p, StarSystem.class);
            ObjectReference owner = ObjectReference.INVALID_REFERENCE;
            for (int i = 0; i < starsystem.getBodyCount(); i++) {
                Body body = gameState.getObject(starsystem.getBody(i), Body.class);
                if (body instanceof Planet) {
                    Planet planet = (Planet) body;
                    if (planet.getOwnerReference() == ObjectReference.INVALID_REFERENCE) {
                        continue;
                    }
                    if (owner == ObjectReference.INVALID_REFERENCE) {
                        owner = planet.getOwnerReference();
                    } else if (owner != planet.getOwnerReference()) {
                        owner = ObjectReference.INVALID_REFERENCE;
                    }
                }
            }
            universe.control.put(p, owner);
        }
    }

    public void calculateVision() {
        for (ObjectReference p : universe.control.keySet()) {
            //Vision is always visible when you control it
            ObjectReference civReferene = universe.control.get(p);
            if (civReferene == null) {
                //Ignore, but rais error
            } else if (civReferene != ObjectReference.INVALID_REFERENCE) {
                Civilization civil = gameState.getObject(civReferene, Civilization.class);
                //Deal with later...
                Body bod = gameState.getObject(p, Body.class);
                civil.vision.put(bod.getUniversePath(), VisionTypes.KNOWS_ALL);
            }
        }

        //Loop through all the vision points in the universe
        for (int civ = 0; civ < gameState.getCivilizationCount(); civ++) {
            ObjectReference civid = gameState.getCivilization(civ);
            Civilization civil = gameState.getObject(civid, Civilization.class);
            for (ObjectReference ptref : civil.visionPoints) {
                ConquerSpaceGameObject object = gameState.getObject(ptref);
                if (object instanceof VisionPoint) {
                    VisionPoint pt = (VisionPoint) object;
                    int range = pt.getRange();
                    //Distance between all star systems...
                    UniversePath path = pt.getPosition();
                    if (path.getSystemIndex() > -1) {
                        //Get system position
                        Body body = gameState.getObject(universe.getSpaceObject(new UniversePath(path.getSystemIndex())), Body.class);
                        SpacePoint pos = body.point;
                        for (int g = 0; g < universe.getStarSystemCount(); g++) {
                            //check if in range
                            double dist = Math.hypot(pos.getY() - universe.getStarSystemObject(g).getY(),
                                    pos.getX() - universe.getStarSystemObject(g).getX());

                            if (dist < (range * AU_IN_LTYR)) {
                                //Its in!
                                int amount = ((int) ((1 - (dist / (double) (range * AU_IN_LTYR))) * 100));

                                civil.vision.put(universe.getStarSystemObject(g).getUniversePath(),
                                        (amount > 100) ? 100 : (amount));
                            }
                        }
                    }
                }
            }
        }
    }

    public void updateUniverse() {
        //Loop through star systems
        for (int i = 0; i < universe.getStarSystemCount(); i++) {
            updateStarSystem(universe.getStarSystemObject(i));
        }
    }

    private void updateStarSystem(StarSystem sys) {
        //Maybe later the objects in space.
        for (int i = 0; i < sys.getBodyCount(); i++) {
            Body body = sys.getBodyObject(i);
            if (body instanceof Planet) {
                processPlanet((Planet) body);
            } else if (body instanceof Star) {
                processStar((Star) body);
            }
        }
    }

    private void processPlanet(Planet planet) {
        if (planet.isHabitated()) {
            //So we know how many people there are
            doPlanetCensus(planet);

            processCities(planet);
        }
        processLocalLife(planet);
    }

    /**
     * Increments population of city, creates city jobs, and assigns them.
     *
     * @param planet
     * @param date
     * @param delta
     */
    private void processCities(Planet planet) {
        for (ObjectReference cityId : planet.cities) {
            City city = gameState.getObject(cityId, City.class);
            //Clear ledger
            city.resourceLedger.clear();
            city.resourcesGainedFrom.clear();
            city.resourcesSentTo.clear();
            city.primaryProduction.clear();

            //Process city demands
            //Create orders from cities that have stuff
            //Clear demands
            city.resourceDemands.clear();

            city.setEnergyProvided(0);

            //Update the tags
            for (CityModifier modifier : city.cityModifiers) {
                modifier.incrementTicks(GameRefreshRate);
            }

            //Population upkeep, population events.
            processPopulation(city);

            //Calculate jobs filled
            calculateCityJobs(city);

            //Process areas and their production
            for (ObjectReference areaId : city.areas) {
                Area area = gameState.getObject(areaId, Area.class);
                //if not owned, becomes owned by the planet owner
                if (area.getOwner() == ObjectReference.INVALID_REFERENCE) {
                    area.setOwner(planet.getOwnerReference());
                }
                processArea(planet, city, area);
            }

            //Replace constructing areas
            Iterator<ObjectReference> areaIterator = city.areas.iterator();
            ArrayList<ObjectReference> areasToAdd = new ArrayList<>();
            while (areaIterator.hasNext()) {
                Area area = gameState.getObject(areaIterator.next(), Area.class);

                if (area instanceof ConstructingArea) {
                    ConstructingArea constructingArea = ((ConstructingArea) area);
                    if (constructingArea.getTicksLeft() <= 0) {
                        areasToAdd.add(constructingArea.getToBuild());
                        areaIterator.remove();
                    }
                }
            }
            //May have to fill up jobs...
            city.areas.addAll(areasToAdd);

            CityType type = classifyCity(city);
            city.setCityType(type);

            //Do market, get what to buy
            //Set market price
            //Food is the most important
        }
    }

    /**
     * See the amount of jobs that are filled
     *
     * @param city
     * @param date
     */
    private void calculateCityJobs(City city) {
        long maxJobsProviding = 0;
        long necessaryJobsProviding = 0;
        long size = gameState.getObject(city.population, Population.class).getPopulationSize();

        //Sort them out based off piority
        //Get the area list and sort
        ArrayList<Area> areaTemp = new ArrayList<>();
        for (ObjectReference areaReference : city.areas) {
            Area a = gameState.getObject(areaReference, Area.class);
            areaTemp.add(a);
        }
        Collections.sort(areaTemp);

        for (Area area : areaTemp) {
            necessaryJobsProviding += area.operatingJobsNeeded();
            maxJobsProviding += area.getMaxJobsProvided();
        }

        if (maxJobsProviding < size) {
            //Fill necessary jobs if there are not enough people to get the max amount of people
            for (Area area : areaTemp) {
                area.setCurrentlyManningJobs(area.getMaxJobsProvided());
            }
        } else if (necessaryJobsProviding < size) {
            //Fill all the jobs needed to operate

            for (Area area : areaTemp) {
                size -= area.operatingJobsNeeded();
                area.setCurrentlyManningJobs(area.operatingJobsNeeded());
            }
            //Go through again, and add the jobs
            for (Area area : areaTemp) {
                int toFill = area.getMaxJobsProvided() - area.operatingJobsNeeded();

                if ((size - toFill) > 0) {
                    area.setCurrentlyManningJobs(area.getMaxJobsProvided());
                } else {
                    area.setCurrentlyManningJobs((area.operatingJobsNeeded() + (int) size));
                    break;
                }
            }
        } else {
            //Not enough jobs, so fill stuff according to piority
            for (Area area : areaTemp) {
                int jobsToAdd = area.operatingJobsNeeded();
                if ((size - jobsToAdd) > 0) {
                    size -= jobsToAdd;
                    area.setCurrentlyManningJobs(jobsToAdd);
                }
            }
        }
    }

    private void processPopulation(City city) {
        //Process population upkeep
        Population pop = gameState.getObject(city.population, Population.class);

        for (ObjectReference segid : pop.segments) {
            PopulationSegment seg = gameState.getObject(segid, PopulationSegment.class);
            //Request resources
            long amount = (seg.size / 1000);
            double consume = ((double) amount) * 0.5d;

            double foodAmount = 0;
            Race race = gameState.getObject(seg.species, Race.class);
            //Race race = city.resources.containsKey(gameState.getObject(seg.species, Race.class);
            if (city.resources.containsKey(race.getConsumableResource())) {
                foodAmount = city.getResourceAmount(race.getConsumableResource());
            }

            //Request food
            //Append resources
            city.resourceDemands.addValue(race.getConsumableResource(), consume);

            boolean success = removeResource(race.getConsumableResource(), consume, city);
            //Not enough food
            boolean starving = false;
            if (!success) {
                //can probably calculate other stuff, but who cares for now
                //Calculate ratio of food
                double populationConsumption = foodAmount * 2;
                double percentage = (populationConsumption / consume);
                percentage = 1d - percentage;
                if (!city.cityModifiers.contains(new StarvationModifier())) {
                    city.cityModifiers.add(new StarvationModifier());
                }
                starving = true;
            } else {
                city.cityModifiers.remove(new StarvationModifier());
            }

            //Process riots for starvation
            if (city.cityModifiers.contains(new StarvationModifier())) {
                StarvationModifier modifier = (StarvationModifier) city.cityModifiers.get(city.cityModifiers.indexOf(new StarvationModifier()));
                if (modifier.getTicks() > 1000) {
                    RiotModifier riotModifier = new RiotModifier();
                    if (!city.cityModifiers.contains(riotModifier)) {
                        city.cityModifiers.add(riotModifier);
                    }
                }
            }

            //Get population wealth, however that is defined
            //Increment population
            double fraction = ((double) GameRefreshRate) / 10000d;

            //if starving, then don't increment
            if (!starving) {
                seg.size = (long) ((double) seg.size * ((1 + seg.populationIncrease * fraction)));
            }
        }

        //Calculate unemployment rate
        double unemploymentRate = city.getUnemploymentRate();
        if (unemploymentRate > 0.1d) {
            if (!city.cityModifiers.contains(new UnemployedModifier())) {
                city.cityModifiers.add(new UnemployedModifier());
            }
        } else {
            city.cityModifiers.remove(new UnemployedModifier());
        }

        //if unemployed for a long time, complain
        if (city.cityModifiers.contains(new UnemployedModifier())) {
            UnemployedModifier modifier = (UnemployedModifier) city.cityModifiers.get(city.cityModifiers.indexOf(new UnemployedModifier()));
            if (modifier.getTicks() > 1000) {
                RiotModifier riotModifier = new RiotModifier();
                if (!city.cityModifiers.contains(riotModifier)) {
                    city.cityModifiers.add(riotModifier);
                }
            }
        }

        //Tax, the tax is paid to population
        long popSize = pop.getPopulationSize();
        long tax = popSize * 5;

        Object obj = gameState.getObject(city.getOwner());
        if (obj instanceof Civilization) {
            ((Civilization) obj).changeMoney(tax);
        }
    }

    private void doPlanetCensus(Planet p) {
        //Index panet population
        long total = 0;
        for (ObjectReference cityId : p.cities) {
            City city = gameState.getObject(cityId, City.class);
            total += gameState.getObject(city.population, Population.class).getPopulationSize();
        }
        p.population = total;
    }

    private void processArea(Planet planet, City city, Area area) {
        area.accept(new AreaBehaviorDispatcher(gameState, GameRefreshRate, city, planet));
    }

    private void processLocalLife(Planet p) {
        //Process locallife
        for (LocalLife localLife : p.localLife) {
            int biomass = localLife.getBiomass();
            float breedingRate = localLife.getSpecies().getBaseBreedingRate();
            localLife.setBiomass((int) (breedingRate * biomass) + biomass);
        }
    }

    private void processStar(Star s) {

    }

    private void processResearch(int delta) {
        for (int i = 0; i < gameState.getCivilizationCount(); i++) {
            Civilization civilization = gameState.getCivilizationObject(i);

            Iterator<Technology> tech = civilization.currentlyResearchingTechonologys.keySet().iterator();

            while (tech.hasNext()) {
                Technology t = tech.next();

                if ((Technologies.estFinishTime(t) - civilization.civResearch.get(t)) <= 0) {
                    //Then tech is finished
                    civilization.researchTech(gameState, t);
                    civilization.civResearch.remove(t);
                    //c.currentlyResearchingTechonologys.remove(t);
                    tech.remove();

                    civilization.getBehavior().alert(new Alert(0, 0, "Tech " + t.getName() + " is finished"));
                } else {
                    //Increment by number of ticks
                    civilization.civResearch.put(t, civilization.civResearch.get(t) + civilization.currentlyResearchingTechonologys.get(t).getSkill() * delta);
                }
            }

            //Process science labs
            for (ObjectReference scienceLab : civilization.scienceLabs) {
                //TODO...
            }
        }
    }

    private void processResources() {
        for (int i = 0; i < gameState.getCivilizationCount(); i++) {
            Civilization civilization = gameState.getCivilizationObject(i);

            for (Map.Entry<StoreableReference, Double> entry : civilization.resourceList.entrySet()) {
                civilization.resourceList.put(entry.getKey(), 0d);
            }
            //Process resources
            for (ResourceStockpile s : civilization.getResourceStorages()) {
                //Get resource types allowed

                for (StoreableReference type : s.storedTypes()) {
                    //add to index
                    if (!civilization.resourceList.containsKey(type)) {
                        civilization.resourceList.put(type, 0d);
                    }
                    Double amountToAdd = (civilization.resourceList.get(type) + s.getResourceAmount(type));
                    civilization.resourceList.put(type, amountToAdd);
                }
            }
        }
    }

    private CityType classifyCity(City dis) {
        //Get the type of areas
        HashMap<AreaClassification, Integer> areaType = new HashMap<>();
        for (ObjectReference areaId : dis.areas) {
            Area a = gameState.getObject(areaId, Area.class);
            if (areaType.containsKey(a.getAreaType())) {
                Integer num = areaType.get(a.getAreaType());
                num++;
                areaType.put(a.getAreaType(), num);
            } else {
                areaType.put(a.getAreaType(), 1);
            }
        }

        //Calulate stuff
        int highest = 0;
        AreaClassification highestArea = AreaClassification.Generic;
        for (Map.Entry<AreaClassification, Integer> entry : areaType.entrySet()) {
            AreaClassification key = entry.getKey();
            Integer val = entry.getValue();
            if (val > highest && key != AreaClassification.Generic) {
                highest = val;
                highestArea = key;
            }
        }

        switch (highestArea) {
            case Financial:
                return CityType.City;
            case Generic:
                return CityType.Generic;
            case Infrastructure:
                return CityType.Infrastructure;
            case Research:
                return CityType.Research;
            case Residential:
                return CityType.City;
            case Manufacturing:
                //City because it represents it better
                return CityType.City;
            case Farm:
                return CityType.Farm;
            case Mine:
                return CityType.Mine;
        }
        return CityType.Generic;
    }

    private void moveShips() {
        for (int i = 0; i < gameState.getCivilizationCount(); i++) {
            Civilization civilization = gameState.getCivilizationObject(i);

            //Process ship actions
            for (ObjectReference shipId : civilization.spaceships) {
                Ship ship = gameState.getObject(shipId, Ship.class);
                ShipAction sa = ship.getActionAndPopIfDone(gameState);
                if (!sa.checkIfDone(gameState)) {
                    sa.doAction(gameState);
                } else {
                    //Next action and init
                    ship.getActionAndPopIfDone(gameState).initAction(gameState);
                }
            }
        }
    }

    public void updateObjectPositions() {
        long start = System.currentTimeMillis();
        //Loop through star systems
        for (int i = 0; i < universe.getStarSystemCount(); i++) {
            StarSystem system = universe.getStarSystemObject(i);

            system.setPoint(system.getOrbit().toSpacePoint());
            for (int k = 0; k < system.getBodyCount(); k++) {
                Body body = system.getBodyObject(k);
                body.setPoint(body.getOrbit().toSpacePoint());
            }
        }
        long end = System.currentTimeMillis();
        //System.out.println((end - start));
    }

    private void createPeople() {
        for (int i = 0; i < gameState.getCivilizationCount(); i++) {
            Civilization civ = gameState.getCivilizationObject(i);

            //Because people stay now, will ignore them for now
        }
    }

    private PersonalityTrait getRandomPersonalityTrait() {
        return gameState.personalityTraits.get((int) (gameState.personalityTraits.size() * Math.random()));
    }
}
