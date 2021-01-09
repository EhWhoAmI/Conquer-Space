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
import static ConquerSpace.ConquerSpace.LOCALE_MESSAGES;
import ConquerSpace.common.game.universe.bodies.Galaxy;
import ConquerSpace.common.util.ResourceLoader;
import ConquerSpace.common.util.logging.CQSPLogger;
import ConquerSpace.server.generators.CivilizationConfig;
import ConquerSpace.server.generators.DefaultUniverseGenerator;
import ConquerSpace.server.generators.UniverseGenerationConfig;
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
 * @author EhWhoAmI
 */
public class NewGame extends JFrame implements ActionListener, WindowListener {

    private static final Logger LOGGER = CQSPLogger.getLogger(NewGame.class.getName());

    UniverseConfigPanel universeConfigPanel;
    private JLabel quoteLabel;
    private JButton exitButton;

    private Galaxy universe = null;
    private Color civColor = Color.CYAN;

    private MainMenu menu;

    /**
     *
     */
    public NewGame(MainMenu menu) {
        //For referencing when done loading
        this.menu = menu;

        setSize(500, 400);
        setTitle(LOCALE_MESSAGES.getMessage("newgame.title"));
        setLayout(new VerticalFlowLayout(10, 10));
        //Add components

        universeConfigPanel = new UniverseConfigPanel();

        JPanel bottomContainer = new JPanel();
        bottomContainer.setLayout(new GridLayout(1, 2));

        quoteLabel = new JLabel(LOCALE_MESSAGES.getMessage("newgame.glhf"));
        exitButton = new JButton(LOCALE_MESSAGES.getMessage("newgame.done"));
        exitButton.addActionListener(this);
        bottomContainer.add(quoteLabel);
        bottomContainer.add(exitButton);

        add(universeConfigPanel);
        add(bottomContainer);

        addWindowListener(this);

        setIconImage(ResourceLoader.getIcon("game.icon").getImage());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        LOGGER.trace("Loaded new game UI.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitButton) {
            setVisible(false);
            UniverseGenerationConfig config = new UniverseGenerationConfig();
            //This button will only be pressed by the `done` button.
            //Read all the info, pass to scripts.
            config.universeSize = universeConfigPanel.getUniverseSize();
            config.universeShape = universeConfigPanel.getUniverseShape();
            config.universeAge = universeConfigPanel.getUniverseAge();
            config.civilizationCount = universeConfigPanel.getCivCount();
            config.planetCommonality = universeConfigPanel.getPlanetRarity();

            config.seed = universeConfigPanel.getSeed();
            LOGGER.info("Seed: " + config.seed);
            
            //Set the player Civ options
            CivilizationConfig civilizationConfig = new CivilizationConfig();
            civilizationConfig.civColor = civColor;
            civilizationConfig.civSymbol = universeConfigPanel.getCivSymbol();
            civilizationConfig.civilizationName = universeConfigPanel.getCivCurrencyName();
            civilizationConfig.civilizationPreferredClimate = universeConfigPanel.getCivPreferredClimate();
            civilizationConfig.homePlanetName = universeConfigPanel.getHomePlanet();
            civilizationConfig.speciesName = universeConfigPanel.getSpeciesName();
            civilizationConfig.civCurrencyName = universeConfigPanel.getCivCurrencyName();
            civilizationConfig.civCurrencySymbol = universeConfigPanel.getCivCurrencySymbol();
            config.civConfig = civilizationConfig;

            //Create generator
            DefaultUniverseGenerator gen = new DefaultUniverseGenerator(config, civilizationConfig, config.seed);
            ConquerSpace.generator = gen;

            //Tell main thread that game works
            menu.dispose();
            menu.setLoadedUniverse(true);
        }
    }

    public Galaxy getUniverse() {
        return universe;
    }

    @Override
    public void windowOpened(WindowEvent arg0) {
        //Empty
    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        menu.setEnabled(true);
    }

    @Override
    public void windowClosed(WindowEvent arg0) {
        //Empty
    }

    @Override
    public void windowIconified(WindowEvent arg0) {
        //Empty
    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
        //Empty
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
        //Empty
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
        //Empty
    }
}
