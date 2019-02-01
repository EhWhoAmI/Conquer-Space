package ConquerSpace.gui.start;

import ConquerSpace.Globals;
import ConquerSpace.game.GameController;
import ConquerSpace.game.universe.UniverseConfig;
import ConquerSpace.game.universe.civilization.CivilizationConfig;
import ConquerSpace.game.universe.spaceObjects.Universe;
import ConquerSpace.util.CQSPLogger;
import ConquerSpace.util.ExceptionHandling;
import ConquerSpace.util.ResourceLoader;
import ConquerSpace.util.scripts.RunScript;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.border.LineBorder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.python.core.PyException;
import org.python.core.PyObjectDerived;

/**
 *
 * @author Zyun
 */
public class NewGame extends JFrame implements ActionListener {

    private static final Logger LOGGER = CQSPLogger.getLogger(NewGame.class.getName());

    private JLabel universeSizeLabel;
    private JComboBox<String> universeSizeBox;
    private JLabel universeTypeLabel;
    private JComboBox<String> universeTypeComboBox;
    private JLabel universeHistoryLabel;
    private JComboBox<String> universeHistoryComboBox;
    private JLabel planetCommonalityLabel;
    private JComboBox<String> planetCommonalityComboBox;
    private JLabel civilizationsLabel;
    private JComboBox<String> civilazitionComboBox;
    private JLabel seedLabel;
    private JTextField seedText;

    private JLabel civNameLabel;
    private JTextField civNameTextField;
    private JLabel civColorLabel;
    private JButton civColorChooserButton;
    private JLabel civSymbolLabel;
    private JSpinner civSymbolSpinner;
    private JLabel civHomePlanetNameLabel;
    private JTextField civHomePlanetName;
    private JLabel civTempResistanceLabel;
    private JComboBox<String> civTempResistanceComboBox;
    private JLabel speciesNameLabel;
    private JTextField speciesNameField;

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
        setLayout(new GridLayout(2, 1, 10, 10));
        //Add components
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
        if (Globals.settings.getProperty("debug") != null && Globals.settings.getProperty("debug").equals("yes")) {
            seedText.setText("test");
        } else {
            seedText.setText("" + System.currentTimeMillis());
        }

        quoteLabel = new JLabel("Good luck -- Have Fun!");
        exitButton = new JButton("Done!");
        exitButton.addActionListener(this);

        JPanel rsidePan = new JPanel();
        rsidePan.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Universe Options"));
        rsidePan.setLayout(new GridLayout(3, 4, 10, 10));

        civNameLabel = new JLabel("Civilization Name");
        civNameTextField = new JTextField("Humans");

        civSymbolLabel = new JLabel("Civilization Symbol");
        //symbol list (Alphabet)
        String[] list = new String[26];
        for (int i = 0; i < list.length; i++) {
            list[i] = String.valueOf((char) (i + 65));
        }
        SpinnerListModel mod = new SpinnerListModel(list);
        civSymbolSpinner = new JSpinner(mod);

        civColorLabel = new JLabel("Civilization Color");
        civColorChooserButton = new JButton("Choose");
        civColorChooserButton.setBackground(civColor);
        civColorChooserButton.addActionListener(this);

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

        add(lsidePan);
        add(rsidePan);
        add(quoteLabel);
        add(exitButton);

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
            try {
                //This button will only be pressed by the `done` button.
                //Read all the info, pass to scripts.
                config.setUniverseSize((String) universeSizeBox.getSelectedItem());
                config.setUniverseShape((String) universeTypeComboBox.getSelectedItem());
                config.setUniverseAge((String) universeHistoryComboBox.getSelectedItem());
                config.setCivilizationCount((String) civilazitionComboBox.getSelectedItem());
                config.setPlanetCommonality((String) planetCommonalityComboBox.getSelectedItem());

                long seed;
                LOGGER.trace("Parsing seed.");
                try {
                    seed = Long.parseLong(seedText.getText()); // This will pass a nfe.
                    LOGGER.trace("Seed is long value.");
                } catch (NumberFormatException nfe) {
                    seed = seedText.getText().hashCode();
                    LOGGER.trace("Seed is string literal.");
                }
                LOGGER.trace("Seed: " + seed);

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

                // Start time of logging
                long loadingStart = System.currentTimeMillis();
                //Init script engine
                RunScript s = new RunScript(ResourceLoader.loadResource("script.python.universegen.main"));

                s.addVar("universeConfig", config);
                s.addVar("LOGGER", CQSPLogger.getLogger("Script.universeGen/main.py"));
                s.exec();
                Universe universe = (Universe) ((PyObjectDerived) s.getObject("universeObject")).__tojava__(Universe.class);

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
            } catch (final PyException ex) {
                LogManager.getLogger("ErrorLog").error("Python error " + ex.toString() + " Seed = " + seedText.getText(), ex);
                String trace = "None";
                if (ex.traceback != null) {
                    trace = ex.traceback.dumpStack();
                }
                ExceptionHandling.ExceptionMessageBox("Script error: " + ex.type.toString() + ".\nPython trace: \n" + trace + "\nSeed: " + config.seed, ex);
                System.exit(1);
            }
        } else if (e.getSource() == civColorChooserButton) {
            //Show the civ color chooser
            civColor = JColorChooser.showDialog(null, "Choose Civilization Color", civColor);
            civColorChooserButton.setBackground(civColor);
        }
    }

    public Universe getUniverse() {
        return universe;
    }
}
