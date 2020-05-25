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
import ConquerSpace.game.civilization.Civilization;
import ConquerSpace.game.civilization.vision.VisionPoint;
import ConquerSpace.game.districts.City;
import ConquerSpace.game.districts.area.Area;
import ConquerSpace.game.population.jobs.Workable;
import ConquerSpace.game.science.tech.Technology;
import ConquerSpace.game.ships.Launchable;
import ConquerSpace.game.ships.Ship;
import ConquerSpace.game.ships.satellites.Satellite;
import ConquerSpace.game.ships.satellites.SpaceTelescope;
import ConquerSpace.game.universe.Point;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.game.universe.bodies.StarSystem;
import ConquerSpace.game.universe.bodies.Universe;

/**
 * This is like a driver to do all the actions. All methods must be static.
 *
 * @author EhWhoAmI
 */
public class Actions {

    /*
     * Hide constructor
     */
    private Actions() {
    }

    public static void addArea(Planet on, City city, Area a) {
        city.areas.add(a);
        if (a instanceof Workable) {
            on.jobProviders.add(a);
        }
    }

    public static void researchTech(Civilization c, Technology t) {

    }

    public static void launchSatellite(Satellite what, Planet whichPlanet, Civilization c) {
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

    public static void launchLaunchable(Launchable launch, Planet planet) {
        if (launch instanceof Ship) {
            Ship ship = (Ship) launch;
            ship.setLocation(planet.getUniversePath());
            ship.setIsOrbiting(true);
            planet.putShipInOrbit(ship);
        } else if (launch instanceof Satellite) {
            Satellite satellite = (Satellite) launch;
            planet.addSatellite(satellite);
        }
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
