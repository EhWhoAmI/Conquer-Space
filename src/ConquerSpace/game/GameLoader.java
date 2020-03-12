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

import static ConquerSpace.game.AssetReader.*;
import static ConquerSpace.game.GameController.satelliteTemplates;
import static ConquerSpace.game.GameController.shipComponentTemplates;
import static ConquerSpace.game.GameController.shipTypeClasses;
import static ConquerSpace.game.GameController.shipTypes;
import ConquerSpace.game.people.PersonalityTrait;
import ConquerSpace.game.science.Fields;
import ConquerSpace.game.tech.Technologies;
import ConquerSpace.game.universe.goods.Element;
import ConquerSpace.game.universe.goods.NonElement;
import ConquerSpace.game.universe.goods.Ore;
import ConquerSpace.game.universe.goods.ProductionProcess;
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.ships.components.engine.EngineTechnology;
import ConquerSpace.game.universe.ships.launch.LaunchSystem;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;

/**
 *
 * @author EhWhoAmI
 */
public class GameLoader {

    /**
     * Load all resources.
     */
    public static void load() {
        satelliteTemplates = new ArrayList<>();
        shipTypes = new HashMap<>();
        shipTypeClasses = new HashMap<>();
        shipComponentTemplates = new ArrayList<>();

        loadResources();
        //Init tech and fields
        Fields.readFields();
        Technologies.readTech();

        //All things to load go here!!!
        GameController.launchSystems = readHjsonFromDirInArray("dirs.launch",
                LaunchSystem.class, AssetReader::processLaunchSystem);

        GameController.satelliteTemplates = readHjsonFromDirInArray("dirs.satellite.types",
                JSONObject.class, AssetReader::processSatellite);

        readShipTypes();
        readShipComponents();

        GameController.personalityTraits = readHjsonFromDirInArray("dirs.ship.engine.tech",
                PersonalityTrait.class, AssetReader::processPersonalityTraits);

        GameController.engineTechnologys = readHjsonFromDirInArray("dirs.ship.engine.tech",
                EngineTechnology.class, AssetReader::processEngineTech);

        //Read elements
        GameController.elements = readHjsonFromDirInArray("dirs.elements",
                Element.class, AssetReader::processElement);

        GameController.ores = readHjsonFromDirInArray("dirs.ores", Ore.class, AssetReader::processOre);

        //Fill all goods
        GameController.allGoods = new ArrayList<>();
        GameController.allGoods.addAll(GameController.elements);
        GameController.allGoods.addAll(GameController.ores);
        
        GameController.prodProcesses = readHjsonFromDirInArray("dirs.processes", ProductionProcess.class, AssetReader::processProcess);
        readBuildingCosts();

        //Events
        readPopulationEvents();
    }

    public static void loadResources() {
        GameController.resources = readHjsonFromDirInArray("dirs.resources",
                Resource.class, AssetReader::processResource);
    }
}
