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
import ConquerSpace.game.science.tech.Technologies;
import ConquerSpace.game.ships.components.engine.EngineTechnology;
import ConquerSpace.game.ships.launch.LaunchSystem;
import ConquerSpace.game.resources.Element;
import ConquerSpace.game.resources.Good;
import ConquerSpace.game.resources.ProductionProcess;
import ConquerSpace.game.resources.ResourceDistribution;
import ConquerSpace.util.logging.CQSPLogger;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 *
 * @author EhWhoAmI
 */
public class GameLoader {

    private static final Logger LOGGER = CQSPLogger.getLogger(GameLoader.class.getName());

    /**
     * Load all resources.
     */
    public static void load() {
        long start = System.currentTimeMillis();
        satelliteTemplates = new ArrayList<>();
        shipTypes = new HashMap<>();
        shipTypeClasses = new HashMap<>();
        shipComponentTemplates = new ArrayList<>();

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

        GameController.personalityTraits = readHjsonFromDirInArray("dirs.traits",
                PersonalityTrait.class, AssetReader::processPersonalityTraits);

        GameController.engineTechnologys = readHjsonFromDirInArray("dirs.ship.engine.tech",
                EngineTechnology.class, AssetReader::processEngineTech);


        //Init all resource references
        GameController.goodIdentifiers = new HashMap<>();
        GameController.goodHashMap = new HashMap<>();
        
        //Read elements
        GameController.elements = readHjsonFromDirInArray("dirs.elements",
                Element.class, AssetReader::processElement);

        processGoods();
        
        //Everything is compiled into the resource references, no need for extra loading

        //Resource distributions
        ArrayList<ResourceDistribution> res = readHjsonFromDirInArray("dirs.distributions", ResourceDistribution.class, AssetReader::processDistributions);

        //Sort through the list
        for (ResourceDistribution dist : res) {
            Integer identifier = GameController.goodIdentifiers.get(dist.resourceName);
            GameController.ores.put(identifier, dist);
        }
        GameController.prodProcesses = new HashMap<>();
        readHjsonFromDirInArray("dirs.processes", ProductionProcess.class, AssetReader::processProcess);

        //Events
        readPopulationEvents();
        long end = System.currentTimeMillis();
        LOGGER.trace("Inited all resources: " + (end - start) + "ms");
    }
}
