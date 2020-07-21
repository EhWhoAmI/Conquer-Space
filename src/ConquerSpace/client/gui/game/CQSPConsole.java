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

import ConquerSpace.common.GameState;
import ConquerSpace.common.actions.Alert;
import ConquerSpace.common.game.organizations.civilization.Civilization;
import ConquerSpace.common.game.science.Technologies;
import ConquerSpace.common.game.science.Technology;
import com.alee.extended.layout.VerticalFlowLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.DefaultListModel;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author EhWhoAmI
 */
public class CQSPConsole extends JInternalFrame {

    JList<String> list;
    DefaultListModel<String> model;
    JTextField text;

    @SuppressWarnings("unchecked")
    public CQSPConsole(GameState state, Civilization c) {
        setLayout(new VerticalFlowLayout());
        model = new DefaultListModel<>();
        list = new JList<>(model);
        JScrollPane pane = new JScrollPane(list);

        text = new JTextField();
        text.addActionListener(l -> {
            model.addElement("> " + text.getText());
            //Process the text
            //Split it and split it into argc and argv
            String[] command = text.getText().split(" ");
            //Search it
            if (command[0].toLowerCase().equals("help")) {
                try {
                    //Display console help text
                    Scanner scan = new Scanner(new File(System.getProperty("user.dir") + "/assets/data/console/helptext.txt"));
                    while (scan.hasNextLine()) {
                        model.addElement(scan.nextLine());
                    }
                } catch (FileNotFoundException ex) {
                }
            } else if (command[0].toLowerCase().replaceAll(" ", "").equals("research") || command[0].toLowerCase().replaceAll(" ", "").equals("resh")) {
                //Research tech
                //Get tech
                if (command.length != 2) {
                    model.addElement("You need the id of the tech");
                } else if (StringUtils.isNumeric(command[1])) {

                    Technology t = Technologies.getTechByID(state, Integer.parseInt(command[1]));
                    c.researchTech(state, t);
                    model.addElement("Researched " + t.getName() + " for " + c.getName());
                    //Alert civ
                    c.controller.alert(new Alert(0, 0, "Tech " + t.getName() + " is finished"));
                } else {
                    model.addElement("You need a number!");
                }
            }

            text.setText("");
            pane.getVerticalScrollBar().setValue(pane.getVerticalScrollBar().getMaximum());
        });
        add(pane);
        add(text);
        setVisible(true);
        setResizable(true);
        setClosable(true);
        pack();
    }
}
