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
package ConquerSpace.client.gui.game.planetdisplayer.construction;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author EhWhoAmI
 */
public class BuildResourceStorageMenu extends JPanel {

    JLabel resourceStorageSize;
    JSpinner resourceStorageSizeSpinner;

    @SuppressWarnings("unchecked")
    public BuildResourceStorageMenu() {
        setLayout(new BorderLayout());
        JPanel resourceAmountStorage = new JPanel();
        resourceStorageSize = new JLabel("Amount of Resources:");
        SpinnerNumberModel mod = new SpinnerNumberModel(1000, 0, Integer.MAX_VALUE, 100);
        resourceStorageSizeSpinner = new JSpinner(mod);

        resourceAmountStorage.add(resourceStorageSize);
        resourceAmountStorage.add(resourceStorageSizeSpinner);
        
        //Later, add what the requirements are...
        add(resourceAmountStorage, BorderLayout.NORTH);
    }
}
