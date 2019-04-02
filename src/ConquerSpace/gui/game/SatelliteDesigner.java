package ConquerSpace.gui.game;

import ConquerSpace.game.GameUpdater;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.satellites.SatelliteTypes;
import com.alee.extended.layout.VerticalFlowLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Zyun
 */
public class SatelliteDesigner extends JInternalFrame {

    private JMenuBar menuBar;

    private JList satelliteList;
    private JPanel satelliteDesignerPanel;
    private JList satelliteTypeList;

    private DefaultListModel<String> satelliteListModel;
    private DefaultListModel<String> satelliteTypeListModel;

    private JLabel satelliteNameLabel;
    private JTextField satelliteNameField;
    private JButton getRandomSatelliteNameButton;
    private JLabel satelliteMassLabel;
    private JLabel satelliteMassValueLabel;
    private JLabel satelliteMassValueUnitLabel;
    private JPanel satelliteStatsPanel;
    private CardLayout layout;
    private JPanel upperSatelliteStats;
    private JPanel lowerSatelliteStats;

    //Then all the various panels
    private JPanel testSatellitePlanner;
    private JLabel testSatellitePlannerLabel;
    private final String TEST_SATELLITE_STRING = "test";

    private JPanel telescopeSatellitePlanner;
    private JLabel telescopeSatellitePlannerLabel;
    private JLabel telescopeSatelliteSizeLabel;
    private JSpinner telescopeSatelliteSizeSpinner;
    private JLabel telescopeSatelliteRangeLabel;
    private JLabel telescopeSatelliteRangeValueLabel;
    private JLabel telescopeSatelliteMassLabel;
    private JLabel telescopeSatelliteMassValueLabel;
    private SpinnerNumberModel telescopeSatelliteRangespinnerModel;
    private final String TELESCOPE_SATELLITE_STRING = "telescope";

    private JPanel militarySatellitePlanner;
    private JLabel militarySatellitePlannerLabel;
    private final String MILITARY_SATELLITE_STRING = "military";

    private int mass = 0;

