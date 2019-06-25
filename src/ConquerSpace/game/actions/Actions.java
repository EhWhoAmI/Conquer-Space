package ConquerSpace.game.actions;

import ConquerSpace.Globals;
import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.BuildingBuilding;
import ConquerSpace.game.tech.Technology;
import ConquerSpace.game.universe.Point;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.vision.VisionPoint;
import ConquerSpace.game.universe.ships.Ship;
import ConquerSpace.game.universe.ships.satellites.Satellite;
import ConquerSpace.game.universe.ships.satellites.SpaceTelescope;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.StarSystem;
import ConquerSpace.game.universe.spaceObjects.Universe;

/**
 * This is like a driver to do all the actions. All methods must be static.
 *
 * @author Zyun
 */
public class Actions {

    /*
     * Hide constructor
     */
    private Actions() {
    }

    //Codes for the building status
    public static final int BUILD_BUILDING_SUCCESS = 000;
    public static final int BUILD_BUILDING_FAIL_NO_MONEY = 001;
    public static final int BUILD_BUILDING_FAIL_NO_RESOURCES = 002;
    public static final int BUILD_BUILDING_FAIL_NOT_OWNER = 003;

    /**
     * Builds a building on the planet.
     *
     * @param p planet you want to build on.
     * @param sectorID ID of planet sector
     * @param what What do you want to build?
     * @param owner You the owner
     * @param turns number of months...
     * @return Success or not
     */
    public static int buildBuilding(Planet p, Point pt, Building what, int owner, int turns) {
        if (p.getOwnerID() == owner) {
            BuildingBuilding buildings = new BuildingBuilding(what, pt, 10);
            p.buildings.put(pt, buildings);
            return BUILD_BUILDING_SUCCESS;
        } else {
            return BUILD_BUILDING_FAIL_NOT_OWNER;
        }
    }

    public static void researchTech(Civilization c, Technology t) {

    }

    public static void launchSatellite(Satellite what, Planet whichPlanet, int distance, Civilization c) {
        if (what instanceof VisionPoint) {
            SpaceTelescope obs = ((SpaceTelescope) what);
            StarSystem sys = Globals.universe.getStarSystem(whichPlanet.getParentStarSystem());
            obs.setPosition(new Point(sys.getX(), sys.getY()));
            c.visionPoints.add((VisionPoint) what);
            
        }
        what.setOrbiting(whichPlanet.getUniversePath());
        
        whichPlanet.addSatellite(what);
    }

    public static void launchShip(Ship what, Planet planet, Civilization civ) {
        what.setLocation(planet.getUniversePath());
        what.setIsOrbiting(true);
        planet.putShipInOrbit(what);
        civ.spaceships.add(what);
    }
    
    public static void moveShip(Ship what, Civilization civ, long x, long y, Universe u) {
        if(what.isOrbiting()) {
            //Exit orbit
            if( u.getSpaceObject(what.getOrbiting()) instanceof Planet) {
                Planet p = (Planet)u.getSpaceObject(what.getOrbiting());
                //Remove from orbit
                p.getSatellites().remove(what);
                
                what.setX(p.getX());
                what.setY(p.getY());
                what.setIsOrbiting(false);

                //Add
                u.getStarSystem(p.getParentStarSystem()).addSpaceShip(what);
            }
        }
        what.setGoingToX(x);
        what.setGoingToY(y);
    }
}
