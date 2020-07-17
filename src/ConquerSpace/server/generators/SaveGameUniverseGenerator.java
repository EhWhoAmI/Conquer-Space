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
package ConquerSpace.server.generators;

import ConquerSpace.common.GameState;
import ConquerSpace.common.game.universe.bodies.Galaxy;
import ConquerSpace.common.save.SaveGame;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author EhWhoAmI
 */
public class SaveGameUniverseGenerator extends UniverseGenerator {

    private SaveGame save;

    public SaveGameUniverseGenerator(File file) {
        save = new SaveGame(file);
    }

    @Override
    public Galaxy generate(GameState gameState) throws Exception {
        save.read(gameState);
        return null;
    }
}
