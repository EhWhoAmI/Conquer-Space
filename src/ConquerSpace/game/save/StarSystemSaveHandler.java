package ConquerSpace.game.save;

import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.resources.ResourceVein;
import ConquerSpace.game.universe.ships.Orbitable;
import ConquerSpace.game.universe.ships.Ship;
import ConquerSpace.game.universe.ships.satellites.Satellite;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import java.util.ArrayList;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author zyunl
 */
public class StarSystemSaveHandler {

    private StarSystem system;

    public StarSystemSaveHandler(StarSystem system) {
        this.system = system;
    }

    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();

        obj.put("id", system.getId());
        obj.put("deg", system.getGalaticLocation().getDegrees());
        obj.put("dist", system.getGalaticLocation().getDistance());
        obj.put("planet-c", system.getPlanetCount());
        obj.put("star-c", system.getStarCount());

        //Now stuff everything into that
        ArrayList<JSONObject> planets = new ArrayList<>();
        for (int p = 0; p < system.getPlanetCount(); p++) {
            Planet planet = system.getPlanet(p);

            JSONObject object = new JSONObject();
            object.put("type", planet.getPlanetType());
            object.put("size", planet.getPlanetSize());
            object.put("seed", planet.getTerrainSeed());
            object.put("name", planet.getName());
            object.put("dist", planet.getOrbitalDistance());
            object.put("deg", planet.getPlanetDegrees());

            //Buildings
            ArrayList<JSONObject> buildings = new ArrayList<>();
            for (Map.Entry<GeographicPoint, Building> entry : planet.buildings.entrySet()) {
                GeographicPoint key = entry.getKey();
                Building value = entry.getValue();
                BuildingSaveHandler handler = new BuildingSaveHandler(key, value);
                JSONObject building = handler.getJSONObject();
                buildings.add(building);
            }
            object.put("ings", buildings);

            //Resource veins
            ArrayList<JSONObject> veins = new ArrayList<>();
            for (ResourceVein v : planet.resourceVeins) {
                JSONObject veinObject = new JSONObject();
                veinObject.put("id", v.getId());
                veinObject.put("diff", v.getDifficulty());
                veinObject.put("radius", v.getRadius());
                veinObject.put("x", v.getX());
                veinObject.put("y", v.getY());
                veinObject.put("res", v.getResourceType().getId());
                veinObject.put("amt", v.getResourceAmount());
                veins.add(veinObject);
            }
            object.put("veins", veins);

            //Satellites
            ArrayList<JSONObject> satellites = new ArrayList<>();
            for (Orbitable b : planet.getSatellites()) {
                JSONObject orbitable = new JSONObject();

                if (b instanceof Satellite) {
                    orbitable.put("type", "satellite");
                    Satellite ship = (Satellite) b;
                    SatelliteSaveHandler handler = new SatelliteSaveHandler(ship);
                    orbitable.put("object", handler.getJSONObject());
                } else if (b instanceof Ship) {
                    orbitable.put("type", "ship");
                    Ship ship = (Ship) b;
                    ShipSaveHandler handler = new ShipSaveHandler(ship);
                    orbitable.put("object", handler.getJSONObject());
                }

                veins.add(orbitable);
            }
            object.put("satellites", satellites);
            planets.add(object);
        }
        return obj;
    }
}