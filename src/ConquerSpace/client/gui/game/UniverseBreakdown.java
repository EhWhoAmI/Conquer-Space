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
package ConquerSpace.client.gui.game;

import ConquerSpace.common.game.universe.bodies.Galaxy;
import javax.swing.JInternalFrame;

/**
 * Breakdown of the universe.
 * @author EhWhoAmI
 */
public class UniverseBreakdown extends JInternalFrame {

    private static UniverseBreakdown instance;

    private Galaxy universe;
    //Hide constructor
    private UniverseBreakdown(Galaxy u) {
        
    }

    /**
     * Get one instance of the UniverseBreakdown class.
     *
     * @param u Universe
     * @return Instance of universe breakdown class.
     */
    private static UniverseBreakdown getInstance(Galaxy u) {
        if (instance == null) {
            instance = new UniverseBreakdown(u);
        }
        instance.setVisible(true);
        return instance;
    }
}
