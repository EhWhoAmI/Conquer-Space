package ConquerSpace.game.universe.civilization.controllers.PlayerController;

import ConquerSpace.game.actions.Actions;
import ConquerSpace.game.universe.spaceObjects.Planet;
import ConquerSpace.game.universe.spaceObjects.pSectors.PopulationStorage;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Zyun
 */
public class BuildPlanetSectorMenu extends JFrame {

    private JPanel topWrapper;
    private JLabel planetBuildTypeTitle;
    private JComboBox<String> planetBuildType;
    private JLabel costLabel;
    private JButton build;
    private JPanel bottom;
    private CardLayout cardLayout;

    private int currentSelected;

    public BuildPlanetSectorMenu(Planet p, int id) {
        setTitle("Build on planet " + p.getName());
        setLayout(new GridLayout(2, 1));
        topWrapper = new JPanel();
        topWrapper.setLayout(new GridLayout(2, 2, 5, 5));
        planetBuildTypeTitle = new JLabel("Build a ");

        planetBuildType = new JComboBox<>();
        planetBuildType.addItem("Residental area");
        planetBuildType.addItem("Industral area");
        planetBuildType.addItem("Military building");

        planetBuildType.addActionListener((e) -> {
            JComboBox<String> box = (JComboBox) e.getSource();
            box.getSelectedItem();
            cardLayout.show(bottom, "Pop");
        });

        costLabel = new JLabel("Cost : " + 0);

        build = new JButton("Build!");
        build.addActionListener((e) -> {
            //p.planetSectors[id] = new BuildingBuilding(id, 100, p.planetSectors[id], 0);
            String item = (String) planetBuildType.getSelectedItem();
            if (item.equals("Residental area")) {
                for (Component c : bottom.getComponents()) {
                    if (c instanceof BuildPopulationStorage && c.isVisible()) {
                        PopulationStorage storage = new PopulationStorage(Long.parseLong(((BuildPopulationStorage) c).maxPopulation.getText()), 0, (byte) 100, id, 0);
                        Actions.buildBuilding(p, id, storage, 0, 1);
                    }
                }
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
        bottom.add(new BuildPopulationStorage(), "Pop");

        add(bottom);
        pack();
    }

    //Various menus for building stats
    private class BuildPopulationStorage extends JPanel implements ActionListener {

        private JLabel amount;
        JTextField maxPopulation;

        public BuildPopulationStorage() {
            setLayout(new GridLayout(1, 2));
            amount = new JLabel("Max Population");

            maxPopulation = new JTextField();
            maxPopulation.addActionListener((e) -> {
                try {
                    Long.parseLong(maxPopulation.getText());
                } catch (NumberFormatException nfe) {
                    maxPopulation.setText("");
                }
            });

            maxPopulation.addActionListener(this);
            add(amount);
            add(maxPopulation);
        }

        //Determine price
        //Add this 
        @Override
        public void actionPerformed(ActionEvent e) {
            //Calculate the cost...
            long pop = 0;
            try {
                pop = Long.parseLong(maxPopulation.getText());
                long price = (((1000) * pop));
                costLabel.setText("Cost : " + price);
            } catch (NumberFormatException | ArithmeticException nfe) {
                //Because who cares!
            }
        }
    }
}
