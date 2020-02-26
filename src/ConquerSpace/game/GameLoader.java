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
import ConquerSpace.game.universe.resources.Resource;
import ConquerSpace.game.universe.ships.components.engine.EngineTechnology;
import ConquerSpace.game.universe.ships.launch.LaunchSystem;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONObject;

/**
 *
 * @author zyunl
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

        GameController.resources = readHjsonFromDirInArray("dirs.resources",
                Resource.class, AssetReader::processResource);
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

        readBuildingCosts();

        //Events
        readPopulationEvents();
    }
}
