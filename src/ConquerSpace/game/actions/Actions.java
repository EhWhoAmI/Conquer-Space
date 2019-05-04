package ConquerSpace.game.actions;

import ConquerSpace.game.tech.Technology;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.civilization.vision.VisionPoint;
import ConquerSpace.game.universe.ships.Ship;
import ConquerSpace.game.universe.ships.satellites.Satellite;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.game.universe.spaceObjects.pSectors.BuildingBuilding;
import ConquerSpace.game.universe.spaceObjects.pSectors.PlanetSector;

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
    public static int buildBuilding(Planet p, int sectorID, PlanetSector what, int owner, int turns) {
        if (p.getOwnerID() == owner) {
            //Get type of the planet sector
            //Pass
            p.planetSectors[sectorID] = new BuildingBuilding(turns, what, p, owner, sectorID);
            return BUILD_BUILDING_SUCCESS;
        } else {
            return BUILD_BUILDING_FAIL_NOT_OWNER;
        }
    }

    public static void researchTech(Civilization c, Technology t) {

    }

    public static void launchSatellite(Satellite what, Planet whichPlanet, int distance, Civilization c) {
        if (what instanceof VisionPoint) {
            c.visionPoints.add((VisionPoint) what);
        }
        what.setOrbiting(whichPlanet.getUniversePath());
        whichPlanet.addSatellite(what);
    }

    public static void launchShip(Ship what, Planet planet, Civilization civ) {
        what.setLocation(planet.getUniversePath());
        what.setIsOrbiting(true);
        planet.putShipInOrbit(what);
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
