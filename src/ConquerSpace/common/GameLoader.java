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

import static ConquerSpace.common.AssetReader.*;
import ConquerSpace.common.game.characters.PersonalityTrait;
import ConquerSpace.common.game.resources.Element;
import ConquerSpace.common.game.resources.StoreableReference;
import ConquerSpace.common.game.resources.ProductionProcess;
import ConquerSpace.common.game.resources.ResourceDistribution;
import ConquerSpace.common.game.science.Fields;
import ConquerSpace.common.game.science.Technologies;
import ConquerSpace.common.game.ships.EngineTechnology;
import ConquerSpace.common.game.ships.ShipType;
import ConquerSpace.common.game.ships.launch.LaunchSystem;
import ConquerSpace.common.util.ResourceLoader;
import ConquerSpace.common.util.logging.CQSPLogger;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author EhWhoAmI
 */
public class GameLoader {

    private static final Logger LOGGER = CQSPLogger.getLogger(GameLoader.class.getName());

    /**
     * Load all resources.
     */
    public static void load(GameState gameState) {
        long start = System.currentTimeMillis();
        gameState.shipTypeClasses = new HashMap<>();

        //Init tech and fields
        gameState.fieldNodeRoot = Fields.readFields();
        gameState.techonologies = Technologies.readTech();

        //All things to load go here!!!
        gameState.launchSystems = readHjsonFromDirInArray("dirs.launch",
                LaunchSystem.class, gameState, AssetReader::processLaunchSystem);

        gameState.shipTypes = readHjsonFromDirInArray("dirs.ship.types.types",
                ShipType.class, AssetReader::processShipType);
        
        gameState.personalityTraits = readHjsonFromDirInArray("dirs.traits",
                PersonalityTrait.class, AssetReader::processPersonalityTraits);

        gameState.engineTechnologys = readHjsonFromDirInArray("dirs.ship.engine.tech",
                EngineTechnology.class, gameState, AssetReader::processEngineTech);

        //Read elements
        ArrayList<Element> elements = readHjsonFromDirInArray("dirs.elements",
                Element.class, AssetReader::processElement);

        //Have to insert to places...
        for (Element e : elements) {
            gameState.addGood(e);
        }

        processGoods(gameState);

        //Everything is compiled into the resource references, no need for extra loading
        //Resource distributions
        ArrayList<ResourceDistribution> res = readHjsonFromDirInArray("dirs.distributions", ResourceDistribution.class, AssetReader::processDistributions);

        gameState.oreDistributions = new HashMap<>();

        //Sort through the list
        for (ResourceDistribution dist : res) {
            StoreableReference identifier = gameState.getGoodId(dist.resourceName);
            gameState.oreDistributions.put(identifier, dist);
        }
        gameState.prodProcesses = new HashMap<>();

        ArrayList<ProductionProcess> processes = readHjsonFromDirInArray("dirs.processes", ProductionProcess.class, gameState, AssetReader::processProcess);
        for (ProductionProcess process : processes) {
            gameState.prodProcesses.put(process.identifier, process);
        }

        try {
            //Constants
            gameState.constants.load(new FileInputStream(ResourceLoader.getResourceByFile("game.constants")));
        } catch (IOException ex) {
            LOGGER.warn("No constants!", ex);
        }

        //Events
        //Skip for now...
        //readPopulationEvents();
        long end = System.currentTimeMillis();
        LOGGER.info("Inited all resources: " + (end - start) + "ms");
    }
}
