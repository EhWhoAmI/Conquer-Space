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
package ConquerSpace.common;

/**
 *
 * @author EhWhoAmI
 */
public abstract class ConquerSpaceGameObject {
    protected final GameState gameState;
    
    private final int id;
    
    public ConquerSpaceGameObject(GameState gameState) {
        this.gameState = gameState;
        id = gameState.addGameObject(this);
    }

    @Override
    public final int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ConquerSpaceGameObject other = (ConquerSpaceGameObject) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public final GameState getGameState() {
        return gameState;
    }

    public final int getId() {
        return id;
    }
}
