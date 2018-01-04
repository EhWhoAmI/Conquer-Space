package ConquerSpace.script;

import ConquerSpace.ConquerSpace;
import ConquerSpace.start.gui.NewGame;
import ConquerSpace.start.gui.UniverseConfig;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.Serializer;
import nu.xom.ValidityException;

/**
 *
 * @author Zyun
 */
public class UniverseGenTest extends JFrame implements ActionListener{
    private volatile static boolean waiting = false;
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
    public UniverseGenTest() {
        waiting = true;
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
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileReader reader = null;
        try {
            waiting = false;
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
            engine.put("version", ConquerSpace.VERSION);
            reader = new FileReader(System.getProperty("user.dir") + "/assets/scripts/universeGen/main.py");
            engine.eval(reader);
            setVisible(false);
            // Show universe
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NewGame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage() + "\n" + ex.getStackTrace(), "File not found", JOptionPane.ERROR_MESSAGE);
        } catch (ScriptException ex) {
            Logger.getLogger(NewGame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Script Error: " + ex.getMessage() + "\n" + ex.getStackTrace().toString(), "Script Error", JOptionPane.ERROR_MESSAGE);

        } finally {
            try {
                if (reader != null)
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(NewGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    public static void main(String[] args) throws FileNotFoundException, ParsingException, ValidityException, IOException {
        while (true) {
            UniverseGenTest test = new UniverseGenTest();
            test.setVisible(true);
            while (UniverseGenTest.waiting);
            //Get all xml.
            File xmlFile = new File(System.getProperty("user.home") + "/.conquerspace/save1.xml");
            if (!xmlFile.exists()) {
                throw new FileNotFoundException("The file " + xmlFile + " was not found");
            }
            Builder xmlBuilder = new Builder();
            Document build = xmlBuilder.build(xmlFile);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Serializer serializer = new Serializer(baos);
            serializer.setIndent(4);
            serializer.write(build);
            
            JTextArea area = new JTextArea(baos.toString(), 10, 50);
            JScrollPane sampleScrollPane = new JScrollPane (area,     JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            JOptionPane.showMessageDialog(null, sampleScrollPane);
            test = null;
        }
    }
}
