package ConquerSpace;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.logging.Logger;

import ConquerSpace.start.gui.Manual;
import ConquerSpace.start.gui.NewGame;
import ConquerSpace.util.CQSPLogger;
import javax.swing.JOptionPane;

/**
 *
 * @author Zyun
 */
public class MainMenu extends JFrame {

    private static final Logger LOGGER = CQSPLogger.getLogger(MainMenu.class.getName());

    public MainMenu() {
        setTitle("Conquer Space");
        setLayout(new GridLayout(2, 1, 10, 10));
        add(new TopBanner());
        add(new BottomMenu());
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private class TopBanner extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawString("TODO: TITLE SCREEN", 0, 0);
        }
    }

    private class BottomMenu extends JPanel {

        private JButton startGame;
        private JButton resumeGame;
        private JButton about;
        private JButton manual;
        private JButton credits;
        private JButton options;

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
                JOptionPane.showMessageDialog(this, "Forgot to implement LOL :P", "Oops", JOptionPane.INFORMATION_MESSAGE);
            });
            
            add(startGame);
            add(resumeGame);
            add(manual);
            add(about);
            add(credits);
            add(options);
        }
    }
}
