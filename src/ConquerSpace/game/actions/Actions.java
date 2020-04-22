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
package ConquerSpace.game.actions;

import ConquerSpace.Globals;
import ConquerSpace.game.buildings.Building;
import ConquerSpace.game.buildings.City;
import ConquerSpace.game.buildings.ConstructingBuilding;
import ConquerSpace.game.buildings.area.Area;
import ConquerSpace.game.population.jobs.Workable;
import ConquerSpace.game.tech.Technology;
import ConquerSpace.game.universe.GeographicPoint;
import ConquerSpace.game.universe.Point;
import ConquerSpace.game.civilization.Civilization;
import ConquerSpace.game.civilization.vision.VisionPoint;
import ConquerSpace.game.ships.Ship;
import ConquerSpace.game.ships.satellites.Satellite;
import ConquerSpace.game.ships.satellites.SpaceTelescope;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.StarSystem;
import ConquerSpace.game.universe.bodies.Universe;

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
     * @param pt of planet sector
     * @param what What do you want to build?
     * @param owner You the owner
     * @param turns number of months...
     * @return Success or not
     */
    public static int buildBuilding(Planet p, GeographicPoint pt, Building what, Civilization owner, int turns) {
        if (p.getOwnerID() == owner.getID()) {
            ConstructingBuilding buildings = new ConstructingBuilding(what, pt, turns, owner);
            buildings.setScale(1);
            //Determine cost...

            p.buildings.put(pt, buildings);
            return BUILD_BUILDING_SUCCESS;
        } else {
            return BUILD_BUILDING_FAIL_NOT_OWNER;
        }
    }

    /**
     * Places building for free
     *
     * @param p
     * @param what
     */
    public static void forcePlaceBuilding(Planet p, GeographicPoint pt, Building what) {
        p.buildings.put(pt, what);
        //Check for working for
        if (what instanceof Workable) {
            p.jobProviders.add(what);
        }
    }

    /**
     * Demolishes the building at point
     *
     * @param p
     * @param pt
     * @param turns
     */
    public static int demolishBuilding(Planet p, GeographicPoint pt, Civilization owner, int turns) {
        if (p.getOwnerID() == owner.getID()) {
            ConstructingBuilding buildings = new ConstructingBuilding(null, pt, turns, owner);
            buildings.setScale(1);
            //Determine cost...

            p.buildings.put(pt, buildings);
            return BUILD_BUILDING_SUCCESS;
        } else {
            return BUILD_BUILDING_FAIL_NOT_OWNER;
        }
    }

    public static void addArea(Planet on, Building building, Area a) {
        building.areas.add(a);
        if (a instanceof Workable) {
            on.jobProviders.add(a);
        }
    }

    public static City addBuildingToCity(Planet p, GeographicPoint pt, Building what) {
        //Check surroundings, and add to city if it's around it
        City c = null;
        if (p.buildings.containsKey(pt.getNorth())) {
            c = p.buildings.get(pt.getNorth()).getCity();
            c.addDistrict(what);
        } else if (p.buildings.containsKey(pt.getSouth())) {
            c = p.buildings.get(pt.getSouth()).getCity();
            c.addDistrict(what);
        } else if (p.buildings.containsKey(pt.getEast())) {
            c = p.buildings.get(pt.getEast()).getCity();
            c.addDistrict(what);
        } else if (p.buildings.containsKey(pt.getWest())) {
            c = p.buildings.get(pt.getWest()).getCity();
            c.addDistrict(what);
        }
        return c;
    }

    public static void researchTech(Civilization c, Technology t) {

    }

    public static void launchSatellite(Satellite what, Planet whichPlanet, int distance, Civilization c) {
        if (what instanceof VisionPoint) {
            SpaceTelescope obs = ((SpaceTelescope) what);
            StarSystem sys = Globals.universe.getStarSystem(whichPlanet.getParentStarSystem());
            obs.setPosition(new Point((long) sys.getX(), (long) sys.getY()));
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
        if (what.isOrbiting()) {
            //Exit orbit
            if (u.getSpaceObject(what.getOrbiting()) instanceof Planet) {
                Planet p = (Planet) u.getSpaceObject(what.getOrbiting());
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
