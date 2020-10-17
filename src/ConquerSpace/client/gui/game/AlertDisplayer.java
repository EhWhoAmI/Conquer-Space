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

import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.common.actions.Alert;
import java.awt.Dimension;
import javax.swing.DefaultListModel;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

/**
 *
 * @author EhWhoAmI
 */
public class AlertDisplayer extends JInternalFrame {

    private JList<Alert> alertList;
    private DefaultListModel<Alert> model;

    private static AlertDisplayer instance;

    private AlertDisplayer() {
        setTitle(LOCALE_MESSAGES.getMessage("game.alerts"));

        init();

    }

    public void addAlert(Alert a) {
        //Insert in front
        model.add(0, a);
        //Later, sort, etc, etc...
        //Do that
    }

    public static AlertDisplayer getInstance() {
        if (instance == null) {
            instance = new AlertDisplayer();
        } else {
            instance.setVisible(true);
        }
        return instance;
    }

    private void initWindow() {
        setResizable(true);

        setSize(new Dimension(100, 200));
        setVisible(true);
        setClosable(true);
    }

    public void init() {
        //Form array
        model = new DefaultListModel<>();
        alertList = new JList<>(model);
        JScrollPane pane = new JScrollPane(alertList);

        add(pane);
        initWindow();
    }

    public void reload() {
        init();
    }
}
