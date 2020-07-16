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

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameState;
import ConquerSpace.common.save.Serialize;
import ConquerSpace.common.game.universe.UniversePath;
import ConquerSpace.common.save.SerializeClassName;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Universe Object.
 *
 * @author EhWhoAmI
 */
@SerializeClassName("galaxy")
public class Galaxy extends ConquerSpaceGameObject {

    @Serialize("systems")
    private ArrayList<Integer> starSystems;

    @Serialize("control")
    public HashMap<Integer, Integer> control;

    //Spaceships in orbit in the galaxy
    @Serialize("ships")
    public ArrayList<Integer> spaceShips;

    public Galaxy(GameState gameState) {
        super(gameState);
        control = new HashMap<>();
        starSystems = new ArrayList<>();
        spaceShips = new ArrayList<>();
    }

    public void addStarSystem(StarSystem s) {
        int index = getStarSystemCount();
        s.setIndex(index);
        starSystems.add(s.getId());

        //Add system to control
        control.put(s.getId(), -1);
    }

    public int getStarSystemCount() {
        return starSystems.size();
    }

    public Integer getStarSystem(int i) {
        return starSystems.get(i);
    }

    public StarSystem getStarSystemObject(int i) {
        if (starSystems == null || gameState == null) {
            System.out.println(starSystems + " \n" + gameState);
        }
        return (gameState.getObject(
                starSystems.get(i),
                StarSystem.class));
    }

    public Iterator<Integer> getStarSystemIterator() {
        return starSystems.iterator();
    }

    /**
     * Get the space object (Planet, sector, etc,) as referenced by UniversePath
     * <code>p</code>.
     *
     * @param p Path
     * @return Space object
     */
    public Integer getSpaceObject(UniversePath p) {
        Integer systemId = getStarSystem(p.getSystemID());
        StarSystem system = gameState.getObject(systemId, StarSystem.class);
        if (p.getBodyID() != -1) {
            if (system.getBodyCount() < p.getBodyID()) {
                return null;
            } else {
                return system.getBody(p.getBodyID());
            }
        } else {
            return systemId;
        }

    }
}
