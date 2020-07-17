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
package ConquerSpace.common.game.city.area;

import ConquerSpace.common.GameState;
import ConquerSpace.common.game.ships.launch.LaunchSystem;
import ConquerSpace.common.game.ships.launch.SpacePortLaunchPad;
import ConquerSpace.common.save.SerializeClassName;
import java.util.ArrayList;

/**
 * A place where you can start to Conquer Space!
 * @author EhWhoAmI
 */
@SerializeClassName("space-port-area")
public class SpacePortArea extends Area{
    public ArrayList<SpacePortLaunchPad> launchPads = new ArrayList<>();
    private Integer system;

    public SpacePortArea(GameState gameState, Integer system, int amount) {
        super(gameState);
        this.system = system;
        launchPads = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            launchPads.add(new SpacePortLaunchPad(system));
        }
    }
    
    public Integer getLaunchSystem() {
        return system;
    }

    @Override
    public String toString() {
        return "Space Port";
    }
}
