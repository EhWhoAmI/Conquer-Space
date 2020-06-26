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

import ConquerSpace.game.universe.generators.CivilizationInitializer;
import ConquerSpace.game.logistics.SupplyChain;
import ConquerSpace.game.people.Person;
import ConquerSpace.game.people.PersonalityTrait;
import ConquerSpace.game.resources.Element;
import ConquerSpace.game.resources.Good;
import ConquerSpace.game.resources.ProductionProcess;
import ConquerSpace.game.resources.ResourceDistribution;
import ConquerSpace.game.ships.components.engine.EngineTechnology;
import ConquerSpace.game.ships.launch.LaunchSystem;
import ConquerSpace.game.ships.satellites.Satellite;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.gui.music.MusicPlayer;
import ConquerSpace.util.Timer;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;

/**
 * Game object.
 * @author EhWhoAmI
 */
public class GameState {
    public Universe u;
   
    //For evals... 
    //Rate the game refreshes buildings and stuff like that
    //Set to 5 days
    public static final int GameRefreshRate = (5 * 24);

    //All variables...
    public ArrayList<LaunchSystem> launchSystems;
    private Timer ticker;
    public ArrayList<Satellite> satellites;
    public ArrayList<JSONObject> satelliteTemplates;
    public ArrayList<JSONObject> shipComponentTemplates;
    //public static Resource foodResource = null;
    public ArrayList<EngineTechnology> engineTechnologys;
    public ArrayList<JSONObject> events;
    public ArrayList<PersonalityTrait> personalityTraits;
    public ArrayList<Person> people = new ArrayList<>();

    public HashMap<String, Integer> shipTypes;
    public HashMap<String, Integer> shipTypeClasses;
    public GameUpdater updater;
    public CivilizationInitializer initer;
    public MusicPlayer musicPlayer;

    public ArrayList<Element> elements;
    public HashMap<Integer, ResourceDistribution> ores = new HashMap<>();

    public HashMap<Integer, Good> goodHashMap;
    public HashMap<String, Integer> goodIdentifiers;

    public ArrayList<Good> goods;

    public ArrayList<SupplyChain> supplyChains = new ArrayList<>();

    public HashMap<String, ProductionProcess> prodProcesses;
    
    //private GameUpdater updater;

    public GameState() {
        
    }
}
