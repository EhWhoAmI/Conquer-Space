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
package ConquerSpace.game.universe.bodies;

import ConquerSpace.game.universe.GalacticLocation;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.ships.SpaceShip;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * A star system.
 *
 * @author Zyun
 */
public class StarSystem extends SpaceObject {
    private static final long serialVersionUID = 1L;

    /**
     * All planets in this star system.
     */
    private ArrayList<Planet> planets;

    /**
     * All stars in this star system.
     */
    private ArrayList<Star> stars;

    /**
     * ID of this star system.
     */
    int id;

    /**
     * Galactic location.
     */
    private GalacticLocation location;

    public ArrayList<SpaceShip> spaceShips;

    private double xpos;
    private double ypos;
    
    private String name = "";

    /**
     * Creates a new star system.
     *
     * @param id ID of this star system
     * @param location Galatic location.
     */
    public StarSystem(int id, GalacticLocation location) {
        this.id = id;
        this.location = location;
        planets = new ArrayList<>();
        stars = new ArrayList<>();
        spaceShips = new ArrayList<>();
    }

    /**
     * Add star.
     *
     * @param star Star
     */
    public void addStar(Star star) {
        star.setParentStarSystem(id);
        star.id = stars.size();

        stars.add(star);
        stars.trimToSize();
    }

    /**
     * Add planet to this star system
     *
     * @param planet Planet
     */
    public void addPlanet(Planet planet) {
        planet.setParentStarSystem(id);
        planet.id = planets.size();

        planets.add(planet);
        planets.trimToSize();
    }

    /**
     * Get planet
     *
     * @param i Get the planet <code>i</code>
     * @return The planet on i
     */
    public Planet getPlanet(int i) {
        return (planets.get(i));
    }

    /**
     * Get number of planets.
     *
     * @return number of planets
     */
    public int getPlanetCount() {
        return (planets.size());
    }

    /**
     * Get the star.
     *
     * @param i star id
     * @return star id as <code>i</code> puts it.
     */
    public Star getStar(int i) {
        return (stars.get(i));
    }

    /**
     * Get number of stars
     *
     * @return Number of stars
     */
    public int getStarCount() {
        return (stars.size());
    }

    /**
     * Get this star system's id
     *
     * @return ID
     */
    public int getId() {
        return id;
    }

    /**
     * Get galatic location of this star system
     *
     * @return Galatic location of this star system
     */
    public GalacticLocation getGalaticLocation() {
        return location;
    }

    /**
     * Get readable string of the star system, stars and planets.
     *
     * @return a readable string
     */
    public String toReadableString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Star system " + this.id + " Location-" + location.toString() + " Rectangular pos"
                + xpos + ", " + ypos + ": [\n");
        //Display stars
        builder.append("Stars: <");
        for (Star s : stars) {
            builder.append(s.toReadableString());
        }
        builder.append(">\n");

        //Display planets
        builder.append("Planets: <");
        for (Planet p : planets) {
            builder.append(p.toReadableString());
        }
        builder.append(">\n");
        return (builder.toString());
    }

    public void addSpaceShip(SpaceShip ship) {
        spaceShips.add(ship);
    }

    public SpaceShip getSpaceShip(int id) {
        return spaceShips.get(id);
    }

    public Stream<SpaceShip> getSpaceShipStream() {
        return spaceShips.stream();
    }

    /**
     * Get the path of this star system
     *
     * @return The path of this star system
     */
    public UniversePath getUniversePath() {
        return (new UniversePath(id));
    }

    public double getX() {
        return xpos;
    }
    public double getY() {
        return ypos;
    }

    public void setX(double x) {
        xpos = x;
    }

    public void setY(double y) {
        ypos = y;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}