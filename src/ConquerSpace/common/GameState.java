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
package ConquerSpace.common;

import ConquerSpace.common.game.life.Species;
import ConquerSpace.common.game.characters.PersonalityTrait;
import ConquerSpace.common.game.organizations.Organization;
import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.common.game.resources.FoodGood;
import ConquerSpace.common.game.resources.Good;
import ConquerSpace.common.game.resources.LiveGood;
import ConquerSpace.common.game.resources.ProductionProcess;
import ConquerSpace.common.game.resources.ResourceDistribution;
import ConquerSpace.common.save.Serialize;
import ConquerSpace.common.game.science.FieldNode;
import ConquerSpace.common.game.science.Technology;
import ConquerSpace.common.game.ships.components.engine.EngineTechnology;
import ConquerSpace.common.game.ships.launch.LaunchSystem;
import ConquerSpace.common.game.universe.bodies.Galaxy;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Game object.
 *
 * @author EhWhoAmI
 */
public final class GameState {

    private long seed;

    //May need to be thread safe in the future
    protected final HashMap<Integer, ConquerSpaceGameObject> gameObjects;

    public Galaxy universe;

    @Serialize(key = "date")
    public StarDate date;

    //For evals
    //Rate the game refreshes buildings and stuff like that
    //Set to 5 days
    public final int GameRefreshRate = (5 * 24);

    //Variables read from 
    public ArrayList<LaunchSystem> launchSystems;

    private final ArrayList<Integer> civilizations;
    private final ArrayList<Integer> organizations;

    public ArrayList<EngineTechnology> engineTechnologys;
    public ArrayList<PersonalityTrait> personalityTraits;

    public ArrayList<Integer> species;

    public HashMap<String, Integer> shipTypes;
    public HashMap<String, Integer> shipTypeClasses;

    //Can theoratically delete this after universe generation is finished. Only needed for generating a star system
    public HashMap<Integer, ResourceDistribution> oreDistributions = new HashMap<>();

    //Handles goods
    private HashMap<Integer, Good> goodHashMap;
    private HashMap<String, Integer> goodIdentifiers;

    public HashMap<String, ProductionProcess> prodProcesses;

    public Integer playerCiv;
    public FieldNode fieldNodeRoot;
    public ArrayList<Technology> techonologies = new ArrayList<>();

    private Random random;

    public File saveFile;

    //private GameUpdater updater;
    public GameState(int seed) {
        this.seed = seed;

        //Init all stuff
        gameObjects = new HashMap<>();

        civilizations = new ArrayList<>();
        organizations = new ArrayList<>();

        goodIdentifiers = new HashMap<>();
        goodHashMap = new HashMap<>();

        date = new StarDate(1l);

        random = new Random(seed);

        universe = new Galaxy(this);
    }

    public Galaxy getUniverse() {
        return universe;
    }

    public void addGood(Good good) {
        int newId = goodHashMap.size();
        good.setId(newId);
        //Add the other stuff
        goodHashMap.put(newId, good);
        goodIdentifiers.put(good.getIdentifier(), newId);
    }

    public void addSpecies(Species species) {
        addGameObject(species);

        //Nice
        //Other stats...
        FoodGood foodGoodResource = new FoodGood(species, 1, 420);
        LiveGood speciesGoodResource = new LiveGood(species, 1, 420);
        addGood(foodGoodResource);
        addGood(speciesGoodResource);

        species.setFoodGood(foodGoodResource.getId());
        species.setSpeciesGood(speciesGoodResource.getId());
    }

    public Good getGood(int id) {
        return goodHashMap.get(id);
    }

    public Good getGood(String identifier) {
        return goodHashMap.get(goodIdentifiers.get(identifier));
    }

    public Good[] getGoodList() {
        Object[] goods = goodHashMap.values().toArray();
        return Arrays.copyOf(goods, goods.length, Good[].class);
    }

    /**
     * Adds the object to the index
     *
     * @param object object to add
     * @return the id of the added object
     */
    public int addGameObject(ConquerSpaceGameObject object) {
        int id = gameObjects.size();
        gameObjects.put(id, object);
        return id;
    }

    public ConquerSpaceGameObject getObject(int id) {
        return gameObjects.get(id);
    }

    public Integer getGoodId(String identifier) {
        return goodIdentifiers.get(identifier);
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(int id, Class<T> expectedClass) {
        ConquerSpaceGameObject o = gameObjects.get(id);
        if (expectedClass.isInstance(o)) {
            return (T) o;
        }
        return null;
    }

    public void addOrganization(Organization org) {
        organizations.add(org.getId());
    }
    
    public int getOrganizationCount() {
        return organizations.size();
    }
    
    public Integer getOrganization(int id) {
        return organizations.get(id);
    }
    
    public Organization getOrganizationObject(int id) {
        return getObject(getOrganization(id), Organization.class);
    }

    public void addCivilization(Civilization civ) {
        civilizations.add(civ.getId());
    }
    
    public int getCivilizationCount() {
        return civilizations.size();
    }
    
    public Integer getCivilization(int id) {
        return civilizations.get(id);
    }
    
    public Civilization getCivilizationObject(int id) {
        return getObject(getCivilization(id), Civilization.class);
    }

    public Random getRandom() {
        return random;
    }

    public void setSeed(long seed) {
        this.seed = seed;
        random.setSeed(seed);
    }

    public long getSeed() {
        return seed;
    }
    
    public void dumpEverything() {
        for (Map.Entry<Integer, ConquerSpaceGameObject> entry : gameObjects.entrySet()) {
            Object key = entry.getKey();
            Object val = entry.getValue();
            System.out.println(key + " " + val);
        }
        
    }
}
