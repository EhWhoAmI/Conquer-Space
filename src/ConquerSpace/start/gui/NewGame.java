package ConquerSpace.start.gui;

import ConquerSpace.ConquerSpace;
import ConquerSpace.game.universe.Universe;
import ConquerSpace.util.CQSPLogger;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Zyun
 */
public class NewGame extends JFrame implements ActionListener{
    private static final Logger LOGGER = CQSPLogger.getLogger(NewGame.class.getName());
    
    private JLabel universeSizeLabel;
    private JComboBox<String> universeSizeBox;
    private JLabel universeTypeLabel;
    private JComboBox<String> universeTypeComboBox;
    private JLabel universeHistoryLabel;
    private JComboBox<String>universeHistoryComboBox;
    private JLabel planetCommonalityLabel;
    private JComboBox<String> planetCommonalityComboBox;
    private JLabel civilizationsLabel;
    private JComboBox<String>civilazitionComboBox;
    private JLabel quoteLabel;
    private JButton exitButton;
    
    private Universe universe = null;

    /**
     *
     */
    public NewGame() {
        setSize(500, 400);
        setTitle("New Game");
        setLayout(new GridLayout(3, 4, 10, 10));
        //Add components
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
        
        quoteLabel = new JLabel("Good luck -- Have Fun!");
        exitButton = new JButton("Done!");
        exitButton.addActionListener(this);
        
        add(universeSizeLabel);
        add(universeSizeBox);
        add(universeTypeLabel);
        add(universeTypeComboBox);
        add(universeHistoryLabel);
        add(universeHistoryComboBox);
        add(planetCommonalityLabel);
        add(planetCommonalityComboBox);
        add(civilizationsLabel);
        add(civilazitionComboBox);
        add(quoteLabel);
        add(exitButton);
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        LOGGER.info("Loaded new game UI.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileReader reader = null;
        try {
            //This button will only be pressed by the `done` button.
            //Read all the info, pass to scripts.
            UniverseConfig config = new UniverseConfig();
            config.setUniverseSize((String) universeSizeBox.getSelectedItem());
            config.setUniverseShape((String) universeTypeComboBox.getSelectedItem());
            config.setUniverseAge((String) universeHistoryComboBox.getSelectedItem());
            config.setCivilizationCount((String) civilazitionComboBox.getSelectedItem());
            config.setPlanetCommonality((String) planetCommonalityComboBox.getSelectedItem());
            long loadingStart = System.currentTimeMillis();
            //Init script
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("python");
            
            engine.put("universeConfig", config);
            engine.put("version", ConquerSpace.VERSION);
            reader = new FileReader(System.getProperty("user.dir") + "/assets/scripts/universeGen/main.py");
            engine.eval(reader);
            Universe universe = (Universe) engine.get("universeObject");
            long loadingEnd = System.currentTimeMillis();
            LOGGER.info("Took " + (loadingEnd - loadingStart) + " ms to generate universe.");
            
            // Log info
            LOGGER.info("Universe:" + universe.toReadableString());
            
        } catch (FileNotFoundException ex) {
            LogManager.getLogger("ErrorLog").error("Error!", ex);
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage() + "\n" + ex.getStackTrace(), "File not found", JOptionPane.ERROR_MESSAGE);
        } catch (ScriptException ex) {
            LogManager.getLogger("ErrorLog").error("Error!", ex);
            JOptionPane.showMessageDialog(this, "Script Error: " + ex.getMessage() + "\n" + ex.getStackTrace().toString(), "Script Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (reader != null)
                reader.close();
            } catch (IOException ex) {
                LOGGER.error("Error!", ex);
            }
        }
    }
    
    public Universe getUniverse() {
        return universe;
    }
}
