package ConquerSpace;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.logging.Logger;

import ConquerSpace.start.gui.Manual;
import ConquerSpace.util.CQSPLogger;
/**
 *
 * @author Zyun
 */
public class MainMenu extends JFrame{
	private static final Logger LOGGER = CQSPLogger.getLogger(MainMenu.class.getName());
    public MainMenu(){
		setTitle("Conquer Space");
		setLayout(new GridLayout(2, 1, 10, 10));
		add(new TopBanner());
		add(new BottomMenu());
		pack();
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
		
		public BottomMenu(){
			setLayout(new GridLayout(2, 3, 10, 10));
			
			startGame = new JButton("Start Game");
			resumeGame = new JButton("Resume Game");
			about = new JButton("About");
			manual = new JButton("Manual");
			credits = new JButton("Credits");
			options = new JButton("Options");
			
			//Action Listeners
			manual.addActionListener(e -> {
				Manual man = Manual.getInstance();
				man.setVisible(true);
			});
			add(startGame);
			add(resumeGame);
			add(about);
			add(manual);
			add(credits);
			add(options);
		}
	}
}
