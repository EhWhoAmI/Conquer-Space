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
import ConquerSpace.server.GameController;
import ConquerSpace.client.gui.music.GraphicsUtil;
import ConquerSpace.common.util.ResourceLoader;
import ConquerSpace.common.util.logging.CQSPLogger;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.apache.logging.log4j.Logger;
import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.Globals;
import ConquerSpace.server.generators.SaveGameUniverseGenerator;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Main menu.
 *
 * @author EhWhoAmI
 */
public class MainMenu extends JFrame implements WindowListener {

    //Logger
    private static final Logger LOGGER = CQSPLogger.getLogger(MainMenu.class.getName());

    private boolean loadedUniverse = false;

    /**
     * Constructor, show main menu.
     */
    public MainMenu() {
        setTitle(LOCALE_MESSAGES.getMessage("GameName"));
        setLayout(new BorderLayout());

        TopBanner topBanner = new TopBanner();
        topBanner.setPreferredSize(new Dimension(600, 150));

        topBanner.repaint();
        //Add the classes on the bottom
        add(topBanner, BorderLayout.NORTH);
        add(new BottomMenu(), BorderLayout.SOUTH);

        setIconImage(ResourceLoader.getIcon("game.icon").getImage());

        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(this);
        LOGGER.trace("Loaded main menu");
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        //play sound
    }

    @Override
    public void windowOpened(WindowEvent arg0) {
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        //Stop playing sound
        GameController.musicPlayer.clean();
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent arg0) {
    }

    @Override
    public void windowIconified(WindowEvent arg0) {
    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
    }

    /**
     * Top banner of the main menu. Drawings.
     */
    private class TopBanner extends JPanel {

        int selectedImage;
        BufferedImage image;
        Font f = new Font(getFont().getFontName(), Font.BOLD, 28);

        public TopBanner() {
            File dir = ResourceLoader.getResourceByFile("image.titlebanner.dir");
            File[] files = dir.listFiles();
            //Select random file
            selectedImage = (int) (Math.random() * (files.length));
            try {
                image = ImageIO.read(files[selectedImage]);
            } catch (IOException ex) {
                //No image, so F
                LOGGER.warn("Unable to open image!", ex);
            }
        }

        /**
         * The paint component part, for the title screen.
         *
         * @param g Graphics.
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            //Turn on AA.
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);

            g2d.setColor(Color.BLUE);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            //Image is a bit small though.
            if (image != null) {
                g2d.drawImage(image, null, 0, 0);
            } else {
                g2d.setColor(Color.WHITE);
                Font f = g2d.getFont().deriveFont(40f);
                String cqspString = LOCALE_MESSAGES.getMessage("GameName");
                int height = getFontMetrics(f).getHeight();
                GraphicsUtil.paintTextWithOutline(cqspString, g2d, 60f, 0, 0);

                String secondString = LOCALE_MESSAGES.getMessage("start.gui.MainMenu.nobanner");
                g2d.drawString(secondString, 0, height * 2);
            }
        }
    }

    /**
     * The bottom menu of the main menu. Buttons.
     */
    private class BottomMenu extends JPanel {

        //Components
        private JButton startGame;
        private JButton resumeGame;
        private JButton about;
        private JButton manual;
        private JButton credits;
        private JButton options;

        /**
         * The whole menu.
         */
        public BottomMenu() {
            setLayout(new GridLayout(2, 3, 5, 5));

            startGame = new JButton(LOCALE_MESSAGES.getMessage("start.gui.MainMenu.startgame"));
            resumeGame = new JButton(LOCALE_MESSAGES.getMessage("start.gui.MainMenu.resumegame"));
            about = new JButton(LOCALE_MESSAGES.getMessage("start.gui.MainMenu.about"));
            manual = new JButton(LOCALE_MESSAGES.getMessage("start.gui.MainMenu.manual"));
            credits = new JButton(LOCALE_MESSAGES.getMessage("start.gui.MainMenu.credits"));
            options = new JButton(LOCALE_MESSAGES.getMessage("start.gui.MainMenu.options"));

            //Action Listeners
            startGame.addActionListener(e -> {
                dispose();
                NewGame game = new NewGame(MainMenu.this);
                game.setVisible(true);
            });

            resumeGame.addActionListener(e -> {
                JFileChooser chooser = new JFileChooser("./save");
                chooser.setFileFilter(new FileNameExtensionFilter("Zip File", "zip"));
                chooser.showOpenDialog(null);
                File save = chooser.getSelectedFile();
                //Then load the universe
                if (save != null) {
                    Globals.generator = new SaveGameUniverseGenerator(save);
                    setLoadedUniverse(true);
                    dispose();
                }
            });

            about.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, String.format(LOCALE_MESSAGES.getMessage("start.gui.MainMenu.versiontext"), ConquerSpace.VERSION.toString(), System.getProperty("java.version")), LOCALE_MESSAGES.getMessage("start.gui.MainMenu.about"), JOptionPane.INFORMATION_MESSAGE);
            });

            manual.addActionListener(e -> {
                Manual man = Manual.getInstance();
                man.setVisible(true);
            });

            credits.addActionListener(e -> {
                FileInputStream fis = null;
                try {
                    File file = new File(System.getProperty("user.dir") + "/assets/credits.html");
                    fis = new FileInputStream(file);
                    byte[] data = new byte[(int) file.length()];
                    fis.read(data);
                    fis.close();
                    String text = new String(data);

                    JEditorPane pane = new JEditorPane("text/html", text);
                    pane.setEditable(false);
                    pane.setSize(500, 300);
                    JScrollPane scroll = new JScrollPane(pane);
                    scroll.setMaximumSize(new Dimension(500, 250));
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            scroll.getVerticalScrollBar().setValue(0);
                        }
                    });
                    pane.addHyperlinkListener((s) -> {
                        if (s.getInputEvent() instanceof MouseEvent) {
                            MouseEvent me = (MouseEvent) s.getInputEvent();
                            if (SwingUtilities.isLeftMouseButton(me)) {
                                try {
                                    Desktop.getDesktop().browse(s.getURL().toURI());

                                } catch (IOException ex) {
                                } catch (URISyntaxException ex) {
                                }
                            }
                        }
                    });
                    pane.setBackground(UIManager.getDefaults().getColor("Panel.background"));

                    pane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
                    pane.setFont(UIManager.getDefaults().getFont("Label.font"));
                    JFrame frame = new JFrame();
                    frame.add(scroll);
                    frame.setSize(300, 500);
                    frame.setTitle(LOCALE_MESSAGES.getMessage("start.gui.MainMenu.credits"));
                    frame.setVisible(true);
                } catch (FileNotFoundException ex) {
                    LOGGER.warn("", ex);
                } catch (IOException ex) {
                    LOGGER.warn("", ex);
                } finally {
                    try {
                        fis.close();
                    } catch (IOException ex) {
                        LOGGER.warn("", ex);
                    }
                }
            });

            options.addActionListener(e -> {
                OptionsWindow.getInstance();
            });

            //Add components
            add(startGame);
            add(resumeGame);
            add(manual);
            add(about);
            add(credits);
            add(options);
        }
    }

    public boolean isLoadedUniverse() {
        return loadedUniverse;
    }

    void setLoadedUniverse(boolean loadedUniverse) {
        this.loadedUniverse = loadedUniverse;
    }
}
