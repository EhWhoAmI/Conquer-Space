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
package ConquerSpace.common.game.ships.satellites;

import ConquerSpace.common.ConquerSpaceGameObject;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.ships.Launchable;
import ConquerSpace.common.game.ships.Orbitable;
import ConquerSpace.common.game.universe.UniversePath;
import ConquerSpace.common.save.SerializeClassName;

/**
 *
 * @author EhWhoAmI
 */
@SerializeClassName("satellite")
public class Satellite extends ConquerSpaceGameObject implements Launchable, Orbitable{
    
    protected int mass;
    protected String name = "";
    protected int id;
    protected ObjectReference owner = ObjectReference.INVALID_REFERENCE;
    protected UniversePath orbiting = null;
        
    public Satellite(GameState gameState) {
        super(gameState);
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

    @Override
    public String toString() {
        return getName();
    }

    public ObjectReference getOwner() {
        return owner;
    }

    public void setOwner(ObjectReference owner) {
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