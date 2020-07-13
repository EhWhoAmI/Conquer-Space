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

import ConquerSpace.game.save.Serialize;
import ConquerSpace.game.ships.SpaceShip;
import ConquerSpace.game.universe.Orbit;
import ConquerSpace.game.universe.PolarCoordinate;
import ConquerSpace.game.universe.UniversePath;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * A star system.
 *
 * @author EhWhoAmI
 */
public class StarSystem extends Body {
    private static final long serialVersionUID = 1L;
    
    public int planetCount = 0;
    
    @Serialize(key = "bodies")
    public ArrayList<Body> bodies;

    @Serialize(key = "ships")
    public ArrayList<SpaceShip> spaceShips;
    
    @Serialize(key = "name")
    private String name = "";

    /**
     * Creates a new star system.
     *
     * @param id ID of this star system
     * @param location Galactic location.
     */
    public StarSystem(PolarCoordinate location) {
        orbit = new Orbit(location.getDegrees(), location.getDistance(), 0, 0);
        spaceShips = new ArrayList<>();
        bodies = new ArrayList<>();
    }

    public int getPlanetCount() {
        return planetCount;
    }

    /**
     * Get readable string of the star system, stars and planets.
     *
     * @return a readable string
     */
    public String toReadableString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Star system " + this.id + " Rectangular pos"
                + getX() + ", " + getY() + ": [\n");
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

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public void addBody(Body b){ 
        b.setId(bodies.size());
        if(b instanceof Planet) {
            planetCount++;
        }
        bodies.add(b);
    }
}
