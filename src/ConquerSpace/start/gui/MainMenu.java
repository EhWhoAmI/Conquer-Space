package ConquerSpace.start.gui;

import ConquerSpace.ConquerSpace;
import ConquerSpace.util.CQSPLogger;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
         * @param g Graphics.
         */
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLUE);
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setColor(Color.black);
            
            g2d.setFont(f);
            //Turn on antialias, looks better.
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawString("Conquer Space", 10, 30);
            
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
