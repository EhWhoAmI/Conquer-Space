package ConquerSpace.gui.game;

import ConquerSpace.game.actions.Alert;
import ConquerSpace.game.tech.Technologies;
import ConquerSpace.game.tech.Technology;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.spaceObjects.Universe;
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
 * @author Zyun
 */
public class CQSPConsole extends JInternalFrame {

    JList<String> list;
    DefaultListModel<String> model;
    JTextField text;

    @SuppressWarnings("unchecked")
    public CQSPConsole(Universe u, Civilization c) {
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
            //System.out.println("\"" + command[0] + "\"");
            if (command[0].toLowerCase().equals("help")) {
                try {
                    //Display console help text
                    Scanner scan = new Scanner(new File(System.getProperty("user.dir") + "/assets/data/console/helptext.txt"));
                    while (scan.hasNextLine()) {
                        model.addElement(scan.nextLine());
                    }
                } catch (FileNotFoundException ex) {
                }
            } else if (command[0].toLowerCase().replaceAll(" ","").equals("research") || command[0].toLowerCase().replaceAll(" ","").equals("resh")) {
                //Research tech
                //Get tech
                if (command.length != 2) {
                    model.addElement("You need the id of the tech");
                } else if (StringUtils.isNumeric(command[1])) {
                    
                    Technology t = Technologies.getTechByID(Integer.parseInt(command[1]));
                    c.researchTech(t);
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
