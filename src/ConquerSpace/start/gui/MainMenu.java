package ConquerSpace.start.gui;

import ConquerSpace.ConquerSpace;
import static ConquerSpace.ConquerSpace.localeMessages;
import ConquerSpace.util.CQSPLogger;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
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

/**
 * Main menu.
 *
 * @author Zyun
 */
public class MainMenu extends JFrame {

    //Logger
    private static final Logger LOGGER = CQSPLogger.getLogger(MainMenu.class.getName());

    /**
     * Constructor, show main menu.
     */
    public MainMenu() {
        setTitle(localeMessages.getMessage("GameName"));
        setLayout(new GridLayout(2, 1, 10, 10));

        TopBanner topBanner = new TopBanner();
        topBanner.repaint();
        //Add the classes on the bottom
        add(topBanner);
        add(new BottomMenu());
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        LOGGER.trace("Loaded main menu");
    }

    /**
     * Top banner of the main menu. Drawings.
     */
    private class TopBanner extends JPanel {

        Font f = new Font(getFont().getFontName(), Font.BOLD, 28);

        /**
         * The paint component part, for the title screen.
         *
         * @param g Graphics.
         */
        @Override
        protected void paintComponent(Graphics g) {
            try {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.BLUE);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                //Image is a bit small though.
                g2d.drawImage(ImageIO.read(new File(
                        System.getProperty("user.dir")
                        + "/assets/img/title.png")), null, 0, 0);
            } catch (IOException ex) {
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
            setLayout(new GridLayout(2, 3, 10, 10));

            startGame = new JButton(localeMessages.getMessage("start.gui.MainMenu.startgame"));
            resumeGame = new JButton(localeMessages.getMessage("start.gui.MainMenu.resumegame"));
            about = new JButton(localeMessages.getMessage("start.gui.MainMenu.about"));
            manual = new JButton(localeMessages.getMessage("start.gui.MainMenu.manual"));
            credits = new JButton(localeMessages.getMessage("start.gui.MainMenu.credits"));
            options = new JButton(localeMessages.getMessage("start.gui.MainMenu.options"));

            //Action Listeners
            startGame.addActionListener(e -> {
                dispose();
                NewGame game = new NewGame();
                game.setVisible(true);
            });

            resumeGame.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, localeMessages.getMessage("start.gui.MainMenu.notimplemented"), localeMessages.getMessage("start.gui.MainMenu.oops"), JOptionPane.INFORMATION_MESSAGE);
            });

            about.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, String.format(localeMessages.getMessage("start.gui.MainMenu.versiontext"), ConquerSpace.VERSION.toString()), localeMessages.getMessage("start.gui.MainMenu.about"), JOptionPane.INFORMATION_MESSAGE);
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

//                    JPanel pan = new JPanel();
//                    pan.setLayout(new VerticalFlowLayout(5, 5));
//                    JLabel l = new JLabel("<html>" + text.replace("\n", "<br/>") + "</html>");
//                    pan.add(l);
                    JEditorPane pane = new JEditorPane("text/html", text);
                    pane.setEditable(false);
                    JScrollPane scroll = new JScrollPane(pane);
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

                    JOptionPane.showMessageDialog(this, scroll, localeMessages.
                            getMessage("start.gui.MainMenu.credits"),
                            JOptionPane.INFORMATION_MESSAGE);
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
}
