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
package ConquerSpace.game.ships.satellites;

import ConquerSpace.game.ships.Launchable;
import ConquerSpace.game.ships.Orbitable;
import ConquerSpace.game.universe.UniversePath;

/**
 *
 * @author EhWhoAmI
 */
public class Satellite implements Launchable, Orbitable{
    
    protected int mass;
    protected String name = "";
    protected int id;
    protected int owner = -1;
    protected UniversePath orbiting = null;
        
    public Satellite() {
    }

    public int getMass() {
        return mass;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return getName();
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    @Override
    public UniversePath getOrbiting() {
        return orbiting;
    }

    public void setOrbiting(UniversePath orbiting) {
        this.orbiting = orbiting;
    }
}