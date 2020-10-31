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

import ConquerSpace.common.game.characters.PersonalityTrait;
import ConquerSpace.common.game.life.Species;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.organizations.Organization;
import ConquerSpace.common.game.resources.Good;
import ConquerSpace.common.game.resources.ProductionProcess;
import ConquerSpace.common.game.resources.ResourceDistribution;
import ConquerSpace.common.game.resources.StoreableReference;
import ConquerSpace.common.game.science.FieldNode;
import ConquerSpace.common.game.science.Technology;
import ConquerSpace.common.game.ships.EngineTechnology;
import ConquerSpace.common.game.ships.ShipType;
import ConquerSpace.common.game.ships.launch.LaunchSystem;
import ConquerSpace.common.game.universe.bodies.Galaxy;
import ConquerSpace.common.save.Serialize;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Random;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

/**
 * Game object.
 *
 * @author EhWhoAmI
 */
public final class GameState implements Serializable {

    //All game objects
    //May need to be thread safe in the future
    EntityManager entities;

    @Serialize("seed")
    private long seed;

    @Serialize("universe")
    private ObjectReference universeId;

    @Serialize("date")
    public StarDate date;

    //For evals
    //Rate the game refreshes buildings and stuff like that
    //Set to 5 days
    public final int GameRefreshRate = (5 * 24);

    //Variables read from 
    public ArrayList<LaunchSystem> launchSystems;

    @Serialize("civs")
    private ArrayList<ObjectReference> civilizations;

    @Serialize("orgs")
    private ArrayList<ObjectReference> organizations;

    public ArrayList<EngineTechnology> engineTechnologys;
    public ArrayList<PersonalityTrait> personalityTraits;

    @Serialize("species")
    public HashSet<ObjectReference> species;

    public ArrayList<ShipType> shipTypes;
    public HashMap<String, Integer> shipTypeClasses;

    //Can theoratically delete this after universe generation is finished. Only needed for generating a star system
    public HashMap<StoreableReference, ResourceDistribution> oreDistributions = new HashMap<>();

    //Handles goods
    private HashMap<StoreableReference, Good> goodHashMap;
    public DualHashBidiMap<String, StoreableReference> goodIdentifiers;

    public HashMap<String, ProductionProcess> prodProcesses;

    public Properties constants;

    @Serialize("player")
    public ObjectReference playerCiv;

    public FieldNode fieldNodeRoot;
    public ArrayList<Technology> techonologies = new ArrayList<>();

    private Random random;

    public transient File saveFile;

    //private GameUpdater updater;
    public GameState(int seed) {
        this.seed = seed;

        entities = new EntityManager();

        civilizations = new ArrayList<>();
        organizations = new ArrayList<>();

        species = new HashSet<>();

        goodIdentifiers = new DualHashBidiMap<>();
        goodHashMap = new HashMap<>();

        date = new StarDate(1l);

        random = new Random(seed);

        constants = new Properties();

        //Create new galaxy
        universeId = new Galaxy(this).getReference();
    }

    public void addGood(Good good) {
        //Add the other stuff
        goodHashMap.put(good.getId(), good);
        goodIdentifiers.put(good.getIdentifier(), good.getId());
    }

    public void addSpecies(Species speciesToAdd) {
        species.add(speciesToAdd.getReference());
    }

    public Good getGood(StoreableReference id) {
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
     */
    public void addGameObject(ConquerSpaceGameObject object) {
        entities.addGameObject(object);
    }

    public ConquerSpaceGameObject getObject(ObjectReference id) {
        return entities.getObject(id);
    }

    public StoreableReference getGoodId(String identifier) {
        return goodIdentifiers.get(identifier);
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(ObjectReference id, Class<T> expectedClass) {
        ConquerSpaceGameObject o = entities.getObject(id);
        if (expectedClass.isInstance(o)) {
            return (T) o;
        }
        return null;
    }

    public int getObjectCount() {
        return entities.getEntitiyCount();
    }

    public void addOrganization(Organization org) {
        organizations.add(org.getReference());
    }

    public int getOrganizationCount() {
        return organizations.size();
    }

    public ObjectReference getOrganization(int id) {
        return organizations.get(id);
    }

    public Organization getOrganizationObject(int id) {
        return getObject(getOrganization(id), Organization.class);
    }

    public Organization getOrganizationObjectByReference(ObjectReference id) {
        return getObject(id, Organization.class);
    }

    public void addCivilization(Civilization civ) {
        civilizations.add(civ.getReference());
        organizations.add(civ.getReference());
    }

    public int getCivilizationCount() {
        return civilizations.size();
    }

    public ObjectReference getCivilization(int id) {
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

    public Galaxy getUniverse() {
        return getObject(universeId, Galaxy.class);
    }

    public ObjectReference getUniverseId() {
        return universeId;
    }

    public ArrayList<Good> getGoodArrayList() {
        return new ArrayList<>(goodHashMap.values());
    }

    /**
     * Set itself to the gamestate
     *
     * @param gameState
     */
    public void convert(GameState gameState) {
        seed = gameState.seed;

        entities = gameState.entities;

        universeId = gameState.universeId;

        date = gameState.date;
        launchSystems = gameState.launchSystems;
        civilizations = gameState.civilizations;
        organizations = gameState.organizations;

        engineTechnologys = gameState.engineTechnologys;
        personalityTraits = gameState.personalityTraits;
        species = gameState.species;

        shipTypes = gameState.shipTypes;
        shipTypeClasses = gameState.shipTypeClasses;

        //Can theoratically delete this after universe generation is finished. Only needed for generating a star system
        oreDistributions = gameState.oreDistributions;

        //Handles goods
        goodHashMap = gameState.goodHashMap;
        goodIdentifiers = gameState.goodIdentifiers;

        prodProcesses = gameState.prodProcesses;

        playerCiv = gameState.playerCiv;

        fieldNodeRoot = gameState.fieldNodeRoot;
        techonologies = gameState.techonologies;

        constants = gameState.constants;

        random = gameState.random;
    }
}
