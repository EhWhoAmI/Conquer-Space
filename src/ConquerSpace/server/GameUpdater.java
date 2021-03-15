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
import ConquerSpace.common.actions.Alert;
import ConquerSpace.common.actions.OrganizationAction;
import ConquerSpace.common.actions.ShipAction;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.economy.Market;
import ConquerSpace.common.game.life.LocalLife;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.organizations.Organization;
import ConquerSpace.common.game.organizations.behavior.Behavior;
import ConquerSpace.common.game.organizations.civilization.vision.VisionPoint;
import ConquerSpace.common.game.organizations.civilization.vision.VisionTypes;
import ConquerSpace.common.game.population.Population;
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
import ConquerSpace.common.util.profiler.Profiler;
import static ConquerSpace.server.generators.DefaultUniverseGenerator.KM_IN_LTYR;
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

    private final int ledgerClearInterval;

    private GameIndexer indexer;
    private PeopleProcessor peopleProcessor;

    private Profiler profiler;

    public GameUpdater(GameState gameState) {
        universe = gameState.getUniverse();
        starDate = gameState.date;
        this.gameState = gameState;
        profiler = gameState.getProfiler();

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
        profiler.push("organization-actions");
        performActions();
        profiler.pop();

        profiler.push("ship-moving");
        //Move ships
        moveShips();
        profiler.pop();

        profiler.push("control");
        calculateControl();
        profiler.pop();

        profiler.push("vision");
        calculateVision();
        profiler.pop();

        //Check for month increase
        if (starDate.getDate() % GameRefreshRate == 1) {
            updateGame();

            profiler.push("positions");
            updateObjectPositions();
            profiler.pop();
        }
        //Process people and generate every 1000 ticks, which is about every 41 days
        if (starDate.getDate() % (GameRefreshRate * 2) == 1) {
            createPeople();
        }

        profiler.reset();
    }

    public synchronized void updateGame() {
        updateUniverse();

        //Increment tech
        profiler.push("research");
        processResearch(GameRefreshRate);
        for (int i = 0; i < gameState.getCivilizationCount(); i++) {
            gameState.getCivilizationObject(i).calculateTechLevel();
        }
        profiler.pop();

        //Increment resources
        processResources();

        profiler.push("people");
        peopleProcessor.processPeople(GameRefreshRate);
        profiler.pop();
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
                    ExceptionHandling.exceptionMessageBox("Exception while executing actions!", e);
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

            Civilization civil = gameState.getObject(civReferene, Civilization.class);
            //Deal with later...
            Body bod = gameState.getObject(p, Body.class);
            civil.getVision().put(bod.getUniversePath(), VisionTypes.KNOWS_ALL);
        }

        //Extra vision
        //Loop through all the vision points in the universe
        for (int civ = 0; civ < gameState.getCivilizationCount(); civ++) {
            ObjectReference civid = gameState.getCivilization(civ);
            Civilization civil = gameState.getObject(civid, Civilization.class);

            for (ObjectReference ptref : civil.getVisionPoints()) {
                ConquerSpaceGameObject object = gameState.getObject(ptref);
                if (!(object instanceof VisionPoint)) {
                    continue;
                }

                VisionPoint pt = (VisionPoint) object;
                int range = pt.getRange();
                //Distance between all star systems...
                UniversePath path = pt.getUniversePath();

                //Get system position
                Body body = gameState.getObject(universe.getSpaceObject(new UniversePath(path.getSystemIndex())), Body.class);

                SpacePoint pos = body.point;
                for (int g = 0; g < universe.getStarSystemCount(); g++) {
                    Body systemObject = universe.getStarSystemObject(g);
                    //check if in range
                    double dist = Math.hypot(pos.getY() - systemObject.getY(),
                            pos.getX() - systemObject.getX());

                    if (dist >= (range * KM_IN_LTYR)) {
                        continue;
                    }
                    //Its visible
                    int amount = ((int) ((1 - (dist / (double) (range * KM_IN_LTYR))) * 100));

                    civil.getVision().put(systemObject.getUniversePath(),
                            (amount > 100) ? 100 : (amount));
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
        //Process market stuff...
        for (ObjectReference cityId : planet.getCities()) {
            City city = gameState.getObject(cityId, City.class);
            CityProcessor processor = new CityProcessor(city, planet, gameState, GameRefreshRate);
            processor.process();
        }
    }

    private void doPlanetCensus(Planet p) {
        profiler.push("planet-census");
        //Index panet population
        long total = 0;
        for (ObjectReference cityId : p.getCities()) {
            City city = gameState.getObject(cityId, City.class);
            total += gameState.getObject(city.getPopulation(), Population.class).getPopulationSize();
        }
        p.setPopulation(total);
        profiler.pop();
    }

    private void processLocalLife(Planet p) {
        //Process locallife
        for (LocalLife localLife : p.getLocalLife()) {
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

            Iterator<Technology> tech = civilization.getCurrentlyResearchingTechonologys().keySet().iterator();

            while (tech.hasNext()) {
                Technology t = tech.next();

                if ((Technologies.estFinishTime(t) - civilization.getCivResearch().get(t)) <= 0) {
                    //Then tech is finished
                    civilization.researchTech(gameState, t);
                    civilization.getCivResearch().remove(t);
                    //c.currentlyResearchingTechonologys.remove(t);
                    tech.remove();

                    civilization.getBehavior().alert(new Alert(0, 0, "Tech " + t.getName() + " is finished"));
                } else {
                    //Increment by number of ticks
                    civilization.getCivResearch().put(t, civilization.getCivResearch().get(t) + civilization.getCurrentlyResearchingTechonologys().get(t).getSkill() * delta);
                }
            }
        }
    }

    private void processResources() {
        for (int i = 0; i < gameState.getCivilizationCount(); i++) {
            Civilization civilization = gameState.getCivilizationObject(i);

            for (Map.Entry<StoreableReference, Double> entry : civilization.getResourceList().entrySet()) {
                civilization.getResourceList().put(entry.getKey(), 0d);
            }
            //Process resources
            for (ResourceStockpile s : civilization.getResourceStorages()) {
                //Get resource types allowed

                for (StoreableReference type : s.storedTypes()) {
                    //add to index
                    if (!civilization.getResourceList().containsKey(type)) {
                        civilization.getResourceList().put(type, 0d);
                    }
                    Double amountToAdd = (civilization.getResourceList().get(type) + s.getResourceAmount(type));
                    civilization.getResourceList().put(type, amountToAdd);
                }
            }
        }
    }

    private void moveShips() {
        for (int i = 0; i < gameState.getCivilizationCount(); i++) {
            Civilization civilization = gameState.getCivilizationObject(i);

            //Process ship actions
            for (ObjectReference shipId : civilization.getSpaceships()) {
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
        //Loop through star systems
        for (int i = 0; i < universe.getStarSystemCount(); i++) {
            StarSystem system = universe.getStarSystemObject(i);
            SpacePoint starSystemPosition = system.getOrbit().toSpacePoint();
            system.setPoint(starSystemPosition);
            for (int k = 0; k < system.getBodyCount(); k++) {
                Body body = system.getBodyObject(k);

                SpacePoint pt = body.getOrbit().toSpacePoint();
                body.setPoint(new SpacePoint(pt.getX() + starSystemPosition.getX(), pt.getY() + starSystemPosition.getY()));
            }
        }
    }

    private void createPeople() {
        for (int i = 0; i < gameState.getCivilizationCount(); i++) {
            //Civilization civ = gameState.getCivilizationObject(i);

            //Because people stay now, will ignore them for now
        }
    }
}
