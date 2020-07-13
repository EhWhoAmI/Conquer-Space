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
package ConquerSpace.game;

import ConquerSpace.game.life.Species;
import ConquerSpace.game.logistics.SupplyChain;
import ConquerSpace.game.organizations.civilization.Civilization;
import ConquerSpace.game.people.Person;
import ConquerSpace.game.people.PersonalityTrait;
import ConquerSpace.game.resources.FoodGood;
import ConquerSpace.game.resources.Good;
import ConquerSpace.game.resources.LiveGood;
import ConquerSpace.game.resources.ProductionProcess;
import ConquerSpace.game.resources.ResourceDistribution;
import ConquerSpace.game.save.Serialize;
import ConquerSpace.game.science.FieldNode;
import ConquerSpace.game.science.tech.Technology;
import ConquerSpace.game.ships.components.engine.EngineTechnology;
import ConquerSpace.game.ships.launch.LaunchSystem;
import ConquerSpace.game.ships.satellites.Satellite;
import ConquerSpace.game.universe.bodies.Universe;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;

/**
 * Game object.
 *
 * @author EhWhoAmI
 */
public class GameState {

    @Serialize(key = "universe")
    public Universe universe;

    @Serialize(key = "date")
    public StarDate date;

    //For evals... 
    //Rate the game refreshes buildings and stuff like that
    //Set to 5 days
    public final int GameRefreshRate = (5 * 24);

    //All variables...
    public ArrayList<LaunchSystem> launchSystems;
    
    //S
    public HashMap<Integer, Object> entityManager;
    
    public ArrayList<Satellite> satellites;
    public transient ArrayList<JSONObject> satelliteTemplates;
    public transient ArrayList<JSONObject> shipComponentTemplates;

    public transient ArrayList<EngineTechnology> engineTechnologys;
    public transient ArrayList<JSONObject> events;
    public ArrayList<PersonalityTrait> personalityTraits;
    public ArrayList<Person> people = new ArrayList<>();

    public HashMap<String, Integer> shipTypes;
    public HashMap<String, Integer> shipTypeClasses;

    public HashMap<Integer, ResourceDistribution> ores = new HashMap<>();

    public HashMap<Integer, Good> goodHashMap;
    public HashMap<String, Integer> goodIdentifiers;

    public ArrayList<Good> goods;

    public ArrayList<SupplyChain> supplyChains = new ArrayList<>();

    public HashMap<String, ProductionProcess> prodProcesses;

    public Civilization playerCiv;
    public FieldNode fieldNodeRoot;
    public ArrayList<Technology> techonologies = new ArrayList<>();

    public File saveFile;

    //private GameUpdater updater;
    public GameState() {
        //Init all stuff
        goods = new ArrayList<>();
        date = new StarDate();
    }

    public void addGood(Good good) {
        int newId = goods.size();
        good.setId(newId);
        goods.add(good);

        //Add the other stuff
        goodHashMap.put(newId, good);
        goodIdentifiers.put(good.getIdentifier(), newId);
    }

    public void addSpecies(Species species) {
        //Add food stuff

        //Nice
        FoodGood foodGoodResource = new FoodGood(species, 1, 420);
        LiveGood speciesGoodResource = new LiveGood(species, 1, 420);
        addGood(foodGoodResource);
        addGood(speciesGoodResource);

        species.setFoodGood(foodGoodResource.getId());
        species.setSpeciesGood(speciesGoodResource.getId());
    }
}
