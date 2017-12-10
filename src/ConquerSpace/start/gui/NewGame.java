package ConquerSpace.start.gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Zyun
 */
public class NewGame extends JFrame implements ActionListener{
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
        universeTypeComboBox.addItem("Spiral");
        universeTypeComboBox.addItem("Irregular");
        universeTypeComboBox.addItem("Elliptical");
        
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
            
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("python");
            
            engine.put("universeConfig", config);
            reader = new FileReader(System.getProperty("user.dir") + "/assets/scripts/universeGen/main.py");
            engine.eval(reader);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NewGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ScriptException ex) {
            Logger.getLogger(NewGame.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (reader != null)
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(NewGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
