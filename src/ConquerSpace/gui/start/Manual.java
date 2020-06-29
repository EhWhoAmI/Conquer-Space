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
package ConquerSpace.gui.start;

import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.util.ExceptionHandling;
import ConquerSpace.util.logging.CQSPLogger;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Scanner;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.logging.log4j.Logger;

/**
 * Shows the selection of manuals.
 *
 * @author EhWhoAmI
 */
public class Manual extends JFrame implements ListSelectionListener {

    private static final long serialVersionUID = -2589166556749207075L;
    private static Manual current;
    private static final Logger LOGGER = CQSPLogger.getLogger(Manual.class.getName());
    //Linked hashmap because it has to be in order.
    private LinkedHashMap<String, String> prop = new LinkedHashMap<>();
    private JList<String> list;

    private Manual() {
        setTitle(LOCALE_MESSAGES.getMessage("manual.title"));
        DefaultListModel<String> model = new DefaultListModel<>();
        // Open property file

        // Load properties. Need to use a hashmap, because the properties
        // does not load in the order that we like. It is kind of random. 
        try {
            Scanner scan = new Scanner(new File(System.getProperty("user.dir") + "/assets/manuals/manlist.properties"));
            while (scan.hasNextLine()) {
                String text = scan.nextLine();
                String values[] = text.split("=");
                prop.put(values[0], values[1]);
            }
        } catch (IOException ioe) {
            ExceptionHandling.ExceptionMessageBox("We could not open the manual. Not a problem.\nJust don\'t use it.\nReason: Cannot open manual list.", ioe);
            LOGGER.warn("Cannot open manual list. " + ioe.getMessage(), ioe);
        }

        for (Object val : prop.values()) {
            model.addElement((String) val);
        }

        LOGGER.info("Manual entries: " + model.getSize());
        list = new JList<>(model);
        list.addListSelectionListener(this);
        JScrollPane pane = new JScrollPane(list);
        add(pane);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent arg0) {
                current.dispose();
                current = null;
                LOGGER.info("Closed manual window.");
            }
        });
        pane.setPreferredSize(new Dimension(300, 200));
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    /**
     * Creates a singleton of a manual list.
     *
     * @return an instance of manual.
     */
    public static Manual getInstance() {
        if (current == null) {
            current = new Manual();
        }
        return current;
    }

    @Override
    public void valueChanged(ListSelectionEvent arg0) {
        ManualContent value = null;
        Iterator<String> keys = prop.keySet().iterator();
        while (keys.hasNext()) {
            String str = (String) keys.next();
            if (prop.get(str).equals(list.getSelectedValue())) {
                try {
                    value = ManualContent.getInstance(
                            new String(Files.readAllBytes(Paths.get(
                                    System.getProperty("user.dir") + "/assets/manuals/" + str)), StandardCharsets.UTF_8));
                    LOGGER.info("Loading manual " + str);
                    
                    value.setVisible(true);
                    break;
                } catch (IOException ex) {
                    ExceptionHandling.ExceptionMessageBox("We could not open the manual. Not a problem.\nJust don\'t use it.", ex);
                    LOGGER.warn("Unable to open manual", ex);
                }
            }
        }
    }
}