    @SuppressWarnings("unchecked")
    public SatelliteDesigner(Civilization c) {
        setLayout(new GridLayout(1, 2));
        setTitle("Satellite Designer");

        menuBar = new JMenuBar();
        JMenu satelliteMenu = new JMenu("Satellites");
        JMenuItem saveSatelliteMenu = new JMenuItem("Save Satellite");
        JMenuItem newMenu = new JMenuItem("New Satellite");

        saveSatelliteMenu.addActionListener(a -> {
            //Create the satellite
            if (!satelliteNameField.getText().equals("")) {
                JSONObject obj = new JSONObject();

                obj.put("name", satelliteNameField.getText());
                obj.put("mass", mass);
                String type = "";
                //Doesnt really matter
                obj.put("dist", 0);

                switch (satelliteTypeList.getSelectedIndex()) {
                    case SatelliteTypes.NONE:
                        type = "none";
                        break;
                    case SatelliteTypes.TELESCOPE:
                        type = "telescope";
                        //Add Info
                        obj.put("range", GameUpdater.Calculators.Optics.getRange(c.values.get("optics.quality"), (int) telescopeSatelliteSizeSpinner.getValue()));
                        break;
                    case SatelliteTypes.MILITARY:
                        type = "military";
                        break;
                }
                obj.put("type", type);
                obj.put("mass", mass);
                obj.put("cost", new JSONArray(new Integer[]{0}));
                obj.put("id", c.satelliteTemplates.size());
                c.satelliteTemplates.add(obj);
            }
        });

        satelliteMenu.add(saveSatelliteMenu);
        satelliteMenu.add(newMenu);
        menuBar.add(satelliteMenu);

        setJMenuBar(menuBar);

        satelliteListModel = new DefaultListModel<>();
        satelliteTypeListModel = new DefaultListModel<>();

        satelliteList = new JList(satelliteListModel);
        satelliteDesignerPanel = new JPanel();
        satelliteTypeList = new JList(satelliteTypeListModel);

        satelliteTypeList.setSelectedIndex(0);
        
        satelliteDesignerPanel.setLayout(new VerticalFlowLayout());

        upperSatelliteStats = new JPanel();
        upperSatelliteStats.setLayout(new GridLayout(2, 3));

        satelliteNameLabel = new JLabel("Name: ");
        satelliteNameField = new JTextField();
        getRandomSatelliteNameButton = new JButton("Get random name");

        satelliteMassLabel = new JLabel("Mass: ");
        satelliteMassValueLabel = new JLabel("0");
        satelliteMassValueUnitLabel = new JLabel("kg");

        upperSatelliteStats.add(satelliteNameLabel);
        upperSatelliteStats.add(satelliteNameField);
        upperSatelliteStats.add(getRandomSatelliteNameButton);
        upperSatelliteStats.add(satelliteMassLabel);
        upperSatelliteStats.add(satelliteMassValueLabel);
        upperSatelliteStats.add(satelliteMassValueUnitLabel);//Empty

        satelliteDesignerPanel.add(upperSatelliteStats);

        lowerSatelliteStats = new JPanel();
        satelliteStatsPanel = new JPanel();
        layout = new CardLayout();
        satelliteStatsPanel.setLayout(layout);
        {
            testSatellitePlanner = new JPanel();
            testSatellitePlannerLabel = new JLabel("This is just for a test satellite. There is nothing here!");

            testSatellitePlanner.add(testSatellitePlannerLabel);
            satelliteStatsPanel.add(testSatellitePlanner, TEST_SATELLITE_STRING);
        }
        {
            telescopeSatellitePlanner = new JPanel();
            telescopeSatellitePlanner.setLayout(new GridLayout(3, 2));
            telescopeSatelliteSizeLabel = new JLabel("Optics Radius (10 cm): ");
            telescopeSatelliteRangespinnerModel = new SpinnerNumberModel();
            telescopeSatelliteRangespinnerModel.setValue(10);
            telescopeSatelliteRangespinnerModel.setMinimum(0);
            telescopeSatelliteSizeSpinner = new JSpinner(telescopeSatelliteRangespinnerModel);
            int quality = c.values.get("optics.quality");

            telescopeSatelliteSizeSpinner.addChangeListener(a -> {
                //Set all values
                mass = GameUpdater.Calculators.Optics.getLensMass(quality, (int) telescopeSatelliteRangespinnerModel.getValue());
                satelliteMassValueLabel.setText(mass + "");
                telescopeSatelliteRangeValueLabel.setText(GameUpdater.Calculators.Optics.getRange(quality, (int) telescopeSatelliteRangespinnerModel.getValue()) + " light years");
                telescopeSatelliteMassValueLabel.setText(GameUpdater.Calculators.Optics.getLensMass(quality, (int) telescopeSatelliteRangespinnerModel.getValue()) + "kg");
            });

            telescopeSatelliteRangeLabel = new JLabel("Range: ");
            telescopeSatelliteRangeValueLabel = new JLabel(GameUpdater.Calculators.Optics.getRange(quality, (int) telescopeSatelliteRangespinnerModel.getValue()) + " light years");

            telescopeSatelliteMassLabel = new JLabel("Lens Mass: ");
            telescopeSatelliteMassValueLabel = new JLabel(GameUpdater.Calculators.Optics.getLensMass(quality, (int) telescopeSatelliteRangespinnerModel.getValue()) + " kg");

            telescopeSatellitePlanner.add(telescopeSatelliteSizeLabel);
            telescopeSatellitePlanner.add(telescopeSatelliteSizeSpinner);
            telescopeSatellitePlanner.add(telescopeSatelliteRangeLabel);
            telescopeSatellitePlanner.add(telescopeSatelliteRangeValueLabel);
            telescopeSatellitePlanner.add(telescopeSatelliteMassLabel);
            telescopeSatellitePlanner.add(telescopeSatelliteMassValueLabel);

            satelliteStatsPanel.add(telescopeSatellitePlanner, TELESCOPE_SATELLITE_STRING);
        }

        lowerSatelliteStats.add(satelliteStatsPanel);

        satelliteDesignerPanel.add(lowerSatelliteStats);
        for (String s : SatelliteTypes.SATELLITE_TYPE_NAMES) {
            satelliteTypeListModel.addElement(s);
        }

        satelliteTypeList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //Empty
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //Empty
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //This is the important one
                //Get selected one
                int selected = satelliteTypeList.getSelectedIndex();
                //Because the strings in SatelliteTypes.SATELLITE_TYPE_NAMES is 
                //coordinated with the index, so we do not need for much
                //coordination
                switch (selected) {
                    case SatelliteTypes.NONE:
                        //Set mass to 84 kg : mass of sputnik 1
                        mass = 84;
                        satelliteMassValueLabel.setText(mass + "");
                        layout.show(satelliteStatsPanel, TEST_SATELLITE_STRING);
                        break;
                    case SatelliteTypes.TELESCOPE:
                        //Set all default values
                        mass = 0;
                        satelliteMassValueLabel.setText(mass + "");
                        layout.show(satelliteStatsPanel, TELESCOPE_SATELLITE_STRING);
                        break;
                    case SatelliteTypes.MILITARY:
                        layout.show(satelliteStatsPanel, TEST_SATELLITE_STRING);
                        break;
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //Empty
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //Empty
            }
        });
        add(satelliteList);
        add(satelliteDesignerPanel);
        add(satelliteTypeList);

        setClosable(true);
        setVisible(true);
        setResizable(true);
        //setSize(100, 100);
        pack();
    }
}
