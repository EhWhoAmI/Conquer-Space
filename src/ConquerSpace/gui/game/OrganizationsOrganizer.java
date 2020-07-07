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
package ConquerSpace.gui.game;

import ConquerSpace.game.organizations.Organization;
import ConquerSpace.game.organizations.civilization.Civilization;
import ConquerSpace.gui.ObjectListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author EhWhoAmI
 */
public class OrganizationsOrganizer extends JPanel {

    private JList<String> orgList;
    private ObjectListModel<Organization> orgListModel;

    public OrganizationsOrganizer(Civilization c) {
        orgListModel = new ObjectListModel<>();
        for (Organization o : c.getChildren()) {
            orgListModel.addElement(o);
        }
        orgListModel.setHandler(l -> {
            return l.getName();
        });

        orgList = new JList<>(orgListModel);

        add(new JScrollPane(orgList));
    }
}
