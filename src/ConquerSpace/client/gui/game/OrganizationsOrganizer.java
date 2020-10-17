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

import ConquerSpace.client.gui.ObjectListModel;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.organizations.Administrable;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.organizations.Organization;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.util.ArrayList;
import javax.swing.JLabel;
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
    
    private JPanel orgInfoPanel;
    
    private JList<String> controlList;
    private ObjectListModel<ObjectReference> adminstratableListModel;
    
    public OrganizationsOrganizer(GameState state, Civilization c) {
        //Lazy for now...
        setLayout(new HorizontalFlowLayout());
        
        orgListModel = new ObjectListModel<>();
        for (ObjectReference o : c.getChildren()) {
            orgListModel.addElement(state.getObject(o, Organization.class));
        }
        
        orgListModel.setHandler(l -> {
            return l.getName();
        });
        
        orgList = new JList<>(orgListModel);
        
        orgList.addListSelectionListener(l -> {
            ArrayList<ObjectReference> data = orgListModel.getObject(orgList.getSelectedIndex()).region.bodies;
            for (int i = 0; i < data.size(); i++) {
                adminstratableListModel.addElement(data.get(i));
            }
            controlList.updateUI();
        });
        
        orgInfoPanel = new JPanel(new VerticalFlowLayout());
        
        adminstratableListModel = new ObjectListModel<>();
        adminstratableListModel.setHandler(l -> {
            return (state.getObject(l, Administrable.class).toString());
        });
        controlList = new JList<>(adminstratableListModel);
        orgInfoPanel.add(new JLabel("Area under jurisdiction"));
        orgInfoPanel.add(new JScrollPane(controlList));
        
        add(new JScrollPane(orgList));
        add(orgInfoPanel);
    }
}
