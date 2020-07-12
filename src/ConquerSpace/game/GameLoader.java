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
import ConquerSpace.game.people.PersonalityTrait;
import ConquerSpace.game.science.Fields;
import ConquerSpace.game.science.tech.Technologies;
import ConquerSpace.game.ships.components.engine.EngineTechnology;
import ConquerSpace.game.ships.launch.LaunchSystem;
import ConquerSpace.game.resources.Element;
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
    public static void load(GameState state) {
        long start = System.currentTimeMillis();
        state.shipTypes = new HashMap<>();
        state.shipTypeClasses = new HashMap<>();

        //Init tech and fields
        state.fieldNodeRoot = Fields.readFields();
        state.techonologies = Technologies.readTech();

        //All things to load go here!!!
        state.launchSystems = readHjsonFromDirInArray("dirs.launch",
                LaunchSystem.class, state, AssetReader::processLaunchSystem);

        state.satelliteTemplates = readHjsonFromDirInArray("dirs.satellite.types",
                JSONObject.class, AssetReader::processSatellite);

        readShipTypes(state);

        state.personalityTraits = readHjsonFromDirInArray("dirs.traits",
                PersonalityTrait.class, AssetReader::processPersonalityTraits);

        state.engineTechnologys = readHjsonFromDirInArray("dirs.ship.engine.tech",
                EngineTechnology.class, AssetReader::processEngineTech);

        //Init all resource references
        state.goodIdentifiers = new HashMap<>();
        state.goodHashMap = new HashMap<>();

        //Read elements
        ArrayList<Element> elements = readHjsonFromDirInArray("dirs.elements",
                Element.class, AssetReader::processElement);

        //Have to insert to places...
        for (Element e : elements) {
            state.addGood(e);
        }

        processGoods(state);

        //Everything is compiled into the resource references, no need for extra loading
        //Resource distributions
        ArrayList<ResourceDistribution> res = readHjsonFromDirInArray("dirs.distributions", ResourceDistribution.class, AssetReader::processDistributions);

        state.ores = new HashMap<>();

        //Sort through the list
        for (ResourceDistribution dist : res) {
            Integer identifier = state.goodIdentifiers.get(dist.resourceName);
            state.ores.put(identifier, dist);
        }
        state.prodProcesses = new HashMap<>();
        
        ArrayList<ProductionProcess> processes = readHjsonFromDirInArray("dirs.processes", ProductionProcess.class, state, AssetReader::processProcess);
        for (ProductionProcess process : processes) {
            state.prodProcesses.put(process.identifier, process);
        }

        //Events
        //Skip for now...
        //readPopulationEvents();
        long end = System.currentTimeMillis();
        LOGGER.info("Inited all resources: " + (end - start) + "ms");
    }
}
