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
package ConquerSpace.tools;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

/**
 *
 * @author EhWhoAmI
 */
public class ToolsSelectionMenu extends JFrame {

    private DefaultListModel<String> toolsListModel;
    private JList<String> toolsList;

    private final String GOODS_STRING = "Goods viewer";
    private final String TECH_STRING = "Tech formatter";

    public ToolsSelectionMenu() {
        toolsListModel = new DefaultListModel<>();
        toolsListModel.addElement(GOODS_STRING);
        toolsListModel.addElement(TECH_STRING);

        toolsList = new JList<>(toolsListModel);
        toolsList.addListSelectionListener(l -> {
            String text = toolsList.getSelectedValue();
            switch (text) {
                case GOODS_STRING:
                    new ResourceViewer();
                    this.dispose();
                    break;
                case TECH_STRING:
                    new TechFormatter();
                    this.dispose();
                    break;
            }
        });

        add(new JScrollPane(toolsList));
        pack();
        setVisible(true);
    }
}
