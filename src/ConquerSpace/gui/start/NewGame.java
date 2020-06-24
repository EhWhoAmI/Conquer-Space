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

import ConquerSpace.Globals;
import ConquerSpace.game.GameController;
import ConquerSpace.game.organizations.civilization.CivilizationConfig;
import ConquerSpace.game.universe.UniverseConfig;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.game.universe.generators.DefaultUniverseGenerator;
import ConquerSpace.util.logging.CQSPLogger;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author EhWhoAmI
 */
public class NewGame extends JFrame implements ActionListener, WindowListener {

    private static final Logger LOGGER = CQSPLogger.getLogger(NewGame.class.getName());

    UniverseConfigPanel universeConfigPanel;
    private JLabel quoteLabel;
    private JButton exitButton;

    private Universe universe = null;
    private Color civColor = Color.CYAN;

    private MainMenu menu;

    /**
     *
     */
    public NewGame(MainMenu menu) {
        //For referencing when done loading
        this.menu = menu;

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

        try {
            setIconImage(ImageIO.read(new File("assets/img/icon.png")));
        } catch (IOException ioe) {
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        LOGGER.trace("Loaded new game UI.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitButton) {
            setVisible(false);
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
            civilizationConfig.setCivCurrencyName(universeConfigPanel.currencyNameTextField.getText());
            civilizationConfig.setCivCurrencySymbol(universeConfigPanel.currencySymbolTextField.getText());
            config.setCivilizationConfig(civilizationConfig);

            //Create generator
            DefaultUniverseGenerator gen = new DefaultUniverseGenerator(config, civilizationConfig, seed);
            Globals.generator = gen;
            
            //Tell main thread that game works
            menu.setLoadedUniverse(true);
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
