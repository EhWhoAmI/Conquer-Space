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

import static ConquerSpace.ConquerSpace.VERSION;
import ConquerSpace.Globals;
import ConquerSpace.game.GameController;
import ConquerSpace.util.ResourceLoader;
import ConquerSpace.util.logging.CQSPLogger;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.Color;
import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author EhWhoAmI
 */
public class InternalOptionsWindow extends JInternalFrame implements InternalFrameListener {

    //Logger
    private static final Logger LOGGER = CQSPLogger.getLogger(JInternalFrame.class.getName());

    private static InternalOptionsWindow instance;

    //Components
    private JPanel logsPanel;
    private JLabel deleteLogsLabel;
    private JButton deleteLogsButton;
    private JPanel musicPanel;
    private JCheckBox musicOnButton;
    private JSlider musicVolumeSlider;

    private JPanel lafPanel;
    private JLabel lookAndFeelLabel;
    private JComboBox<String> lookAndFeelComboBox;

    Properties lafProperties = new Properties();

    private InternalOptionsWindow() {
        setTitle("Options");
        setLayout(new VerticalFlowLayout());

        musicPanel = new JPanel();
        musicPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Music"));
        musicOnButton = new JCheckBox("Music");
        if (Globals.settings.get("music").equals("yes")) {
            musicOnButton.setSelected(true);
        }
        musicOnButton.addActionListener(a -> {
            if (Globals.settings.get("music").equals("no")) {
                Globals.settings.setProperty("music", "yes");
                if (!GameController.musicPlayer.isPlaying()) {
                    GameController.musicPlayer.playMusic();
                }
            } else if (Globals.settings.get("music").equals("yes")) {
                Globals.settings.setProperty("music", "no");
                GameController.musicPlayer.stopMusic();
            }
        });

        if (Globals.settings.get("music").equals("no")) {
            musicOnButton.setSelected(false);
        }

        musicVolumeSlider = new JSlider(0, 100);
        musicVolumeSlider.setValue((int) (Double.parseDouble(Globals.settings.getProperty("music.volume")) * 100));
        musicVolumeSlider.setPaintLabels(true);
        musicVolumeSlider.setPaintTicks(true);
        musicVolumeSlider.addChangeListener(l -> {
            GameController.musicPlayer.setVolume(((float) musicVolumeSlider.getValue()) / 100f);
            Globals.settings.put("music.volume", "" + ((float) musicVolumeSlider.getValue()) / 100f);
        });

        DefaultComboBoxModel<String> lafComboBoxModel = new DefaultComboBoxModel<>();

        lafPanel = new JPanel();
        lookAndFeelLabel = new JLabel("Look and feel: ");
        lookAndFeelComboBox = new JComboBox<>(lafComboBoxModel);

        //Fill with text
        File lafPropertyFile = ResourceLoader.getResourceByFile("laf.properties");
        FileInputStream fis;
        try {
            fis = new FileInputStream(lafPropertyFile);
            lafProperties.load(fis);
            for (String s : lafProperties.stringPropertyNames()) {
                lafComboBoxModel.addElement(s);
            }
            //lafComboBoxModel.(lafProperties.stringPropertyNames());
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
        lafComboBoxModel.setSelectedItem(Globals.settings.getProperty("laf"));

        lookAndFeelComboBox.addActionListener(l -> {
            //Set the laf
            try {
                //Set look and feel
                String lafText = (String) lookAndFeelComboBox.getSelectedItem();
                if (lafText.equals("default")) {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } else {
                    UIManager.setLookAndFeel(lafProperties.getProperty(lafText));
                }

                //Update all the UI to follow the new look and feel
                for (Frame f : JFrame.getFrames()) {
                    SwingUtilities.updateComponentTreeUI(f);
                    f.pack();
                }

                Globals.settings.setProperty("laf", lafText);

                File settingsFile = new File(System.getProperty("user.dir") + "/settings.properties");
                //Store the new settings
                Globals.settings.store(new FileOutputStream(settingsFile), "Created by Conquer Space version " + VERSION.toString());
            } catch (ClassNotFoundException ex) {
                LOGGER.warn("", ex);
            } catch (InstantiationException ex) {
                LOGGER.warn("", ex);
            } catch (IllegalAccessException ex) {
                LOGGER.warn("", ex);
            } catch (UnsupportedLookAndFeelException ex) {
                LOGGER.warn("", ex);
            } catch (FileNotFoundException ex) {
                LOGGER.warn("", ex);
            } catch (IOException ex) {
                LOGGER.warn("", ex);
            }
        });

        lafPanel.setLayout(new HorizontalFlowLayout());

        lafPanel.add(lookAndFeelLabel);
        lafPanel.add(lookAndFeelComboBox);

        musicPanel.setLayout(new VerticalFlowLayout());
        musicPanel.add(musicOnButton);
        musicPanel.add(new JLabel("Volume"));
        musicPanel.add(musicVolumeSlider);
        addInternalFrameListener(this);
        add(musicPanel);
        add(lafPanel);
        setClosable(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }

    public static InternalOptionsWindow getInstance() {
        if (instance == null) {
            instance = new InternalOptionsWindow();
        }

        instance.setVisible(true);
        return (instance);
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
        //Write
        File settingsFile = new File(System.getProperty("user.dir") + "/settings.properties");
        if (settingsFile.exists()) {
            FileOutputStream fis = null;
            try {
                //Read from file.
                fis = new FileOutputStream(settingsFile);
                PrintWriter pw = new PrintWriter(fis);
                Globals.settings.store(pw, "Created by Conquer Space version " + VERSION.toString());
                pw.close();
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
            } finally {
                try {
                    fis.close();
                } catch (IOException ex) {
                }
            }
        }
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
    }
}
