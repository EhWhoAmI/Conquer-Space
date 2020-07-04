package ConquerSpace.game.universe;

import ConquerSpace.game.universe.generators.UniverseGenerationConfig;
import ConquerSpace.Globals;
import ConquerSpace.game.GameController;
import ConquerSpace.game.GameLoader;
import ConquerSpace.game.organizations.civilization.Civilization;
import ConquerSpace.game.universe.generators.CivilizationConfig;
import ConquerSpace.game.universe.bodies.Universe;
import ConquerSpace.game.universe.generators.DefaultUniverseGenerator;
import ConquerSpace.game.universe.generators.UniverseGenerator;
import ConquerSpace.gui.renderers.UniverseRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.script.ScriptException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.border.LineBorder;

/**
 *
 * @author EhWhoAmI
 */
public final class UniverseRenderTest {

    public static void main(String[] args) throws FileNotFoundException, ScriptException {
        //Select universe
        JPanel pan = new JPanel(new GridLayout(4, 4, 10, 10));
        //Init components
        JLabel universeSizeLabel;
        JComboBox<String> universeSizeBox;
        JLabel universeTypeLabel;
        JComboBox<String> universeTypeComboBox;
        JLabel universeHistoryLabel;
        JComboBox<String> universeHistoryComboBox;
        JLabel planetCommonalityLabel;
        JComboBox<String> planetCommonalityComboBox;
        JLabel civilizationsLabel;
        JComboBox<String> civilazitionComboBox;
        JLabel seedLabel;
        JTextField seedText;

        JLabel civNameLabel;
        JTextField civNameTextField;
        JLabel civColorLabel;
        JButton civColorChooserButton;
        JLabel civSymbolLabel;
        JSpinner civSymbolSpinner;
        JLabel civHomePlanetNameLabel;
        JTextField civHomePlanetName;
        JLabel civTempResistanceLabel;
        JComboBox<String> civTempResistanceComboBox;
        JLabel speciesNameLabel;
        JTextField speciesNameField;

        JLabel quoteLabel;

        Universe universe = null;
        Color civColor = Color.CYAN;

        JPanel lsidePan = new JPanel();
        lsidePan.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Universe Options"));
        lsidePan.setLayout(new GridLayout(3, 4, 10, 10));

        universeSizeLabel = new JLabel("Universe Size");
        universeSizeBox = new JComboBox<>();
        universeSizeBox.addItem("Small");
        universeSizeBox.addItem("Medium");
        universeSizeBox.addItem("Large");

        universeTypeLabel = new JLabel("Universe Type");
        universeTypeComboBox = new JComboBox<>();
        //Remove spiral and elliptical for now because it is easier
        //universeTypeComboBox.addItem("Spiral");
        universeTypeComboBox.addItem("Irregular");
        //universeTypeComboBox.addItem("Elliptical");

        //Doesnt make a difference for now...
        universeHistoryLabel = new JLabel("Universe Age");
        universeHistoryComboBox = new JComboBox<>();
        universeHistoryComboBox.addItem("Short");
        universeHistoryComboBox.addItem("Medium");
        universeHistoryComboBox.addItem("Long");
        universeHistoryComboBox.addItem("Ancient");

        planetCommonalityLabel = new JLabel("Planet Commonality");
        planetCommonalityComboBox = new JComboBox<>();
        planetCommonalityComboBox.addItem("Common");
        planetCommonalityComboBox.addItem("Sparse");

        civilizationsLabel = new JLabel("Civilization Count");
        civilazitionComboBox = new JComboBox<>();
        civilazitionComboBox.addItem("Sparse");
        civilazitionComboBox.addItem("Common");

        seedLabel = new JLabel("Seed");
        seedText = new JTextField();
        seedText.setText("" + System.currentTimeMillis());

        quoteLabel = new JLabel("Good luck -- Have Fun!");
        JPanel rsidePan = new JPanel();
        rsidePan.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Universe Options"));
        rsidePan.setLayout(new GridLayout(3, 4, 10, 10));

        civNameLabel = new JLabel("Civilization Name");
        civNameTextField = new JTextField("Humans");

        civSymbolLabel = new JLabel("Civilization Symbol");
        //Greek symbol list
        String[] list = new String[26];
        for (int i = 0; i < list.length; i++) {
            list[i] = String.valueOf((char) (i + 65));
        }
        SpinnerListModel mod = new SpinnerListModel(list);
        civSymbolSpinner = new JSpinner(mod);

        civColorLabel = new JLabel("Civilization Color");
        civColorChooserButton = new JButton("Choose");
        civColorChooserButton.setBackground(civColor);
        civColorChooserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Do nothing
            }

        });

        civHomePlanetNameLabel = new JLabel("Home Planet Name");
        civHomePlanetName = new JTextField("Earth");

        civTempResistanceLabel = new JLabel("Civilization Preferred Climate");
        civTempResistanceComboBox = new JComboBox<>();
        civTempResistanceComboBox.addItem("Varied");
        civTempResistanceComboBox.addItem("Cold");
        civTempResistanceComboBox.addItem("Hot");

        speciesNameLabel = new JLabel("Species Name");
        speciesNameField = new JTextField("Earthlings");

        lsidePan.add(universeSizeLabel);
        lsidePan.add(universeSizeBox);
        lsidePan.add(universeTypeLabel);
        lsidePan.add(universeTypeComboBox);
        lsidePan.add(universeHistoryLabel);
        lsidePan.add(universeHistoryComboBox);
        lsidePan.add(planetCommonalityLabel);
        lsidePan.add(planetCommonalityComboBox);
        lsidePan.add(civilizationsLabel);
        lsidePan.add(civilazitionComboBox);
        lsidePan.add(seedLabel);
        lsidePan.add(seedText);

        rsidePan.add(civNameLabel);
        rsidePan.add(civNameTextField);
        rsidePan.add(civSymbolLabel);
        rsidePan.add(civSymbolSpinner);
        rsidePan.add(civColorLabel);
        rsidePan.add(civColorChooserButton);
        rsidePan.add(civHomePlanetNameLabel);
        rsidePan.add(civHomePlanetName);
        rsidePan.add(civTempResistanceLabel);
        rsidePan.add(civTempResistanceComboBox);
        rsidePan.add(speciesNameLabel);
        rsidePan.add(speciesNameField);

        pan.add(lsidePan);
        pan.add(rsidePan);
        pan.add(quoteLabel);
        //Show a option pane
        JOptionPane.showMessageDialog(null, pan);

        //Parse arguments.
        FileReader reader = null;
        try {
            //This button will only be pressed by the `done` button.
            //Read all the info, pass to scripts.
            UniverseGenerationConfig config = new UniverseGenerationConfig();
            config.setUniverseSize((String) universeSizeBox.getSelectedItem());
            config.setUniverseShape((String) universeTypeComboBox.getSelectedItem());
            config.setUniverseAge((String) universeHistoryComboBox.getSelectedItem());
            config.setCivilizationCount((String) civilazitionComboBox.getSelectedItem());
            config.setPlanetCommonality((String) planetCommonalityComboBox.getSelectedItem());

            long seed;
            try {
                seed = Long.parseLong(seedText.getText()); // This will pass a nfe.
            } catch (NumberFormatException nfe) {
                seed = seedText.getText().hashCode();
            }

            config.setSeed(seed);

            //Set the player Civ options
            CivilizationConfig civilizationConfig = new CivilizationConfig();
            civilizationConfig.setCivColor(civColor);
            civilizationConfig.setCivSymbol((String) civSymbolSpinner.getValue());
            civilizationConfig.setCivilizationName(civNameTextField.getText());
            civilizationConfig.setCivilizationPreferredClimate((String) civTempResistanceComboBox.getSelectedItem());
            civilizationConfig.setHomePlanetName(civHomePlanetName.getText());
            civilizationConfig.setSpeciesName(speciesNameField.getText());
            config.setCivilizationConfig(civilizationConfig);

            UniverseGenerator gen = new DefaultUniverseGenerator(config, civilizationConfig, seed);
            GameLoader.load();
            Universe main = gen.generate();
            GameController.universe = main;
            long loadingEnd = System.currentTimeMillis();

        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
            }
        }

        //Then create window, display the universe.
        JFrame frame = new JFrame("Universe displayer");

        frame.setTitle("Conquer Space");
        frame.setLayout(new BorderLayout());
        //Create universe renderer
        UniverseRenderer renderer = new UniverseRenderer(new Dimension(1500, 1500), GameController.universe, new Civilization(0, universe));
        
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                renderer.drawUniverse(g, 0, 0, 1);
            }
        };

        //Place renderer into a scroll pane.
        JScrollPane scrollPane = new JScrollPane(panel);

        frame.add(scrollPane);

        frame.setSize(500, 500);
        frame.setVisible(true);
    }
}
