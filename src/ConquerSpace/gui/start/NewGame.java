package ConquerSpace.gui.start;

import ConquerSpace.Globals;
import ConquerSpace.game.GameController;
import ConquerSpace.game.universe.UniverseConfig;
import ConquerSpace.game.universe.civilization.CivilizationConfig;
import ConquerSpace.game.universe.generators.DefaultUniverseGenerator;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.util.CQSPLogger;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Zyun
 */
public class NewGame extends JFrame implements ActionListener, WindowListener {

    private static final Logger LOGGER = CQSPLogger.getLogger(NewGame.class.getName());

    UniverseConfigPanel universeConfigPanel;
    private JLabel quoteLabel;
    private JButton exitButton;

    private Universe universe = null;
    private Color civColor = Color.CYAN;

    /**
     *
     */
    public NewGame() {
        setSize(500, 400);
        setTitle("New Game");
        setLayout(new VerticalFlowLayout(10, 10));
        //Add components

        universeConfigPanel = new UniverseConfigPanel();

        JPanel bottomContainer = new JPanel();
        bottomContainer.setLayout(new GridLayout(1, 2));

        quoteLabel = new JLabel("Good luck -- Have Fun!");
        exitButton = new JButton("Done!");
        exitButton.addActionListener(this);
        bottomContainer.add(quoteLabel);
        bottomContainer.add(exitButton);

        add(universeConfigPanel);
        add(bottomContainer);

        addWindowListener(this);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        LOGGER.trace("Loaded new game UI.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitButton) {
            setVisible(false);
            //Show loading screen
            Loading load = new Loading();
            UniverseConfig config = new UniverseConfig();
            //This button will only be pressed by the `done` button.
            //Read all the info, pass to scripts.
            config.setUniverseSize((String) universeConfigPanel.universeSizeBox.getSelectedItem());
            config.setUniverseShape((String) universeConfigPanel.universeTypeComboBox.getSelectedItem());
            config.setUniverseAge((String) universeConfigPanel.universeHistoryComboBox.getSelectedItem());
            config.setCivilizationCount((String) universeConfigPanel.civilazitionComboBox.getSelectedItem());
            config.setPlanetCommonality((String) universeConfigPanel.planetCommonalityComboBox.getSelectedItem());

            long seed;
            LOGGER.trace("Parsing seed.");
            try {
                seed = Long.parseLong(universeConfigPanel.seedText.getText()); // This will pass a nfe.
                LOGGER.trace("Seed is long value.");
            } catch (NumberFormatException nfe) {
                seed = universeConfigPanel.seedText.getText().hashCode();
                LOGGER.trace("Seed is string literal.");
            }
            LOGGER.info("Seed: " + seed);

            config.setSeed(seed);

            //Set the player Civ options
            CivilizationConfig civilizationConfig = new CivilizationConfig();
            civilizationConfig.setCivColor(civColor);
            civilizationConfig.setCivSymbol((String) universeConfigPanel.civSymbolSpinner.getValue());
            civilizationConfig.setCivilizationName(universeConfigPanel.civNameTextField.getText());
            civilizationConfig.setCivilizationPreferredClimate((String) universeConfigPanel.civTempResistanceComboBox.getSelectedItem());
            civilizationConfig.setHomePlanetName(universeConfigPanel.civHomePlanetName.getText());
            civilizationConfig.setSpeciesName(universeConfigPanel.speciesNameField.getText());
            config.setCivilizationConfig(civilizationConfig);

            // Start time of logging
            long loadingStart = System.currentTimeMillis();
            DefaultUniverseGenerator gen = new DefaultUniverseGenerator();
            Universe universe = gen.generate(config, civilizationConfig, seed);
            //Logger end time
            long loadingEnd = System.currentTimeMillis();
            LOGGER.info("Took " + (loadingEnd - loadingStart) + " ms to generate universe, or about " + ((loadingEnd - loadingStart) / 60000) + " minutes");

            // Log info
            //LOGGER.info("Universe:" + universe.toReadableString());
            //Insert universe into globals
            Globals.universe = universe;
            System.gc();
            load.setVisible(false);
            new GameController();
        }
    }

    public Universe getUniverse() {
        return universe;
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
}
