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
package ConquerSpace.common.actions;

import ConquerSpace.common.GameState;
import ConquerSpace.common.game.city.City;
import ConquerSpace.common.game.city.area.Area;
import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.common.game.organizations.civilization.vision.VisionPoint;
import ConquerSpace.common.game.population.jobs.Workable;
import ConquerSpace.common.game.resources.ResourceStockpile;
import ConquerSpace.common.game.science.Technology;
import ConquerSpace.common.game.ships.Launchable;
import ConquerSpace.common.game.ships.Ship;
import ConquerSpace.common.game.ships.satellites.Satellite;
import ConquerSpace.common.game.ships.satellites.SpaceTelescope;
import ConquerSpace.common.game.universe.bodies.Body;
import ConquerSpace.common.game.universe.bodies.Galaxy;
import ConquerSpace.common.game.universe.bodies.Planet;

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
        city.areas.add(a.getId());
        if (a instanceof Workable) {
            on.jobProviders.add(a);
        }
    }

    public static void researchTech(Civilization c, Technology t) {

    }

    public static void launchSatellite(Satellite what, Planet whichPlanet, Civilization c) {
        if (what instanceof VisionPoint) {
            SpaceTelescope obs = ((SpaceTelescope) what);
            obs.setPosition(whichPlanet.getUniversePath());
            c.visionPoints.add(what.getId());

        }
        what.setOrbiting(whichPlanet.getUniversePath());

        whichPlanet.addSatellite(what);
    }

    public static void launchShip(Ship what, Planet planet, Civilization civ) {
        what.setLocation(planet.getUniversePath());
        what.setIsOrbiting(true);
        planet.putShipInOrbit(what);
        civ.spaceships.add(what.getId());
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

    public static void moveShip(GameState gameState, Ship what, Civilization civ, long x, long y, Galaxy u) {
        if (what.isOrbiting()) {
            //Exit orbit
            Body body = gameState.getObject(u.getSpaceObject(what.getOrbiting()), Body.class);
            if (body instanceof Planet) {
                Planet planet = (Planet) body;
                //Remove from orbit
                planet.getSatellites().remove(what);

                what.setX(planet.getX());
                what.setY(planet.getY());
                what.setIsOrbiting(false);

                //Add
                //u.get(planet.getParentStarSystem()).addSpaceShip(what);
            }
        }
        what.setGoingToX(x);
        what.setGoingToY(y);
    }
    
    public static void storeResource(Integer resourceType, Double amount, ResourceStockpile from) {
        if (resourceType != null && amount > 0) {
            if (from.canStore(resourceType)) {
                //Store resource
                from.addResource(resourceType, amount);
            } else {
                //Do something
            }
        }
    }

    public static boolean removeResource(Integer resourceType, Double amount, ResourceStockpile from) {
        if (resourceType != null && amount != 0) {
            if (from.canStore(resourceType)) {
                //Store resource
                return from.removeResource(resourceType, amount);
            } else {
                //Store somewhere else
                //removeResource(resourceType, amount, owner, from.getUniversePath());
            }
        }
        return false;
    }
    
    public static boolean hasSufficientResources(Integer resourceType, Double amount, ResourceStockpile from) {
        return false;
    }

    public static boolean sendResources(Integer resourceType, Double amount, ResourceStockpile from, ResourceStockpile to) {
        if (removeResource(resourceType, amount, from)) {
            storeResource(resourceType, amount, to);
            return true;
        }
        return false;
    }
}
