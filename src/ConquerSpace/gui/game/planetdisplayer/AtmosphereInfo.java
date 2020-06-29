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
package ConquerSpace.gui.game.planetdisplayer;

import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.game.organizations.civilization.Civilization;
import ConquerSpace.game.ships.Orbitable;
import ConquerSpace.game.ships.Ship;
import ConquerSpace.game.universe.bodies.Planet;
import ConquerSpace.gui.game.ShipInformationMenu;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

/**
 *
 * @author EhWhoAmI
 */
public class AtmosphereInfo extends JPanel {

    private JPanel atmosphereInfoPanel;
    private JPanel stuffInOrbitPanel;
    private ShipInformationMenu shipInformationMenu;
    private JList<Orbitable> shipsInOrbitList;
    private DefaultListModel<Orbitable> shipsInOrbitListModel;
    private JPanel shipInfoContainer;

    private Planet planet;

    public AtmosphereInfo(Planet p, Civilization c) {
        setLayout(new BorderLayout());
        planet = p;
        //Draw panels and stuff
        //The objects in orbit
        stuffInOrbitPanel = new JPanel(new GridLayout(1, 2));
        stuffInOrbitPanel.setBorder(new TitledBorder(LOCALE_MESSAGES.getMessage("game.planet.atmosphere.orbit")));

        //Show all the things in orbit
        shipsInOrbitListModel = new DefaultListModel<>();
        updateList();

        shipsInOrbitList = new JList<>(shipsInOrbitListModel);
        JScrollPane shipScrollPane = new JScrollPane(shipsInOrbitList);
        stuffInOrbitPanel.add(shipScrollPane);

        shipInfoContainer = new JPanel();
        stuffInOrbitPanel.add(shipInfoContainer);
        //Display ship info....
        shipsInOrbitList.addListSelectionListener(a -> {
            if (shipsInOrbitList.getSelectedValue() instanceof Ship) {
                shipInformationMenu = new ShipInformationMenu((Ship) shipsInOrbitList.getSelectedValue(), c);
                shipInfoContainer.removeAll();
                shipInfoContainer.add(shipInformationMenu);
            }
        });
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent arg0) {
            }

            @Override
            public void componentMoved(ComponentEvent arg0) {
            }

            @Override
            public void componentShown(ComponentEvent arg0) {
            }

            @Override
            public void componentHidden(ComponentEvent arg0) {
                updateList();
            }
        });
        add(stuffInOrbitPanel, BorderLayout.NORTH);
    }

    public void updateList() {
        for (int i = 0; i < planet.getSatelliteCount(); i++) {
            Orbitable orb = planet.getSatellite(i);
            if (!shipsInOrbitListModel.contains(orb)) {
                shipsInOrbitListModel.addElement(orb);
            }
        }
    }
}
