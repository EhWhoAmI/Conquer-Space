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

import ConquerSpace.game.organizations.Organization;
import ConquerSpace.game.organizations.civilization.Civilization;
import ConquerSpace.game.people.Person;
import ConquerSpace.game.population.Race;
import ConquerSpace.game.save.Serialize;
import ConquerSpace.game.ships.SpaceShip;
import ConquerSpace.game.universe.UniversePath;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

/**
 * Universe Object.
 *
 * @author EhWhoAmI
 */
public class Universe {

    @Serialize(key = "seed")
    private final long seed;

    public ArrayList<Civilization> civs;

    public ArrayList<StarSystem> starSystems;

    public HashMap<UniversePath, Integer> control;

    public ArrayList<SpaceShip> spaceShips;
    
    public DualHashBidiMap<Integer, Person> people;
    
    public HashMap<Integer, Race> races;
    
    public HashMap<Integer, Organization> organizations;

    public Universe(long seed) {
        this.seed = seed;
        civs = new ArrayList<>();
        control = new HashMap<>();
        starSystems = new ArrayList<>();
        spaceShips = new ArrayList<>();
        races = new HashMap<>();
        people = new DualHashBidiMap<>();
        organizations = new HashMap<>();
    }

    /**
     * Returns a human-readable string
     *
     * @return a human-readable string.
     */
    public String toReadableString() {
        //To a string that is human readable
        StringBuilder builder = new StringBuilder();
        //Just call the same method in the various containing objects
        for (StarSystem s : starSystems) {
            builder.append(s.toReadableString());
        }
        for (Civilization c : civs) {
            builder.append(c.toReadableString());
        }
        return (builder.toString());
    }

    public void addStarSystem(StarSystem s) {
        s.id = starSystems.size();
        starSystems.add(s);
        //Add system to control
        control.put(s.getUniversePath(), -1);
    }

    public int getStarSystemCount() {
        return starSystems.size();
    }

    public StarSystem getStarSystem(int i) {
        return starSystems.get(i);
    }

    public Iterator<StarSystem> getStarSystemIterator() {
        return starSystems.iterator();
    }

    public void addCivilization(Civilization c) {
        civs.add(c);
    }

    public int getCivilizationCount() {
        return (civs.size());
    }

    public Civilization getCivilization(int i) {
        return (civs.get(i));
    }

    /**
     * Get the space object (Planet, sector, etc,) as referenced by UniversePath
     * <code>p</code>.
     *
     * @param p Path
     * @return Space object
     */
    public Body getSpaceObject(UniversePath p) {
        StarSystem system = getStarSystem(p.getSystemID());
        if (p.getBodyID() != -1) {
            if (system.bodies.size() < p.getBodyID()) {
                return null;
            } else {
                return system.bodies.get(p.getBodyID());
            }
        } else {
            return system;
        }

    }

    public long getSeed() {
        return seed;
    }
    
    public void addOrganization(Organization org) {
        organizations.put(org.getId(), org);
    }
}
