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

import ConquerSpace.game.universe.PolarCoordinate;
import ConquerSpace.game.universe.UniversePath;
import ConquerSpace.game.universe.ships.SpaceShip;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * A star system.
 *
 * @author Zyun
 */
public class StarSystem extends Body {
    private static final long serialVersionUID = 1L;
    
    public int planetCount = 0;
    
    public ArrayList<Body> bodies;

    /**
     * ID of this star system.
     */
    int id;

    /**
     * Galactic location.
     */
    private PolarCoordinate location;

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
    public StarSystem(int id, PolarCoordinate location) {
        this.id = id;
        this.location = location;
        spaceShips = new ArrayList<>();
        bodies = new ArrayList<>();
    }

    public int getPlanetCount() {
        return planetCount;
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
    public PolarCoordinate getGalaticLocation() {
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
    
    public void addBody(Body b){ 
        b.setID(bodies.size());
        if(b instanceof Planet) {
            planetCount++;
        }
        bodies.add(b);
    }
}
