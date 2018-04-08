package ConquerSpace.start.gui;

import ConquerSpace.ConquerSpace;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;

import ConquerSpace.start.gui.Manual;
import ConquerSpace.start.gui.NewGame;
import ConquerSpace.start.gui.OptionsWindow;
import ConquerSpace.util.CQSPLogger;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.Logger;

/**
 * Main menu.
 * @author Zyun
 */
public class MainMenu extends JFrame {
    
    //Logger
    private static final Logger LOGGER = CQSPLogger.getLogger(MainMenu.class.getName());

    /**
     * Constructor, show main menu.
     */
    public MainMenu() {
        setTitle("Conquer Space");
        setLayout(new GridLayout(2, 1, 10, 10));
        
        //Add the classes on the bottom
        add(new TopBanner());
        add(new BottomMenu());
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        LOGGER.info("Loaded main menu");
    }

    /**
     * Top banner of the main menu. Drawings.
     */
    private class TopBanner extends JPanel {
        /**
         * The paint component part, for the title screen.
         * @param g Graphics.
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawString("TODO: TITLE SCREEN", 0, 0);
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

            startGame = new JButton("Start Game");
            resumeGame = new JButton("Resume Game");
            about = new JButton("About");
            manual = new JButton("Manual");
            credits = new JButton("Credits");
            options = new JButton("Options");

            //Action Listeners
            startGame.addActionListener(e -> {
                dispose();
                NewGame game = new NewGame();
                game.setVisible(true);
            });
            
            resumeGame.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Forgot to implement LOL :P", "Oops", JOptionPane.INFORMATION_MESSAGE);
            });
            
            about.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "About:\nVersion: " + ConquerSpace.VERSION.toString(), "About", JOptionPane.INFORMATION_MESSAGE);
            });
            
            manual.addActionListener(e -> {
                Manual man = Manual.getInstance();
                man.setVisible(true);
            });

            credits.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Coding:\nZyun\nConcept:\nZyun", "Credits", JOptionPane.INFORMATION_MESSAGE);
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
