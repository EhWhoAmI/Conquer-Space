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
package ConquerSpace.common.game.universe.bodies;

import ConquerSpace.common.GameState;
import ConquerSpace.common.save.Serialize;
import ConquerSpace.common.game.universe.Orbit;
import ConquerSpace.common.game.universe.PolarCoordinate;
import ConquerSpace.common.game.universe.UniversePath;
import ConquerSpace.common.save.SerializeClassName;
import java.util.ArrayList;

/**
 * A star system.
 *
 * @author EhWhoAmI
 */
@SerializeClassName("star-system")
public class StarSystem extends Body {
    private int index;
    
    public int planetCount = 0;
    
    @Serialize("bodies")
    private ArrayList<Integer> bodies;

    @Serialize("ships")
    public ArrayList<Integer> spaceShips;
    
    @Serialize("name")
    private String name = "";

    /**
     * Creates a new star system.
     *
     * @param gameState
     * @param location Galactic location
     */
    public StarSystem(GameState gameState, PolarCoordinate location) {
        super(gameState);
        
        orbit = new Orbit(location.getDegrees(), location.getDistance(), 0, 0);
        spaceShips = new ArrayList<>();
        bodies = new ArrayList<>();
    }

    public int getPlanetCount() {
        return planetCount;
    }
    
    public void addSpaceShip(Integer ship) {
        spaceShips.add(ship);
    }

    public ArrayList<Integer> getSpaceShips() {
        return spaceShips;
    }

    /**
     * Get the path of this star system
     *
     * @return The path of this star system
     */
    public UniversePath getUniversePath() {
        return (new UniversePath(index));
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public void addBody(StarSystemBody b){ 
        b.setIndex(bodies.size());
        b.setParent(this.index);
        
        if(b instanceof Planet) {
            planetCount++;
        }
        bodies.add(b.getId());
    }
    
    public Integer getBody(int id) {
        return bodies.get(id);
    }
    
    public Body getBodyObject(int id) {
        return gameState.getObject(getBody(id), Body.class);
    }
    
    public int getBodyCount() {
        return bodies.size();
    }

    void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
