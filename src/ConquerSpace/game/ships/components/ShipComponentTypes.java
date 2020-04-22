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
package ConquerSpace.game.ships.components;

/**
 *
 * @author EhWhoAmI
 */
public class ShipComponentTypes {
    public static final int TEST_COMPONENT = 0;
    public static final int SCIENCE_COMPONENT = 1;
    public static final int BRIDGE_COMPONENT = 2;
    public static final int PROBE_COMPONENT = 3;
    public static final int ENGINE_COMPONENT = 4;
    
    public static final String[] SHIP_COMPONENT_TYPE_NAMES  = {
        "Test",
        "Science",
        "Bridge",
        "Probe",
        "Engine"
    };
    private ShipComponentTypes() {
    }
}
