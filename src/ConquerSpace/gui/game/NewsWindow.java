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

import ConquerSpace.game.events.Event;
import ConquerSpace.game.universe.civilization.Civilization;
import javax.swing.DefaultListModel;
import javax.swing.JInternalFrame;
import javax.swing.JList;

/**
 *
 * @author EhWhoAmI
 */
public class NewsWindow extends JInternalFrame {

    private Civilization civ;
    private DefaultListModel<Event> eventListModel;
    private JList<Event> eventList;

    public NewsWindow(Civilization c) {
        this.civ = c;
        eventListModel = new DefaultListModel<>();
        eventList = new JList<>(eventListModel);
        add(eventList);
        pack();
        setClosable(true);
        setResizable(true);
        setVisible(true);
    }

    public void update() {
        eventListModel.clear();  
        for (Event e : civ.events) {
            eventListModel.addElement(e);
        }
    }
    //private class EventListModel extends 
}
