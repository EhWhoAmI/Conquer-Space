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
package ConquerSpace.client.gui.start;

import ConquerSpace.ConquerSpace;
import ConquerSpace.server.generators.SaveGameUniverseGenerator;
import com.alee.extended.layout.HorizontalFlowLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.json.JSONObject;

/**
 *
 * @author EhWhoAmI
 */
public class SaveGameChooser extends JFrame {

    private JPanel saveInfoPanel;
    private JButton selectGameButton;

    public SaveGameChooser(MainMenu menu) {
        setTitle("Choose Save File");
        setLayout(new HorizontalFlowLayout());
        File file = new File(ConquerSpace.USER_DIR + "/save");
        File[] list = file.listFiles();
        JList<File> fileJList = new JList<>(list);

        fileJList.addListSelectionListener(l -> {
            try {
                ZipFile zipFile = new ZipFile(fileJList.getSelectedValue());

                Enumeration<? extends ZipEntry> entries = zipFile.entries();

                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    if (entry.getName().equals("meta")) {
                        InputStream stream = zipFile.getInputStream(entry);
                        byte[] data = new byte[(int) fileJList.getSelectedValue().length()];
                        stream.read(data);
                        stream.close();
                        JSONObject json = new JSONObject(new String(data));

                        //Set UI
                        saveInfoPanel.removeAll();
                        saveInfoPanel.add(new JLabel("Version: " + json.getString("version")));
                        saveInfoPanel.add(selectGameButton);
                        saveInfoPanel.repaint();
                        saveInfoPanel.updateUI();
                    }
                }
                zipFile.close();
            } catch (IOException ex) {
                //Print out error
                //FIXME
            }
        });

        saveInfoPanel = new JPanel();

        selectGameButton = new JButton("Go with this save");
        selectGameButton.addActionListener((a) -> {
            if (!fileJList.isSelectionEmpty()) {
                ConquerSpace.generator = new SaveGameUniverseGenerator(fileJList.getSelectedValue());
                dispose();

                //Tell main thread that game works
                menu.setLoadedUniverse(true);
                menu.dispose();
            }
        });

        add(new JScrollPane(fileJList));
        add(saveInfoPanel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //Reenable main window...
                menu.setEnabled(true);
            }

        });

        setSize(new Dimension(500, 200));
    }
}
