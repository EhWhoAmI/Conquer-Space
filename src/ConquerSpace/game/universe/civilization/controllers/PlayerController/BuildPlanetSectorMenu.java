package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.GameController;
import ConquerSpace.game.actions.Actions;
import ConquerSpace.game.universe.civilization.Civilization;
import ConquerSpace.game.universe.ships.launch.LaunchSystem;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.pSectors.PopulationStorage;
import ConquerSpace.game.universe.spaceObjects.pSectors.SpacePortBuilding;
import ConquerSpace.util.ExceptionHandling;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.logging.log4j.LogManager;
import org.python.core.PyException;
import org.python.core.PyInteger;
import org.python.core.PyLong;
import org.python.core.PyObject;

/**
 *
 * @author Zyun
 */
public class BuildPlanetSectorMenu extends JInternalFrame {

    private JPanel topWrapper;
    private JLabel planetBuildTypeTitle;
    private JComboBox<String> planetBuildType;
    private JLabel costLabel;
    private JButton build;
    private JPanel bottom;
    private CardLayout cardLayout;

    private BuildPopulationStorage bps;
    private BuildSpaceLaunchSite bsls;

    Civilization c;

    @SuppressWarnings("unchecked")
    public BuildPlanetSectorMenu(Planet p, int id, Civilization c) {
        this.c = c;
        setTitle("Build on planet " + p.getName());
        setLayout(new GridLayout(2, 1));
        topWrapper = new JPanel();
        topWrapper.setLayout(new GridLayout(2, 2, 5, 5));
        planetBuildTypeTitle = new JLabel("Build a ");

        planetBuildType = new JComboBox<>();
        planetBuildType.addItem("Residental area");
        //planetBuildType.addItem("Industral area");
        //planetBuildType.addItem("Military building");
        planetBuildType.addItem("Space Launch Site");

        planetBuildType.addActionListener((e) -> {
            JComboBox<String> box = (JComboBox) e.getSource();
            box.getSelectedItem();
            if (box.getSelectedItem().equals("Residental area")) {
                cardLayout.show(bottom, "Pop");
            } else if (box.getSelectedItem().equals("Space Launch Site")) {
                cardLayout.show(bottom, "Space Launch Site");
            }
        });

        costLabel = new JLabel("Cost : " + 0);

        build = new JButton("Build!");
        build.addActionListener((e) -> {
            String item = (String) planetBuildType.getSelectedItem();
            if (item.equals("Residental area")) {
                PopulationStorage storage = new PopulationStorage(Long.parseLong(bps.maxPopulationTextField.getText()), 0, (byte) 100);
                Actions.buildBuilding(p, id, storage, 0, 1);
            } else if (item.equals("Space Launch Site")) {
                //Get civ launching type...
                SpacePortBuilding port = new SpacePortBuilding(0, Integer.parseInt(bsls.maxPopulation.getText()), (LaunchSystem) bsls.launchTypesValue.getSelectedItem());
                Actions.buildBuilding(p, id, port, 0, 1);
            }
            this.dispose();
        });

        topWrapper.add(planetBuildTypeTitle);
        topWrapper.add(planetBuildType);
        topWrapper.add(costLabel);
        topWrapper.add(build);
        add(topWrapper);

        bottom = new JPanel();
        cardLayout = new CardLayout();
        bottom.setLayout(cardLayout);
        //Configure the components
        bps = new BuildPopulationStorage();
        bsls = new BuildSpaceLaunchSite(c);
        bottom.add(bps, "Pop");
        bottom.add(bsls, "Space Launch Site");
        setResizable(true);
        setClosable(true);
        add(bottom);
        pack();
    }

    //Various menus for building stats
    private class BuildPopulationStorage extends JPanel implements ActionListener {

        long maxPopulation;
        private JLabel amount;
        JTextField maxPopulationTextField;

        public BuildPopulationStorage() {
            setLayout(new GridLayout(1, 2));
            amount = new JLabel("Max Population");

            maxPopulationTextField = new JTextField("1000000");
            maxPopulationTextField.addActionListener((e) -> {
                try {
                    maxPopulation = Long.parseLong(maxPopulationTextField.getText());
                } catch (NumberFormatException nfe) {
                    maxPopulationTextField.setText("1000000");
                }
            });

            maxPopulationTextField.addActionListener(this);
            add(amount);
            add(maxPopulationTextField);
        }

        //Determine price
        //Add this 
        @Override
        public void actionPerformed(ActionEvent e) {
            //Calculate the cost...
            try {
                maxPopulation = Long.parseLong(maxPopulationTextField.getText());
                //Load it from js
                PyObject function = GameController.pythonEngine.eval("calculatePopulationStorageCost");
                long o = function.__call__(new PyLong(maxPopulation)).asLong();
                costLabel.setText("Cost : " + o);
            } catch (NumberFormatException | ArithmeticException nfe) {
                //Because who cares!
            } catch (final PyException ex) {
                LogManager.getLogger("ErrorLog").error("Python error " + ex.toString(), ex);
                String trace = "None";
                if (ex.traceback != null) {
                    trace = ex.traceback.dumpStack();
                }
                ExceptionHandling.ExceptionMessageBox("Script error: " + ex.type.toString() + ".\nPython trace: \n" + trace, ex);
                System.exit(1);
            }
        }
    }

    private class BuildSpaceLaunchSite extends JPanel implements ActionListener {

        private JLabel amount;

        JTextField maxPopulation;
        private JLabel launchTypes;
        private JComboBox<LaunchSystem> launchTypesValue;

        public BuildSpaceLaunchSite(Civilization c) {
            setLayout(new GridLayout(2, 2));
            amount = new JLabel("Amount of launch ports");

            launchTypes = new JLabel("Launch types");

            maxPopulation = new JTextField();
            maxPopulation.addActionListener((e) -> {
                try {
                    Long.parseLong(maxPopulation.getText());
                } catch (NumberFormatException nfe) {
                    maxPopulation.setText("");
                }
            });

            launchTypesValue = new JComboBox<LaunchSystem>();

            for (LaunchSystem t : c.launchSystems) {
                launchTypesValue.addItem(t);
            }
            maxPopulation.addActionListener(this);
            add(amount);
            add(maxPopulation);
            add(launchTypes);
            add(launchTypesValue);
        }

        //Determine price
        //Add this 
        @Override
        public void actionPerformed(ActionEvent e) {
            //Calculate the cost...
            long pop = 0;
            try {
                pop = Long.parseLong(maxPopulation.getText());
                PyObject function = GameController.pythonEngine.eval("calculateSpacePortCost");

                long price = function.__call__(new PyLong(pop), new PyInteger(((LaunchSystem) launchTypesValue.getSelectedItem()).getConstructCost())).asInt();
                costLabel.setText("Cost : " + price);
            } catch (NumberFormatException | ArithmeticException nfe) {
                //Because who cares!
            }
        }
    }
}
