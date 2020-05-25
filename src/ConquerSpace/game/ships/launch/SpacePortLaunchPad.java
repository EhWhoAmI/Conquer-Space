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
package ConquerSpace.game.ships.launch;

import ConquerSpace.game.ships.Launchable;

/**
 * Used for building a rocket and launching it. It will remain full throughout
 * the duration.
 * @author EhWhoAmI
 */
public class SpacePortLaunchPad {
    public int ticks;
    private Launchable launching = null;
    private LaunchSystem type;

    public SpacePortLaunchPad(LaunchSystem type) {
        this.type = type;
    }
    
    public void beginLaunch(Launchable launch, int ticks) {
        launching = launch;
        this.ticks = ticks;
    }
    
    public boolean isLaunching() {
        if(launching != null) {
            return true;
        }
        return false;
    }

    public LaunchSystem getType() {
        return type;
    }

    @Override
    public String toString() {
        return type.getName();
    }

    public Launchable getLaunching() {
        return launching;
    }
}
