package ConquerSpace.game.universe;

import ConquerSpace.ConquerSpace;
import ConquerSpace.game.ui.UniverseRenderer;
import ConquerSpace.start.gui.NewGame;
import ConquerSpace.start.gui.UniverseConfig;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.FileReader;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Zyun
 */
public final class UniverseRenderTest {

    public static void main(String[] args) throws FileNotFoundException, ScriptException {
        //Select universe
        JPanel pan = new JPanel(new GridLayout(3, 4, 10, 10));
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
        JLabel quoteLabel;
        
        //Add components.
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
        
        //Add to panel.
        pan.add(universeSizeLabel);
        pan.add(universeSizeBox);
        pan.add(universeTypeLabel);
        pan.add(universeTypeComboBox);
        pan.add(universeHistoryLabel);
        pan.add(universeHistoryComboBox);
        pan.add(planetCommonalityLabel);
        pan.add(planetCommonalityComboBox);
        pan.add(civilizationsLabel);
        pan.add(civilazitionComboBox);
        pan.add(quoteLabel);
        //Show a option pane
        JOptionPane.showMessageDialog(null, pan);
        
        //Parse arguments.
        UniverseConfig config = new UniverseConfig();
        config.setUniverseSize((String) universeSizeBox.getSelectedItem());
        config.setUniverseShape((String) universeTypeComboBox.getSelectedItem());
        config.setUniverseAge((String) universeHistoryComboBox.getSelectedItem());
        config.setCivilizationCount((String) civilazitionComboBox.getSelectedItem());
        config.setPlanetCommonality((String) planetCommonalityComboBox.getSelectedItem());
            
        //Init script
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("python");
            
        engine.put("universeConfig", config);
        engine.put("version", ConquerSpace.VERSION);
        FileReader reader = new FileReader(System.getProperty("user.dir") + "/assets/scripts/universeGen/main.py");
        engine.eval(reader);
        Universe universe = (Universe) engine.get("universeObject");
        
        //Then create window, display the universe.
        JFrame frame = new JFrame("Universe displayer");

        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        //Add the universe renderer
        UniverseRenderer renderer = new UniverseRenderer(Toolkit.getDefaultToolkit().getScreenSize(), universe, new Point(0, 0));

        frame.add(renderer);

        frame.setVisible(true);
    }
}
