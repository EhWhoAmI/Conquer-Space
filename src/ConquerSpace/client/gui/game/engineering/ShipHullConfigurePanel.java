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
package ConquerSpace.client.gui.game.engineering;

import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.common.GameState;
import ConquerSpace.common.ObjectReference;
import ConquerSpace.common.game.organizations.Civilization;
import ConquerSpace.common.game.ships.Hull;
import ConquerSpace.common.game.ships.HullMaterial;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.GridLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author EhWhoAmI
 */
public class ShipHullConfigurePanel extends JPanel {

    private DefaultListModel<Hull> hullListModel;
    private JList<Hull> hullList;

    private JPanel hullInfoPanel;

    private Hull selectedHull = null;

    public ShipHullConfigurePanel(GameState gameState, Civilization civ) {
        setLayout(new GridLayout(1, 2));
        hullListModel = new DefaultListModel<>();
        for (ObjectReference obj : civ.hulls) {
            Hull hull = gameState.getObject(obj, Hull.class);
            hullListModel.addElement(hull);
        }
        
        hullList = new JList<>(hullListModel);
        hullList.addListSelectionListener(l -> {
            hullInfoPanel.removeAll();
            //Add components
            hullInfoPanel.setLayout(new VerticalFlowLayout());

            Hull h = hullList.getSelectedValue();
            hullInfoPanel.add(new JLabel(h.getName()));
            hullInfoPanel.add(new JLabel(
                    LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.hull.dialog.material", 
                            gameState.getObject(h.getMaterial(), HullMaterial.class).getName())));
            hullInfoPanel.add(
                    new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.hull.dialog.mass", h.getMass())));
            hullInfoPanel.add(
                    new JLabel(LOCALE_MESSAGES.getMessage("game.engineering.ship.designer.hull.dialog.rated", h.getThrust())));
            //hullInfoPanel.add(new JLabel(h()));
            hullInfoPanel.validate();
            hullInfoPanel.repaint();

            selectedHull = h;
        });
        JScrollPane scrollPane = new JScrollPane(hullList);
        hullInfoPanel = new JPanel();
        add(scrollPane);
        add(hullInfoPanel);
    }

    public Hull getSelectedHull() {
        return selectedHull;
    }
}
